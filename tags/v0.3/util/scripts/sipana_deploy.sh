#!/bin/bash

export JBOSS_HOME=/home/mhack/app/jboss-4.2.1.GA
export JBOSS_SERVER=sipana
export SERVER_DEPLOY="${JBOSS_HOME}/server/${JBOSS_SERVER}/deploy/"
export SERVER_LIB="${JBOSS_HOME}/server/${JBOSS_SERVER}/lib/"
export SIPANA_CLIENT=/home/mhack/app/sipana-client

cd `dirname $0`
cd ../../

case "$1" in 
  server)
    deploy_server();
    ;;
  client)
    deploy_client();
    ;;
  all)
    deploy_server();
    deploy_client();
    ;;
  *)
    echo "\nUsage: $0 {server|client|all}\n"
esac

deploy_server() {
  cp --verbose sipana-server-ejb/target/sipana-server-ejb-1.0.jar $SERVER_DEPLOY 
  cp --verbose sipana-server-war/target/sipana-server-war-1.0.war $SERVER_DEPLOY 
  cp --verbose sipana-commons/target/sipana-commons-1.0.jar $SERVER_LIB 
  cp --verbose sipana-sipscenario/target/sipana-sipscenario-1.0.jar $SERVER_LIB 
  cp --verbose sipana-server-ear/src/main/resources/*.xml $SERVER_DEPLOY
}

deploy_client() {
  cp --verbose sipana-commons/target/sipana-commons-1.0.jar $SIPANA_CLIENT/jar
  cp --verbose sipana-client/target/sipana-client-1.0.jar $SIPANA_CLIENT/jar
  cp --verbose sipana-client/conf/* $SIPANA_CLIENT/conf/
  cp --verbose sipana-client/conf/jvm $SIPANA_CLIENT/conf/jvm/
  cp --verbose sipana-client/bin $SIPANA_CLIENT/bin/
}

