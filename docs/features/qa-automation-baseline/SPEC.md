# SPEC: QA Automation Baseline

**State:** Approved

<!--
This document defines WHAT must be true.
It does not select coverage libraries, test frameworks beyond existing platform capabilities,
source-set structure changes, CI commands or implementation files.
Only the user can approve the SPEC.
-->

## Product goal

Establish a measurable, reproducible QA automation baseline for KMP-Zero-Cost-Lab so that the laboratory can demonstrate its own SDD-KMP quality rules before those rules are applied to a real product.

The baseline must maximize meaningful automated coverage while preserving truthful evidence. It must not optimize for a cosmetic percentage at the expense of useful scenarios.

## Verified current behavior

At the reviewed repository baseline:

- the application already demonstrates shared KMP product/data/persistence behavior;
- Android uses native Jetpack Compose / Material 3 presentation;
- iOS uses native SwiftUI presentation;
- Room/SQLite CRUD and restart persistence have prior physical-device evidence on Android and iPhone;
- iOS has prior installed-upgrade data-preservation evidence;
- Android installed-upgrade preservation remains unverified;
- real Room schema migration remains unverified on both platforms;
- the repository currently contains one automated test file: `shared/src/commonTest/.../SharedSmokeTest.kt`;
- current CI builds/tests the shared Android-host path and Android APK, and builds/packages the iOS app, but does not yet publish comprehensive QA coverage reports.

## Target scope

| Target | In scope? | Expected role |
| --- | --- | --- |
| COMMON | Yes | Highest meaningful automated coverage of deterministic shared behavior |
| ANDROID | Yes | Automated validation of Android-owned behavior and integration where practical |
| IOS | Yes | Automated validation of iOS/Kotlin-Native and Swift-owned behavior where practical |
| ANDROID-UX | Yes | Preserve existing native UX behavior and define automated/manual evidence split |
| IOS-UX | Yes | Preserve existing native UX behavior and define automated/manual evidence split |

## In scope

- **RF-01:** Every acceptance criterion introduced by this QA baseline must have an explicit target, validation method and executed evidence status.
- **RF-02:** Deterministic shared behavior must have a broad automated regression suite covering normal, boundary, invalid and failure-relevant scenarios where such scenarios exist.
- **RF-03:** New or materially changed deterministic code governed by this baseline must target at least 90% meaningful line coverage and at least 85% branch/decision coverage when trustworthy tooling can measure those metrics.
- **RF-04:** Critical deterministic rules, transformations, persistence mappings and similar high-risk logic must pursue 95-100% meaningful scenario coverage where feasible.
- **RF-05:** Android-owned behavior must have automated QA for the scenarios that can be proven reliably without a physical device, and explicit manual/device evidence for the remainder.
- **RF-06:** iOS-owned behavior must have automated QA for the scenarios that can be proven reliably in the hosted macOS/Xcode path, and explicit manual/device evidence for the remainder.
- **RF-07:** Shared Room/SQLite CRUD and persistence behavior must gain automated regression coverage without changing the current product data model or user-visible behavior.
- **RF-08:** QA must include regression checks for the existing create/read/update/delete product behavior.
- **RF-09:** QA must include architecture and static-quality checks where they can detect meaningful boundary or platform-leak regressions.
- **RF-10:** QA results must report coverage truthfully by relevant surface/target. Incompatible Android, JVM, Kotlin/Native and Swift coverage numbers must not be averaged into a misleading global score.
- **RF-11:** The QA baseline must preserve the existing USD 0 public-repository CI constraint using only the already accepted zero-cost execution model.
- **RF-12:** CI must expose enough QA evidence to determine whether the required automated checks passed, failed, were blocked or were not run.
- **RF-13:** The QA baseline must define a release/PR quality gate that blocks success when mandatory automated checks fail or when required evidence is missing.
- **RF-14:** Existing proven Android and iOS behavior must not change as a side effect of adding QA automation.
- **RF-15:** The QA baseline must identify any important behavior that remains unautomated and state the evidence required to close it.

## Out of scope

- Changing the product's functional behavior.
- Changing Android or iOS visual design.
- Replacing the current Room/SQLite data model.
- Implementing the future Room schema v1 -> v2 migration itself.
- Claiming Android installed-upgrade preservation without executing that scenario.
- Solving private-repository zero-cost iOS CI.
- Applying the process to PanicLab.
- Modifying SoftwareDevelopmentBlueprint.
- Pursuing 100% line coverage through trivial tests, generated code tests or implementation-coupled assertions.

