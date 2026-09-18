# TASKS: QA Automation Baseline

**Reference PLAN:** docs/features/qa-automation-baseline/PLAN.md
**PLAN version:** 4a726cadaac35d158521bf81870c3b20b21aab80
**Implementation authorized:** Yes

## Status vocabulary

- TODO
- IN PROGRESS
- BLOCKED
- DONE

A task is DONE only when implementation and required validation have both completed.

## Tasks

### TASK-KMP-QA-001 · Establish QA host and coverage tooling

- **Status:** IN PROGRESS
- **Target:** COMMON
- **RF/CA:** RF-02, RF-03, RF-04, RF-10 / CA-02, CA-03, CA-04, CA-10
- **Objective:** Add the QA-only JVM target, Kover reporting and coroutine test support.
- **Scope:** Gradle/version catalog/shared QA source-set configuration only.
- **Dependencies:** None
- **Coverage expectation:** >=90% line and >=85% branch for the filtered deterministic COMMON scope.
- **Regression scope:** Existing Android/iOS targets must continue compiling.
- **Validation method:** JVM tests, Kover XML/HTML generation, coverage gate.
- **Evidence required:** CI logs + uploaded coverage report.
- **Actual result:** JVM QA host, Kover XML/HTML and 90/85 gate execute successfully on Ubuntu. Final scoped deterministic COMMON result: LINE 100.00%, BRANCH 100.00%. Existing Android host/lint/APK are green. iOS compilation remains pending before this task can be DONE.
- **Evidence reference:** GitHub Actions run #40, run 35296308302, artifact `kmp-zero-cost-lab-qa-core` (10527569096).

### TASK-KMP-QA-002 · Cover deterministic COMMON behavior

- **Status:** IN PROGRESS
- **Target:** COMMON
- **RF/CA:** RF-02, RF-04, RF-08 / CA-02, CA-04, CA-08
- **Objective:** Add regression tests for ProductController, ProductSnapshot and RoomProductRepository mapping.
- **Scope:** commonTest plus minimal internal test seam preserving public behavior.
- **Dependencies:** TASK-KMP-QA-001
- **Coverage expectation:** 95-100% identified meaningful scenarios for ProductController branches where feasible.
- **Regression scope:** name trimming, blank-name no-op, negative coercion, observation, create/update/delete.
- **Validation method:** jvmTest and iosSimulatorArm64Test.
- **Evidence required:** test reports.
- **Actual result:** ProductController and RoomProductRepository COMMON regression tests pass on JVM and Android host. ProductController decision branches are included in the scoped 100% line / 100% branch result. Kotlin/Native execution on iosSimulatorArm64 is still pending.
- **Evidence reference:** GitHub Actions run #40, run 35296308302.

### TASK-KMP-QA-003 · Automate Room CRUD and same-schema persistence

- **Status:** DONE
- **Target:** COMMON
- **RF/CA:** RF-07, RF-08 / CA-07, CA-08
- **Objective:** Add JVM Room/SQLite integration tests for CRUD and close/reopen persistence.
- **Scope:** QA-only jvmMain/jvmTest. Production schema remains v1.
- **Dependencies:** TASK-KMP-QA-001
- **Coverage expectation:** 100% planned CRUD scenarios.
- **Regression scope:** empty/read ordering/create/update/delete/reopen.
- **Validation method:** jvmTest with Room 3 + BundledSQLiteDriver.
- **Evidence required:** test report.
- **Actual result:** PASS. QA-only JVM Room test executed empty DB, create, ordered read, update, delete, close/reopen and preserved the remaining row with production schema still at version 1.
- **Evidence reference:** GitHub Actions run #40, COMMON JVM + Room step, run 35296308302.

### TASK-KMP-QA-004 · Add architecture/static quality guard

- **Status:** DONE
- **Target:** MULTI
- **RF/CA:** RF-09 / CA-09
- **Objective:** Fail CI if Android/Apple/UI framework dependencies leak into commonMain.
- **Scope:** lightweight repository script; Android lint retained.
- **Dependencies:** None
- **Coverage expectation:** Not applicable.
- **Regression scope:** commonMain dependency boundaries.
- **Validation method:** script self-check + CI execution.
- **Evidence required:** CI log.
- **Actual result:** PASS. Boundary guard contract tests passed, real commonMain boundary scan passed, and Android lint passed.
- **Evidence reference:** GitHub Actions run #40, run 35296308302.

### TASK-KMP-QA-005 · Add executable QA gate

