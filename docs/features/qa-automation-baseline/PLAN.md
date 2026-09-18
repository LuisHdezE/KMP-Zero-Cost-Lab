# PLAN: QA Automation Baseline

**Reference SPEC:** docs/features/qa-automation-baseline/SPEC.md
**Reviewed SPEC version:** bc2feac8c4276640b74c23473ef134eb97733a5a
**State:** In review

<!--
This document defines HOW the approved SPEC will be implemented.
Do not implement while the plan is being reviewed.
Only the user can approve the PLAN.
Implementation authorization is separate and explicit.
-->

## Verified technical context

| Existing component | Verified path | Target | Current responsibility |
| --- | --- | --- | --- |
| Shared KMP module | `shared/` | COMMON / ANDROID / IOS | Domain model, controller, repository, Room/SQLite core and target DB builders |
| Product controller | `shared/src/commonMain/.../ProductController.kt` | COMMON | Product observation and CRUD orchestration; currently owns an internal Main coroutine scope |
| Room repository | `shared/src/commonMain/.../data/RoomProductRepository.kt` | COMMON | Maps Product <-> ProductEntity and delegates to DAO |
| Product DAO | `shared/src/commonMain/.../data/local/ProductDao.kt` | COMMON | Ordered read, insert, update and delete |
| Room database | `shared/src/commonMain/.../data/local/AppDatabase.kt` | COMMON | Room 3 schema v1 with BundledSQLiteDriver |
| Android host | `androidApp/` | ANDROID / ANDROID-UX | Compose + Material 3 application |
| iOS host | `iosApp/` | IOS / IOS-UX | SwiftUI application and Xcode project |
| Existing automated test | `shared/src/commonTest/.../SharedSmokeTest.kt` | COMMON | One smoke assertion only |
| Android CI | `.github/workflows/kmp-android.yml` | COMMON / ANDROID | Android-host tests + APK on GitHub-hosted Ubuntu |
| iOS CI | `.github/workflows/ios-app.yml` | IOS | Xcode iPhone build + unsigned IPA on macOS-15 |

Verified gaps:

- no broad COMMON regression suite;
- no explicit JVM QA target;
- no Room integration suite;
- no Android UI/instrumented suite;
- no iOS test target;
- no XCUITest suite;
- no coverage reports;
- no automated architecture boundary check;
- no executable QA gate.

External compatibility verified for this plan:

- Kover 0.9.8 supports Kotlin Multiplatform coverage through JVM/local Android test execution, but does not collect Kotlin/Native coverage.
- Kotlin Multiplatform can run common tests on `iosSimulatorArm64` with the Kotlin/Native test runner.
- Room KMP supports local JVM database tests and recommends `BundledSQLiteDriver` for consistent SQLite behavior.
- Gradle Managed Devices can provision Android emulators for instrumented CI tests.
- Xcode/XCTest supports unit, integration, UI tests and code coverage in result bundles.

## Target extraction matrix

| Responsibility | Required semantics | Proposed target | Why shared or native | Existing behavior to preserve |
| --- | --- | --- | --- | --- |
| Product input normalization | Identical | commonMain | Same rule on both products | trim name, ignore blank, clamp negative numeric values |
| Product observation | Identical | commonMain | Shared controller behavior | start/stop observation |
| Repository mapping | Identical | commonMain | Data semantics are shared | Product <-> ProductEntity |
| Room CRUD contract | Identical | commonMain + qa JVM execution | Fast hermetic KMP DB validation | ordering and persistence semantics |
| Coverage measurement for shared deterministic code | QA host only | JVM/Kover | Trustworthy line/branch measurement is JVM-based | does not create a shipped desktop product |
| Native portability execution | iOS simulator | iosSimulatorArm64Test | Proves common tests compile/run under Kotlin/Native | same common behavior |
| Android UI regression | Android | androidApp instrumented test | Native Compose behavior | Material 3 UX unchanged |
| iOS native regression | iOS | Xcode test targets | Native SwiftUI behavior | SwiftUI UX unchanged |
| QA gate | Repository/CI | scripts + workflows | Target-neutral reporting contract | USD 0 public CI |
| Architecture boundary checks | Repository | lightweight script/Gradle CI | No heavy static-analysis dependency required | COMMON remains framework-clean |

