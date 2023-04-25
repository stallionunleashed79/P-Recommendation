#!/bin/bash

JACOCO1=$(grep '<tr><th>Line coverage:</th><td>' target/site/reportgenerator/index.html)
JACOCO2=${JACOCO1: 31:5}
JACOCO3=${JACOCO2%%\%*}
JACOCO4=${JACOCO3%%.*}

RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'
echo "Code Coverage ${JACOCO3}%"


if [[ ${JACOCO4} -lt 80 ]]; then
echo -e "Code coverage is less than 80%, ${RED}FAIL...${NC}"
echo "Failing the build..."
exit 1
else
echo -e "Code coverage is more than 80%, ${GREEN}PASS...${NC}"
fi