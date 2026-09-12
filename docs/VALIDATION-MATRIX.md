# Validation Matrix

The pilot is not complete until every required row is PASS.

| ID | Validation | Executor | Cost target | Status |
|---|---|---|---:|---|
| P0-01 | Shared Kotlin/Compose compiles for Android | WSL self-hosted | $0 | PENDING |
| P0-02 | `commonTest` executes through Android host test | WSL self-hosted | $0 | PENDING |
| P0-03 | Android debug APK is produced | WSL self-hosted | $0 | PENDING |
| P0-04 | Android APK runs on a physical Android device | Human/device | $0 | PENDING |
| P0-05 | `iosArm64` Compose/Kotlin framework compiles | GitHub macOS | $0 | PENDING |
| P1-01 | Xcode shell app compiles for `iphoneos` | GitHub macOS | $0 | PENDING |
| P1-02 | Unsigned development IPA artifact is produced | GitHub macOS | $0 | PENDING |
| P1-03 | IPA is provisioned/sideloaded from Windows | Windows + iPhone | $0 | PENDING |
| P1-04 | App opens on physical iPhone | Human/device | $0 | PENDING |
| P2-01 | Room/SQLite database opens on Android | Shared + Android | $0 | PENDING |
| P2-02 | Room/SQLite database opens on iOS | Shared + iOS | $0 | PENDING |
| P2-03 | Product CREATE/READ/UPDATE/DELETE works on Android | Physical Android | $0 | PENDING |
| P2-04 | Product CREATE/READ/UPDATE/DELETE works on iOS | Physical iPhone | $0 | PENDING |
| P2-05 | Data survives app restart | Both platforms | $0 | PENDING |
| P3-01 | Build update preserves existing data | Both platforms | $0 | PENDING |
| P3-02 | DB v1 -> v2 migration preserves existing data | Both platforms | $0 | PENDING |
| P4-01 | Same iOS commit also compiles in Codemagic free tier | Codemagic | $0 | PENDING |

## Stop condition

Any provider that requires paid billing is out of scope. The pilot must remain usable with a hard development infrastructure budget of USD 0.
