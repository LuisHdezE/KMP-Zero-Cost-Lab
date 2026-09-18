import importlib.util
from pathlib import Path
import tempfile
import unittest

SCRIPT = Path(__file__).parents[1] / "check_boundaries.py"
SPEC = importlib.util.spec_from_file_location("check_boundaries", SCRIPT)
MODULE = importlib.util.module_from_spec(SPEC)
assert SPEC and SPEC.loader
SPEC.loader.exec_module(MODULE)


class BoundaryCheckTest(unittest.TestCase):
    def test_clean_common_source_passes(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            (root / "Clean.kt").write_text(
                "package sample\nimport kotlinx.coroutines.flow.Flow\n",
                encoding="utf-8",
            )
            self.assertEqual([], MODULE.find_violations(root))

    def test_platform_import_is_reported(self):
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            (root / "Leak.kt").write_text(
                "package sample\nimport android.content.Context\n",
                encoding="utf-8",
            )
            violations = MODULE.find_violations(root)
            self.assertEqual(1, len(violations))
            self.assertIn("android.content.Context", violations[0])


if __name__ == "__main__":
    unittest.main()
