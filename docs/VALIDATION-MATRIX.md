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

Current database schema is version 1. A restart test or an app upgrade with the same schema is **not** evidence of a schema migration.

## Zero-cost workflow

| ID | Scope | Validation | Evidence | Status |
|---|---|---|---|---|
| P5-01 | PUBLIC REPO | Standard GitHub-hosted Ubuntu is used for routine KMP/Android CI | Current `.github/workflows/kmp-android.yml` | PASS |
| P5-02 | PUBLIC REPO | Standard GitHub-hosted macOS is used for iOS/Xcode build | Current `.github/workflows/ios-app.yml` | PASS |
| P5-03 | PUBLIC REPO | Validated lab path required no paid build infrastructure | Historical lab validation | PASS |
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

## Current stop condition

The core public-lab KMP path is validated, but the persistence story is **not fully closed** until the explicit schema migration proof exists on both targets.

The next high-value validation is therefore:

1. Android installed-upgrade preservation;
2. Room v1 -> v2 non-destructive migration on Android;
3. Room v1 -> v2 non-destructive migration on iOS.

No screenshot, fresh install or green build may substitute for those checks.
