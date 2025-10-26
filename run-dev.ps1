# =====================================================
# RUN DEV MODE - Bypass Authentication
# =====================================================

Write-Host "`n🔧 Setting up JDK 17...`n" -ForegroundColor Cyan

# Set JAVA_HOME to JDK 17
$env:JAVA_HOME="C:\jdk-17"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"

# Verify Java version
Write-Host "Java Version:" -ForegroundColor Yellow
java -version

Write-Host "`n📦 Compiling project...`n" -ForegroundColor Cyan
mvn clean compile -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n✅ Compile SUCCESS!`n" -ForegroundColor Green
    Write-Host "🚀 Starting application in DEV MODE...`n" -ForegroundColor Green
    Write-Host "⚠️  Authentication is BYPASSED!`n" -ForegroundColor Yellow
    Write-Host "🌐 After app starts, open browser:" -ForegroundColor Cyan
    Write-Host "   http://localhost:8080/`n" -ForegroundColor Green
    
    # Run app
    mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
} else {
    Write-Host "`n❌ Compile FAILED!`n" -ForegroundColor Red
    Write-Host "Please check the error messages above." -ForegroundColor Yellow
}