## Proposed solution

Build the QA baseline in five layers.

### Layer A - COMMON deterministic regression

Expand `commonTest` with behavior-focused tests for:

- `ProductController`;
- `ProductSnapshot`;
- repository contract behavior through fakes where DB integration is not required;
- input trimming;
- blank-name rejection;
- negative quantity/price coercion;
- create/update/delete dispatch;
- observation start/stop/restart behavior.

To make controller tests deterministic without changing user-visible behavior, introduce an internal test seam so tests can supply a controlled coroutine scope while the public production constructor preserves its current behavior.

### Layer B - JVM QA host + Room integration

Add a JVM target to `shared` explicitly documented as **QA-only**.

It will not produce a user-facing desktop application.

The JVM QA target will:

- compile common code;
- configure Room code generation for JVM;
- provide the required JVM `Platform` actual;
- use Room 3 + `BundledSQLiteDriver`;
- execute file-backed and/or in-memory Room tests.

Room integration scenarios:

- empty database;
- create;
- ordered read;
- update;
- delete;
- multiple rows;
- close/reopen of a file-backed database with data preserved;
- repository mapping round-trip.

Schema migration v1 -> v2 remains out of scope.

### Layer C - Android native QA

Add an Android instrumented Compose regression suite.

Use a Gradle Managed Device on the public Ubuntu runner so the CI remains USD 0 and does not require a persistent self-hosted emulator.

Initial UI scenarios:

- empty-state rendering;
- open create sheet;
- input product values;
- create product;
- observe created row;
- edit product;
- observe changed row;
- delete product;
- verify removal.

Prefer one end-to-end CRUD flow over many redundant UI tests.

Where stable selectors are missing, add invisible test/accessibility identifiers or semantics without changing visual design.

The Room database itself remains covered primarily by the faster JVM integration suite. Android instrumentation proves application integration and native UI behavior rather than duplicating every DAO test.

### Layer D - iOS native QA

Extend the Xcode project with:

- an XCTest unit/integration target;
- an XCUITest UI target where required by the approved scenarios;
- code coverage enabled for the native iOS target;
- a result bundle suitable for CI evidence.

Initial iOS scenarios:

- app launches in simulator;
- empty state is observable on a clean test install;
- create product;
- edit product;
- delete product;
- relaunch and verify persistence for a retained product where the test isolation strategy permits it.

Use robust accessibility identifiers for UI automation when native labels are not sufficiently stable.

Separately run:

`shared:iosSimulatorArm64Test`

so the COMMON suite itself executes through Kotlin/Native.

Swift/iOS code coverage will be reported separately from Kover coverage. It will not be presented as coverage of Kotlin shared code.

### Layer E - QA gate and evidence aggregation

Add a lightweight repository QA gate.

Responsibilities:

- verify required commands completed;
- read or receive the relevant coverage results;
- enforce the approved COMMON coverage thresholds;
- reject missing mandatory evidence;
- emit PASS / FAIL / BLOCKED / NOT RUN / NOT APPLICABLE;
- never synthesize incompatible coverage percentages.

The gate itself receives automated contract tests:

- all mandatory evidence PASS -> exit success;
- one mandatory FAIL -> exit failure;
- mandatory NOT RUN -> exit failure;
- COMMON coverage below threshold -> exit failure;
- approved NOT APPLICABLE -> does not fail;
- optional informational metric missing -> does not masquerade as PASS.

This controlled test proves CA-13 without committing a deliberately broken application test.

## Source sets, modules and boundaries

