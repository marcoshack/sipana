#!/bin/bash

export JBOSS_HOME=/home/mhack/app/jboss-4.2.1.GA

cd ..

mvn clean 
mvn 

# server
cp sipana-server/sipana-server-ejb/target/sipana-server-ejb-1.0.jar $JBOSS_HOME/server/default/deploy/ 
cp sipana-server/sipana-server-war/target/sipana-server-war-1.0.war $JBOSS_HOME/server/default/deploy/ 
cp sipana-commons/target/sipana-commons-1.0.jar $JBOSS_HOME/server/default/lib/ 
cp sipana-server/sipana-server-ear/src/main/resources/*.xml $JBOSS_HOME/server/default/deploy/

# client
cp sipana-commons/target/sipana-commons-1.0.jar /home/marcos/app/sipana-client/jar
cp sipana-client/target/sipana-client-1.0.jar /home/marcos/app/sipana-client/jar
