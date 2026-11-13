# pintabien-common-lib

Contratos de eventos, enums compartidos y utilidades pequeñas de **Pintabien.app**.
Se publica como artefacto Maven (`app.pintabien:pintabien-common-lib`) con **SemVer**.

> Regla de oro: los cambios en eventos deben ser **retrocompatibles** (agregar campos, nunca quitarlos ni
> renombrarlos). Un cambio incompatible exige un **nuevo evento** (p. ej. `EvaluationCompletedV2Event`) y
> una versión MAJOR de la librería.

## Contenido

| Paquete | Qué hay |
|---|---|
| `app.pintabien.common.events` | `DomainEvent` y los 4 eventos: `EvaluationRequestedEvent`, `SourcesCollectedEvent`, `EvaluationCompletedEvent`, `DeclarationSubmittedEvent`; DTOs de proveedores (`ReniecData`, `SbsData`, `InfocorpData`, `AssetsData`), `ProviderResult<T>`, `FiredRule`, `EvaluationContext`, `EventJson` |
| `app.pintabien.common.model` | Enums: `TrafficLight`, `EvaluationResultStatus`, `DocumentType`, `Purpose`, `Provider`, `SourceStatus`, `SbsRating`, `DeclarationStatus`, `CandidateEvaluationStatus`; códigos de `Warnings` |
| `app.pintabien.common.messaging` | `PintabienAmqp`: exchange `pintabien.events`, DLX, routing keys y convención de nombres de colas |
| `app.pintabien.common.util` | `DocumentMasker` (`44****45`), `DocumentHasher` (SHA-256 para claves de caché), `CorrelationIds`, `TenantContext`, `Headers` |

## Eventos (exchange `pintabien.events`, tipo *topic*)

Todos los eventos llevan `eventId`, `correlationId`, `tenantId`, `candidateId`, `occurredAt`.
Los consumidores **deben ser idempotentes** (descartar `eventId` repetidos).

| Routing key | Productor → Consumidor | Payload |
|---|---|---|
| `evaluation.requested` | core → integrations | documento en claro (solo dentro del broker), propósito, `EvaluationContext` (tolerancias del tenant y estado de la declaración) |
| `sources.collected` | integrations → scoring | `ProviderResult` por fuente (RENIEC, SBS, INFOCORP, ASSETS) con `status` OK/ERROR/NO_DATA y datos normalizados; documento **enmascarado** |
| `evaluation.completed` | scoring → core | `evaluationId`, `color`, `score`, `status` COMPLETA/INCOMPLETA, versión de reglas, `firedRules`, `warnings` |
| `declaration.submitted` | core → scoring | declaración informativa enviada/validada; scoring re-evalúa con el último snapshot |

### Ejemplo `evaluation.completed`
```json
{
  "eventId": "6f1c…", "correlationId": "9a2b…", "tenantId": "…", "candidateId": "…",
  "occurredAt": "2026-11-01T15:04:05Z", "evaluationId": "…",
  "color": "AMARILLO", "score": 58, "status": "COMPLETA", "ruleSetVersion": "global-v1",
  "firedRules": [{"ruleId": "amarillo-sin-planilla", "name": "...", "color": "AMARILLO", "priority": 3, "message": "...", "matchedFacts": {"payrollDetected": false}}],
  "warnings": ["REQUIERE_DECLARACION"]
}
```

## Uso desde un servicio

```xml
<dependency>
  <groupId>app.pintabien</groupId>
  <artifactId>pintabien-common-lib</artifactId>
  <version>1.1.0</version>
</dependency>
```

- **Desarrollo local:** `./mvnw install` en este repo deja el artefacto en `~/.m2`.
- **CI / producción:** el artefacto se publica en GitHub Packages al crear un tag `vX.Y.Z`. Los consumidores
  declaran el repositorio `https://maven.pkg.github.com/Jeliases/pintabien-common-lib` y autentican con
  `GITHUB_ACTOR`/`GITHUB_TOKEN` (scope `read:packages`) en `~/.m2/settings.xml`.

## Build

```bash
./mvnw verify
```

## Versionado

- `1.0.0` — versión inicial: 4 eventos, DTOs de proveedores, utilidades.
- `1.1.0` — `SbsData` con detalle RCC (entidades, historial mensual, castigos, % Normal, peor calificación 12m); `ProviderResult.mode` (`real`/`mock`/`off`) y `notContracted()`; `EvaluationContext.declaredMonthlyIncome`. Todo aditivo: los constructores v1.0 se conservan.
- Cambios **MINOR**: agregar campos opcionales / nuevos enums (los consumidores ignoran lo desconocido).
- Cambios **MAJOR**: quitar/renombrar campos → en su lugar crear un evento nuevo.
