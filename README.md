# KMP Zero-Cost Lab

Reference pilot to prove a **USD 0 development workflow** for one Kotlin Multiplatform mobile product targeting Android and iOS from a Windows/WSL workstation with no locally owned Mac.

## What this repository proves

The finished pilot must demonstrate:

1. Shared Kotlin + Compose Multiplatform UI.
2. Android build and tests on a WSL self-hosted GitHub Actions runner.
3. iOS compilation only on an on-demand hosted macOS runner.
4. An installable iOS development package that can be provisioned/sideloaded to a physical iPhone from Windows using a free Apple ID workflow.
5. Shared Room/SQLite CRUD on Android and iOS.
6. Persistence across restart and app updates.
7. A real Room/SQLite migration.
8. Codemagic as a zero-cost iOS fallback provider.

## Current phase

**Phase 0 - build baseline.** Database code is intentionally deferred until Android and iOS target compilation are green. This isolates build-toolchain failures from database/KSP failures.

## Modules

```text
androidApp/     Thin Android host
shared/         KMP shared UI + logic (later DB/repositories/use cases)
.github/        CI policy: WSL for normal work, macOS only on demand
docs/           Validation evidence and roadmap
scripts/        Local bootstrap/diagnostics
```

## Versions pinned for Phase 0

- Kotlin 2.4.20
- Compose Multiplatform 1.12.0
- Android Gradle Plugin 9.0.1
- Gradle 9.1.0
- Android compile/target SDK 36
- Android min SDK 24

The AGP/Gradle structure follows the current official Kotlin KMP wizard family, while Kotlin/Compose are pinned to current stable releases for this pilot.

## Local bootstrap in WSL

Requirements: JDK 17+, Android SDK, `curl`, `unzip`, Git.

```bash
./scripts/doctor.sh
./scripts/bootstrap-gradle-wrapper.sh
./gradlew :shared:testAndroidHostTest
./gradlew :androidApp:assembleDebug
```

## CI policy

### Continuous KMP / Android validation

Runs on the repository's **self-hosted WSL runner**.

### iOS validation

`iOS KMP Framework (manual)` is `workflow_dispatch` only and uses `macos-15`. Phase 0 compiles the real `iosArm64` framework but deliberately does not yet package an IPA.

## Next increment

Once Phase 0 is green, add the official thin Xcode host and produce the first unsigned iPhone application artifact. Only after that is green do we activate Room/SQLite CRUD.

See `docs/VALIDATION-MATRIX.md` and `docs/ROADMAP.md`.
