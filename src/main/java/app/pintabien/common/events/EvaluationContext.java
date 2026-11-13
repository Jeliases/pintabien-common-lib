package app.pintabien.common.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

/**
 * Datos del tenant/candidato que el motor de reglas necesita y que no provienen de proveedores externos.
 * Viaja desde core-service en {@code evaluation.requested} y se reenvia intacto en {@code sources.collected}.
 * {@code declaredMonthlyIncome} (v1.1): ingreso mensual promedio de la declaracion informativa, si existe.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EvaluationContext(
        BigDecimal utilityDebtToleranceAmount,
        boolean utilityDebtToleranceEnabled,
        boolean declarationSubmitted,
        boolean declarationValidated,
        BigDecimal declaredMonthlyIncome
) {
    /** Constructor del contrato v1.0. */
    public EvaluationContext(BigDecimal utilityDebtToleranceAmount, boolean utilityDebtToleranceEnabled,
                             boolean declarationSubmitted, boolean declarationValidated) {
        this(utilityDebtToleranceAmount, utilityDebtToleranceEnabled, declarationSubmitted, declarationValidated, null);
    }

    public static EvaluationContext defaults() {
        return new EvaluationContext(new BigDecimal("150.00"), true, false, false, null);
    }
}
