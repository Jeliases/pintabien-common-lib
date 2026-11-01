package app.pintabien.common.events;

import app.pintabien.common.messaging.PintabienAmqp;
import app.pintabien.common.model.DeclarationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * core-service -> scoring-service. Se publica al enviar (ENVIADA) y al validar/rechazar (VALIDADA/RECHAZADA)
 * una declaracion informativa; el scoring re-evalua con el ultimo snapshot de fuentes.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DeclarationSubmittedEvent(
        String eventId,
        String correlationId,
        UUID tenantId,
        UUID candidateId,
        Instant occurredAt,
        UUID declarationId,
        String economicActivity,
        BigDecimal monthlyIncomeAvg,
        int filesCount,
        DeclarationStatus status
) implements DomainEvent {

    public static DeclarationSubmittedEvent create(String correlationId, UUID tenantId, UUID candidateId, UUID declarationId,
                                                   String economicActivity, BigDecimal monthlyIncomeAvg, int filesCount,
                                                   DeclarationStatus status) {
        return new DeclarationSubmittedEvent(UUID.randomUUID().toString(), correlationId, tenantId, candidateId, Instant.now(),
                declarationId, economicActivity, monthlyIncomeAvg, filesCount, status);
    }

    @Override
    public String eventType() {
        return PintabienAmqp.RK_DECLARATION_SUBMITTED;
    }
}
