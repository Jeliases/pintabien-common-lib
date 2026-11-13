package app.pintabien.common.events;

import static org.assertj.core.api.Assertions.assertThat;

import app.pintabien.common.model.DeclarationStatus;
import app.pintabien.common.model.DocumentType;
import app.pintabien.common.model.EvaluationResultStatus;
import app.pintabien.common.model.Provider;
import app.pintabien.common.model.Purpose;
import app.pintabien.common.model.SbsRating;
import app.pintabien.common.model.TrafficLight;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/** Tests de contrato: cada evento debe sobrevivir un round-trip JSON y tolerar campos desconocidos. */
class EventContractsTest {

    private final ObjectMapper mapper = EventJson.mapper();
    private final UUID tenant = UUID.randomUUID();
    private final UUID candidate = UUID.randomUUID();

    @Test
    void evaluationRequestedRoundTrip() throws Exception {
        var event = EvaluationRequestedEvent.create("corr-1", tenant, candidate, DocumentType.DNI, "44123451",
                Purpose.ALQUILER, UUID.randomUUID(), new EvaluationContext(new BigDecimal("150.00"), true, false, false));

        String json = mapper.writeValueAsString(event);
        var back = mapper.readValue(json, EvaluationRequestedEvent.class);

        assertThat(back).isEqualTo(event);
        assertThat(back.eventType()).isEqualTo("evaluation.requested");
        assertThat(json).contains("\"occurredAt\":\"");
    }

    @Test
    void sourcesCollectedRoundTripWithTypedProviderResults() throws Exception {
        var sbs = ProviderResult.ok(Provider.SBS,
                new SbsData(SbsRating.NORMAL, 0, new BigDecimal("1200.50"), BigDecimal.ZERO, 2, LocalDate.of(2026, 10, 1)), 120, false);
        var infocorp = ProviderResult.ok(Provider.INFOCORP, new InfocorpData(false, false, BigDecimal.ZERO, false, List.of(),
                false, true, false, true, new BigDecimal("3500"), true, true), 90, true);
        var assets = ProviderResult.ok(Provider.ASSETS, new AssetsData(
                List.of(new AssetsData.Property("P12345", "DEPARTAMENTO", "Surco", new BigDecimal("250000"))),
                List.of(new AssetsData.Vehicle("ABC-123", "Toyota", "Yaris", 2019))), 300, false);
        ProviderResult<ReniecData> reniec = ProviderResult.error(Provider.RENIEC, "timeout", 3000);

        var event = SourcesCollectedEvent.create("corr-2", tenant, candidate, DocumentType.DNI, "44****51", Purpose.COMPRA,
                EvaluationContext.defaults(), reniec, sbs, infocorp, assets);

        String json = mapper.writeValueAsString(event);
        var back = mapper.readValue(json, SourcesCollectedEvent.class);

        assertThat(back).isEqualTo(event);
        assertThat(back.sbs().data().rating()).isEqualTo(SbsRating.NORMAL);
        assertThat(back.assets().data().count()).isEqualTo(2);
        assertThat(back.reniec().isOk()).isFalse();
        assertThat(json).doesNotContain("44123451");
    }

    @Test
    void evaluationCompletedRoundTrip() throws Exception {
        var fired = new FiredRule("rojo-mora", "Mora o clasificacion negativa", "ROJO", 1, "Deuda impaga", Map.of("infocorpDelinquent", true));
        var event = EvaluationCompletedEvent.create("corr-3", tenant, candidate, UUID.randomUUID(), TrafficLight.ROJO, 12,
                EvaluationResultStatus.COMPLETA, "global-v1", List.of(fired), List.of("FUENTE_NO_DISPONIBLE:RENIEC"));

        String json = mapper.writeValueAsString(event);
        var back = mapper.readValue(json, EvaluationCompletedEvent.class);

        assertThat(back).isEqualTo(event);
        assertThat(back.color().label()).isEqualTo("No Apto");
    }

    @Test
    void declarationSubmittedRoundTrip() throws Exception {
        var event = DeclarationSubmittedEvent.create("corr-4", tenant, candidate, UUID.randomUUID(), "Comercio independiente",
                new BigDecimal("2800.00"), 3, DeclarationStatus.ENVIADA);

        var back = mapper.readValue(mapper.writeValueAsString(event), DeclarationSubmittedEvent.class);
        assertThat(back).isEqualTo(event);
    }

    @Test
    void unknownFieldsAreIgnoredForBackwardCompatibility() throws Exception {
        String json = """
                {"eventId":"e1","correlationId":"c1","tenantId":"%s","candidateId":"%s","occurredAt":"2026-11-01T10:00:00Z",
                 "evaluationId":"%s","color":"VERDE","score":90,"status":"COMPLETA","ruleSetVersion":"global-v1",
                 "firedRules":[],"warnings":[],"nuevoCampoFuturo":"x"}
                """.formatted(tenant, candidate, UUID.randomUUID());

        var back = mapper.readValue(json, EvaluationCompletedEvent.class);
        assertThat(back.color()).isEqualTo(TrafficLight.VERDE);
        assertThat(back.score()).isEqualTo(90);
    }

    @Test
    void sbsDataV10JsonStillReadable() throws Exception {
        String v10 = """
                {"rating":"NORMAL","cppDays":0,"totalDebt":1200.00,"overdueDebt":0,"entitiesCount":2,"reportDate":"2026-10-01"}""";
        SbsData back = mapper.readValue(v10, SbsData.class);
        assertThat(back.rating()).isEqualTo(SbsRating.NORMAL);
        assertThat(back.history()).isNull();
        assertThat(back.hasHistory()).isTrue();
    }

    @Test
    void sbsDataV11RoundTripWithDetail() throws Exception {
        var data = new SbsData(SbsRating.PERDIDA, 0, BigDecimal.ZERO, new BigDecimal("188.85"), 1, LocalDate.of(2026, 4, 28),
                new BigDecimal("188.85"), BigDecimal.ZERO, BigDecimal.ZERO, SbsRating.PERDIDA, 7, 9, LocalDate.of(2023, 12, 28),
                List.of(new SbsData.Entity("BANCO X", SbsRating.PERDIDA, 0, BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal("188.85"),
                        List.of("CREDITOS CASTIGADOS"), "2023-12", "2026-04", true)),
                List.of(new SbsData.Month("2026-04", SbsRating.PERDIDA, BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal("188.85"), 1, 0)));
        var back = mapper.readValue(mapper.writeValueAsString(data), SbsData.class);
        assertThat(back).isEqualTo(data);
    }

    @Test
    void providerResultModeIsOptional() throws Exception {
        var legacy = mapper.readValue("{\"provider\":\"SBS\",\"status\":\"NO_DATA\",\"latencyMs\":5,\"fromCache\":false}", ProviderResult.class);
        assertThat(legacy.mode()).isNull();
        ProviderResult<SbsData> off = ProviderResult.notContracted(Provider.INFOCORP);
        assertThat(off.isNotContracted()).isTrue();
        assertThat(off.isOk()).isFalse();
    }
}
