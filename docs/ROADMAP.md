# Reference Pilot Roadmap

This roadmap reflects the repository state after the validated Room/SQLite and platform-native UX increments.

It distinguishes **completed evidence** from **remaining proofs**. Historical phases are kept to explain how the laboratory evolved.

## Completed: Phase 0 - Toolchain and Android/iOS compilation baseline

Validated:

- KMP shared module compiles for Android and physical-iPhone ARM64 target.
- Common/Android-host tests execute in GitHub-hosted Ubuntu.
- Android debug APK is produced.
- iOS KMP framework compilation runs on GitHub-hosted macOS.
- Public-repository CI moved away from a self-hosted WSL runner for security and simplicity.

Evidence: PR #1 and PR #2.

## Completed: Phase 1 - Runnable iPhone application artifact

Validated:

- thin Xcode host application;
- `iphoneos` build with code signing disabled in CI;
- unsigned `.app`;
- unsigned `.ipa`;
- provisioning/sideloading from Windows using the validated free Apple ID path;
- launch on a physical iPhone.

Evidence: PR #3 and subsequent physical validation recorded by PR #4.

## Completed: Phase 2 - Shared Room/SQLite CRUD

The shared KMP core now contains:

- Room 3 database;
- bundled SQLite driver;
- product entity and DAO;
- repository contract and Room implementation;
- shared product controller;
- Android database builder;
- iOS database builder.

Validated on physical devices:

- iPhone CRUD;
- persistence after restart;
- preservation of iPhone data when installing a newer IPA over the previous build.

Evidence: PR #4.

## Completed: Phase 3 - Platform-native UX

The production-oriented presentation model is now:

- **shared:** business/data/persistence core;
- **Android:** Jetpack Compose + Material 3;
- **iOS:** native SwiftUI.

Validated:

- physical Android CREATE / READ / UPDATE / DELETE;
- physical Android Room/SQLite persistence;
- physical iPhone CREATE / READ / UPDATE / DELETE;
- existing iPhone Room data preserved through the native-UX upgrade;
- native Android and iOS interaction patterns;
- Android and iOS CI builds.

Evidence: PR #5.

## Current: Phase 4 - SDD-KMP process enrichment and evidence reconciliation

Goals:

- adapt useful SDD discipline to KMP without importing Android-only architectural choices;
- introduce COMMON / ANDROID / IOS / ANDROID-UX / IOS-UX target reasoning;
- require target-aware evidence;
- formalize deterministic equivalence for real migrations;
- formalize zero-cost CI constraints;
- keep repository documentation synchronized with observed evidence.

This phase changes process/documentation, not application behavior.

## Next: Phase 5 - Real persistence migration proof

This is the highest-value remaining technical gap.

Target experiment:

1. establish a representative Room database at schema version 1;
2. create persisted products;
3. introduce a controlled schema version 2, for example a nullable field approved by the experiment;
4. implement an explicit non-destructive migration;
5. install/upgrade without clearing data;
6. verify old rows survive;
7. create/update/delete after migration;
8. restart and verify persistence again;
9. execute equivalent evidence on Android and iOS.

No destructive fallback may substitute for the migration proof.

## Remaining: Android installed-upgrade preservation

Physical Android persistence after restart is validated.

What remains separate and unproven is the exact installed-upgrade scenario:

- create representative data with an older Android build;
- install the newer APK over it without uninstalling;
- verify the previous data is preserved.

This should be validated alongside or before the real schema migration experiment.

## Conditional future research - private repository / alternative iOS providers

The current public-repository strategy already provides the reference USD 0 hosted path.

A Codemagic or other provider fallback is therefore **not required to prove the present public-lab architecture**.

It becomes relevant only if a future product:

- must remain private;
- cannot use the same GitHub-hosted macOS cost model;
- exhausts or loses access to the reference path;
- explicitly requires provider redundancy.

Any such option must be re-verified against current provider policy before adoption.

## Promotion boundary

Do not promote this process into a broader software-development blueprint yet.

Promotion requires evidence from:

1. this KMP laboratory;
2. the enriched SDD-KMP process;
3. application to a real existing product;
4. preservation of proven behavior;
5. Android and iOS evidence under real migration pressure.

PanicLab is the intended real-product validation candidate after the KMP process is internally reconciled.
