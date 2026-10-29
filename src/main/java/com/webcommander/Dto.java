package com.webcommander;

import com.webcommander.errors.SdkUsageError;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/** The typed view over a body — what every DTO is built from. */
public abstract class Dto {

    /** Only a subclass constructor reaches this. */
    protected Dto() {
    }

    private static final Map<Class<?>, Map<String, Field>> RESOLVED = new ConcurrentHashMap<>();

    /** The key the API wraps this record in, or {@code null} when the body is the record itself. */
    protected String envelopeKey() {
        return null;
    }

    /** Wire names {@link #toMap()} writes beside the envelope rather than inside it. */
    protected Set<String> topLevel() {
        return Set.of();
    }

    /** This type's fields in declaration order, the order a payload writes them, by wire name. */
    public static Map<String, Type> fields(Class<? extends Dto> type) {
        Map<String, Type> declared = new LinkedHashMap<>();
        for (Map.Entry<String, Field> entry : declaredFields(type).entrySet()) {
            declared.put(entry.getKey(), entry.getValue().getGenericType());
        }
        return Collections.unmodifiableMap(declared);
    }

    /** Read a decoded body into this type; a missing or mistyped field reads {@code null}. */
    public static <T extends Dto> T fromMap(Class<T> type, Object body) {
        if (!(body instanceof Map<?, ?> mapping)) {
            throw new SdkUsageError(type.getSimpleName() + ".fromMap expects a decoded "
                    + "mapping body, got " + (body == null ? "null" : body.getClass().getSimpleName())
                    + " — a binary or empty body has no DTO");
        }

        T view = instantiate(type);
        Map<String, Object> source = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : mapping.entrySet()) {
            source.put(String.valueOf(entry.getKey()), entry.getValue());
        }

        String envelope = view.envelopeKey();
        if (envelope != null && source.get(envelope) instanceof Map<?, ?> inner) {
            Map<String, Object> unwrapped = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : inner.entrySet()) {
                unwrapped.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            for (Map.Entry<String, Object> entry : source.entrySet()) {
                if (!entry.getKey().equals(envelope)) {
                    unwrapped.put(entry.getKey(), entry.getValue());
                }
            }
            source = unwrapped;
        }

