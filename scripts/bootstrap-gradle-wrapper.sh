#!/usr/bin/env bash
set -euo pipefail

GRADLE_VERSION="9.1.0"
GRADLE_SHA256="a17ddd85a26b6a7f5ddb71ff8b05fc5104c0202c6e64782429790c933686c806"
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
TOOLS="$ROOT/.tools"
ZIP="$TOOLS/gradle-${GRADLE_VERSION}-bin.zip"
DIST="$TOOLS/gradle-${GRADLE_VERSION}"

mkdir -p "$TOOLS"

if [[ ! -x "$DIST/bin/gradle" ]]; then
  echo "Downloading Gradle ${GRADLE_VERSION}..."
  curl -fL "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -o "$ZIP"
  echo "${GRADLE_SHA256}  ${ZIP}" | sha256sum -c -
  unzip -q -o "$ZIP" -d "$TOOLS"
fi

cd "$ROOT"
"$DIST/bin/gradle" wrapper --gradle-version "$GRADLE_VERSION" --distribution-type bin
./gradlew --version

echo "Gradle wrapper ready. Commit gradlew, gradlew.bat and gradle/wrapper/."