| Module / source set | Existing / proposed | Responsibility | May depend on |
| --- | --- | --- | --- |
| `shared/commonMain` | Existing | Product/data/persistence semantics | common KMP dependencies only |
| `shared/commonTest` | Existing, expand | Portable deterministic regression | kotlin.test, coroutine test support |
| `shared/jvmMain` | Proposed QA support | Minimal JVM actuals required to compile shared code | JVM-safe APIs only |
| `shared/jvmTest` | Proposed | Room/SQLite integration and Kover host execution | common code, Room test/runtime |
| `shared/androidMain` | Existing | Android DB builder/factory | Android APIs |
| `shared/iosMain` | Existing | iOS DB builder/factory | Apple APIs |
| `androidApp/src/androidTest` | Proposed | Compose native integration/UI regression | Android test APIs |
| `iosAppTests` | Proposed | Native Swift unit/integration QA | XCTest + app module |
| `iosAppUITests` | Proposed | Native simulator UI regression | XCTest/XCUIAutomation |
| `scripts/qa/` | Proposed | QA gate, architecture checks and report normalization | shell/Python stdlib only if needed |

No dependency direction from COMMON to Android/iOS framework code is permitted.

## Contracts and platform adapters

| Capability / contract | COMMON contract needed? | Android implementation | iOS implementation | Decision state |
| --- | --- | --- | --- | --- |
| ProductRepository | Yes, existing | Room-backed through shared implementation | Same shared implementation | VERIFIED |
| Coroutine scope test seam | Internal only | production default remains Main scope | production default remains Main scope | PROPOSED |
| Room database for tests | No product contract | Android app uses existing builder; JVM QA uses test builder | existing iOS production builder unchanged | PROPOSED |
| UI selectors | No | Compose semantics/test tags as needed | accessibility identifiers as needed | PROPOSED |
| QA result status | Repository-level | same status vocabulary | same status vocabulary | PROPOSED |

No new runtime DI framework is introduced.

## Data and persistence

- **Logical models and contracts:** unchanged.
- **Production Room schema:** unchanged at version 1.
- **JVM test database:** isolated from production, uses Room 3 and BundledSQLiteDriver.
- **Persistent QA scenario:** use a temporary file-backed database; close and reopen it to prove same-schema persistence.
- **Android UI tests:** use isolated app/emulator state.
- **iOS UI tests:** use clean simulator state per suitable test boundary; a dedicated persistence scenario may preserve state across relaunch within one test.
- **Migration strategy:** Not applicable to this feature; real v1 -> v2 migration remains a future experiment.
- **Upgrade evidence:** Not claimed by this automation baseline beyond same-schema close/reopen/relaunch scenarios.

No production data is deleted or modified by CI tests.

## State, lifecycle and errors

| Concern | COMMON semantics | Android mechanism | iOS mechanism | Related RF/CA |
| --- | --- | --- | --- | --- |
| Controller async execution | Deterministic repository effects | test scope for COMMON tests | same COMMON tests on Native | RF-02/CA-02 |
| Observation start/stop | Shared | common tests | common tests on Native | RF-02/CA-02 |
| App relaunch persistence | Persisted product remains | existing physical evidence + UI integration where practical | simulator UI persistence scenario | RF-07/CA-07 |
| UI error isolation | Test failure visible | instrumentation result | XCTest/XCUITest result | RF-12/CA-12 |
| Gate failure | Shared evidence rule | workflow job fails | workflow job fails | RF-13/CA-13 |

## Native UX implementation

- **Android:** keep current Compose / Material 3 design. Add only testability/accessibility metadata required for robust automation.
- **iOS:** keep current SwiftUI design. Add only stable accessibility identifiers/test seams required for automation.
- **Shared UI:** Not applicable. The lab retains native platform UI.

## Dependencies and compatibility

| Dependency / SDK | Used by | Existing / new | Android verified | iOS/Kotlin-Native verified | Reason |
| --- | --- | --- | --- | --- | --- |
| Kotlin test | commonTest | Existing | Yes | Yes | Portable assertions/tests |
| kotlinx-coroutines-test matching current coroutines line | commonTest | New | Verify during implementation | Verify during implementation | Deterministic coroutine tests |
| Kover 0.9.8 | shared QA/JVM | New | JVM/local Android coverage supported | Native coverage explicitly unsupported | COMMON line/branch coverage gate |
| Room 3.0.3 | shared/JVM QA | Existing | Yes | Yes | Production persistence + QA DB |
| Bundled SQLite 2.7.1 | shared/JVM QA | Existing | Yes | Yes | SQLite consistency |
| Android Compose UI test stack | androidApp androidTest | New test-only | Verify resolved Compose compatibility | N/A | Native UI regression |
| AndroidX test runner/core | androidApp androidTest | New test-only | Verify against AGP 9.1.1 | N/A | Instrumented tests |
| Gradle Managed Devices | Android CI | New configuration | Supported by AGP family in use; validate exact AGP 9.1.1 behavior during implementation | N/A | Zero-cost emulator CI |
| XCTest / XCUIAutomation | Xcode test targets | Platform SDK | N/A | Yes | Native iOS QA |
| Xcode code coverage | iOS CI | Platform tooling | N/A | Yes | Native Swift coverage |

