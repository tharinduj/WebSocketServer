#!/bin/bash

#title = MOMServer

CLASSPATH=:.
CLASSPATH=$CLASSPATH:./lib/jWebSocketCommon-1.0.jar
CLASSPATH=$CLASSPATH:./lib/jWebSocketServer-1.0.jar
CLASSPATH=$CLASSPATH:./lib/jWebSocketServerAPI-1.0.jar
CLASSPATH=$CLASSPATH:./lib/aopalliance-1.0.jar
CLASSPATH=$CLASSPATH:./lib/commons-logging-1.1.1.jar
CLASSPATH=$CLASSPATH:./lib/gson-2.2.4.jar
CLASSPATH=$CLASSPATH:./lib/spring-aop-3.0.5.RELEASE.jar
CLASSPATH=$CLASSPATH:./lib/spring-asm-3.0.5.RELEASE.jar
CLASSPATH=$CLASSPATH:./lib/spring-beans-3.0.5.RELEASE.jar
CLASSPATH=$CLASSPATH:./lib/spring-context-3.0.5.RELEASE.jar
CLASSPATH=$CLASSPATH:./lib/spring-core-3.0.5.RELEASE.jar
CLASSPATH=$CLASSPATH:./lib/spring-expression-3.0.5.RELEASE.jar
CLASSPATH=$CLASSPATH:./server-1.0-SNAPSHOT.jar
CLASSPATH=.:$CLASSPATH

java -Xmx256m -cp $CLASSPATH  temp.FileSender

echo ON