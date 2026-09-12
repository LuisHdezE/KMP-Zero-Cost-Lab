#!/usr/bin/env bash
set -u

printf '=== KMP Zero-Cost Lab doctor ===\n'
printf 'OS: '; uname -a || true
printf '\nJava:\n'; java -version 2>&1 || true
printf '\nGit:\n'; git --version || true
printf '\nAndroid SDK: %s\n' "${ANDROID_HOME:-${ANDROID_SDK_ROOT:-NOT_SET}}"
printf '\nGradle wrapper: '
if [[ -x ./gradlew ]]; then echo YES; else echo NO; fi
