package com.webcommander.client;

import com.webcommander.Redaction;
import com.webcommander.config.AuthConfig;
import com.webcommander.config.SdkConfig;
import com.webcommander.errors.SdkError;
import com.webcommander.errors.SdkUsageError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

/** Several instances open at once: one cached client per registered credentials block. */
public final class ClientRegistry implements AutoCloseable {

    private final List<SdkConfig> blocks = new ArrayList<>();
    private final Map<String, Integer> names = new HashMap<>();
    private final List<String> labels = new ArrayList<>();
    private final Map<Integer, WebCommanderClient> clients = new HashMap<>();
    private final Set<Integer> supplied = new HashSet<>();

    /** A registry over a caller-owned credentials list; the first entry is the default. */
    public ClientRegistry(List<?> credentials) {
        if (credentials == null || credentials.isEmpty()) {
            throw new SdkUsageError("at least one credentials block is needed");
        }
        for (Object block : credentials) {
            blocks.add(asConfig(block));
        }
        indexDeclaredNames();
        for (SdkConfig block : blocks) {
            labels.add(block.getClientName());
        }
    }

    private static SdkConfig asConfig(Object block) {
        if (block instanceof SdkConfig config) {
            return config;
        }
        if (block instanceof AuthConfig auth) {
            return SdkConfig.builder(auth).build();
        }
        throw new SdkUsageError("credentials entries must be SdkConfig or AuthConfig, got "
                + (block == null ? "null" : block.getClass().getSimpleName()));
    }

    private void indexDeclaredNames() {
        for (int i = 0; i < blocks.size(); i++) {
            String name = blocks.get(i).getClientName();
            Integer earlier = names.putIfAbsent(name, i);
            if (earlier != null) {
                throw new SdkUsageError("two instances share clientName '" + name
                        + "' (positions " + earlier + " and " + i + "); names must be "
                        + "unique so one can be told from the other");
            }
        }
    }

    /** Turn a selector ({@code null}, a position or a {@code clientName}) into a position. */
    public int resolveIndex(Object name) {
        if (name == null) {
            return 0;
        }
        if (name instanceof Integer position) {
            int size = blocks.size();
            if (position >= -size && position < size) {
                return ((position % size) + size) % size;
            }
            throw new SdkUsageError("no instance at index " + position + "; "
                    + size + " registered (0-" + (size - 1) + ")");
        }
        if (name instanceof String clientName) {
            Integer index = names.get(clientName);
            if (index != null) {
                return index;
            }
            throw new SdkUsageError("unknown instance '" + clientName + "'; registered: "
                    + String.join(", ", labels));
        }
        throw new SdkUsageError("name must be null, an index or a clientName");
    }

    /** A client for one instance, built and cached on first use; this registry closes it. */
    public synchronized WebCommanderClient getClient(Object name) {
        int index = resolveIndex(name);
        return clients.computeIfAbsent(index,
                position -> new WebCommanderClient(blocks.get(position)));
    }

    /** The cached client for the default (first) instance. */
    public WebCommanderClient getClient() {
        return getClient(null);
    }

    /** Register a pre-built client under a name; its lifetime stays the caller's. */
    public synchronized void addClient(String name, WebCommanderClient client) {
        if (name == null || name.strip().isEmpty()) {
            throw new SdkUsageError("addClient needs a non-blank name; got '" + name
                    + "'. Anything else cannot be passed back to getClient — an int selects a "
                    + "position, so it would never reach the client at all");
        }
        if (names.containsKey(name)) {
            throw new SdkUsageError("instance '" + name + "' is already registered");
        }
        blocks.add(client.getConfig());
        int index = blocks.size() - 1;
        names.put(name, index);
        labels.add(name);
        clients.put(index, client);
        supplied.add(index);
    }

    /** Close every client this registry built, attempting each even when one fails. */
    public synchronized void closeAllClients() {
        List<String> failures = new ArrayList<>();
        Exception first = null;
        int attempted = 0;
        try {
            for (Map.Entry<Integer, WebCommanderClient> entry : new LinkedHashMap<>(clients).entrySet()) {
                if (supplied.contains(entry.getKey())) {
                    continue;
                }
                attempted++;
                try {
                    entry.getValue().close();
                } catch (Exception failure) {
                    failures.add(labels.get(entry.getKey()) + ": "
                            + Redaction.describeFailure(failure));
                    if (first == null) {
                        first = failure;
                    }
                }
            }
        } finally {
            clients.keySet().removeIf(index -> !supplied.contains(index));
        }
        if (first != null) {
            throw new SdkError(failures.size() + " of " + attempted
                    + " clients failed to close (" + String.join("; ", failures)
                    + "); all of them were attempted and dropped, so the registry is usable again",
                    first);
        }
    }

    /** Equivalent to {@link #closeAllClients()}. */
    @Override
    public void close() {
        closeAllClients();
    }

    private static final Object REGISTRATION_LOCK = new Object();
    private static List<SdkConfig> registered = List.of();

    /** The refusal every static entry point shares; it names both remedies. */
    private static final String NOTHING_REGISTERED =
            "no instances registered — call ClientRegistry.registerInstances(...) first, "
            + "or pass a credentials list to buildClient(instance, credentials, overrides)";

    /** Register every instance this process can talk to; the first is the default. */
    public static void registerInstances(List<?> credentials) {
        ClientRegistry validated = new ClientRegistry(credentials);
        synchronized (REGISTRATION_LOCK) {
            registered = List.copyOf(validated.blocks);
        }
    }

    /** The instances {@link #registerInstances} holds, first entry the default. */
    public static List<SdkConfig> getRegisteredInstances() {
        List<SdkConfig> snapshot;
        synchronized (REGISTRATION_LOCK) {
            snapshot = registered;
        }
        if (snapshot.isEmpty()) {
            throw new SdkUsageError(NOTHING_REGISTERED);
        }
        return snapshot;
    }

    /** One label per registered instance, default first. */
    public static List<String> listInstanceNames() {
        List<SdkConfig> snapshot = getRegisteredInstances();
        List<String> names = new ArrayList<>(snapshot.size());
        for (SdkConfig instance : snapshot) {
            names.add(instance.getClientName());
        }
        return List.copyOf(names);
    }

    /** A new, uncached client for the default registered instance; the caller closes it. */
    public static WebCommanderClient buildClient() {
        return buildClient(null);
    }

    /** A new client for the selected registered instance; a negative index counts from the end. */
    public static WebCommanderClient buildClient(Object instance) {
        return buildClient(instance, null);
    }

    /** A new client for a registered instance, with overrides applied to a copy of its config. */
    public static WebCommanderClient buildClient(Object instance,
            UnaryOperator<SdkConfig.Builder> overrides) {
        return buildClient(instance, null, overrides);
    }

    /** As above, selecting from caller-supplied credentials so process-wide state is untouched. */
    public static WebCommanderClient buildClient(Object instance, List<?> credentials,
            UnaryOperator<SdkConfig.Builder> overrides) {
        List<?> blocks;
        if (credentials != null) {
            blocks = credentials;
        } else {
            synchronized (REGISTRATION_LOCK) {
                blocks = registered;
            }
            if (blocks.isEmpty()) {
                throw new SdkUsageError(NOTHING_REGISTERED);
            }
        }
        return new WebCommanderClient(SdkConfig.fromCredentials(blocks, instance, overrides));
    }
}
