package app.pintabien.common.model;

/** Codigos de advertencia estandar que puede emitir el motor de reglas. */
public final class Warnings {
    private Warnings() {}

    public static final String DEUDA_MENOR_SERVICIOS = "DEUDA_MENOR_SERVICIOS";
    public static final String FUENTE_NO_DISPONIBLE_PREFIX = "FUENTE_NO_DISPONIBLE:";
    public static final String REQUIERE_DECLARACION = "REQUIERE_DECLARACION";
    public static final String REQUIERE_EVALUACION_MANUAL = "REQUIERE_EVALUACION_MANUAL";
    public static final String DECLARACION_VALIDADA = "DECLARACION_VALIDADA";
    public static final String PATRIMONIO_SIN_HISTORIAL = "PATRIMONIO_SIN_HISTORIAL";

    public static String fuenteNoDisponible(Provider provider) {
        return FUENTE_NO_DISPONIBLE_PREFIX + provider.name();
    }
}
