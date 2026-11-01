package app.pintabien.common.events;

import app.pintabien.common.messaging.PintabienAmqp;
import app.pintabien.common.model.EvaluationResultStatus;
import app.pintabien.common.model.TrafficLight;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** scoring-service -> core-service. Resultado final del semaforo. */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EvaluationCompletedEvent(
        String eventId,
        String correlationId,
        UUID tenantId,
        UUID candidateId,
        Instant occurredAt,
        UUID evaluationId,
        TrafficLight color,
        int score,
        EvaluationResultStatus status,
        String ruleSetVersion,
        List<FiredRule> firedRules,
        List<String> warnings
) implements DomainEvent {

    public static EvaluationCompletedEvent create(String correlationId, UUID tenantId, UUID candidateId, UUID evaluationId,
                                                  TrafficLight color, int score, EvaluationResultStatus status,
                                                  String ruleSetVersion, List<FiredRule> firedRules, List<String> warnings) {
        return new EvaluationCompletedEvent(UUID.randomUUID().toString(), correlationId, tenantId, candidateId, Instant.now(),
                evaluationId, color, score, status, ruleSetVersion,
                firedRules == null ? List.of() : firedRules, warnings == null ? List.of() : warnings);
    }

    @Override
    public String eventType() {
        return PintabienAmqp.RK_EVALUATION_COMPLETED;
    }
}
