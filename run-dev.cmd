@echo off
REM =====================================================
REM RUN DEV MODE - Bypass Authentication
REM =====================================================

echo.
echo ======================================
echo   SETTING UP JDK 17
echo ======================================
echo.

REM Set JAVA_HOME to JDK 17
set JAVA_HOME=C:\jdk-17
set PATH=%JAVA_HOME%\bin;%PATH%

echo Java Version:
java -version

echo.
echo ======================================
echo   STARTING DEV MODE
echo ======================================
echo.
echo Authentication is BYPASSED!
echo.
echo After app starts, open browser:
echo   http://localhost:8080/
echo.

REM Run app with dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
