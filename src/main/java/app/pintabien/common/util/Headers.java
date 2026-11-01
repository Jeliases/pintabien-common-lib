package app.pintabien.common.util;

/** Cabeceras internas que el gateway propaga a los servicios. */
public final class Headers {
    private Headers() {}

    public static final String TENANT_ID = "X-Tenant-Id";
    public static final String USER_ID = "X-User-Id";
    public static final String USER_ROLE = "X-User-Role";
    public static final String CORRELATION_ID = "X-Correlation-Id";
    /** Secreto compartido gateway -> servicios. Sin el, los servicios rechazan la peticion. */
    public static final String INTERNAL_TOKEN = "X-Internal-Token";
}
