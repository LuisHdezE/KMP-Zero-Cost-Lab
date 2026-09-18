# PLAN: [feature name]

**Reference SPEC:** [path]
**Reviewed SPEC version:** [commit/version/date]
**State:** Draft <!-- Draft | In review | Approved -->

<!--
This document defines HOW the approved SPEC will be implemented.
Do not implement while the plan is being drafted.
Only the user can approve the PLAN.
Implementation authorization is a separate explicit action.
-->

## Verified technical context

| Existing component | Verified path | Target | Current responsibility |
| --- | --- | --- | --- |
| [PENDING] | [PENDING] | [COMMON/ANDROID/IOS] | [PENDING] |

## Target extraction matrix

For every material responsibility, decide where it belongs and why.

| Responsibility | Required semantics | Proposed target | Why shared or native | Existing behavior to preserve |
| --- | --- | --- | --- | --- |
| [PENDING] | [PENDING] | [commonMain/androidMain/iosMain/androidApp/iosApp] | [PENDING] | [PENDING] |

## Proposed solution

[PENDING]

## Source sets, modules and boundaries

| Module / source set | Existing / proposed | Responsibility | May depend on |
| --- | --- | --- | --- |
| [PENDING] | [PENDING] | [PENDING] | [PENDING] |

Document dependency direction. Platform frameworks must not leak into common domain/application contracts.

## Contracts and platform adapters

| Capability / contract | COMMON contract needed? | Android implementation | iOS implementation | Decision state |
| --- | --- | --- | --- | --- |
| [PENDING] | [Yes/No] | [PENDING] | [PENDING] | [VERIFIED/PROPOSED/PENDING/APPROVED] |

For each adapter choice, compare interface/injection, `expect/actual`, or direct target composition where relevant. Do not default to one mechanism.

## Data and persistence

- **Logical models and contracts:** [PENDING]
- **Local persistence:** [PENDING / Not applicable]
- **Remote data:** [PENDING / Not applicable]
- **Mapping boundaries:** [PENDING]
- **Existing data compatibility:** [PENDING / Not applicable]
- **Migration strategy:** [PENDING / Not applicable]
- **Upgrade evidence:** [PENDING / Not applicable]

Destructive migration may only appear when explicitly compatible with the approved product requirement.

## State, lifecycle and errors

| Concern | COMMON semantics | Android mechanism | iOS mechanism | Related RF/CA |
| --- | --- | --- | --- | --- |
| State retention | [PENDING] | [PENDING] | [PENDING] | [PENDING] |
| Background / foreground | [PENDING] | [PENDING] | [PENDING] | [PENDING] |
| Cancellation | [PENDING] | [PENDING] | [PENDING] | [PENDING] |
| Error recovery | [PENDING] | [PENDING] | [PENDING] | [PENDING] |

## Native UX implementation

- **Android:** [PENDING / Not applicable]
- **iOS:** [PENDING / Not applicable]
- **Shared UI:** No by default. If proposed, record the approved reason here: [PENDING / Not applicable]

## Dependencies and compatibility

| Dependency / SDK | Used by | Existing / new | Android verified | iOS/Kotlin-Native verified | Reason |
| --- | --- | --- | --- | --- | --- |
| [PENDING] | [target/source set] | [PENDING] | [PENDING] | [PENDING] | [PENDING] |

Do not infer iOS support from Android or JVM support.

## Zero-cost CI and repository visibility

Use `docs/ZERO_COST_CI.md`.

- **USD 0 is an approved constraint?** [Yes / No]
- **Repository visibility:** [Public / Private / Pending]
- **Linux/Android execution path:** [PENDING]
- **macOS/iOS execution path:** [PENDING]
- **Standard hosted runners only?** [PENDING]
- **If private, validated alternative for iOS builds:** [PENDING / Not applicable]
- **Publication-readiness review required?** [PENDING / Not applicable]

## QA strategy

Use `docs/QA_STRATEGY.md`.

- **Feature criticality:** [Low / Medium / High / Critical + reason]
- **Requirements coverage target:** 100% of approved CA mapped to evidence
- **Changed deterministic code line coverage target:** [Default >= 90% / stricter approved value / justified exception]
- **Changed deterministic branch/decision coverage target:** [Default >= 85% / stricter approved value / tooling unavailable + alternative]
- **Critical algorithm/rule scenario coverage:** [PENDING / Not applicable]
- **Regression scope:** [PENDING]
- **Architecture checks:** [PENDING / Not applicable]
- **Static quality checks:** [PENDING / Not applicable]
- **Android automated QA:** [PENDING / Not applicable]
- **iOS automated QA:** [PENDING / Not applicable]
- **Integration QA:** [PENDING / Not applicable]
- **Accessibility QA:** [PENDING / Not applicable]
- **Security/privacy QA:** [PENDING / Not applicable]
- **Performance/resource QA:** [PENDING / Not applicable]
- **Device/OS matrix:** [PENDING]
- **Release-blocking defect policy:** [Use default / stricter approved policy]
- **Coverage tooling and limitations:** [PENDING]

Do not combine incompatible platform coverage reports into a misleading global percentage. Prefer truthful per-surface metrics plus 100% CA traceability.

## Validation and evidence plan

Use `docs/QA_STRATEGY.md` and `docs/EVIDENCE_GUIDELINES.md`.

| CA | Target | Method | Environment | Evidence to record |
| --- | --- | --- | --- | --- |
| CA-01 | COMMON | [PENDING] | [PENDING] | [PENDING] |
| CA-02 | ANDROID | [PENDING] | [PENDING] | [PENDING] |
| CA-03 | IOS | [PENDING] | [PENDING] | [PENDING] |

### Deterministic equivalence

<!-- Required when replacing existing deterministic behavior with shared behavior. -->

- **Baseline behavior:** [PENDING / Not applicable]
- **Representative inputs:** [PENDING]
- **Relevant outputs to compare:** [PENDING]
- **Excluded nondeterminism and treatment:** [PENDING]
- **Regression evidence:** [PENDING]

### Physical-device validation

- **Android:** [Required / Not required / Pending] because [reason]
- **iOS:** [Required / Not required / Pending] because [reason]

## Implementation order

Prefer the smallest risk-first increments.

1. [PENDING]
2. [PENDING]
3. [PENDING]

For migrations, normally validate deterministic common behavior before replacing platform orchestration, then persistence, then capabilities/UI unless the approved feature requires another dependency order.

## Risks and pending decisions

- **Risks:** [PENDING]
- **Pending decisions:** [PENDING]

<!--
Before requesting approval:
- every RF/CA has an implementation and evidence path;
- source-set boundaries are explicit;
- platform capabilities are not assumed equivalent;
- existing data has an upgrade strategy when relevant;
- zero-cost claims have a real CI path;
- proposed dependencies are compatible with every target where they will run.
-->
