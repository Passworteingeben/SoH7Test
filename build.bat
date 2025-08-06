@echo off
REM Clean bin and jars
IF EXIST bin (
    rmdir /s /q bin
)

IF EXIST jars (
    rmdir /s /q jars
)
mkdir bin
mkdir jars

REM Compile client and server
javac -d bin\Client src\Client\*.java
javac -d bin\Server src\Server\*.java

REM Package client JAR
jar cfe jars\Client.jar Client -C bin\Client .

REM Package server JAR
jar cfe jars\Server.jar Server -C bin\Server .

echo Build complete. JARs are in the jars\ directory.
pause
