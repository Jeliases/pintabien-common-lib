package app.pintabien.common.model;

/** Proveedores externos consultados por integrations-service. */
public enum Provider {
    RENIEC,
    SBS,
    INFOCORP,
    ASSETS;

    /** SBS e Infocorp son fuentes criticas: sin ellas la evaluacion queda INCOMPLETA. */
    public boolean critical() {
        return this == SBS || this == INFOCORP;
    }
}
