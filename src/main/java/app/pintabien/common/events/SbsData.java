package app.pintabien.common.events;

import app.pintabien.common.model.SbsRating;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Reporte crediticio SBS normalizado. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SbsData(
        SbsRating rating,
        int cppDays,
        BigDecimal totalDebt,
        BigDecimal overdueDebt,
        int entitiesCount,
        LocalDate reportDate
) {}