Important limitation:

Kover does not collect Kotlin/Native coverage. COMMON coverage percentage is therefore the JVM-executed COMMON metric, while `iosSimulatorArm64Test` independently proves Native execution of the shared tests.

## Zero-cost CI and repository visibility

- **USD 0 is an approved constraint?** Yes.
- **Repository visibility:** Public.
- **Linux/Android execution path:** standard GitHub-hosted Ubuntu.
- **macOS/iOS execution path:** standard GitHub-hosted macOS-15.
- **Standard hosted runners only?** Yes.
- **Private-repository alternative:** Not applicable to this lab.
- **Publication-readiness review:** already satisfied by the public-lab model; no secrets may be introduced.

CI execution policy:

### Common/JVM job

Runs on Ubuntu:

- architecture boundary script;
- COMMON/JVM tests;
- Room JVM integration;
- Kover XML/HTML report;
- Kover threshold verification;
- Android lint;
- QA gate unit/contract tests.

### Android native job

Runs on Ubuntu with a Gradle Managed Device:

- Android instrumented Compose CRUD smoke/regression;
- result/report artifact.

### iOS job

Runs on macOS-15:

- `iosSimulatorArm64Test`;
- Xcode unit/UI tests on an available iPhone simulator;
- Xcode code coverage/result bundle;
- existing unsigned IPA build/package after tests pass.

Path filters should avoid invoking macOS for documentation-only changes.

## QA strategy

- **Feature criticality:** High, because this increment defines the quality baseline later used to validate real KMP migrations.
- **Requirements coverage target:** 100% of approved CA mapped to evidence.
- **Changed deterministic code line coverage target:** >= 90% through the trustworthy Kover/JVM scope.
- **Changed deterministic branch/decision coverage target:** >= 85% through the trustworthy Kover/JVM scope.
- **Critical scenario coverage:** target 95-100% for ProductController deterministic branches and Room CRUD scenarios.
- **Regression scope:** Product controller rules, repository mapping, CRUD, persistence close/reopen, Android CRUD UI, iOS CRUD UI, existing build/package paths.
- **Architecture checks:** forbid Android/Apple framework imports in `commonMain`; forbid application UI imports in shared common code.
- **Static quality checks:** Android lint plus compilation warnings surfaced by normal builds.
- **Android automated QA:** COMMON/JVM + Room JVM + instrumented Compose flow.
- **iOS automated QA:** COMMON Kotlin/Native suite + XCTest/XCUITest.
- **Integration QA:** Room repository/DAO, Kotlin->Swift app integration, Android host integration.
- **Accessibility QA:** stable semantics/identifiers required by automated flows; broader accessibility audit remains evidence-based and no visual redesign is introduced.
- **Security/privacy QA:** verify no secrets/test credentials introduced; test artifacts contain no sensitive identifiers.
- **Performance/resource QA:** Not a quantitative gate in this feature because the SPEC contains no performance threshold.
- **Device/OS matrix:** Android managed virtual device at a current supported API suitable for targetSdk 36; iPhone simulator available on macOS-15; existing physical Android/iPhone evidence remains separate.
- **Release-blocking defect policy:** default QA strategy, BLOCKER/CRITICAL block completion.
- **Coverage tooling limitation:** Kover JVM coverage and Xcode Swift coverage are separate surfaces.

## Validation and evidence plan