        for (Map.Entry<String, Field> entry : declaredFields(type).entrySet()) {
            if (!source.containsKey(entry.getKey())) {
                continue;
            }
            Field field = entry.getValue();
            set(view, field, read(source.get(entry.getKey()), field.getGenericType()));
        }
        return view;
    }

    /** The body this DTO stands for, envelope included: what the SDK sends as a payload. */
    public Map<String, Object> toMap() {
        Map<String, Object> record = new LinkedHashMap<>();
        Map<String, Object> beside = new LinkedHashMap<>();
        Set<String> outside = topLevel();
        for (Map.Entry<String, Field> entry : declaredFields(getClass()).entrySet()) {
            Object value = get(this, entry.getValue());
            if (value == null) {
                continue;
            }
            (outside.contains(entry.getKey()) ? beside : record)
                    .put(entry.getKey(), write(value));
        }

        String envelope = envelopeKey();
        Map<String, Object> body = new LinkedHashMap<>();
        if (envelope == null) {
            body.putAll(record);
        } else {
            body.put(envelope, record);
        }
        body.putAll(beside);
        return body;
    }

    /** Equal when the same type carries the same field values. */
    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        for (Field field : declaredFields(getClass()).values()) {
            if (!Objects.equals(get(this, field), get((Dto) other, field))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public final int hashCode() {
        List<Object> values = new ArrayList<>();
        values.add(getClass());
        for (Field field : declaredFields(getClass()).values()) {
            values.add(get(this, field));
        }
        return values.hashCode();
    }

    /** A rendering safe to print, secrets masked and unset fields omitted. */
    @Override
    public String toString() {
        StringBuilder shown = new StringBuilder(getClass().getSimpleName()).append('(');
        boolean first = true;
        for (Map.Entry<String, Field> entry : declaredFields(getClass()).entrySet()) {
            Object value = get(this, entry.getValue());
            if (value == null) {
                continue;
            }
            if (!first) {
                shown.append(", ");
            }
            first = false;
            shown.append(entry.getKey()).append('=').append(Redaction.shouldRedact(entry.getKey())
                    ? Redaction.REDACTED : Redaction.redactValue(value));
        }
        return shown.append(')').toString();
    }

    /** One value as its declared type, or {@code null} when it is not that type. */
    private static Object read(Object value, Type declared) {
        if (value == null) {
            return null;
        }

        if (declared instanceof ParameterizedType generic && generic.getRawType() == List.class) {
            if (!(value instanceof List<?> entries)) {
                return null;
            }
            Type item = generic.getActualTypeArguments()[0];
            List<Object> read = new ArrayList<>(entries.size());
            for (Object entry : entries) {
                read.add(read(entry, item));
            }
            return Collections.unmodifiableList(read);
        }

        if (!(declared instanceof Class<?> type)) {
            return value;
        }
        if (Dto.class.isAssignableFrom(type)) {
            return value instanceof Map ? fromMap(type.asSubclass(Dto.class), value) : null;
        }
        if (type == Object.class) {
            return value;
        }
        if (type == Double.class) {
            // Jackson decodes 0 as an Integer; the one widening allowed, and lossless.
            return value instanceof Number amount ? Double.valueOf(amount.doubleValue()) : null;
        }
        if (type == Long.class) {
            return isWholeNumber(value) ? Long.valueOf(((Number) value).longValue()) : null;
        }
        return type.isInstance(value) ? value : null;
    }

    /** Whether a decoded value is a JSON integer. */
    private static boolean isWholeNumber(Object value) {
        return value instanceof Integer || value instanceof Long
                || value instanceof Short || value instanceof Byte
                || value instanceof BigInteger;
    }

    /** One value as the API receives it: a nested DTO becomes its record, lists walked for them. */
    private static Object write(Object value) {
        if (value instanceof Dto nested) {
            Map<String, Object> record = nested.toMap();
            // Nested inside its parent's record, so its own envelope would be an extra layer.
            String envelope = nested.envelopeKey();
            return envelope == null ? record : record.get(envelope);
        }
        if (value instanceof List<?> entries) {
            List<Object> written = new ArrayList<>(entries.size());
            for (Object entry : entries) {
                written.add(write(entry));
            }
            return written;
        }
        if (value instanceof Map<?, ?> mapping) {
            Map<Object, Object> written = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : mapping.entrySet()) {
                written.put(entry.getKey(), write(entry.getValue()));
            }
            return written;
        }
        return value;
    }

    /** The instance fields a DTO class declares, in declaration order, keyed by wire name. */
    private static Map<String, Field> declaredFields(Class<?> type) {
        return RESOLVED.computeIfAbsent(type, resolving -> {
            Map<String, Field> found = new LinkedHashMap<>();
            for (Class<?> level : declaringChain(resolving)) {
                for (Field field : level.getDeclaredFields()) {
                    if (field.isSynthetic() || Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    found.put(wireName(field.getName()), field);
                }
            }
            return Collections.unmodifiableMap(found);
        });
    }

    /** The DTO classes making up one type, base first, so a shared base's fields come first. */
    private static List<Class<?>> declaringChain(Class<?> type) {
        List<Class<?>> chain = new ArrayList<>();
        for (Class<?> level = type;
                level != null && level != Dto.class && Dto.class.isAssignableFrom(level);
                level = level.getSuperclass()) {
            chain.add(0, level);
        }
        return chain;
    }

    /** The wire spelling: {@code firstName} is {@code first_name}; an all-lower name stands. */
    private static String wireName(String fieldName) {
        if (isWireSpelling(fieldName)) {
            return fieldName;
        }
        StringBuilder wire = new StringBuilder(fieldName.length() + 4);
        for (int index = 0; index < fieldName.length(); index++) {
            if (index > 0 && startsWireWord(fieldName, index)) {
                wire.append('_');
            }
            wire.append(Character.toLowerCase(fieldName.charAt(index)));
        }
        return wire.toString();
    }

    /** Whether a field name is already the wire's own spelling, which one capital rules out. */
    private static boolean isWireSpelling(String fieldName) {
        for (int index = 0; index < fieldName.length(); index++) {
            if (Character.isUpperCase(fieldName.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    /** Whether one character opens a new wire word: a capital always, a digit usually. */
    private static boolean startsWireWord(String fieldName, int index) {
        char letter = fieldName.charAt(index);
        if (Character.isUpperCase(letter)) {
            return true;
        }
        if (!Character.isDigit(letter) || Character.isDigit(fieldName.charAt(index - 1))) {
            return false;
        }
        int next = index + 1;
        return next >= fieldName.length() || !Character.isLowerCase(fieldName.charAt(next));
    }

    private static <T extends Dto> T instantiate(Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (ReflectiveOperationException unreachable) {
            throw new SdkUsageError(type.getSimpleName() + " cannot be built: a DTO needs a "
                    + "public no-argument constructor", unreachable);
        }
    }

    private static Object get(Dto view, Field field) {
        try {
            return field.get(view);
        } catch (IllegalAccessException unreachable) {
            throw new SdkUsageError(view.getClass().getSimpleName() + "." + field.getName()
                    + " is not readable: a DTO's fields are its declaration and must be "
                    + "public", unreachable);
        }
    }

    private static void set(Dto view, Field field, Object value) {
        try {
            field.set(view, value);
        } catch (IllegalAccessException unreachable) {
            throw new SdkUsageError(view.getClass().getSimpleName() + "." + field.getName()
                    + " is not writable: a DTO's fields are its declaration and must be "
                    + "public and non-final", unreachable);
        }
    }
}
