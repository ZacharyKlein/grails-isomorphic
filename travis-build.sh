#!/bin/bash
set -e
./gradlew clean check assemble

EXIT_STATUS=0

exit $EXIT_STATUS
