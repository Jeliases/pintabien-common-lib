package app.pintabien.common.util;

import java.util.Optional;
import java.util.UUID;

/**
 * Contexto del request (tenant, usuario, correlationId) en un ThreadLocal.
 * Lo rellena un filtro HTTP o un listener AMQP y lo limpia al terminar.
 */
public final class TenantContext {
    private TenantContext() {}

    private record Ctx(UUID tenantId, UUID userId, String role, String correlationId) {}

    private static final ThreadLocal<Ctx> HOLDER = new ThreadLocal<>();

    public static void set(UUID tenantId, UUID userId, String role, String correlationId) {
        HOLDER.set(new Ctx(tenantId, userId, role, CorrelationIds.orNew(correlationId)));
    }

    public static void set(UUID tenantId, String correlationId) {
        set(tenantId, null, null, correlationId);
    }

    public static void clear() {
        HOLDER.remove();
    }

    public static Optional<UUID> tenantId() {
        Ctx c = HOLDER.get();
        return c == null ? Optional.empty() : Optional.ofNullable(c.tenantId());
    }

    public static UUID requireTenantId() {
        return tenantId().orElseThrow(() -> new IllegalStateException("No hay tenant en el contexto"));
    }

    public static Optional<UUID> userId() {
        Ctx c = HOLDER.get();
        return c == null ? Optional.empty() : Optional.ofNullable(c.userId());
    }

    public static Optional<String> role() {
        Ctx c = HOLDER.get();
        return c == null ? Optional.empty() : Optional.ofNullable(c.role());
    }

    public static String correlationId() {
        Ctx c = HOLDER.get();
        return c == null ? CorrelationIds.newId() : c.correlationId();
    }

    public static boolean isPresent() {
        return HOLDER.get() != null;
    }
}
