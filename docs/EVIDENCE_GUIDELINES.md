# Evidence Guidelines for KMP

Evidence must prove the acceptance criterion being claimed. Select the smallest method that actually demonstrates the behavior.

Use this document together with `docs/QA_STRATEGY.md`. The QA strategy defines coverage goals and quality gates; this document defines what evidence is capable of proving each type of claim.

## Evidence hierarchy by concern

| Concern | Useful evidence | What is not enough by itself |
| --- | --- | --- |
| Pure shared rule/algorithm | `commonTest` with representative inputs | Android-only JVM test when iOS compatibility is part of the claim |
| Shared serialization/parsing | common tests plus target compilation where needed | successful Android execution only |
| Android integration | Android unit/instrumented test, emulator or device depending on behavior | common test |
| iOS integration | iOS/Kotlin-Native compile/test, simulator or device depending on behavior | Android test |
| Native Android UX | direct Android UI review/test | iOS screenshot |
| Native iOS UX | direct iOS UI review/test | Android screenshot |
| Persistence | read/write/restart evidence | screenshot of a populated screen |
| Migration | upgrade from representative previous persisted state | fresh install |
| Lifecycle | controlled background/recreation/relaunch scenario | build success |
| Permission/camera/device capability | target OS flow, physical device when hardware is material | unit test of an adapter |
| Offline behavior | network-controlled scenario with expected local/failure behavior | successful online request |
| Distribution/installability | install the produced artifact through the intended path | artifact upload alone |
| Deterministic migration to COMMON | baseline-vs-new equivalence on representative corpus | new tests that never execute the old semantics |

## Common evidence

Use `commonTest` when the behavior is truly target-neutral and the code under test lives in common source sets.

When moving existing deterministic logic into common code, prefer a characterization/equivalence corpus before replacing the old path.

## Android evidence

Choose among:

- Kotlin/JVM tests;
- Android unit/Robolectric tests when Android runtime semantics are needed;
- instrumented tests;
- emulator;
- physical Android device.

Use the level required by the CA. Hardware- or OS-integration claims may require a real device.

## iOS evidence

Choose among:

- Kotlin/Native target compilation;
- common/iOS unit tests;
- Xcode build;
- simulator;
- physical iPhone/iPad;
- installation/provisioning validation.

A successful unsigned build does not prove installation on a physical device.

## Persistence and migration

For an upgrade requirement:

1. create or obtain representative data using the previous application/database version;
2. upgrade through the proposed path;
3. open/read the data with the new version;
4. execute representative updates;
5. restart and verify preservation.

Record the versions and scenario used.

## Deterministic equivalence record

For each migrated behavior, record:

- baseline implementation/version;
- new common implementation/version;
- input corpus;
- relevant output fields;
- intentionally ignored nondeterministic fields;
- comparison result.

If a mismatch is accepted, it is a behavior change and must be reflected in the approved SPEC.

## Coverage evidence

When code coverage is measured, record:

- scope being measured;
- tool and version when material;
- line coverage;
- branch/decision coverage when supported;
- changed-code coverage when available;
- excluded/generated code and reason;
- uncovered critical paths.

Do not average unrelated Kotlin/JVM, Kotlin/Native, Android instrumentation and Swift reports into one number unless a verified toolchain makes that aggregation meaningful.

Coverage percentages complement, but never replace, acceptance-criteria evidence.

## CI evidence

Record:

- workflow name;
- run/commit;
- target;
- result;
- artifact when relevant.

CI is reproducibility evidence, not a universal substitute for runtime evidence.

## Physical-device evidence

Record at minimum:

- platform;
- device/model when useful;
- OS version when material;
- app build/commit;
- scenario executed;
- observed result.

Do not store personal device identifiers or secrets unnecessarily.

## Reporting

Use only these outcome labels:

- PASS
- FAIL
- BLOCKED
- NOT RUN
- NOT APPLICABLE

Never translate `not executed` into PASS.
