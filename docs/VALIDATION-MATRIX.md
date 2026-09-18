# Validation Matrix

This matrix records observed evidence. It does not mark a row PASS because the code appears capable of the behavior.

Allowed status values:

- PASS
- FAIL
- BLOCKED
- NOT RUN
- NOT APPLICABLE

## Toolchain, CI and artifacts

| ID | Target | Validation | Executor / evidence source | Cost target | Status |
|---|---|---|---|---:|---|
| P0-01 | COMMON/ANDROID | Shared KMP/Android host test path compiles and executes | GitHub-hosted Ubuntu | $0 | PASS |
| P0-02 | ANDROID | Android debug APK is produced | GitHub-hosted Ubuntu | $0 | PASS |
| P0-03 | IOS | `iosArm64` KMP framework compiles | GitHub-hosted macOS | $0 | PASS |
| P1-01 | IOS | Xcode app compiles for `iphoneos` | GitHub-hosted macOS | $0 | PASS |
| P1-02 | IOS | Unsigned development IPA artifact is produced | GitHub-hosted macOS | $0 | PASS |
| P1-03 | IOS | IPA is provisioned/sideloaded from Windows using validated free Apple ID path | Windows + physical iPhone; PR #4 history | $0 | PASS |
| P1-04 | IOS | Application launches on physical iPhone | Physical iPhone; PR #4 history | $0 | PASS |

## Room / SQLite and CRUD

| ID | Target | Validation | Evidence source | Cost target | Status |
|---|---|---|---|---:|---|
| P2-A01 | ANDROID | Room/SQLite database opens through the application flow | Physical Android CRUD/persistence; PR #5 | $0 | PASS |
| P2-I01 | IOS | Room/SQLite database opens through the application flow | Physical iPhone CRUD/persistence; PR #4/#5 | $0 | PASS |
| P2-A02 | ANDROID | CREATE / READ / UPDATE / DELETE | Physical Android; PR #5 | $0 | PASS |
| P2-I02 | IOS | CREATE / READ / UPDATE / DELETE | Physical iPhone; PR #4/#5 | $0 | PASS |
| P2-A03 | ANDROID | Persisted product data survives app restart | Physical Android; PR #5 | $0 | PASS |
| P2-I03 | IOS | Persisted product data survives app restart | Physical iPhone; PR #4 | $0 | PASS |

## Native UX

| ID | Target | Validation | Evidence source | Status |
|---|---|---|---|---|
| P3-A01 | ANDROID-UX | Android presentation uses native Compose / Material 3 interaction model | Current source + physical Android validation in PR #5 | PASS |
| P3-I01 | IOS-UX | iOS presentation uses native SwiftUI interaction model | Current source + physical iPhone validation in PR #5 | PASS |
| P3-C01 | COMMON | Shared module owns product/data/persistence behavior without owning application UI | Current source tree at validated baseline | PASS |

## Upgrade and migration

| ID | Target | Validation | Evidence source | Status |
|---|---|---|---|---|
| P4-I01 | IOS | Existing product data survives installing a newer IPA over the previous build | Physical iPhone; PR #4 and PR #5 | PASS |
| P4-A01 | ANDROID | Existing product data survives installing a newer APK over the previous build | No equivalent executed Android upgrade evidence located | NOT RUN |
| P4-A02 | ANDROID | Real Room schema v1 -> v2 migration preserves existing data | No migration implemented/executed | NOT RUN |
| P4-I02 | IOS | Real Room schema v1 -> v2 migration preserves existing data | No migration implemented/executed | NOT RUN |

Current database schema is version 1. A restart, close/reopen, terminate/relaunch, or same-schema app update is **not** evidence of a schema migration.

## QA Automation Baseline

