package app.pintabien.common.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

/**
 * Datos del tenant/candidato que el motor de reglas necesita y que no provienen de proveedores externos.
 * Viaja desde core-service en {@code evaluation.requested} y se reenvia intacto en {@code sources.collected}.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EvaluationContext(
        BigDecimal utilityDebtToleranceAmount,
        boolean utilityDebtToleranceEnabled,
        boolean declarationSubmitted,
        boolean declarationValidated
) {
    public static EvaluationContext defaults() {
        return new EvaluationContext(new BigDecimal("150.00"), true, false, false);
    }
}
