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

# Check if current user has root privileges
if [[ $UID != 0 ]]; then
  echo -e "\nSipana Agent needs root privileges to run.\n";
  exit 1;
fi

# Startup script debug output
SIPANA_DEBUG=1

# Set SIPANA_HOME
SIPANA_BIN=`dirname $0`
cd ${SIPANA_BIN}/../
export SIPANA_HOME=`pwd`

# Load auxiliar functions
. ${SIPANA_BIN}/functions.sh

# Path and environment variables
export SIPANA_MAIN="org.sipana.agent.SipanaAgentLaucher"
export SIPANA_OPTIONS=`get_sipana_options`
export JAVA_OPTIONS="`get_log4j_options` `get_jvm_options`"
export JAVA_CLASSPATH=`get_classpath`
export JAVA=`get_java_command`
export LD_LIBRARY_PATH=`get_library_path`

# startup command line
START_CMD="$JAVA -cp $JAVA_CLASSPATH $JAVA_OPTIONS $SIPANA_OPTIONS $SIPANA_MAIN"

# debug
if [[ ${SIPANA_DEBUG} ]]; then
    echo "====================================================================="
    echo -e "\n\tCLASSPATH = $JAVA_CLASSPATH"
    echo -e "\n\tOPTIONS = $JAVA_OPTIONS"
    echo -e "\n\tLD_LIBRARY_PATH = $LD_LIBRARY_PATH"
    echo -e "\n\tSTART_CMD = $START_CMD\n"
    echo "====================================================================="
fi

echo -e "\nStarting Sipana Agent\n"

# start
cd $SIPANA_HOME
$START_CMD

# end
echo -e "\nShutdown complete.\n"