- **Status:** DONE
- **Target:** MULTI
- **RF/CA:** RF-01, RF-10, RF-12, RF-13 / CA-01, CA-10, CA-12, CA-13
- **Objective:** Enforce coverage thresholds and mandatory evidence states with controlled PASS/FAIL tests.
- **Scope:** scripts/qa using Python standard library only.
- **Dependencies:** TASK-KMP-QA-001
- **Coverage expectation:** 100% gate contract scenarios.
- **Regression scope:** PASS/FAIL/NOT RUN/NOT APPLICABLE and below-threshold cases.
- **Validation method:** Python unittest + real Kover XML verification.
- **Evidence required:** CI log.
- **Actual result:** PASS. Contract suite proves PASS and controlled failure cases for low line coverage, low branch coverage, mandatory FAIL and mandatory NOT_RUN. The real gate initially rejected 89.06%/70.00% in run #39, then accepted the corrected 100.00%/100.00% scoped result in run #40 without lowering thresholds.
- **Evidence reference:** Runs #39 (35296093947) and #40 (35296308302).

### TASK-KMP-QA-006 · Android native regression automation

- **Status:** DONE
- **Target:** ANDROID
- **RF/CA:** RF-05, RF-08, RF-14 / CA-05, CA-08, CA-14
- **Objective:** Add one robust Compose CRUD end-to-end flow on a managed Android device.
- **Scope:** androidTest/testability metadata + CI managed device.
- **Dependencies:** TASK-KMP-QA-001..005
- **Coverage expectation:** scenario-based; UI line percentage is not the primary metric.
- **Regression scope:** empty/create/read/update/delete.
- **Validation method:** Android instrumentation on managed device.
- **Evidence required:** test report/artifact.
- **Actual result:** PASS. A real Compose instrumentation flow executed on a GitHub-hosted Gradle Managed Device (Pixel 2, API 35, aosp-atd) and verified empty state, create, render, edit, updated render, delete and return to empty state. Existing COMMON/JVM/Room, Android host, lint, APK and 90/85 coverage gate remained green.
- **Evidence reference:** GitHub Actions run #44, run 35297235909, job 105452197227; QA artifact `kmp-zero-cost-lab-qa-core` (10528925603).

### TASK-KMP-QA-007 · iOS native regression automation

- **Status:** TODO
- **Target:** IOS
- **RF/CA:** RF-06, RF-08, RF-14 / CA-06, CA-08, CA-14
- **Objective:** Run COMMON tests through Kotlin/Native and add native XCTest/XCUITest regression.
- **Scope:** iOS simulator test targets + CI result bundle.
- **Dependencies:** TASK-KMP-QA-001..005
- **Coverage expectation:** scoped Xcode native coverage plus scenario evidence.
- **Regression scope:** launch/create/read/update/delete/relaunch persistence where stable.
- **Validation method:** iosSimulatorArm64Test + Xcode tests.
- **Evidence required:** test summary/xcresult.
- **Actual result:** Not run
- **Evidence reference:** PENDING

### TASK-KMP-QA-008 · Close QA evidence ledger

- **Status:** TODO
- **Target:** MULTI
- **RF/CA:** RF-01..RF-15 / CA-01..CA-15
- **Objective:** Reconcile executed evidence, remaining gaps and final QA gate.
- **Scope:** TASKS/validation docs only after tests execute.
- **Dependencies:** TASK-KMP-QA-001..007
- **Coverage expectation:** 100% required CA accounted for.
- **Regression scope:** all approved QA baseline behavior.
- **Validation method:** evidence review against CI/device results.
- **Evidence required:** completed ledger.
- **Actual result:** Not run
- **Evidence reference:** PENDING

## QA ledger

| QA dimension | Target | Planned threshold / scenarios | Actual result | Status |
| --- | --- | --- | --- | --- |
| Requirements / CA traceability | MULTI | 100% required CA mapped | Ledger exists; final closure waits for Android/iOS native tasks | NOT RUN |
| Deterministic line coverage | COMMON/JVM | >= 90% | 100.00% on explicitly filtered deterministic COMMON scope | PASS |
| Deterministic branch coverage | COMMON/JVM | >= 85% | 100.00% on explicitly filtered deterministic COMMON scope | PASS |
| Critical controller scenarios | COMMON | 95-100% meaningful scenarios | Positive/negative/no-op/observation/restart/dispose branches covered on JVM/Android host; Native run pending | NOT RUN |
| Room CRUD/persistence | COMMON/JVM | all planned scenarios | Empty/create/order/update/delete/close/reopen PASS | PASS |
| COMMON portability | IOS | common suite on iosSimulatorArm64 | PENDING | NOT RUN |
| Architecture/static | MULTI | approved checks pass | Boundary guard PASS; Android lint PASS | PASS |
| Android UI regression | ANDROID | approved CRUD flow | Managed Device API 35 verified empty/create/read/edit/update/delete/empty | PASS |
| iOS UI regression | IOS | approved CRUD flow | PENDING | NOT RUN |

