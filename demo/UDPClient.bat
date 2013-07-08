title = MOMServer

echo OFF

set CLASSPATH=.;
set CLASSPATH=%CLASSPATH%;.\lib\jWebSocketCommon-1.0.jar
set CLASSPATH=%CLASSPATH%;.\lib\jWebSocketServer-1.0.jar
set CLASSPATH=%CLASSPATH%;.\lib\jWebSocketServerAPI-1.0.jar
set CLASSPATH=%CLASSPATH%;.\lib\aopalliance-1.0.jar
set CLASSPATH=%CLASSPATH%;.\lib\commons-logging-1.1.1.jar
set CLASSPATH=%CLASSPATH%;.\lib\gson-2.2.4.jar
set CLASSPATH=%CLASSPATH%;.\lib\spring-aop-3.0.5.RELEASE.jar
set CLASSPATH=%CLASSPATH%;.\lib\spring-asm-3.0.5.RELEASE.jar
set CLASSPATH=%CLASSPATH%;.\lib\spring-beans-3.0.5.RELEASE.jar
set CLASSPATH=%CLASSPATH%;.\lib\spring-context-3.0.5.RELEASE.jar
set CLASSPATH=%CLASSPATH%;.\lib\spring-core-3.0.5.RELEASE.jar
set CLASSPATH=%CLASSPATH%;.\lib\spring-expression-3.0.5.RELEASE.jar
set CLASSPATH=%CLASSPATH%;.\server-1.0-SNAPSHOT.jar

java -Xmx256m -classpath %CLASSPATH%  temp.FileSender

echo ON

exit

