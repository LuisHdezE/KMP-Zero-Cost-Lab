import importlib.util
from pathlib import Path
import tempfile
import unittest

SCRIPT = Path(__file__).parents[1] / "qa_gate.py"
SPEC = importlib.util.spec_from_file_location("qa_gate", SCRIPT)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC and SPEC.loader
SPEC.loader.exec_module(MODULE)


def write_report(path: Path, line_covered: int, line_missed: int, branch_covered: int, branch_missed: int):
    path.write_text(
        f"""<?xml version="1.0"?>
<report name="fixture">
  <counter type="LINE" missed="{line_missed}" covered="{line_covered}"/>
  <counter type="BRANCH" missed="{branch_missed}" covered="{branch_covered}"/>
</report>
""",
        encoding="utf-8",
    )


class QaGateTest(unittest.TestCase):
    def test_gate_passes_above_threshold_with_required_checks(self):
        with tempfile.TemporaryDirectory() as temp:
            report = Path(temp) / "report.xml"
            write_report(report, 95, 5, 90, 10)
            code = MODULE.main([
                "--coverage", str(report),
                "--check", "common-tests=PASS",
                "--check", "manual-device=NOT_APPLICABLE:not-required-for-core-increment",
            ])
            self.assertEqual(0, code)

    def test_gate_fails_when_line_coverage_is_low(self):
        with tempfile.TemporaryDirectory() as temp:
            report = Path(temp) / "report.xml"
            write_report(report, 89, 11, 90, 10)
            code = MODULE.main(["--coverage", str(report), "--check", "tests=PASS"])
            self.assertEqual(1, code)

    def test_gate_fails_when_branch_coverage_is_low(self):
        with tempfile.TemporaryDirectory() as temp:
            report = Path(temp) / "report.xml"
            write_report(report, 95, 5, 84, 16)
            code = MODULE.main(["--coverage", str(report), "--check", "tests=PASS"])
            self.assertEqual(1, code)

    def test_gate_fails_when_mandatory_check_failed(self):
        with tempfile.TemporaryDirectory() as temp:
            report = Path(temp) / "report.xml"
            write_report(report, 95, 5, 90, 10)
            code = MODULE.main(["--coverage", str(report), "--check", "room=FAIL"])
            self.assertEqual(1, code)

    def test_gate_fails_when_mandatory_check_was_not_run(self):
        with tempfile.TemporaryDirectory() as temp:
            report = Path(temp) / "report.xml"
            write_report(report, 95, 5, 90, 10)
            code = MODULE.main(["--coverage", str(report), "--check", "ios=NOT_RUN"])
            self.assertEqual(1, code)


if __name__ == "__main__":
    unittest.main()
