#!/bin/bash

cd ..

mvn clean ; mvn && cp sipana-server/sipana-server-ejb/target/sipana-server-ejb-1.0.jar /home/mhack/app/jboss-4.2.1.GA/server/default/deploy/ ; cp sipana-server/sipana-server-war/target/sipana-server-war-1.0.war /home/mhack/app/jboss-4.2.1.GA/server/default/deploy/ ; cp sipana-commons/target/sipana-commons-1.0.jar /home/mhack/app/jboss-4.2.1.GA/server/default/lib/ ; cp sipana-commons/target/sipana-commons-1.0.jar ~/tmp/sipana/jar/ ; cp sipana-client/target/sipana-client-1.0.jar ~/tmp/sipana/jar/ ; cp sipana-server/sipana-server-ear/src/main/resources/*.xml /home/mhack/app/jboss-4.2.1.GA/server/default/deploy/

