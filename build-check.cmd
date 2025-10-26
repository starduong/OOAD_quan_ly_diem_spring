@echo off
REM ===================================================================
REM Build script for OOAD_quan_ly_diem_spring project
REM This script compiles the project using Maven with JDK 17
REM ===================================================================

echo.
echo ========================================
echo    OOAD Project - Build Script
echo ========================================
echo.

REM Set JDK 17 path
set "JAVA_HOME=C:\jdk-17"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo [INFO] Using JDK 17 from: %JAVA_HOME%
echo.

REM Check Java version
echo [CHECK] Verifying Java version...
java -version
echo.

REM Navigate to project directory
cd /d "%~dp0"
echo [INFO] Project directory: %CD%
echo.

REM Clean previous build
echo [STEP 1/3] Cleaning previous build...
call mvnw.cmd clean
if errorlevel 1 (
    echo [ERROR] Maven clean failed!
    pause
    exit /b 1
)
echo [SUCCESS] Clean completed
echo.

REM Compile project
echo [STEP 2/3] Compiling project with Lombok...
call mvnw.cmd compile -DskipTests
if errorlevel 1 (
    echo [ERROR] Compilation failed!
    echo [HINT] Check the errors above and fix them
    pause
    exit /b 1
)
echo [SUCCESS] Compilation completed
echo.

REM Display summary
echo [STEP 3/3] Generating build summary...
echo.
echo ========================================
echo    BUILD SUMMARY
echo ========================================
echo Status: SUCCESS
echo JDK: 17 (Lombok compatible)
echo Build Tool: Maven Wrapper
echo Output: target/classes/
echo.
echo Files compiled:
dir /s /b target\classes\com\example\myproject\*.class 2>nul | find /c ".class"
echo class files generated
echo.

REM Check specific repository files
echo [VERIFICATION] Checking Repository files...
if exist "target\classes\com\example\myproject\repository\BangDiemRepository.class" (
    echo [OK] BangDiemRepository.class
) else (
    echo [MISSING] BangDiemRepository.class
)

if exist "target\classes\com\example\myproject\repository\CapNhatDiemRepository.class" (
    echo [OK] CapNhatDiemRepository.class
) else (
    echo [MISSING] CapNhatDiemRepository.class
)

if exist "target\classes\com\example\myproject\repository\LopTinChiRepository.class" (
    echo [OK] LopTinChiRepository.class
) else (
    echo [MISSING] LopTinChiRepository.class
)

if exist "target\classes\com\example\myproject\repository\MonHocRepository.class" (
    echo [OK] MonHocRepository.class
) else (
    echo [MISSING] MonHocRepository.class
)

echo.
echo ========================================
echo [SUCCESS] Build completed successfully!
echo ========================================
echo.
echo Next steps:
echo   1. Run application: run-dev.cmd
echo   2. Access form: http://localhost:8080/giang-vien/nhap-diem/LTC001
echo   3. Check logs: target/logs/
echo.

pause
