# Mobile KMP Guidelines

Use this guide while drafting or reviewing a SPEC, PLAN, TASKS and validation evidence.

These questions do not automatically expand scope. Apply only the relevant items. When product behavior is undefined, mark it PENDING and obtain a decision instead of inventing one.

## 1. Target behavior

For each relevant requirement, decide whether the expected behavior is:

- identical on Android and iOS;
- intentionally different by platform;
- supported on only one platform;
- not yet decided.

Do not confuse shared behavior with shared implementation.

## 2. Lifecycle and interruption

Consider:

- background and foreground transitions;
- Android activity/process recreation;
- iOS scene/application transitions;
- process termination and relaunch;
- cancellation or continuation of in-flight work;
- return from system pickers, camera, share flows or another app.

Describe expected outcomes in the SPEC. Select platform mechanisms later in the PLAN.

## 3. State retention

Distinguish:

- transient UI state;
- navigation state;
- restorable user input;
- persisted domain data.

Specify what must survive navigation, recreation and relaunch. Do not promise identical restoration mechanisms across platforms unless required.

## 4. Connectivity

When relevant, cover:

- first launch without connectivity;
- slow network;
- interruption during a request;
- retries;
- cached/local data;
- stale data;
- recovery after connection returns.

Offline support is not implied by KMP.

## 5. Persistence and migrations

Define:

- what data must persist;
- whether Android and iOS must represent the same logical model;
- compatibility with data saved by previous versions;
- duplicate/conflict rules;
- upgrade behavior;
- whether a migration must be non-destructive.

When data preservation matters, include an explicit upgrade acceptance criterion.

## 6. UI states and recovery

Consider loading, content, empty, error, success and retry states where relevant.

The same domain state may be presented differently on each platform.

## 7. Native navigation and system behavior

Android and iOS should follow their platform expectations unless the SPEC explicitly requires a cross-platform interaction contract.

Consider:

- Android back behavior;
- iOS navigation stack and gestures;
- cancellation;
- unsaved changes;
- deep links;
- external navigation;
- system sheets and pickers.

## 8. Input, keyboard and forms

When applicable, define observable rules for:

- focus;
- validation;
- submission;
- repeated taps;
- keyboard visibility;
- field restoration;
- invalid values.

Implementation remains platform-specific unless a shared rule clearly belongs in COMMON.

## 9. Visual adaptation

Consider:

- supported screen sizes;
- orientation policy;
- safe areas / system bars;
- dynamic text;
- display scaling;
- content that can scroll;
- accessibility sizes.

Do not require pixel-identical Android and iOS interfaces unless explicitly requested.

## 10. Accessibility

Consider:

- labels and semantics;
- screen readers;
- focus order;
- contrast;
- touch targets;
- dynamic text;
- non-color communication;
- gesture alternatives where relevant.

Validate on the target platform whose behavior is claimed.

## 11. Permissions and capabilities

For camera, files, notifications, location, biometrics, microphone or other capabilities, define:

- unavailable capability;
- denied permission;
- revoked permission;
- user cancellation;
- recovery path.

The semantic contract may be common while permission APIs and system flows remain native.

## 12. Performance and resources

Consider work that may affect:

- main-thread responsiveness;
- memory;
- battery;
- network usage;
- large files or lists;
- image processing;
- long-running computation.

A common algorithm still needs target-specific runtime validation when resource behavior matters.

## 13. Background work

Do not assume Android and iOS background execution models are equivalent.

When background execution is required, specify the user-visible requirement first, then design each platform mechanism separately.

## 14. Privacy and security

Consider:

- data stored locally;
- data transmitted;
- sensitive logs;
- screenshots;
- analytics;
- temporary files;
- deletion behavior;
- secrets and service credentials.

Public-repository CI must never be achieved by exposing secrets.

## 15. Localization, time and formats

When relevant, define expected behavior for:

- language;
- long strings;
- locale-specific numbers;
- currency;
- time zones;
- dates;
- units.

Keep domain values separate from platform formatting when semantics must remain common.

## 16. Platform capability matrix

For any feature touching device capabilities, the PLAN must identify the implementation status per target:

| Capability | COMMON semantic contract | Android mechanism | iOS mechanism | Status |
| --- | --- | --- | --- | --- |
| Example | Proposed/No aplica | Verified/Proposed | Verified/Proposed | VERIFIED/PROPOSED/PENDING/APPROVED |

Do not claim parity until both target implementations are validated.

## 17. Deterministic equivalence

When existing product behavior moves into COMMON, define representative inputs and compare the semantically relevant output before removing the old path.

Examples include parsers, classifiers, ranking, calculations, rule engines and transformations.

## 18. Real mobile validation

Select evidence according to the criterion:

- COMMON deterministic logic: common tests where sufficient;
- Android integration: Android test/emulator/device as appropriate;
- iOS integration: compile/unit/simulator/device as appropriate;
- hardware capability: physical device when simulator/emulator cannot prove the behavior;
- persistence migration: upgrade from representative prior data;
- native UX: direct platform review;
- provisioning/distribution: installation on the intended device path.

Record what was actually executed.
