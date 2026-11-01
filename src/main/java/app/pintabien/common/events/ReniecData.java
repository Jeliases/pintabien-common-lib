package app.pintabien.common.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReniecData(
        String fullName,
        String firstNames,
        String lastNames,
        boolean documentValid
) {}
