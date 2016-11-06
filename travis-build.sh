#!/bin/bash
set -e
./gradlew clean test assemble

EXIT_STATUS=0

exit $EXIT_STATUS
