package app.pintabien.common.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Producto de credito que el cliente solicita (v1.2). Pintabien evalua aptitud crediticia en general: el producto decide
 * la cuota y la recomendacion principal del informe, no si se consultan las fuentes.
 *
 * <p>{@code ALQUILER} y {@code COMPRA} son del contrato v1.0 y se mantienen ({@code COMPRA} = compra de inmueble, equivale
 * a {@code HIPOTECARIO}). Un valor desconocido para un consumidor se lee como {@code OTRO} cuando su ObjectMapper activa
 * {@code READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE} (ver {@code EventJson}).
 */
public enum Purpose {
    ALQUILER,
    COMPRA,
    PRESTAMO_PERSONAL,
    CONSUMO,
    VEHICULAR,
    HIPOTECARIO,
    TARJETA,
    NEGOCIO,
    @JsonEnumDefaultValue
    OTRO;

    /** Productos de credito con cuota (todos menos el alquiler, que se compara con la renta mensual). */
    public boolean isLoan() {
        return this != ALQUILER && this != OTRO;
    }

    /** COMPRA (v1.0) se trata como hipotecario. */
    public Purpose normalized() {
        return this == COMPRA ? HIPOTECARIO : this;
    }
}
