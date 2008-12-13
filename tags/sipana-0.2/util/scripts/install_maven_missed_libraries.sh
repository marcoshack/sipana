#!/bin/bash

cd `dirname $0`
cd ../lib

mvn install:install-file \
    -DgroupId=org.jboss \
    -DartifactId=jbossall-client \
    -Dversion=4.2.1.GA \
    -Dpackaging=jar \
    -Dfile=jbossall-client-4.2.1.GA.jar

mvn install:install-file \
    -DgroupId=org.jboss \
    -DartifactId=jboss-annotations-ejb3 \
    -Dversion=4.2.1.GA \
    -Dpackaging=jar \
    -Dfile=jboss-annotations-ejb3.jar

mvn install:install-file \
    -DgroupId=net.sourceforge \
    -DartifactId=jpcap-core \
    -Dversion=0.01.16 \
    -Dpackaging=jar \
    -Dfile=jpcap-core.jar \

mvn install:install-file \
    -DgroupId=gov.nist \
    -DartifactId=jain-sip \
    -Dversion=1.2 \
    -Dpackaging=jar \
    -Dfile=jain-sip-1.2.jar \

