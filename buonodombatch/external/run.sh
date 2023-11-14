JAVA_PATH=/appserv/jboss/awf170/jdk-11.0.4

if [ "X${JAVA_PATH}" == "X" ]; then
   echo "Cannot find Java 8 installation"
   if type -p java; then
        echo found java executable in PATH
        _java=java
   elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
        echo found java executable in JAVA_HOME
        _java="$JAVA_HOME/bin/java"
   else
        echo "no java"
        exit -1
   fi
else
        JAVA_PATH="$( cd ${JAVA_PATH} ; pwd -P )"
        _java="$JAVA_PATH/bin/java"
fi


SCRIPTPATH="$( cd "$(dirname "$0")" ; pwd -P )"
cd $SCRIPTPATH