## Acceptance evidence ledger

| CA | Target | Task(s) | Status | Executed evidence | Actual result |
| --- | --- | --- | --- | --- | --- |
| CA-01 | MULTI | 005,008 | NOT RUN | PENDING | PENDING |
| CA-02 | COMMON | 002 | NOT RUN | PENDING | PENDING |
| CA-03 | COMMON | 001,002 | PASS | Kover scoped deterministic COMMON report | LINE 100.00%, BRANCH 100.00%, thresholds 90/85 |
| CA-04 | COMMON | 002 | NOT RUN | JVM/Android host scenarios executed | Native portability portion remains pending before full COMMON scenario claim |
| CA-05 | ANDROID | 006 | PASS | Managed Device CRUD regression, run #44 | Native Material 3 flow passed on Pixel 2 API 35 |
| CA-06 | IOS | 007 | NOT RUN | PENDING | PENDING |
| CA-07 | MULTI | 003,006,007 | NOT RUN | PENDING | PENDING |
| CA-08 | MULTI | 002,003,006,007 | NOT RUN | COMMON/JVM/Room and Android native regression PASS | iOS COMMON/native evidence still pending before multi-target PASS |
| CA-09 | MULTI | 004 | PASS | Boundary guard + Android lint, run #40 | Detectable commonMain platform leak guard and lint PASS |
| CA-10 | MULTI | 001,005 | PASS | Scoped Kover report + QA gate output | Metrics explicitly reported as COMMON/JVM scope; no synthetic global percentage |
| CA-11 | MULTI | 006,007 | NOT RUN | Android Managed Device evidence PASS | iOS simulator/native evidence still pending |
| CA-12 | MULTI | 005,008 | NOT RUN | Core gate statuses exercised | Final multi-target completion report still pending |
| CA-13 | MULTI | 005 | PASS | Gate contract tests + real fail-then-pass CI sequence | Run #39 rejected 89.06/70.00; run #40 accepted 100/100 |
| CA-14 | ANDROID-UX/IOS-UX | 006,007 | NOT RUN | Android native Material 3 automation PASS | iOS SwiftUI/XCUITest still pending |
| CA-15 | MULTI | 008 | NOT RUN | PENDING | PENDING |

## Outstanding checks

- Execute COMMON regression suite through `iosSimulatorArm64Test`.
- Revalidate iOS app compilation after the QA-only JVM target/test seam changes.
- Implement iOS XCTest/XCUITest native regression (TASK-KMP-QA-007).
- Close 100% CA evidence ledger after all mandatory target checks (TASK-KMP-QA-008).

## Core increment evidence

- **Run #39 / 35296093947:** QA infrastructure and Room tests passed; real coverage gate correctly failed at LINE 89.06%, BRANCH 70.00%.
- **Run #40 / 35296308302:** complete Ubuntu core validation PASS.
- **Final scoped Kover result:** LINE 100.00%, BRANCH 100.00% for `ProductController/ProductSnapshot/RoomProductRepository` only.
- **QA artifact:** `kmp-zero-cost-lab-qa-core`, artifact 10527569096.
- **APK artifact:** `kmp-zero-cost-lab-android-debug`, artifact 10527963830.
- **Android regression checks in run #40:** host tests PASS, lint PASS, debug APK PASS.
- **Android native UI evidence, run #44 / 35297235909:** Gradle Managed Device Pixel 2 API 35 PASS for create/read/edit/update/delete with the existing Material 3 UI.
- **Run #42:** exposed obsolete Managed Device DSL usage before device startup.
- **Run #43:** exposed incorrect Compose UI test imports before device startup.
- Both defects were corrected without weakening the scenario or QA gate.
- **Run #44:** full Android stack PASS, including Managed Device CRUD and scoped Kover 100.00% line / 100.00% branch.
- This is not a claim of 100% repository-wide or Kotlin/Native/Swift coverage.
