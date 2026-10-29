package com.webcommander.resources.settings.site_settings.site_settings_dto;

import com.webcommander.Dto;

import java.util.List;

/** Typed views over the site settings bodies. */
public final class SiteSettingsDto {

    private SiteSettingsDto() {
    }

    /** A country or a state as a read sends it. */
    public static final class Region extends Dto {

        public Long id;

        public String code;

        public String name;
    }

    /** The {@code auto_suggest.addresses} block: whether addresses are suggested as typed. */
    public static final class AutoSuggestAddresses extends Dto {

        public Boolean enabled;

        public Boolean validate;
    }

    /** The {@code auto_suggest} block — address and suburb suggestions. */
    public static final class AutoSuggest extends Dto {

        public AutoSuggestAddresses addresses;

        public Boolean suburbsCities;
    }

    /** The {@code captcha} block: the challenge shown on store forms and its provider keys. */
    public static final class Captcha extends Dto {

        public Boolean enabled;

        public Double minimumScore;

        public String privateKey;

        public String privateKeyTurnstile;

        public String private_key_v3;

        public String publicKey;

        public String publicKeyTurnstile;

        public String public_key_v3;

        public String type;

        public Boolean useOwn;

        public String version;
    }

    /** The {@code administration} section: units, precision, default region and error pages. */
    public static final class Administration extends Dto {

        public AutoSuggest autoSuggest;

        public Captcha captcha;

        public Region defaultCountry;

        public Region defaultState;

        public Boolean ecommerceEnabled;

        public Long maxPrecision;

        public String page_403;

        public String page_404;

        public String unitLength;

        public String unitWeight;

        @Override
        protected String envelopeKey() {
            return "administration";
        }
    }

    /** The {@code brand_kit.colors} block: six roles, each a hex colour. */
    public static final class BrandColors extends Dto {

        public String accent;

        public String background;

        public String primary;

        public String secondary;

        public String surface;

        public String text;
    }

    /** The {@code brand_kit} section: the site-wide palette. */
    public static final class BrandKit extends Dto {

        public BrandColors colors;

        public Boolean enabled;

        public String paletteName;

        @Override
        protected String envelopeKey() {
            return "brand_kit";
        }
    }

    /** The three fields every {@code date_time} audience block carries. */
    public static class DateTimeFormat extends Dto {

        public String dateFormat;

        public String timeFormat;

        public String timeZone;
    }

    /** The {@code date_time.admin} block: the admin area's own formats. */
    public static final class AdminDateTime extends DateTimeFormat {

        public String firstDayOfWeek;
    }

    /** The {@code date_time.email} and {@code date_time.user} blocks. */
    public static final class SeparableDateTime extends DateTimeFormat {

        public Boolean separate;
    }

    /** The {@code date_time} section: formats and zones for admin, emails and the storefront. */
    public static final class DateTime extends Dto {

        public AdminDateTime admin;

        public SeparableDateTime email;

        public Boolean hideTimeZone;

        public SeparableDateTime user;

        @Override
        protected String envelopeKey() {
            return "date_time";
        }
    }

    /** The {@code maintenance} block: whether the storefront is closed, and what it says. */
    public static final class Maintenance extends Dto {

        public Boolean enabled;

        public String message;
    }

    /** The {@code security_code} block: the site lock. */
    public static final class SecurityCode extends Dto {

        public String code;

        public Boolean enabled;

        public Boolean numberOnly;
    }

    /** The {@code ssl.key_file} block: what the store holds for the private key. */
    public static class SslKeyFile extends Dto {

        public String fileName;

        public Boolean uploaded;
    }

    /** The {@code ssl.certificate_file} block: {@link SslKeyFile} plus expiry and subject. */
    public static final class SslCertificateFile extends SslKeyFile {

        public String expiresAt;

        public String subject;
    }

    /** The {@code ssl} block: what is stored for the certificate and its key. */
    public static final class Ssl extends Dto {

        public SslCertificateFile certificateFile;

        public SslKeyFile keyFile;
    }

    /** The {@code domain_settings} section: the store's address, aliases and site lock. */
    public static final class DomainSettings extends Dto {

        public List<String> aliases;

        public Boolean httpsEnabled;

        public Maintenance maintenance;

        public SecurityCode securityCode;

        public Ssl ssl;

        public String websiteAddress;

        @Override
        protected String envelopeKey() {
            return "domain_settings";
        }
    }

    /** One entry of {@code available}: a provider the store can send through. */
    public static final class EmailProvider extends Dto {

        public String key;

        public String label;

        public String type;

        public String connectUrl;
    }

    /** A {@code reply} block: whether replies go somewhere other than the sender. */
    public static final class ReplyTo extends Dto {

