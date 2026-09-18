# KMP QA Strategy

This document defines the quality-assurance discipline for work governed by the repository's SDD-KMP process.

The objective is to achieve the **highest meaningful coverage that is practical**, not to maximize a percentage by testing trivial implementation details.

Quality is evaluated through several independent dimensions:

- requirements coverage;
- automated code coverage;
- branch/decision coverage;
- platform coverage;
- regression coverage;
- integration coverage;
- device/OS coverage;
- non-functional coverage;
- release-readiness evidence.

No single number replaces these dimensions.

## 1. QA principles

1. Every approved acceptance criterion must have an evidence path before implementation begins.
2. Shared behavior must be tested as shared behavior.
3. Platform-specific behavior must be tested on the platform that owns it.
4. Critical deterministic logic should approach exhaustive scenario coverage.
5. New or changed code should be covered more aggressively than untouched legacy code.
6. Code coverage is a signal, not proof of correctness.
7. A green CI build is not a release-quality verdict.
8. Physical-device evidence is mandatory when emulators/simulators cannot demonstrate the behavior.
9. A defect discovered during QA is not "fixed" by weakening a test or acceptance criterion.
10. Unverified behavior remains NOT RUN or BLOCKED.

## 2. QA levels

### L1 - COMMON deterministic quality

Applies to domain logic, application rules, parsers, classifiers, ranking, validation, transformations, mapping, calculations and other behavior intended to be identical on Android and iOS.

Expected evidence:

- `commonTest` where appropriate;
- representative positive, negative and boundary cases;
- branch/decision cases for business rules;
- deterministic equivalence when replacing existing logic;
- execution on more than one supported target when portability itself is part of the claim.

### L2 - Platform quality

Applies to Android-only and iOS-only code.

Android examples:

- Android unit tests;
- Robolectric when Android runtime behavior is required;
- instrumented tests;
- Compose UI tests;
- permission/lifecycle/navigation checks.

iOS examples:

- Kotlin/Native target tests where applicable;
- XCTest/XCUITest when native Swift/iOS behavior is involved;
- SwiftUI behavior checks;
- permission/lifecycle/navigation checks.

### L3 - Integration quality

Validates boundaries between components:

- shared core <-> platform adapter;
- repository <-> persistence;
- parser <-> rule engine;
- application use case <-> storage;
- Kotlin <-> Swift boundary;
- Kotlin <-> Android framework boundary;
- external API/service boundaries.

Use real implementations when the acceptance criterion depends on the integration. Use fakes/stubs only when they preserve the behavior being tested.

### L4 - Device quality

Use physical Android/iOS hardware where required for:

- camera;
- OCR;
- permissions;
- file import/export;
- share sheets;
- hardware-dependent sensors;
- installation/provisioning;
- lifecycle behavior not faithfully reproduced by emulators/simulators;
- performance/resource behavior where device characteristics matter.

### L5 - Release quality

A release-quality gate combines:

- acceptance evidence;
- regression results;
- architecture/static checks;
- automated coverage;
- integration evidence;
- physical-device evidence where required;
- security/privacy review;
- accessibility checks where relevant;
- documentation consistency;
- CI reproducibility;
- unresolved-defect review.

## 3. Coverage model

### 3.1 Requirements coverage

Target: **100% of approved acceptance criteria mapped to evidence**.

No approved CA may be left without:

- target;
- validation method;
- environment;
- expected evidence;
- actual result.

### 3.2 Critical business-rule coverage

For deterministic business/domain logic, pursue the highest feasible decision coverage.

Default goals:

- line coverage: **>= 90%**;
- branch/decision coverage: **>= 85%**;
- critical rule engines, parsers, classifiers, migrations and transformations: strive for **95-100% meaningful scenario coverage** where feasible.

If a critical branch is intentionally uncovered, document why.

These are default QA goals, not permission to write low-value tests only to satisfy the metric.

### 3.3 Changed-code coverage

For new or materially changed deterministic code:

- target **>= 90% meaningful line coverage**;
- target **>= 85% branch/decision coverage** when the tooling can measure it;
- require tests for defect fixes whenever a deterministic regression test is practical.

A project may raise these thresholds in an approved PLAN.

### 3.4 Platform/adapters coverage

Platform code often contains framework glue that is expensive or misleading to measure purely by line percentage.

For platform adapters:

- pursue automated coverage where tooling is reliable;
- require scenario coverage for each approved CA;
- prefer direct integration/device evidence over artificial tests of trivial wrappers.

A lower line percentage does not excuse an untested capability.

### 3.5 UI coverage

UI QA is scenario-based first.

Cover relevant flows such as:

- loading;
- empty;
- success;
- error;
- retry;
- create/edit/delete;
- navigation/back/cancel;
- keyboard/focus;
- accessibility semantics;
- screen adaptation;
- permission denied/revoked;
- interruption/recovery.

Do not attempt to make UI line coverage the primary quality metric.

### 3.6 Persistence coverage

When persistence is in scope, cover:

- create/read/update/delete;
- restart persistence;
- duplicate/conflict behavior where applicable;
- corrupted/invalid input handling when relevant;
- installed upgrade;
- schema migration;
- migration failure behavior when required;
- post-migration CRUD;
- restart after migration.

### 3.7 Cross-platform equivalence coverage

When behavior is intended to be COMMON, define a representative corpus and verify semantically equivalent outputs.

This is especially important for:

- parsers;
- classifiers;
- ranking;
- diagnostic engines;
- calculations;
- normalization;
- validation;
- rule packs.

## 4. Coverage tooling policy

Do not prescribe one universal coverage tool.

The PLAN must verify tooling against the actual project and targets.

Possible measurement paths include:

