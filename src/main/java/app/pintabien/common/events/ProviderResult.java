package app.pintabien.common.events;

import app.pintabien.common.model.Provider;
import app.pintabien.common.model.SourceStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Resultado normalizado de una fuente. El campo data es null cuando status no es OK. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProviderResult<T>(
        Provider provider,
        SourceStatus status,
        T data,
        String error,
        long latencyMs,
        boolean fromCache
) {
    public static <T> ProviderResult<T> ok(Provider provider, T data, long latencyMs, boolean fromCache) {
        return new ProviderResult<>(provider, SourceStatus.OK, data, null, latencyMs, fromCache);
    }

    public static <T> ProviderResult<T> noData(Provider provider, long latencyMs) {
        return new ProviderResult<>(provider, SourceStatus.NO_DATA, null, null, latencyMs, false);
    }

    public static <T> ProviderResult<T> error(Provider provider, String error, long latencyMs) {
        return new ProviderResult<>(provider, SourceStatus.ERROR, null, error, latencyMs, false);
    }

    public boolean isOk() {
        return status == SourceStatus.OK && data != null;
    }
}
