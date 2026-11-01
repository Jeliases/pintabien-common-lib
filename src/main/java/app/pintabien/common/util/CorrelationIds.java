package app.pintabien.common.util;

import java.util.UUID;

public final class CorrelationIds {
    private CorrelationIds() {}

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    public static String orNew(String existing) {
        return (existing == null || existing.isBlank()) ? newId() : existing;
    }
}
