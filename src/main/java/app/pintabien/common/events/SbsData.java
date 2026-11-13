package app.pintabien.common.events;

import app.pintabien.common.model.SbsRating;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Reporte crediticio SBS normalizado (Reporte Crediticio Consolidado, RCC).
 *
 * <p>Los seis primeros campos son el contrato v1.0. Desde v1.1 se agregan el detalle por entidad, el historial mensual y los
 * indicadores derivados; pueden venir null de productores antiguos, por eso los consumidores deben tolerarlos.
 *
 * @param rating            clasificacion vigente (la peor entre las entidades del ultimo periodo reportado)
 * @param cppDays           maximo de dias de atraso en el ultimo periodo
 * @param totalDebt         deuda directa vigente (sin rendimientos devengados ni garantias)
 * @param overdueDebt       deuda vencida + en cobranza judicial + castigada
 * @param entitiesCount     entidades que reportan deuda en el ultimo periodo
 * @param reportDate        fecha del ultimo periodo reportado
 * @param writtenOffDebt    creditos castigados (cuenta 81) en el ultimo periodo  (v1.1)
 * @param contingentDebt    creditos indirectos / contingentes (cuentas 71-72)     (v1.1)
 * @param normalPercent     % de la deuda calificada como Normal (0-100)           (v1.1)
 * @param worstRating12m    peor clasificacion de los ultimos 12 meses             (v1.1)
 * @param monthsWithIssues12m meses de los ultimos 12 con atraso o calificacion distinta de Normal (v1.1)
 * @param monthsReported    periodos con informacion en el historial               (v1.1)
 * @param firstReportDate   primer periodo con informacion (antiguedad crediticia) (v1.1)
 * @param entities          entidades vistas en el historial con su estado        (v1.1)
 * @param history           historial mensual, del mas antiguo al mas reciente     (v1.1)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SbsData(
        SbsRating rating,
        int cppDays,
        BigDecimal totalDebt,
        BigDecimal overdueDebt,
        int entitiesCount,
        LocalDate reportDate,
        BigDecimal writtenOffDebt,
        BigDecimal contingentDebt,
        BigDecimal normalPercent,
        SbsRating worstRating12m,
        Integer monthsWithIssues12m,
        Integer monthsReported,
        LocalDate firstReportDate,
        List<Entity> entities,
        List<Month> history
) {
    /** Constructor del contrato v1.0 (sin detalle). */
    public SbsData(SbsRating rating, int cppDays, BigDecimal totalDebt, BigDecimal overdueDebt, int entitiesCount, LocalDate reportDate) {
        this(rating, cppDays, totalDebt, overdueDebt, entitiesCount, reportDate, null, null, null, null, null, null, null, null, null);
    }

    /** true si el RCC trae al menos un periodo con informacion (persona bancarizada). */
    public boolean hasHistory() {
        return (monthsReported != null && monthsReported > 0) || entitiesCount > 0
                || (history != null && !history.isEmpty());
    }

    /**
     * Entidad financiera que reporto al deudor.
     *
     * @param name         razon social de la entidad
     * @param rating       clasificacion en el ultimo periodo en que reporto
     * @param daysOverdue  dias de atraso en el ultimo periodo en que reporto
     * @param directDebt   deuda directa (sin rendimientos) en el ultimo periodo en que reporto
     * @param overdueDebt  vencida + judicial en el ultimo periodo
     * @param writtenOff   castigada en el ultimo periodo
     * @param products     tipos de credito (descripcion SBS) del ultimo periodo
     * @param firstPeriod  primer periodo en que aparece (yyyy-MM)
     * @param lastPeriod   ultimo periodo en que aparece (yyyy-MM)
     * @param active       aparece en el ultimo periodo del reporte
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Entity(String name, SbsRating rating, int daysOverdue, BigDecimal directDebt, BigDecimal overdueDebt,
                         BigDecimal writtenOff, List<String> products, String firstPeriod, String lastPeriod, boolean active) {}

    /**
     * Foto de un periodo del RCC.
     *
     * @param period       yyyy-MM
     * @param rating       peor clasificacion del periodo
     * @param directDebt   deuda directa total
     * @param overdueDebt  vencida + judicial
     * @param writtenOff   castigada
     * @param entities     entidades que reportan en el periodo
     * @param maxDaysOverdue maximo atraso del periodo
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Month(String period, SbsRating rating, BigDecimal directDebt, BigDecimal overdueDebt, BigDecimal writtenOff,
                        int entities, int maxDaysOverdue) {}
}
