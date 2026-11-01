package app.pintabien.common.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

/** Regla que disparo y con que datos (explicacion del resultado). */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FiredRule(
        String ruleId,
        String name,
        String color,
        int priority,
        String message,
        Map<String, Object> matchedFacts
) {}
