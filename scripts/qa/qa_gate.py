#!/usr/bin/env python3
"""QA gate for scoped Kover coverage and mandatory evidence states."""

from __future__ import annotations

import argparse
from pathlib import Path
import xml.etree.ElementTree as ET

PASSING_STATUSES = {"PASS"}


def read_coverage(xml_path: Path) -> dict[str, float]:
    root = ET.parse(xml_path).getroot()
    counters = {counter.attrib["type"]: counter for counter in root.findall("counter")}

    metrics: dict[str, float] = {}
    for kind in ("LINE", "BRANCH"):
        counter = counters.get(kind)
        if counter is None:
            raise ValueError(f"Coverage counter {kind} is missing from {xml_path}")
        missed = int(counter.attrib["missed"])
        covered = int(counter.attrib["covered"])
        total = missed + covered
        if total == 0:
            raise ValueError(f"Coverage counter {kind} has no measurable units")
        metrics[kind] = covered * 100.0 / total
    return metrics


def parse_check(raw: str) -> tuple[str, str, str | None]:
    name, separator, value = raw.partition("=")
    if not separator or not name or not value:
        raise ValueError(f"Invalid --check value: {raw!r}")

    status, reason_separator, reason = value.partition(":")
    return name, status.upper(), reason if reason_separator else None


def evaluate(
    coverage: dict[str, float],
    checks: list[tuple[str, str, str | None]],
    min_line: float,
    min_branch: float,
) -> list[str]:
    errors: list[str] = []

    if coverage["LINE"] < min_line:
        errors.append(
            f"LINE coverage {coverage['LINE']:.2f}% is below required {min_line:.2f}%"
        )
    if coverage["BRANCH"] < min_branch:
        errors.append(
            f"BRANCH coverage {coverage['BRANCH']:.2f}% is below required {min_branch:.2f}%"
        )

    for name, status, reason in checks:
        if status in PASSING_STATUSES:
            continue
        if status == "NOT_APPLICABLE" and reason:
            continue
        errors.append(f"Mandatory check {name} is {status}")

    return errors


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--coverage", type=Path, required=True)
    parser.add_argument("--min-line", type=float, default=90.0)
    parser.add_argument("--min-branch", type=float, default=85.0)
    parser.add_argument("--check", action="append", default=[])
    args = parser.parse_args(argv)

    try:
        coverage = read_coverage(args.coverage)
        checks = [parse_check(raw) for raw in args.check]
        errors = evaluate(coverage, checks, args.min_line, args.min_branch)
    except (OSError, ET.ParseError, ValueError) as exc:
        print(f"QA gate FAIL: {exc}")
        return 1

    print(
        "QA scoped coverage: "
        f"LINE={coverage['LINE']:.2f}% "
        f"BRANCH={coverage['BRANCH']:.2f}%"
    )
    for name, status, reason in checks:
        suffix = f" ({reason})" if reason else ""
        print(f"QA check {name}: {status}{suffix}")

    if errors:
        print("QA gate FAIL:")
        for error in errors:
            print(f"  {error}")
        return 1

    print("QA gate PASS")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
