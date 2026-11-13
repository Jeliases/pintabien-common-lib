package app.pintabien.common.events;

import app.pintabien.common.model.Provider;
import app.pintabien.common.model.SourceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Resultado normalizado de una fuente. El campo data es null cuando status no es OK.
 * {@code mode} (v1.1) indica el origen del dato: "real", "mock" (simulado) u "off" (fuente no contratada).
 * Puede venir null de productores v1.0.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProviderResult<T>(
        Provider provider,
        SourceStatus status,
        T data,
        String error,
        long latencyMs,
        boolean fromCache,
        String mode
) {
    public static final String MODE_REAL = "real";
    public static final String MODE_MOCK = "mock";
    public static final String MODE_OFF = "off";

    /** Constructor del contrato v1.0. */
    public ProviderResult(Provider provider, SourceStatus status, T data, String error, long latencyMs, boolean fromCache) {
        this(provider, status, data, error, latencyMs, fromCache, null);
    }

    public static <T> ProviderResult<T> ok(Provider provider, T data, long latencyMs, boolean fromCache) {
        return new ProviderResult<>(provider, SourceStatus.OK, data, null, latencyMs, fromCache);
    }

    public static <T> ProviderResult<T> noData(Provider provider, long latencyMs) {
        return new ProviderResult<>(provider, SourceStatus.NO_DATA, null, null, latencyMs, false);
    }

    public static <T> ProviderResult<T> error(Provider provider, String error, long latencyMs) {
        return new ProviderResult<>(provider, SourceStatus.ERROR, null, error, latencyMs, false);
    }

    /** Fuente no contratada: no es un error ni cuenta como fuente critica faltante. */
    public static <T> ProviderResult<T> notContracted(Provider provider) {
        return new ProviderResult<>(provider, SourceStatus.NO_DATA, null, "NO_CONTRATADO", 0, false, MODE_OFF);
    }

    public ProviderResult<T> withMode(String newMode) {
        return new ProviderResult<>(provider, status, data, error, latencyMs, fromCache, newMode);
    }

    public boolean isOk() {
        return status == SourceStatus.OK && data != null;
    }

    public boolean isNotContracted() {
        return MODE_OFF.equals(mode);
    }

    public boolean isSimulated() {
        return MODE_MOCK.equals(mode);
    }
}
