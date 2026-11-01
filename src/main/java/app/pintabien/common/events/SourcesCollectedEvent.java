package app.pintabien.common.events;

import app.pintabien.common.messaging.PintabienAmqp;
import app.pintabien.common.model.DocumentType;
import app.pintabien.common.model.Purpose;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.UUID;

/**
 * integrations-service -> scoring-service. Resultado por fuente con datos normalizados.
 * El documento viaja enmascarado: el scoring no necesita el DNI en claro.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SourcesCollectedEvent(
        String eventId,
        String correlationId,
        UUID tenantId,
        UUID candidateId,
        Instant occurredAt,
        DocumentType documentType,
        String documentNumberMasked,
        Purpose purpose,
        EvaluationContext context,
        ProviderResult<ReniecData> reniec,
        ProviderResult<SbsData> sbs,
        ProviderResult<InfocorpData> infocorp,
        ProviderResult<AssetsData> assets
) implements DomainEvent {

    public static SourcesCollectedEvent create(String correlationId, UUID tenantId, UUID candidateId,
                                               DocumentType documentType, String documentNumberMasked, Purpose purpose,
                                               EvaluationContext context,
                                               ProviderResult<ReniecData> reniec, ProviderResult<SbsData> sbs,
                                               ProviderResult<InfocorpData> infocorp, ProviderResult<AssetsData> assets) {
        return new SourcesCollectedEvent(UUID.randomUUID().toString(), correlationId, tenantId, candidateId, Instant.now(),
                documentType, documentNumberMasked, purpose, context == null ? EvaluationContext.defaults() : context,
                reniec, sbs, infocorp, assets);
    }

    @Override
    public String eventType() {
        return PintabienAmqp.RK_SOURCES_COLLECTED;
    }
}