        public Boolean different;

        public String toEmail;

        public String toName;
    }

    /** One entry of {@code default.domains}: one of the platform's own sending domains. */
    public static final class DefaultDomain extends Dto {

        public Boolean configure;

        public String domain;

        public String emailPrefix;

        public ReplyTo reply;

        public String senderName;
    }

    /** The {@code default} block: the platform's own sending domains. */
    public static final class DefaultEmail extends Dto {

        public List<DefaultDomain> domains;
    }

    /** The {@code gmail} and {@code office365} blocks: one read-only {@link #connectUrl} each. */
    public static final class OauthProvider extends Dto {

        public String connectUrl;
    }

    /** The {@code sendgrid} block: the SendGrid sender identity and the key behind it. */
    public static final class SendGrid extends Dto {

        public String apiKey;

        public ReplyTo reply;

        public String senderEmail;

        public String senderName;
    }

    /** The {@code smtp.authentication} block: whether and how the server is logged in to. */
    public static final class SmtpAuthentication extends Dto {

        public Boolean enabled;

        public String password;

        public String username;
    }

    /** The {@code smtp} block: the server the store sends through and who it sends as. */
    public static final class Smtp extends Dto {

        public SmtpAuthentication authentication;

        public String encryption;

        public String host;

        public Long port;

        public String senderEmail;

        public String senderName;
    }

    /** The {@code email_settings} section; {@link #Default} is the store's {@code default} key. */
    public static final class EmailSettings extends Dto {

        public List<EmailProvider> available;

        public DefaultEmail Default;

        public OauthProvider gmail;

        public OauthProvider office365;

        public SendGrid sendgrid;

        public String serviceProvider;

        public Smtp smtp;

        @Override
        protected String envelopeKey() {
            return "email_settings";
        }
    }

    /** The {@code image} record of either media section: what the store holds and serves. */
    public static final class MediaImage extends Dto {

        public String contentType;

        public String fileName;

        public Long height;

        public Long sizeBytes;

        public String url;

        public Long width;
    }

    /** The {@code favicon} section, {@code kind: "media"}. */
    public static final class Favicon extends Dto {

        public Boolean enabled;

        public MediaImage image;

        @Override
        protected String envelopeKey() {
            return "favicon";
        }
    }

    /** The {@code store_logo.variants} block: logos for dark backgrounds and the sticky header. */
    public static final class StoreLogoVariants extends Dto {

        /** Reads {@code null} while the main logo is used; a set variant is not yet measured. */
        public Object darkBackground;

        /** Reads {@code null} while the main logo is used; a set variant is not yet measured. */
        public Object stickyHeader;
    }

    /** The {@code store_logo} section, {@code kind: "media"}. */
    public static final class StoreLogo extends Dto {

        public MediaImage image;

        public StoreLogoVariants variants;

        @Override
        protected String envelopeKey() {
            return "store_logo";
        }
    }

    /** The {@code store_details.address} block: where the business is. */
    public static final class StoreAddress extends Dto {

        public String addressLine1;

        public String addressLine2;

        public String city;

        public Region country;

        public String postCode;

        public Region state;
    }

    /** The {@code store_details.contact} block — the three numbers the store publishes. */
    public static final class StoreContact extends Dto {

        public String fax;

        public String mobile;

        public String phone;
    }

    /** The {@code store_details} section, {@code kind: "resource"}. */
    public static final class StoreDetails extends Dto {

        public String abn;

        public StoreAddress address;

        public String companyName;

        public StoreContact contact;

        public String email;

        @Override
        protected String envelopeKey() {
            return "store_details";
        }
    }

    /** One entry of {@code group_meta}: what a thing is and whether it can be written. */
    public static final class SectionMeta extends Dto {

        public String kind;

        public Boolean writable;

        public String href;
    }

    /** The {@code group_meta} block: one {@link SectionMeta} per section the area declares. */
    public static final class GroupMeta extends Dto {

        public SectionMeta administration;

        public SectionMeta brandKit;

        public SectionMeta dateTime;

        public SectionMeta domainSettings;

        public SectionMeta emailSettings;

        public SectionMeta favicon;

        public SectionMeta storeDetails;

        public SectionMeta storeLogo;
    }

    /** The site settings, as the facade or one section sends them. */
    public static final class SiteSettings extends Dto {

        public Administration administration;

        public BrandKit brandKit;

        public DateTime dateTime;

        public DomainSettings domainSettings;

        public EmailSettings emailSettings;

        public Favicon favicon;

        public StoreDetails storeDetails;

        public StoreLogo storeLogo;

        public GroupMeta groupMeta;

        public Object fieldMetadata;

        @Override
        protected String envelopeKey() {
            return "site_settings";
        }
    }
}
