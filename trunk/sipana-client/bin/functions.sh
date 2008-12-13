#!/bin/bash

if [[ "${SIPANA_HOME}x" == "x" ]]; then
    echo "WARNING: \$SIPANA_HOME isn't set, paths will be wrong configured."
fi

function get_classpath {
    # Search .jar files in "lib" and "jar" directories
    for i in `find {"${SIPANA_HOME}/lib","${SIPANA_HOME}/jar"} -name "*.jar"`; do
        if [[ $CP ]]; then
            CP="${CP}:$i"
        else
            CP="$i"
        fi
    done
   
    # Add "conf" directory to load JNDI properties (jndi.properties)
    CP="${CP}:${SIPANA_HOME}/conf"
    echo $CP
}

function get_log4j_options {
    OPTIONS="-Dlog4j.configuration=file:${SIPANA_HOME}/conf/log4j.properties"
    echo $OPTIONS
}

function get_sipana_options {
    OPTIONS="-Dsipana.client.properties=${SIPANA_HOME}/conf/sipana-client.properties"
    echo $OPTIONS
}

function get_jvm_options {
    OPTIONS="-Dcom.sun.management.config.file=${SIPANA_HOME}/conf/jvm/management.properties"
    echo $OPTIONS
}

function get_java_command {
    if [[ ${JAVA_HOME} ]]; then
        JAVA="${JAVA_HOME}/bin/java"
    else
        JAVA="java"
    fi

    echo $JAVA
}

function get_library_path {
    PATH="/lib:/usr/lib:/usr/local/lib"
    if [[ ${LD_LIBRARY_PATH} ]]; then
        PATH="${PATH}:${LD_LIBRARY_PATH}"
    else
        PATH="${PATH}"
    fi
    echo $PATH
}

