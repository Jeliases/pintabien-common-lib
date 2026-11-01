package app.pintabien.common.events;

import app.pintabien.common.messaging.PintabienAmqp;
import app.pintabien.common.model.DocumentType;
import app.pintabien.common.model.Purpose;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.UUID;

/**
 * core-service -> integrations-service. Pide recolectar fuentes para un candidato.
 * El documento viaja en claro solo dentro del broker interno; nunca debe loguearse sin enmascarar.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EvaluationRequestedEvent(
        String eventId,
        String correlationId,
        UUID tenantId,
        UUID candidateId,
        Instant occurredAt,
        DocumentType documentType,
        String documentNumber,
        Purpose purpose,
        UUID requestedByUserId,
        EvaluationContext context
) implements DomainEvent {

    public static EvaluationRequestedEvent create(String correlationId, UUID tenantId, UUID candidateId,
                                                  DocumentType documentType, String documentNumber, Purpose purpose,
                                                  UUID requestedByUserId, EvaluationContext context) {
        return new EvaluationRequestedEvent(UUID.randomUUID().toString(), correlationId, tenantId, candidateId,
                Instant.now(), documentType, documentNumber, purpose, requestedByUserId,
                context == null ? EvaluationContext.defaults() : context);
    }

    @Override
    public String eventType() {
        return PintabienAmqp.RK_EVALUATION_REQUESTED;
    }
}
