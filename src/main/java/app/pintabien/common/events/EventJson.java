package app.pintabien.common.events;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * ObjectMapper de referencia para (de)serializar eventos. Los servicios Spring pueden usar su propio
 * ObjectMapper siempre que respete estas opciones (fechas ISO-8601, ignorar propiedades desconocidas, enums desconocidos al valor por defecto).
 */
public final class EventJson {
    private EventJson() {}

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            // v1.2: un enum nuevo (p. ej. un producto) se lee como su valor por defecto en vez de romper al consumidor
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

    public static ObjectMapper mapper() {
        return MAPPER;
    }
}
