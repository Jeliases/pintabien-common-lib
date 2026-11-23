package app.pintabien.common.events;

import app.pintabien.common.model.BusinessType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

/**
 * Datos del tenant/cliente que el motor de reglas necesita y que no provienen de proveedores externos.
 * Viaja desde core-service en {@code evaluation.requested} y se reenvia intacto en {@code sources.collected}.
 * {@code declaredMonthlyIncome} (v1.1): ingreso mensual promedio de la declaracion informativa, si existe.
 * {@code requestedAmount}, {@code termMonths} y {@code businessType} (v1.2): monto (o renta mensual, si es alquiler) y
 * plazo del credito solicitado, y giro de la empresa. Todos opcionales: null si no se indicaron.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EvaluationContext(
        BigDecimal utilityDebtToleranceAmount,
        boolean utilityDebtToleranceEnabled,
        boolean declarationSubmitted,
        boolean declarationValidated,
        BigDecimal declaredMonthlyIncome,
        BigDecimal requestedAmount,
        Integer termMonths,
        BusinessType businessType
) {
    /** Constructor del contrato v1.0. */
    public EvaluationContext(BigDecimal utilityDebtToleranceAmount, boolean utilityDebtToleranceEnabled,
                             boolean declarationSubmitted, boolean declarationValidated) {
        this(utilityDebtToleranceAmount, utilityDebtToleranceEnabled, declarationSubmitted, declarationValidated, null, null, null, null);
    }

    /** Constructor del contrato v1.1. */
    public EvaluationContext(BigDecimal utilityDebtToleranceAmount, boolean utilityDebtToleranceEnabled,
                             boolean declarationSubmitted, boolean declarationValidated, BigDecimal declaredMonthlyIncome) {
        this(utilityDebtToleranceAmount, utilityDebtToleranceEnabled, declarationSubmitted, declarationValidated, declaredMonthlyIncome,
                null, null, null);
    }

    public static EvaluationContext defaults() {
        return new EvaluationContext(new BigDecimal("150.00"), true, false, false, null, null, null, null);
    }
}
