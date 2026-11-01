package app.pintabien.common.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.List;

/**
 * Reporte Infocorp/Equifax normalizado. Incluye los indicadores de formalidad laboral e ingresos
 * que el proveedor reporta (planilla 5ta, 4ta/3ra categoria, historial tributario).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record InfocorpData(
        boolean delinquent,
        boolean hasProtests,
        BigDecimal unpaidDebtTotal,
        boolean unpaidDebtOnlyUtilities,
        List<UnpaidDebt> unpaidDebts,
        boolean hasEvictionLawsuit,
        boolean payrollDetected,
        boolean formalFreelancerDetected,
        boolean incomeProven,
        BigDecimal estimatedMonthlyIncome,
        boolean hasBankHistory,
        boolean hasTaxHistory
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UnpaidDebt(String creditor, String category, BigDecimal amount, int daysOverdue) {
        public static final String CATEGORY_SERVICIOS = "SERVICIOS";
        public static final String CATEGORY_FINANCIERA = "FINANCIERA";
        public static final String CATEGORY_OTRO = "OTRO";
    }
}
