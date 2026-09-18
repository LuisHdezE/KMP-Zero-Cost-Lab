# Generic rules for coding agents

Reusable rules for investigation, planning, implementation and validation in this repository.

## Understand before changing

- Read the relevant source, feature documents and repository instructions before proposing a change.
- Trace behavior across common and platform-specific code when the feature crosses targets.
- Classify statements as VERIFIED, PROPOSED, PENDING or APPROVED.
- Never invent requirements, files, APIs, dependency support, SDK parity, test results or device behavior.
- Resolve routine technical details from verified repository conventions.
- Surface missing product decisions when they materially affect behavior, target support, data preservation, privacy, cost or an irreversible choice.

## Keep scope explicit

- Implement only approved behavior and its necessary support.
- Do not add speculative abstractions, platform parity, refactors or dependency upgrades.
- A KMP migration is not permission to redesign the product.
- If implementation exposes a functional contradiction, update the feature documents and obtain the required decision before continuing that part.
- Do not repeatedly ask for authorization already granted for the current stage.

## Share deliberately

- Shared code must represent intentionally shared semantics.
- Do not optimize for percentage of shared code.
- Keep native behavior native where sharing would degrade UX, capability access, maintainability or validation.
- Do not move code to COMMON merely because it compiles there.
- Do not keep deterministic business logic duplicated by platform when the approved goal is semantic equivalence and a clean common implementation is practical.

## Dependencies

- Inspect the repository's current versions before adding or changing dependencies.
- Verify KMP target compatibility from authoritative dependency documentation when support is uncertain.
- Android support does not prove Kotlin/Native or iOS support.
- Avoid duplicate libraries that solve the same problem without an approved reason.
- Do not change libraries solely to make a migration look more multiplatform.

## Preserve data, secrets and user work

- Never overwrite unrelated work.
- Never commit tokens, credentials, private keys, service secrets or unnecessary personal data.
- Do not publish a private repository to obtain free CI without first reviewing publication risk.
- Treat stored user data and upgrade compatibility as behavior, not implementation detail.
- Destructive data operations require explicit approved scope and evidence.

## Validate what actually matters

- Tie validation to acceptance criteria.
- Prefer observable behavior over implementation-shaped tests.
- Record PASS only for checks that were executed and observed.
- Distinguish PASS, FAIL, BLOCKED, NOT RUN and NOT APPLICABLE.
- Use real-device validation for behavior that depends on hardware, OS integration, permissions, lifecycle, provisioning or distribution when the criterion requires it.
- A build proves compilation, not end-to-end behavior.
- A screenshot proves only what can be observed visually.

## Keep documents synchronized

- SPEC describes required behavior.
- PLAN describes the approved technical solution.
- TASKS records executable work and real validation status.
- Code and evidence must not silently outrun the documents.
- Documentation drift is a defect in a spec-driven reference repository.
- When a change invalidates a documented statement, update the relevant document in the same increment or mark the discrepancy explicitly before merge.

## Communicate precisely

- Use the user's language in discussion and completion reports.
- Keep technical repository documentation internally consistent.
- Report material decisions, blockers and deviations directly.
- Do not label something complete if required target evidence remains pending.
