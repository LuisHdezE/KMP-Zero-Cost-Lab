# Zero-Cost CI Policy

This document defines the repository's reference strategy for keeping routine KMP validation at USD 0 when that constraint is required.

## Goal

Support reproducible Android and iOS builds without paid infrastructure, while preserving security and making the cost assumption explicit.

## Reference path

The laboratory's reference path is:

- public GitHub repository;
- standard GitHub-hosted Linux runner for routine common/Android work;
- standard GitHub-hosted macOS runner for iOS/Xcode work;
- no larger paid runners;
- no paid signing or build service required for the validated laboratory path.

This is a constraint of the workflow, not a guarantee that every future GitHub plan or policy will remain unchanged. Re-verify provider policy when cost behavior matters to a new project.

## Why repository visibility matters

iOS compilation requires macOS/Xcode. A Windows/WSL self-hosted runner can cover common and Android work but cannot replace Xcode.

Therefore a project that relies on GitHub-hosted macOS while requiring USD 0 must verify that its repository visibility and account plan make those standard hosted minutes free for the intended usage.

Do not assume that a private product repository has the same cost profile as this public laboratory.

## Public repository rule

When a project may become public to use the zero-cost hosted path:

1. review current and historical repository content for secrets and sensitive material;
2. review configuration files, service credentials and generated artifacts;
3. rotate any credential that may have been exposed historically;
4. confirm that source publication is acceptable for the product;
5. only then change visibility.

A CI saving is never justification for exposing credentials or proprietary material.

## Private repository rule

If the product must remain private, the PLAN must identify a validated iOS strategy before the USD 0 requirement can be marked satisfied.

Possible strategies may be investigated, but none is approved by default. The plan must document actual constraints, provider policy and evidence for the selected path.

## Runner policy

- Prefer standard hosted runners when using GitHub-hosted execution.
- Larger paid runners are not part of the zero-cost reference architecture.
- Self-hosted runners can be used for common/Android workloads when useful, but availability, security and maintenance become project responsibilities.
- Do not run untrusted pull-request code on a privileged self-hosted runner.

## Workflow frequency

macOS is the expensive/scarce execution environment in many KMP workflows even when direct billing is zero.

Design CI so that:

- fast common checks run early;
- Android checks run without unnecessarily invoking macOS;
- iOS runs when the change affects shared/iOS behavior or when release confidence requires it;
- release/provisioning checks are separate when useful.

Avoid executing macOS merely as ceremony.

## Required PLAN fields

A feature or migration with a zero-cost constraint must record:

- repository visibility;
- current provider policy verification date/source when material;
- Linux/Android runner strategy;
- macOS/iOS runner strategy;
- whether physical iOS installation is part of acceptance;
- whether repository publication is permitted;
- any remaining cost or quota risk.

## Evidence

A zero-cost claim should be supported by the actual workflow configuration and, when material, current provider billing/usage policy. Historical success in this laboratory does not by itself prove future provider pricing.
