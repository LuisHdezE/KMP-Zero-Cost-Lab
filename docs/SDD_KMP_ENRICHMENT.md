# SDD -> KMP Enrichment Record

This document records how the SDD discipline studied from `ArisGuimera/Curso-SDD-Mobile` was evaluated for this KMP laboratory.

**Source baseline reviewed:** `27106fb4d3972d865c031bc2ca7515c33dd94a02`

This is not a fork of that project's architecture. It is a selective process extraction.

## Decision states

- **RETAIN**: useful without changing its intent.
- **ADAPT**: useful principle, changed for Kotlin Multiplatform.
- **ADD**: required by KMP or by evidence from this laboratory.
- **REJECT**: project-specific choice that must not become a general KMP rule.

## Extraction matrix

| Source idea | Decision | KMP treatment |
| --- | --- | --- |
| Understand the repository before changing it | RETAIN | Mandatory investigation before SPEC/PLAN/implementation |
| Distinguish verified facts from proposals and unknowns | ADAPT | Use VERIFIED / PROPOSED / PENDING / APPROVED |
| SPEC before technical design | RETAIN | SPEC remains behavior- and product-focused |
| Stable RF identifiers | RETAIN | RF remains target-neutral when the product rule is shared |
| Stable CA identifiers | ADAPT | CA identifies target: COMMON, ANDROID, IOS, ANDROID-UX or IOS-UX |
| Human approval of SPEC | RETAIN | Filled does not mean approved |
| PLAN only after approved SPEC | RETAIN | PLAN adds source-set and platform-boundary design |
| TASKS only after approved PLAN | RETAIN | KMP task template adds target and evidence fields |
| Explicit implementation authorization | RETAIN | Approval of documents does not authorize code automatically |
| Mobile guidelines | ADAPT | Split observable behavior from Android/iOS mechanisms |
| Lifecycle review | ADAPT | Review Android and iOS lifecycle separately where mechanisms differ |
| Persistence compatibility | ADAPT | Add cross-target logical model and non-destructive upgrade evidence |
| Accessibility and native interaction | ADAPT | Validate separately on Android and iOS |
| Evidence tied to acceptance criteria | ADAPT | Evidence becomes CA -> target -> method -> actual result |
| Screenshots are limited evidence | RETAIN | Explicitly insufficient for persistence/lifecycle/offline/migration |
| Keep SPEC/PLAN/TASKS/code consistent | RETAIN | Documentation drift is treated as a defect in this reference repository |
| Clean architectural boundaries | ADAPT | Platform frameworks must not leak into common domain/application contracts |
| Hilt as DI standard | REJECT | Android-specific project choice |
| Navigation3 as universal navigation | REJECT | Android-specific project choice |
| Android ViewModel as shared state standard | REJECT | Android-specific runtime/lifecycle choice |
| Retrofit as networking standard | REJECT | Project-specific JVM/Android choice |
| Compose as universal UI | REJECT | This lab defaults to Compose on Android and SwiftUI on iOS |
| Single-module Android structure | REJECT | Not a KMP process rule |
| Source project's dependency versions | REJECT | Versions must be verified in the actual target project |

## KMP additions

The following rules are not copied from the source course. They are added because multiplatform work introduces additional failure modes.

### Target matrix

Every material requirement and technical responsibility must identify its relevant target or source-set boundary.

A capability being available on both operating systems does not automatically make it COMMON.

### Shared brain, native face

The default design goal is:

- shared domain/business/data behavior when semantics should be identical;
- native UI and system interaction when platform conventions matter.

No shared-code percentage is a success metric.

### Deterministic equivalence before replacement

When migrating an existing parser, rule engine, classifier, calculation or other deterministic behavior to `commonMain`, establish a representative baseline and compare semantically relevant outputs before deleting the old implementation.

### Platform capability adapters

Camera, OCR, file picking, sharing, notifications, background execution and similar capabilities are modeled semantically only where a shared contract provides value. Implementations remain target-specific unless evidence supports another design.

### Dependency compatibility matrix

A dependency must be verified for every target/source set where the plan intends to use it. Android or JVM success is not proof of iOS/Kotlin-Native support.

### Native UX acceptance

A feature may have one shared business rule but distinct `ANDROID-UX` and `IOS-UX` acceptance criteria.

### Physical-device evidence

Hardware, permissions, provisioning, distribution and other OS-integrated behavior require physical-device evidence when simulator/emulator evidence cannot prove the criterion.

### Zero-cost CI as an explicit constraint

When USD 0 is a requirement, repository visibility, runner type and macOS availability are part of the plan. Public-repository hosted execution is the reference path demonstrated by this laboratory; private products require a separately validated strategy.

See `docs/ZERO_COST_CI.md`.

### Atomic documentation synchronization

The laboratory itself demonstrated that code can advance while README/roadmap/validation documentation remains stale. SDD-KMP therefore treats documentation reconciliation as part of the same increment when a change invalidates existing claims.

## What this enrichment does not decide

This process intentionally does not prescribe:

- a universal DI framework;
- a universal networking library;
- a universal navigation framework;
- a universal settings library;
- a universal persistence library for every product;
- `expect/actual` as the default abstraction;
- shared Compose UI;
- identical Android and iOS UX;
- a private-repository zero-cost iOS solution.

Those decisions belong to each approved PLAN after inspecting the actual product.

## Promotion rule

This repository is the laboratory for the enriched process.

The process should not be promoted into a broader software-development blueprint until it has:

1. been internally reconciled with this KMP lab;
2. been applied incrementally to a real product;
3. preserved that product's proven behavior;
4. produced Android and iOS evidence;
5. exposed and resolved any gaps found under real migration pressure.

Until then, the enrichment remains a validated laboratory process rather than a universal blueprint rule.