## User / maintainer flow

1. A developer changes shared, Android or iOS code.
2. The repository identifies the relevant QA surface and executes the applicable automated checks.
3. Coverage and regression evidence are produced for the affected deterministic code where measurable.
4. Platform-specific checks execute on the appropriate runner/environment.
5. The QA gate reports PASS, FAIL, BLOCKED, NOT RUN or NOT APPLICABLE for required evidence.
6. Physical-device or manual evidence remains explicit for scenarios automation cannot prove.
7. A PR or release is considered QA-complete only when its mandatory checks and acceptance evidence are accounted for.

## Data and business rules

- Existing Product CRUD semantics must remain unchanged.
- Persisted user data must not be cleared or recreated merely to make tests pass.
- Tests may use isolated test data stores/databases where appropriate, but test isolation must not alter production behavior.
- Coverage exclusions must be explicit and justified when they materially affect reported percentages.
- Generated code, trivial platform wrappers or unreachable platform branches must not be used to inflate or deflate quality conclusions without explanation.
- A passing coverage threshold does not override a failing acceptance criterion.
- A failed business scenario cannot be reclassified as acceptable solely to make QA green.

## Mobile and KMP behavior

| Scenario | Target(s) | Expected behavior |
| --- | --- | --- |
| Shared deterministic logic | COMMON | Automated regression tests cover representative normal, boundary and invalid scenarios |
| Product CRUD | COMMON/ANDROID/IOS | Existing create/read/update/delete semantics remain unchanged and gain automated evidence where practical |
| Persistence | COMMON/ANDROID/IOS | Automated evidence proves relevant read/write/restart semantics where the test environment can reproduce them |
| Native Android presentation | ANDROID-UX | Automation covers reliable UI/state behavior; device-only claims remain separately evidenced |
| Native iOS presentation | IOS-UX | Automation covers reliable UI/state behavior; device-only claims remain separately evidenced |
| CI failure | MULTI | Mandatory QA failure prevents the QA gate from reporting success |
| Coverage below approved threshold | relevant target | The QA gate reports failure unless an approved, documented exception applies |
| Tool cannot measure a metric reliably | relevant target | The metric remains unavailable and is replaced by explicit scenario/evidence coverage, never fabricated |
| Physical-device-only behavior | ANDROID/IOS | Marked as device evidence rather than falsely automated |
| Platform-specific behavior not affected by a change | relevant target | May be NOT APPLICABLE only with a valid reason |

**Guideline items that do not apply, with reason:** networking/offline behavior is not introduced by this QA increment; no new permissions or device capabilities are added.

## Native UX expectations

- **ANDROID-UX:** Existing Material 3 interaction model must remain unchanged. QA additions may observe it but must not redesign it.
- **IOS-UX:** Existing SwiftUI interaction model must remain unchanged. QA additions may observe it but must not redesign it.

## Explicit constraints

- Infrastructure budget remains USD 0.
- Repository remains on the current public-lab zero-cost execution path.
- No product behavior change is permitted solely to simplify testing.
- No destructive persistence shortcut is permitted.
- QA metrics must be truthful and target-aware.
- Requirements/evidence traceability target is 100%.
- Default changed deterministic code target is >= 90% meaningful line coverage and >= 85% branch/decision coverage when measurable.
- Critical deterministic scenario coverage should approach 95-100% where feasible.
- Physical-device evidence remains mandatory where automation cannot demonstrate the criterion.

## Acceptance criteria