| ID | Target | Validation | Evidence source | Status |
|---|---|---|---|---|
| QA-01 | COMMON/JVM | Deterministic COMMON line coverage meets >= 90% gate | Kover run #40 / `35296308302`; final scoped result 100.00% | PASS |
| QA-02 | COMMON/JVM | Deterministic COMMON branch coverage meets >= 85% gate | Kover run #40 / `35296308302`; final scoped result 100.00% | PASS |
| QA-03 | COMMON | Real QA gate rejects insufficient coverage without threshold reduction | Run #39 / `35296093947` rejected 89.06% LINE / 70.00% BRANCH | PASS |
| QA-04 | COMMON/JVM | Room CRUD + same-schema close/reopen persistence | JVM Room integration suite, run #40 | PASS |
| QA-05 | COMMON | COMMON source-set architecture boundary guard | Boundary guard + contract tests, run #40 | PASS |
| QA-06 | ANDROID-UX | Native Compose CRUD regression on managed Android device | Pixel 2 API 35, run #44 / `35297235909` | PASS |
| QA-07 | IOS | COMMON regression suite executes under Kotlin/Native | `iosSimulatorArm64Test`, final iOS run #22 / `35304772114` | PASS |
| QA-08 | IOS-UX | Native SwiftUI CRUD regression | XCUITest, final iOS run #22 / `35304772114` | PASS |
| QA-09 | IOS | Room data survives terminate/relaunch inside one isolated XCUITest scenario | Final iOS run #22 / `35304772114` | PASS |
| QA-10 | IOS-UX | Native Xcode application line coverage exported | `xccov`: KMPZeroCostLab.app 97.51% (745/764), artifact `10531750207` | PASS |
| QA-11 | MULTI | Android and iOS final regression lanes pass on the same technical HEAD | HEAD `32ba6153dfc540d02e239149d393ec07a345f879`; Android #51 + iOS #22 | PASS |
| QA-12 | IOS | Unsigned `iphoneos` app and IPA produced after native QA | iOS run #22; IPA artifact `10531750210` | PASS |
| QA-13 | MULTI | QA evidence remains target-scoped with no synthetic global percentage | Kover COMMON/JVM and Xcode Swift metrics reported separately | PASS |

Coverage interpretation:

- Kover 100.00% LINE / 100.00% BRANCH applies only to the explicitly filtered deterministic COMMON/JVM scope (`ProductController` / `ProductSnapshot` / `RoomProductRepository`).
- Xcode `xccov` 97.51% (745/764) applies to `KMPZeroCostLab.app` native Xcode/Swift execution.
- Kotlin/Native COMMON execution is PASS evidence, but no unsupported Kotlin/Native line/branch percentage is claimed.
- These values must not be averaged into a repository-wide or cross-platform score.

## Zero-cost workflow

| ID | Scope | Validation | Evidence | Status |
|---|---|---|---|---|
| P5-01 | PUBLIC REPO | Standard GitHub-hosted Ubuntu is used for routine KMP/Android CI | Current `.github/workflows/kmp-android.yml` + final Android run #51 | PASS |
| P5-02 | PUBLIC REPO | Standard GitHub-hosted macOS is used for iOS/Kotlin-Native/Xcode QA and build | Current `.github/workflows/ios-app.yml` + final iOS run #22 | PASS |
| P5-03 | PUBLIC REPO | Validated lab path required no paid build infrastructure | Historical lab validation + QA baseline runs | PASS |
| P5-04 | PRIVATE REPO | Zero-cost iOS CI strategy is validated | No private-repository strategy selected | NOT RUN |
| P5-05 | OPTIONAL FALLBACK | Codemagic fallback is validated | Not required by current public-lab path | NOT APPLICABLE |

Provider pricing and quotas can change. Re-verify current provider policy when a future project depends on the USD 0 claim.

## Evidence summary

Repository history supports these major milestones:

- PR #1: public-repository Android/common CI moved to GitHub-hosted Ubuntu.
- PR #2: iOS KMP framework validation on GitHub-hosted macOS.
- PR #3: runnable iPhone application and unsigned IPA packaging.
- PR #4: shared Room/SQLite CRUD and physical iPhone persistence/upgrade evidence.
- PR #5: platform-native Android/iOS UX plus physical Android CRUD/persistence and physical iPhone regression validation.
- QA core increment: deterministic COMMON coverage gate, Room JVM integration, architecture guard and controlled fail/pass evidence.
- Android QA increment: native Compose CRUD on a Gradle Managed Device.
- iOS QA increment: COMMON Kotlin/Native execution, native SwiftUI XCUITest CRUD/relaunch persistence, `xccov`, unsigned device build and IPA.

See `docs/features/qa-automation-baseline/COMPLETION.md` for the QA Automation Baseline completion record.

## Current stop condition

The **QA Automation Baseline feature is complete**, but the broader persistence story is **not fully closed** until the explicit migration/upgrade proofs below exist.

The next high-value validation remains:

1. Android installed-upgrade preservation;
2. Room v1 -> v2 non-destructive migration on Android;
3. Room v1 -> v2 non-destructive migration on iOS.

Private-repository zero-cost iOS CI is also still unvalidated.

No screenshot, fresh install, same-schema relaunch, coverage percentage or green build may substitute for those checks.