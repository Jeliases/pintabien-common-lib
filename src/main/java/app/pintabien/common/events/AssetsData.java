package app.pintabien.common.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.List;

/** Patrimonio: predios y vehiculos registrados a nombre del documento. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record AssetsData(List<Property> properties, List<Vehicle> vehicles) {

    public int count() {
        return (properties == null ? 0 : properties.size()) + (vehicles == null ? 0 : vehicles.size());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Property(String registryId, String type, String district, BigDecimal estimatedValue) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Vehicle(String plate, String brand, String model, Integer year) {}
}
