package app.pintabien.common.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/** Giro de la empresa cliente de Pintabien (v1.2): decide los productos y textos por defecto. */
public enum BusinessType {
    /** Financieras, cajas, cooperativas, prestamistas. */
    FINANCIERA,
    /** Tiendas con venta a plazos o credito directo (electro, muebles, celulares). */
    RETAIL,
    /** Concesionarios y venta de vehiculos o motos a credito. */
    AUTOMOTRIZ,
    /** Inmobiliarias, administradoras de alquiler, constructoras. */
    INMOBILIARIA,
    /** Servicios con pago recurrente: educacion, salud, telecomunicaciones. */
    SERVICIOS,
    @JsonEnumDefaultValue
    OTRO
}
