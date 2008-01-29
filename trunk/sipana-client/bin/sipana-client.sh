#
# This file is part of Sipana project <http://sipana.org/>
# 
# Sipana is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation; either version 3 of the License, or
# (at your option) any later version.
# 
# Sipana is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
# 

# JVM user options
JAVA_USER_OPT=""

# init
SIPANA_BIN=`dirname $0`
. ${SIPANA_BIN}/functions.sh

# path and environment variables
export SIPANA_HOME=`get_sipana_home $SIPANA_BIN`
export SIPANA_MAIN="org.sipana.client.SipanaClient"
export SIPANA_OPTIONS=`get_sipana_options $SIPANA_HOME`
export JAVA_OPTIONS=`get_log4j_options $SIPANA_HOME`
export JAVA_CLASSPATH=`get_classpath $SIPANA_HOME`
export JAVA=`get_java_command`
export LD_LIBRARY_PATH=`get_library_path $SIPANA_HOME`

# startup command line
START_CMD="$JAVA -cp $JAVA_CLASSPATH $JAVA_OPTIONS $JAVA_USER_OPT \
    $SIPANA_OPTIONS $SIPANA_MAIN"

# debug
if [[ "${SIPANA_DEBUG}x" != "x" ]]; then
    echo "====================================================================="
    echo -e "\n\tCLASSPATH = $JAVA_CLASSPATH"
    echo -e "\n\tOPTIONS = $JAVA_OPTIONS"
    echo -e "\n\tUSER OPTIONS = $JAVA_USER_OPT"
    echo -e "\n\tLD_LIBRARY_PATH = $LD_LIBRARY_PATH"
    echo -e "\n\tSTART_CMD = $START_CMD\n"
    echo "====================================================================="
fi

echo -e "\nStarting Sipana Client\n"

# start
$START_CMD

# end
echo -e "\nShutdown complete.\n"

