# SPEC: [feature name]

**State:** Draft <!-- Draft | In review | Approved -->

<!--
This document defines WHAT must be true.
It must not design source sets, classes, frameworks, adapters or file structure.
Only the user can approve the SPEC.
-->

## Product goal

[PENDING]

## Verified current behavior

<!-- Describe observable behavior that exists now and must be preserved or changed. -->

[PENDING]

## Target scope

| Target | In scope? | Expected role |
| --- | --- | --- |
| COMMON | [Yes / No / Pending] | [Shared observable semantics, if applicable] |
| ANDROID | [Yes / No / Pending] | [Platform behavior] |
| IOS | [Yes / No / Pending] | [Platform behavior] |

## In scope

- **RF-01:** [PENDING]
- **RF-02:** [PENDING]

## Out of scope

- [PENDING]

## User flow

1. [PENDING]
2. [PENDING]

## Data and business rules

- [PENDING]

## Mobile and KMP behavior

Use `docs/MOBILE_KMP_GUIDELINES.md`. Describe outcomes, not mechanisms.

| Scenario | Target(s) | Expected behavior |
| --- | --- | --- |
| Loading / operation in progress | [COMMON/ANDROID/IOS] | [PENDING] |
| Empty state | [target] | [PENDING] |
| Invalid input | [target] | [PENDING] |
| Error / timeout | [target] | [PENDING] |
| Offline / interrupted connection | [target] | [PENDING] |
| Cancel / back | [target] | [PENDING] |
| Background -> foreground | [target] | [PENDING] |
| Screen recreation / scene restoration | [target] | [PENDING] |
| Process termination -> relaunch | [target] | [PENDING] |
| Permission denied / capability unavailable | [target] | [PENDING] |
| Other relevant guideline scenario | [target] | [PENDING] |

**Guideline items that do not apply, with reason:** [PENDING]

## Native UX expectations

<!-- Use only when UI/interaction is in scope. Do not force visual parity. -->

- **ANDROID-UX:** [PENDING / Not applicable]
- **IOS-UX:** [PENDING / Not applicable]

## Explicit constraints

<!-- Examples: preserve existing data, USD 0 CI, repository must remain private, native UI required. -->

- [PENDING]

## Acceptance criteria

Each criterion must name the target whose behavior it proves.

- **CA-01 · RF-01 · [COMMON]:** Given [context], when [action], then [observable result].
- **CA-02 · RF-02 · [ANDROID]:** Given [context], when [action], then [observable result].
- **CA-03 · RF-02 · [IOS]:** Given [context], when [action], then [observable result].

## Behavior verification map

| Criterion | Target | Conditions / steps | Expected result |
| --- | --- | --- | --- |
| CA-01 | COMMON | [PENDING] | [PENDING] |
| CA-02 | ANDROID | [PENDING] | [PENDING] |
| CA-03 | IOS | [PENDING] | [PENDING] |

## Pending decisions

- [PENDING]

<!--
Before requesting approval:
- scope is explicit;
- relevant targets are explicit;
- mobile scenarios are covered or marked not applicable with reason;
- each RF has observable CA coverage;
- native UX criteria are present when needed;
- product decisions are not disguised as technical assumptions.
-->
