# Reference Pilot Roadmap

## Phase 0 - Baseline

Goal: prove one shared Compose UI compiles for Android and iPhone ARM64 without adding database complexity.

- Shared UI in `shared/commonMain`.
- `expect/actual` platform identification.
- Android APK from a WSL self-hosted runner.
- iOS ARM64 framework from a manually triggered GitHub macOS runner.

## Phase 1 - Real iPhone application package

Add the thin Xcode host application based on the official Kotlin Multiplatform wizard pattern:

- `iosApp/Configuration/Config.xcconfig`
- `iosApp.xcodeproj`
- SwiftUI host embedding `MainViewController()` from `Shared`.
- Build for `iphoneos` with code signing disabled in CI.
- Package an unsigned development `.ipa` for later provisioning/sideloading on Windows.

## Phase 2 - SQLite CRUD

Add current Room KMP / Room3 and `BundledSQLiteDriver`.

Product fields:

- `id: Long`
- `name: String`
- `quantity: Int`
- `priceMinor: Long`
- `createdAtEpochMillis: Long`

Operations:

- Create
- List/read
- Update
- Delete

The UI, repository contract and use cases stay shared. Only database path/builder details that truly require platform APIs live in platform source sets.

## Phase 3 - Persistence and migration

- Install build 1 and create products.
- Install build 2 over build 1 and verify data remains.
- Add a nullable `description` column in DB v2.
- Run a governed migration and verify existing rows remain valid on Android and iOS.

## Phase 4 - Provider fallback

Reproduce the exact iOS build from the same Git commit in Codemagic using only its free allowance and with paid billing disabled.

## Blueprint evidence

Only after the pilot passes do we promote findings into the proposed Blueprint 0.6.0 mobile baseline.
