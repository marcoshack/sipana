#!/bin/bash

function get_classpath {
    SIPANA_HOME=$1
    CP=""
    for i in `find ${SIPANA_HOME} -name '*.jar'`; do
        if [[ "${CP}x" == "x" ]]; then
            CP=$i
        else
            CP=${CP}:$i
        fi
    done
   
    # Add conf/ folder to configure JNDI (jndi.properties)
    CP="${CP}:${SIPANA_HOME}/conf"
    echo $CP
}

function get_sipana_home {
    SIPANA_BIN=$1 
    cd $SIPANA_BIN
    cd ..
    pwd
}

function get_log4j_options {
    SIPANA_HOME=$1
    OPTIONS="-Dlog4j.configuration=file:${SIPANA_HOME}/conf/log4j.properties"
    echo $OPTIONS
}

function get_sipana_options {
    SIPANA_HOME=$1
    OPTIONS="-Dsipana.client.properties=$SIPANA_HOME/conf/sipana-client.properties"
    echo $OPTIONS
}

function get_jvm_options {
    SIPANA_HOME=$1
    OPTIONS="-Dcom.sun.management.config.file=$SIPANA_HOME/conf/jvm/management.properties"
    echo $OPTIONS
}

function get_java_command {
    if [[ "${JAVA_HOME}x" != "x" ]]; then
        JAVA="${JAVA_HOME}/bin/java"
    else
        JAVA="java"
    fi

    echo $JAVA
}

function get_library_path {
    SIPANA_HOME=$1
    SIPANA_NATIVE_LIB="$SIPANA_HOME/lib/native-linux"
    if [[ "${LD_LIBRARY_PATH}x" != "x" ]]; then
        PATH="${LD_LIBRARY_PATH}:$SIPANA_NATIVE_LIB"
    else
        PATH=$SIPANA_NATIVE_LIB
    fi
    echo $PATH
}