- **CA-01 · RF-01 · [MULTI]:** Given the QA baseline is complete, when its acceptance ledger is reviewed, then 100% of required CA entries have a target, validation method, evidence status and actual result.
- **CA-02 · RF-02 · [COMMON]:** Given the current deterministic shared product behavior, when the automated shared regression suite runs, then representative normal, boundary and invalid scenarios execute with observed results.
- **CA-03 · RF-03 · [COMMON]:** Given new or materially changed deterministic shared code introduced during the QA implementation, when coverage is measured with trustworthy tooling, then meaningful line coverage is at least 90% and branch/decision coverage is at least 85%, or an approved documented exception identifies why a metric cannot be trusted.
- **CA-04 · RF-04 · [COMMON]:** Given critical deterministic shared behavior selected by the PLAN, when its scenario matrix is reviewed, then 95-100% of identified meaningful scenarios are automated where feasible and any uncovered critical scenario is explicitly justified.
- **CA-05 · RF-05 · [ANDROID]:** Given the Android QA suite, when it runs in the approved environment, then every Android scenario designated automatable by the PLAN has executed evidence and required failures fail the QA gate.
- **CA-06 · RF-06 · [IOS]:** Given the iOS QA suite, when it runs in the approved macOS/Xcode environment, then every iOS scenario designated automatable by the PLAN has executed evidence and required failures fail the QA gate.
- **CA-07 · RF-07 · [MULTI]:** Given isolated test data, when the persistence regression suite executes its approved CRUD/persistence scenarios, then the observed data behavior matches the existing product contract without modifying the production schema.
- **CA-08 · RF-08 · [MULTI]:** Given the existing product CRUD contract, when regression QA runs, then create, read, update and delete behavior are all represented by executed evidence.
- **CA-09 · RF-09 · [MULTI]:** Given the approved architecture/static rules, when QA runs, then violations detectable by the selected checks cause a visible failure.
- **CA-10 · RF-10 · [MULTI]:** Given QA reports from different targets, when results are published, then each metric identifies its measured scope and no unsupported synthetic global percentage is reported.
- **CA-11 · RF-11 · [MULTI]:** Given the QA workflows execute on the public repository path, when they complete, then no paid runner or paid build service is required.
- **CA-12 · RF-12 · [MULTI]:** Given a QA run, when its results are inspected, then mandatory checks expose PASS/FAIL/BLOCKED/NOT RUN/NOT APPLICABLE state and relevant evidence references.
- **CA-13 · RF-13 · [MULTI]:** Given a mandatory QA check is intentionally made to fail in a controlled validation, when the gate runs, then the gate does not report success.
- **CA-14 · RF-14 · [ANDROID-UX/IOS-UX]:** Given QA automation is added, when the existing Android and iOS application flows are revalidated, then no approved product or native-UX behavior has changed.
- **CA-15 · RF-15 · [MULTI]:** Given the baseline completion report, when remaining QA gaps are reviewed, then each unautomated important behavior is explicitly listed with the evidence needed to close it.

## Behavior verification map

| Criterion | Target | Conditions / steps | Expected result |
| --- | --- | --- | --- |
| CA-01 | MULTI | Review QA evidence ledger | All mandatory CA accounted for |
| CA-02 | COMMON | Execute shared regression suite | Representative scenarios execute with observed outcomes |
| CA-03 | COMMON | Generate trustworthy coverage report | Thresholds met or approved exception recorded |
| CA-04 | COMMON | Review critical scenario matrix | 95-100% meaningful scenarios covered where feasible |
| CA-05 | ANDROID | Execute approved Android automated suite | Mandatory Android checks report observed result |
| CA-06 | IOS | Execute approved iOS automated suite | Mandatory iOS checks report observed result |
| CA-07 | MULTI | Run isolated persistence regression scenarios | Existing persistence semantics preserved |
| CA-08 | MULTI | Review CRUD regression evidence | C/R/U/D all represented |
| CA-09 | MULTI | Execute architecture/static checks | Detectable violation fails visibly |
| CA-10 | MULTI | Inspect published QA reports | Metrics remain scoped and truthful |
| CA-11 | MULTI | Inspect workflow execution path | USD 0 path preserved |
| CA-12 | MULTI | Inspect QA run outputs | Evidence states explicit |
| CA-13 | MULTI | Controlled failure validation | Gate refuses success |
| CA-14 | ANDROID-UX/IOS-UX | Revalidate existing flows | No behavior/UX regression |
| CA-15 | MULTI | Review completion report | Remaining gaps explicitly documented |

## Pending decisions

None at the product/quality-policy level.

Tool selection, exact test framework placement, coverage tooling, CI commands and the detailed device matrix belong to PLAN.md after this SPEC is approved.

<!--
Before approval:
- scope is explicit;
- targets are explicit;
- QA coverage is ambitious but not cosmetic;
- unautomatable evidence remains visible;
- no implementation/tool choice leaks into the SPEC.
-->
