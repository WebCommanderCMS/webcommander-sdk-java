package com.webcommander.http;

import com.webcommander.Redaction;
import com.webcommander.config.SdkConfig;
import com.webcommander.errors.SdkConfigError;
import com.webcommander.errors.SdkUsageError;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/** Builds the {@link OkHttpClient} a {@link Transport} sends over, from an {@link SdkConfig}. */
final class OkHttpClientFactory {

    private OkHttpClientFactory() {}

    /** The client a transport built from this config sends over. */
    static OkHttpClient create(SdkConfig config) {
        OkHttpClient.Builder builder = applySdkInvariants(new OkHttpClient.Builder(), config)
                .connectionPool(new ConnectionPool(config.getPoolMaxsize(), 5, TimeUnit.MINUTES));
        if (config.getProxyUrl() != null && !config.getProxyUrl().isBlank()) {
            applyProxy(builder, config.getProxyUrl());
        }
        if (config.getCaBundle() != null && !config.getCaBundle().isBlank()) {
            applyCaBundle(builder, config.getCaBundle(), config.getMaxCaBundleBytes());
        } else if (!config.isVerifySsl()) {
            applyTrustAll(builder);
        }
        return builder.build();
    }

    /** A caller's own client with the transport's deadlines, no redirects and no silent retry. */
    static OkHttpClient withSdkInvariants(OkHttpClient supplied, SdkConfig config) {
        return applySdkInvariants(supplied.newBuilder(), config).build();
    }

    /** The configured deadlines, no redirects and no OkHttp retry. */
    private static OkHttpClient.Builder applySdkInvariants(
            OkHttpClient.Builder builder, SdkConfig config) {
        return builder
                .connectTimeout(Duration.ofMillis(
                        timeoutMillis(config.getConnectTimeoutSeconds())))
                .readTimeout(Duration.ofMillis(timeoutMillis(config.getTimeoutSeconds())))
                .writeTimeout(Duration.ofMillis(timeoutMillis(config.getTimeoutSeconds())))
                .callTimeout(Duration.ofMillis(timeoutMillis(config.getTimeoutSeconds())))
                .retryOnConnectionFailure(false)
                .followRedirects(false)
                .followSslRedirects(false);
    }

    /** A configured number of seconds as OkHttp milliseconds, never rounding down to zero. */
    private static long timeoutMillis(double seconds) {
        return Math.max(1L, (long) (seconds * 1000));
    }

    /** The whole proxy URL: its scheme chooses the proxy type and its userinfo answers the 407. */
    private static void applyProxy(OkHttpClient.Builder builder, String proxyUrl) {
        // Authority, never java.net.URI: a null host would dial the wildcard address.
        UrlBuilder.Authority proxy = UrlBuilder.Authority.split(proxyUrl);
        String scheme = proxy.getScheme();
        Proxy.Type type = scheme.startsWith("socks") ? Proxy.Type.SOCKS : Proxy.Type.HTTP;
        int defaultPort = type == Proxy.Type.SOCKS ? 1080 : "https".equals(scheme) ? 443 : 80;
        builder.proxy(new Proxy(type, new InetSocketAddress(proxy.getHostForConnect(),
                proxy.getPort() < 0 ? defaultPort : proxy.getPort())));
        String userInfo = proxy.getUserInfo();
        if (userInfo != null && !userInfo.isEmpty() && type == Proxy.Type.HTTP) {
            int colon = userInfo.indexOf(':');
            String credential = okhttp3.Credentials.basic(
                    percentDecode(colon < 0 ? userInfo : userInfo.substring(0, colon)),
                    percentDecode(colon < 0 ? "" : userInfo.substring(colon + 1)),
                    StandardCharsets.UTF_8);
            builder.proxyAuthenticator((route, response) -> {
                if (response.request().header("Proxy-Authorization") != null) {
                    return null;    // the proxy refused these credentials once; stop
                }
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential).build();
            });
        }
    }

    /** Decode the userinfo's percent-escapes, which carry {@code @}, {@code :} or {@code /}. */
    private static String percentDecode(String text) {
        StringBuilder out = new StringBuilder(text.length());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (character == '%' && i + 2 < text.length()) {
                try {
                    bytes.write(Integer.parseInt(text.substring(i + 1, i + 3), 16));
                    i += 2;
                    continue;
                } catch (NumberFormatException notAnEscape) {
                    // fall through: a bare % is kept as written
                }
            }
            if (bytes.size() > 0) {
                out.append(new String(bytes.toByteArray(), StandardCharsets.UTF_8));
                bytes.reset();
            }
            out.append(character);
        }
        if (bytes.size() > 0) {
            out.append(new String(bytes.toByteArray(), StandardCharsets.UTF_8));
        }
        return out.toString();
    }

    /** Trust exactly the certificates in a PEM bundle, reading at most {@code maxBundleBytes}. */
    private static void applyCaBundle(
            OkHttpClient.Builder builder, String pemPath, long maxBundleBytes) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            int index = 0;
            byte[] bundle;
            try (var stream = Files.newInputStream(Path.of(pemPath))) {
                bundle = Transport.readAtMost(stream, maxBundleBytes);
            }
            if (bundle.length > maxBundleBytes) {
                throw new SdkUsageError("caBundle: " + pemPath + " is over the "
                        + maxBundleBytes + "-byte maxCaBundleBytes ceiling, so it was not "
                        + "parsed - raise the ceiling with "
                        + "SdkConfig.builder(...).maxCaBundleBytes(bytes) if the bundle is "
                        + "meant to be that large");
            }
            try (var stream = new ByteArrayInputStream(bundle)) {
                for (Certificate certificate : factory.generateCertificates(stream)) {
                    keyStore.setCertificateEntry("ca" + index++, certificate);
                }
            }
            TrustManagerFactory trust =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trust.init(keyStore);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, trust.getTrustManagers(), null);
            builder.sslSocketFactory(context.getSocketFactory(),
                    (X509TrustManager) trust.getTrustManagers()[0]);
        } catch (SdkUsageError refusal) {
            throw refusal;
        } catch (Exception error) {
            throw new SdkUsageError("caBundle: "
                    + Redaction.redactPath(pemPath)
                    + " could not be loaded: "
                    + Redaction.describeFailure(error), error);
        }
    }

    /** verifySsl=false still encrypts — it only stops checking who is on the other end. */
    private static void applyTrustAll(OkHttpClient.Builder builder) {
        try {
            X509TrustManager trustAll = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[] {trustAll}, new SecureRandom());
            SSLSocketFactory factory = context.getSocketFactory();
            builder.sslSocketFactory(factory, trustAll).hostnameVerifier((host, session) -> true);
        } catch (Exception error) {
            // In the hierarchy, so a JVM refusing trust-all is a config failure callers can catch.
            throw new SdkConfigError(
                    "verifySsl=false could not be applied: this JVM refused the trust-all "
                            + "TLS context (" + error.getClass().getSimpleName()
                            + "); leave verification on, or pass caBundle instead", error);
        }
    }
}