- Kotlin/JVM/Android coverage tooling for common-host and Android code;
- Android Gradle Plugin coverage support for Android unit/instrumented tests;
- Xcode coverage for Swift/iOS tests;
- target-specific reports where available.

A single aggregated percentage across Kotlin/JVM, Kotlin/Native, Android instrumentation and Swift is not required unless a verified toolchain can produce a trustworthy number.

Prefer separate truthful reports over a mathematically neat but misleading global score.

## 5. Regression strategy

Every feature PLAN must identify regression scope.

At minimum consider:

- existing automated tests affected by the change;
- existing acceptance criteria that could regress;
- persisted-data compatibility;
- platform behavior on both targets;
- previously fixed defects;
- deterministic equivalence when extracting existing logic to COMMON.

Defect fixes should add a regression test when practical.

## 6. Architecture QA

Where the architecture has explicit boundaries, validate them.

Examples:

- common domain/application must not depend on Android framework types;
- common domain/application must not depend on Apple framework types;
- UI must not reach persistence implementations directly when an application/domain boundary exists;
- platform-specific implementations must satisfy common contracts without leaking platform representations back into domain;
- source-set dependency direction must match the approved PLAN.

Architecture checks may be automated or reviewed structurally depending on project complexity. The PLAN must state the chosen mechanism.

## 7. Static quality

When relevant and supported by the project, QA should consider:

- compiler warnings;
- lint;
- static analysis;
- formatting checks;
- dead-code signals;
- dependency/version compatibility;
- forbidden API usage;
- resource/configuration validation.

Do not add a tool solely because this document names the category. Tool choice belongs to the approved PLAN.

## 8. Security and privacy QA

When the feature handles sensitive data, external services or public-repository CI, consider:

- secrets committed to source/history;
- sensitive data in logs;
- transport security;
- temporary files;
- local storage of sensitive data;
- exported Android components;
- iOS entitlements/capabilities;
- permissions;
- API keys/configuration;
- debug information exposed in release artifacts.

Security findings that can expose credentials or user data block release until resolved or explicitly risk-accepted by the authorized human decision maker.

## 9. Accessibility QA

When UI is affected, consider:

- screen reader labels;
- focus order;
- dynamic text;
- contrast;
- touch targets;
- keyboard interaction;
- non-color communication;
- gesture alternatives where relevant.

Validate on the target platform whose accessibility behavior is claimed.

## 10. Performance/resource QA

Only when material to the feature, define measurable expectations for:

- startup;
- operation latency;
- large inputs;
- memory;
- battery;
- database operations;
- image/OCR processing;
- network usage;
- background execution.

Do not invent performance thresholds. If a threshold matters, it belongs in the SPEC.

## 11. Device/OS matrix

The PLAN must define the smallest device/OS matrix capable of supporting the claim.

Consider:

- minimum supported Android API;
- current Android target API;
- at least one representative physical Android device where device validation is required;
- minimum supported iOS;
- current iOS/Xcode build target;
- at least one representative physical iPhone where device validation is required;
- additional versions only when risk or acceptance criteria justify them.

Do not claim broad compatibility from one device alone.

## 12. Defect severity

Use this default model unless the PLAN defines a stricter one:

- **BLOCKER**: prevents build, install, launch, core flow, migration, required CI, or required validation.
- **CRITICAL**: data loss, credential exposure, severe security/privacy failure, major incorrect business decision, crash in core flow.
- **MAJOR**: important requirement fails but a limited workaround exists.
- **MINOR**: localized defect without material impact on core behavior.
- **TRIVIAL**: cosmetic or low-impact issue.

BLOCKER and CRITICAL defects block release.

MAJOR defects require explicit disposition before release.

## 13. QA gates

### SPEC gate

Required:

- RF and CA are testable;
- targets are explicit;
- relevant mobile edge cases are represented;
- criticality is understood.

### PLAN gate

Required:

- every CA has a validation method;
- regression scope exists;
- coverage goals are chosen;
- test environments are defined;
- architecture/static checks are defined where useful;
- device requirements are defined;
- unresolved QA risks are visible.

### Implementation gate

Required:

- tests are added with behavior;
- defect fixes gain regression coverage when practical;
- test results are not fabricated;
- coverage gaps are tracked.

### PR gate

Default requirements:

- required automated tests PASS;
- required static/architecture checks PASS;
- no unexplained regression in measured coverage;
- changed deterministic code meets the approved coverage goal or has documented justification;
- required CA evidence available for the increment;
- docs synchronized.

### Release gate

Required:

- 100% required CA evidence accounted for;
- no BLOCKER/CRITICAL defect;
- MAJOR defects explicitly resolved/dispositioned;
- required Android/iOS/device evidence complete;
- persistence/migration evidence complete when relevant;
- security/privacy blockers resolved;
- release documentation synchronized.

## 14. Definition of Done

A KMP feature is DONE only when:

- approved behavior is implemented;
- required tests exist;
- required coverage goals are met or explicitly justified;
- required acceptance criteria have executed evidence;
- platform-specific behavior is validated on its target;
- regression scope passes;
- required physical-device scenarios pass;
- unresolved defects are within approved release policy;
- documents reflect reality.

Compilation alone is never Definition of Done.

## 15. Reporting

Each QA completion report should contain:

| CA | Target | Test/evidence | Environment | Coverage contribution | Result | Evidence reference |
| --- | --- | --- | --- | --- | --- | --- |
| CA-XX | COMMON/ANDROID/IOS | [method] | [environment] | [metric or scenario] | PASS/FAIL/BLOCKED/NOT RUN | [reference] |

Also report:

- line/branch metrics where measured;
- changed-code coverage where measured;
- uncovered critical paths;
- device/OS matrix actually exercised;
- defect summary by severity;
- skipped/blocked checks;
- release-gate status.

Do not report an invented aggregate quality score.
