#!/bin/bash

JAVA_OPT='-Djava.security.egd=file:///dev/urandom'

is_stop () {
    limit=0
    while [ "$limit" -lt 15 ]
    do
        limit=$(expr $limit + 1)
        kill -0 $1 2> /dev/null
        if [ $? -ne 0 ] ;then
          return 1
        fi
        sleep 1
    done
    return 0
}


echo "> Auto deploy starting...."

CURRENT_PID=`cat $1.pid`

echo "> OLD APP PID :: $CURRENT_PID"

if [ -z $CURRENT_PID ]; then
    echo "> Nothing running app ! !"

else
    echo "> $CURRENT_PID will be stopped"
    kill -15 $CURRENT_PID 2> /dev/null

    is_stop $CURRENT_PID
    if [ $? -eq 0 ]; then
        echo "> Force stop ! !"
        kill -9 $PID
    fi
fi

echo "> Do deploy :: $1"

nohup java $JAVA_OPT -jar $2/$1-0.0.1-SNAPSHOT.jar >> running.log &
NEW_PID=$!
echo "> NEW APP PID :: $NEW_PID"

is_stop $NEW_PID
if [ $? -eq 1 ];then
  echo "> Process abnormally stopped.."
  cat running.log
  exit 1
fi

echo "> Deploy Complete ! !"