| CA | Target | Method | Environment | Evidence to record |
| --- | --- | --- | --- | --- |
| CA-01 | MULTI | evidence ledger validation | CI | completed ledger |
| CA-02 | COMMON | commonTest suite | JVM + iOS simulator | test reports |
| CA-03 | COMMON | Kover verification | Ubuntu/JVM | XML/HTML + threshold result |
| CA-04 | COMMON | scenario matrix review + tests | CI | scenario ledger |
| CA-05 | ANDROID | Compose instrumentation | managed Android device | instrumentation report |
| CA-06 | IOS | XCTest/XCUITest | macOS iPhone simulator | xcresult/test summary |
| CA-07 | MULTI | Room JVM persistence + platform regression | JVM + platform suites | DB integration report |
| CA-08 | MULTI | CRUD scenario ledger | JVM + Android + iOS | C/R/U/D evidence |
| CA-09 | MULTI | architecture/static scripts + lint | Ubuntu | job logs |
| CA-10 | MULTI | report contract test | CI | scoped coverage summaries |
| CA-11 | MULTI | inspect runners/services | GitHub Actions | workflow metadata |
| CA-12 | MULTI | QA result normalization | CI | status summary |
| CA-13 | MULTI | controlled gate contract tests | Ubuntu | expected non-zero cases |
| CA-14 | ANDROID-UX/IOS-UX | existing flow regression | emulators/simulators + previous physical evidence | UI test results |
| CA-15 | MULTI | completion report review | repository | explicit remaining-gap section |

### Deterministic equivalence

No product algorithm is being replaced.

However, any testability refactor to `ProductController` must prove:

- same public CRUD calls;
- same name trimming;
- same blank-name no-op;
- same negative-value coercion;
- same observation semantics;
- same production default dispatcher/scope behavior.

### Physical-device validation

- **Android:** Not required for every QA implementation commit because existing physical CRUD evidence exists; required again before claiming any new hardware/device-specific behavior.
- **iOS:** Same rule. Simulator automation does not erase the existing physical-device requirement for provisioning or hardware-specific claims.

## Implementation order

1. Add JVM QA target, Room JVM test capability and Kover reporting without changing production behavior.
2. Add COMMON deterministic tests and Room integration/persistence tests; reach/verify the agreed coverage threshold.
3. Add architecture/static QA checks and executable QA gate with controlled failure tests.
4. Add Android managed-device Compose CRUD regression and integrate it into CI.
5. Add iOS Kotlin/Native shared test execution plus Xcode test targets, native UI regression and Swift coverage.
6. Re-run Android/iOS builds and package artifacts; reconcile QA evidence and documentation.
7. Produce final CA -> target -> evidence ledger and record remaining unautomated gaps.

Each stage is a separate reviewable commit or small PR-sized increment where practical.

## Risks and pending decisions

### Risks

- Kover may require small configuration adjustments for the current Android-KMP/Gradle plugin combination. If the Android-host variant is unreliable, the approved fallback is the explicit JVM QA target, not fabricated coverage.
- Adding a JVM target requires Room KSP generation and a minimal JVM `Platform` actual; it must remain clearly QA-only.
- Managed Android emulators can add CI latency. Keep one focused end-to-end UI flow rather than duplicating lower-level tests.
- iOS simulator availability names can vary across hosted runner images. CI should resolve an available iPhone simulator dynamically rather than hardcode an ephemeral device name.
- Xcode coverage measures native Swift/ObjC execution, not Kotlin/Native shared line coverage.
- UI automation may expose missing stable accessibility identifiers. Add nonvisual identifiers only where necessary; broader UX changes require separate approval.

### Pending decisions

None that require a product decision before implementation.

Routine technical verification still required during implementation:

- resolved Android Compose testing artifact versions;
- exact Gradle task names produced by Kover with the final JVM/Android target configuration;
- exact available Android system image used by the managed device;
- exact iOS simulator selected on macOS-15.

These are environment/tooling details and do not change the approved behavior or scope.

<!--
Before requesting approval:
- every CA has an implementation/evidence path;
- COMMON coverage is measurable without pretending Kover measures Native;
- Room tests use a QA-only JVM path;
- native UX remains native;
- zero-cost CI remains intact;
- migration remains explicitly out of scope.
-->
