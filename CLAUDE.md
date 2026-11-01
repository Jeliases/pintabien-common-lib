# pintabien-common-lib — contexto para Claude Code

Contratos de eventos (`EvaluationRequestedEvent`, `SourcesCollectedEvent`, `EvaluationCompletedEvent`, `DeclarationSubmittedEvent`), DTOs de proveedores, enums compartidos y utilidades (`DocumentMasker`, `DocumentHasher`, `TenantContext`, `Headers`, `PintabienAmqp`). Artefacto Maven `app.pintabien:pintabien-common-lib` con SemVer, publicado en GitHub Packages al crear un tag `vX.Y.Z`.

Forma parte de **Pintabien.app** (SaaS multi-tenant de riesgo crediticio con semáforo), un sistema **multi-repo**: este repo es
autónomo (build, versión, Dockerfile, CI propios) y se comunica con los demás solo por eventos RabbitMQ y REST vía gateway.
La documentación global, la arquitectura, las decisiones y el estado del proyecto viven en el repo `pintabien-infra`
(`README.md`, `docs/arquitectura.md`, `docs/decisiones-y-lecciones.md`, `docs/estado-del-proyecto.md`).
Repos en GitHub: `https://github.com/Jeliases/pintabien-*`. Ramas `main` + `develop`, Conventional Commits.

## Comandos
- `./mvnw verify` — tests de contrato (round-trip JSON de cada evento). `./mvnw install` deja el artefacto en `~/.m2` para los servicios.
- Publicar: subir la versión en `pom.xml`, commit, `git tag vX.Y.Z && git push origin vX.Y.Z` (el workflow hace `mvn deploy`).

## Reglas de este repo
- Este repo es PÚBLICO: no agregar nada sensible.
- Cada cambio en un evento exige actualizar el README (tabla de eventos) y, si es incompatible, crear un evento nuevo.
- Leer el `README.md` de este repo antes de cambiar endpoints, eventos o variables de entorno; mantenerlo al día.
- Los cambios en eventos deben ser retrocompatibles (agregar campos, nunca quitar/renombrar); un cambio incompatible = evento nuevo + MAJOR de common-lib.
- Nunca loguear tokens de proveedores ni documentos completos (usar `DocumentMasker`).
