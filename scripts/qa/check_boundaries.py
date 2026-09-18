#!/usr/bin/env python3
"""Fail when platform/UI framework imports leak into shared commonMain."""

from __future__ import annotations

import argparse
from pathlib import Path

PROHIBITED_PREFIXES = (
    "android.",
    "java.",
    "javax.",
    "platform.",
    "kotlin.jvm.",
    "androidx.activity.",
    "androidx.compose.",
)


def find_violations(root: Path) -> list[str]:
    violations: list[str] = []
    for path in sorted(root.rglob("*.kt")):
        for line_number, raw_line in enumerate(path.read_text(encoding="utf-8").splitlines(), 1):
            line = raw_line.strip()
            if not line.startswith("import "):
                continue
            imported = line.removeprefix("import ").strip()
            if imported.startswith(PROHIBITED_PREFIXES):
                violations.append(f"{path}:{line_number}: {line}")
    return violations


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--root",
        type=Path,
        default=Path("shared/src/commonMain/kotlin"),
        help="commonMain Kotlin source root",
    )
    args = parser.parse_args()

    violations = find_violations(args.root)
    if violations:
        print("COMMON boundary violations:")
        for violation in violations:
            print(f"  {violation}")
        return 1

    print(f"COMMON boundary check PASS: {args.root}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
