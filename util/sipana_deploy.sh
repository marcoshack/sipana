#!/bin/bash

export JBOSS_HOME=/home/mhack/app/jboss-4.2.1.GA
export SIPANA_CLIENT=/home/mhack/app/sipana-client

CMD_CP='cp --verbose'

cd `dirname $0`
cd ..

mvn clean 
mvn 

# server
$CMD_CP sipana-server-ejb/target/sipana-server-ejb-1.0.jar $JBOSS_HOME/server/default/deploy/ 
$CMD_CP sipana-server-war/target/sipana-server-war-1.0.war $JBOSS_HOME/server/default/deploy/ 
$CMD_CP sipana-server-ear/src/main/resources/*.xml $JBOSS_HOME/server/default/deploy/
$CMD_CP sipana-commons/target/sipana-commons-1.0.jar $JBOSS_HOME/server/default/lib/ 

# client
$CMD_CP sipana-commons/target/sipana-commons-1.0.jar $SIPANA_CLIENT/jar
$CMD_CP sipana-client/target/sipana-client-1.0.jar $SIPANA_CLIENT/jar

