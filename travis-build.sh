#!/bin/bash
set -e
./gradlew clean test assemble --info

EXIT_STATUS=0

exit $EXIT_STATUS
