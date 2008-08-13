#!/bin/bash

export JBOSS_HOME=/home/mhack/app/jboss-4.2.1.GA
export SIPANA_CLIENT=/home/mhack/app/sipana-client

cd `dirname $0`
cd ../../

mvn clean 
mvn 

# server
cp --verbose sipana-server-ejb/target/sipana-server-ejb-1.0.jar $JBOSS_HOME/server/default/deploy/ 
cp --verbose sipana-server-war/target/sipana-server-war-1.0.war $JBOSS_HOME/server/default/deploy/ 
cp --verbose sipana-commons/target/sipana-commons-1.0.jar $JBOSS_HOME/server/default/lib/ 
cp --verbose sipana-sipscenario/target/sipana-sipscenario-1.0.jar $JBOSS_HOME/server/default/lib/ 
cp --verbose sipana-server-ear/src/main/resources/*.xml $JBOSS_HOME/server/default/deploy/

# client
cp --verbose sipana-commons/target/sipana-commons-1.0.jar $SIPANA_CLIENT/jar
cp --verbose sipana-client/target/sipana-client-1.0.jar $SIPANA_CLIENT/jar
cp --verbose sipana-client/conf/* $SIPANA_CLIENT/conf/
cp --verbose sipana-client/conf/jvm $SIPANA_CLIENT/conf/jvm/
cp --verbose sipana-client/bin $SIPANA_CLIENT/bin/

