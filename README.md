# KMP Zero-Cost Lab

Reference Kotlin Multiplatform laboratory for a **USD 0 Android + iOS development path** from a Windows/WSL workstation with no locally owned Mac.

The repository also serves as the laboratory for an SDD-KMP process that separates shared semantics from platform-native behavior.

## Current validated model

The current architecture follows this principle:

> Share the brain when behavior should be identical. Keep the face native when platform experience should remain native.

### Shared KMP core

`shared/` currently contains:

- product domain model and repository contract;
- shared product controller;
- Room 3 database, DAO and entity;
- bundled SQLite driver;
- Room-backed repository;
- Android and iOS database builders in their target source sets.

The shared module does **not** own the application UI.

### Android

`androidApp/` provides a native Android presentation using:

- Jetpack Compose;
- Material 3;
- edge-to-edge layout;
- Android keyboard and focus behavior;
- platform composition through `createAndroidProductController(...)`.

### iOS

`iosApp/` provides native SwiftUI presentation using:

- `NavigationStack`;
- native `List`;
- swipe actions;
- native sheets and forms;
- SwiftUI keyboard/focus interaction;
- platform composition through the iOS product controller factory.

## What has been demonstrated

At the current validated baseline, repository history and physical-device validation show:

- shared Room/SQLite CRUD on Android and iOS;
- CREATE / READ / UPDATE / DELETE on physical Android;
- CREATE / READ / UPDATE / DELETE on physical iPhone;
- persistence across app restart on both platforms;
- preservation of existing iPhone data when a newer IPA was installed over the previous build;
- Android-native Compose / Material 3 UI;
- iOS-native SwiftUI UI;
- Android APK build through GitHub-hosted Ubuntu;
- iPhone `.app` and unsigned `.ipa` build through GitHub-hosted `macos-15`;
- provisioning/sideloading from Windows with a free Apple ID path;
- successful launch and CRUD validation on physical iPhone;
- USD 0 infrastructure for the validated public-repository path.

Primary historical evidence is recorded in PR #4 and PR #5.

## What is still not proven

Do not treat these as complete:

- a real Room schema migration such as database v1 -> v2 on both platforms;
- preservation of Android data across an installed app upgrade equivalent to the validated iOS upgrade scenario;
- a private-repository zero-cost iOS CI solution;
- Codemagic as a validated fallback provider under the current process.

See `docs/VALIDATION-MATRIX.md`.

## Current versions

From `gradle/libs.versions.toml`:

- Kotlin: 2.4.20
- Android Gradle Plugin: 9.1.1
- Compose Multiplatform: 1.12.0
- Android Material 3: 1.4.0
- Room 3: 3.0.3
- SQLite bundled driver: 2.7.1
- KSP: 2.3.12
- kotlinx.coroutines: 1.11.0
- Android compile SDK: 37
- Android target SDK: 36
- Android min SDK: 24

Current CI uses Gradle 9.3.1.

## Modules

```text
androidApp/     Android host and native Compose / Material 3 presentation
shared/         KMP domain, controller, Room/SQLite persistence and target DB builders
iosApp/         Native SwiftUI iOS host
.github/        GitHub Actions for Android/Linux and iOS/macOS
docs/           SDD-KMP process, evidence and roadmap
scripts/        Local bootstrap/diagnostics
```

## CI policy

### Common / Android

The public reference repository uses a standard GitHub-hosted Ubuntu runner.

The workflow executes shared Android-host tests and builds the Android debug APK.

### iOS

The iOS application workflow uses standard GitHub-hosted `macos-15` and Xcode to produce an unsigned iPhone app and IPA.

The zero-cost claim is conditional on the provider policy, repository visibility and runner class described in `docs/ZERO_COST_CI.md`.

A Windows/WSL self-hosted runner may be useful for local/common/Android work, but it cannot replace Xcode for iOS compilation.

## SDD-KMP process

When the SDD-KMP enrichment is present, begin with:

- `AGENTS.md`
- `docs/GENERIC_RULES.md`
- `docs/MOBILE_KMP_GUIDELINES.md`
- `docs/SPEC_TEMPLATE.md`
- `docs/PLAN_TEMPLATE.md`
- `docs/TASKS_TEMPLATE.md`
- `docs/EVIDENCE_GUIDELINES.md`
- `docs/ZERO_COST_CI.md`

## Next proof

The most valuable remaining technical proof is **real persisted-data migration**, not another UI layer.

The next persistence experiment should create a prior database state, upgrade the schema through an explicit Room migration, and demonstrate preserved data on Android and iOS.

See `docs/ROADMAP.md` and `docs/VALIDATION-MATRIX.md`.
