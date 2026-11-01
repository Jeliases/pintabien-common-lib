package app.pintabien.common.events;

import java.time.Instant;
import java.util.UUID;

/**
 * Contrato comun de todos los eventos publicados en el exchange {@code pintabien.events}.
 * Los consumidores deben ser idempotentes: descartar un {@code eventId} ya procesado.
 */
public interface DomainEvent {
    String eventId();
    String correlationId();
    UUID tenantId();
    UUID candidateId();
    Instant occurredAt();

    /** Routing key del evento (ver {@link app.pintabien.common.messaging.PintabienAmqp}). */
    String eventType();
}
