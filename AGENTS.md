# AGENTS.md

Guidance for coding agents working in this Kotlin Multiplatform repository.

## Required reading

Before planning, reviewing, or modifying the repository, read:

1. `docs/GENERIC_RULES.md`
2. `docs/MOBILE_KMP_GUIDELINES.md`
3. `docs/QA_STRATEGY.md`
4. The approved feature `SPEC.md`, when one exists
5. The approved feature `PLAN.md`, before deriving tasks or implementing

Explicit user instructions remain authoritative. When repository documents conflict in a way that changes behavior, scope, platform support, data preservation, cost, or validation, stop that affected part and surface the conflict instead of guessing.

## Purpose of this repository

This repository is both:

- a working KMP laboratory that proves Android and iOS delivery at zero infrastructure cost where the selected GitHub plan and repository visibility permit it; and
- a reference process for Spec-Driven Development adapted to Kotlin Multiplatform.

The guiding architectural principle is:

> Share the brain when behavior should be identical. Keep the face native when platform experience should remain native.

Shared code is a design decision, not a percentage target.

## SDD-KMP workflow

Feature work follows this sequence:

1. **Investigation**
   - Inspect the current repository and relevant behavior.
   - Separate VERIFIED facts, PROPOSED decisions, PENDING decisions and APPROVED decisions.
   - Identify the affected targets: COMMON, ANDROID, IOS, or combinations.

2. **SPEC**
   - Copy `docs/SPEC_TEMPLATE.md` into `docs/features/<feature>/SPEC.md`.
   - Define observable behavior, scope, RF identifiers, CA identifiers and applicable mobile scenarios.
   - Acceptance criteria must identify the target or targets whose behavior they constrain.
   - Do not design source sets, classes, libraries, adapters or files in the SPEC.
   - A filled document is not approved. Only the user can approve it.

3. **PLAN**
   - Start only from an approved SPEC.
   - Copy `docs/PLAN_TEMPLATE.md`.
   - Decide what belongs in common code and what remains platform specific.
   - Define adapters, source-set boundaries, persistence strategy, lifecycle behavior, dependencies and evidence.
   - Validate dependency compatibility for every target where it will be used.
   - A filled plan is not approved. Only the user can approve it.

4. **TASKS**
   - Start only from an approved PLAN.
   - Copy `docs/TASKS_TEMPLATE.md`.
   - Tasks must be small, ordered and target-labelled.
   - Each task must reference the RF/CA it advances and how completion will be demonstrated.

5. **Implementation**
   - Requires explicit user authorization in addition to approved documents.
   - Work incrementally.
   - Preserve proven behavior before improving or generalizing it.
   - Do not start with UI when shared deterministic behavior is the real migration risk.

6. **Validation and QA**
   - Demonstrate each CA with evidence appropriate to its target.
   - Use `docs/QA_STRATEGY.md` and `docs/EVIDENCE_GUIDELINES.md`.
   - Pursue the highest meaningful automated coverage that is practical, with stronger expectations for changed deterministic business logic.
   - Track requirements coverage, code/branch coverage where measurable, regression, integration, platform, device, accessibility, security/privacy and release quality separately.
   - A passing build is not proof of runtime behavior.
   - A screenshot is not proof of persistence, lifecycle, migration, offline behavior, deterministic equivalence or background execution.

7. **Completion**
   - Report CA -> target -> executed evidence -> actual result.
   - Record BLOCKED or PENDING checks explicitly.
   - Do not call a feature complete while required evidence is missing.

## Target vocabulary

Use these target labels consistently:

- **COMMON**: behavior intentionally shared by Android and iOS.
- **ANDROID**: Android-only behavior or implementation.
- **IOS**: iOS-only behavior or implementation.
- **ANDROID-UX**: acceptance criteria for Android-native experience.
- **IOS-UX**: acceptance criteria for iOS-native experience.
- **MULTI**: coordinated work spanning more than one target where listing the exact set is clearer in the task.

Do not infer that an API available on both platforms belongs in COMMON. Common code exists only when the semantics should be shared and the dependency is valid for the selected KMP targets.

## Architecture rules

- Domain and application behavior intended to be identical across platforms should be considered for `commonMain`.
- Android framework APIs must not leak into common contracts.
- Apple frameworks must not leak into common contracts.
- Persistence, camera, OCR, file picking, sharing, notifications, background execution and similar device capabilities should expose semantic contracts only when a shared contract adds real value.
- Prefer adapters or target implementations over forcing platform APIs through common code.
- Do not introduce `expect/actual` automatically. Compare it with interfaces, adapters, injected functions or target-specific composition and choose the smallest mechanism that preserves clarity.
- UI is native by default for this laboratory:
  - Android: Jetpack Compose / Material 3.
  - iOS: SwiftUI and native Apple interaction patterns.
- Shared Compose UI requires an explicit approved decision. It is not the default.
- Composition roots remain platform-specific unless an approved plan establishes otherwise.

## Data preservation

- Existing persisted user data is part of observable behavior.
- Destructive migration must never be used as an unexamined shortcut for a released data path.
- Any storage-engine or schema change affecting existing data requires explicit compatibility and migration acceptance criteria.
- Migration evidence must include an upgrade scenario from a representative previous database state when the feature affects persisted production data.

## Deterministic equivalence

When moving existing deterministic business behavior into COMMON, define equivalence before replacement:

- same relevant input;
- same normalized domain evidence;
- same business decision;
- same ordering when ordering is part of the contract;
- same relevant errors or fallback semantics.

Random identifiers, clocks and platform formatting must be controlled, abstracted or excluded from semantic comparisons when they are not part of the requirement.

## Native UX policy

Do not use code sharing to erase platform conventions.

- Android acceptance may cover back behavior, system bars, Material interaction, permissions, keyboard and lifecycle.
- iOS acceptance may cover navigation, gestures, sheets, safe areas, permissions, scene lifecycle and platform accessibility behavior.
- A feature may have one COMMON business rule and separate ANDROID-UX and IOS-UX criteria.

## Zero-cost CI constraint

The zero-cost path is a product constraint when a SPEC says cost must remain USD 0.

- Standard GitHub-hosted runners in a public repository are the reference path used by this laboratory.
- macOS hosted execution is the scarce requirement because iOS/Xcode compilation needs macOS.
- Do not assume a private repository preserves the same zero-cost CI characteristics.
- If a product must remain private, the PLAN must define and validate an alternative iOS build strategy before claiming the zero-cost requirement is satisfied.
- Larger paid runners are outside the zero-cost reference path unless explicitly approved.
- Never expose secrets merely to make a repository public. Public visibility requires a separate publication-readiness review when relevant.

See `docs/ZERO_COST_CI.md`.

## Git and review discipline

- Do not commit directly to `main`.
- Use small branches and reviewable pull requests.
- Do not merge without human approval.
- CI success is required where the change can be exercised by CI, but CI success does not replace acceptance evidence.
- Apply the QA gates in `docs/QA_STRATEGY.md`; do not merge or release around BLOCKER/CRITICAL defects.
- Preserve unrelated work.
- Do not weaken tests or checks to manufacture a green result.

## Completion report

Every implementation completion report must state:

- what changed;
- which RF/CA were addressed;
- targets affected;
- checks actually executed;
- physical-device evidence when required;
- anything skipped, blocked or still pending;
- any documentation that must be reconciled before merge.
