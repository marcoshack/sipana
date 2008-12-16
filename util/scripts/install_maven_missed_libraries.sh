#!/bin/bash

cd `dirname $0`
cd ../lib

mvn install:install-file \
    -DgroupId=gov.nist \
    -DartifactId=jain-sip \
    -Dversion=1.2 \
    -Dpackaging=jar \
    -Dfile=jain-sip-1.2.jar \

