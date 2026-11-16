@echo off
setlocal
cd /d "%~dp0"
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot"
set "PATH=%JAVA_HOME%\bin;%PATH%"
echo ==============================================
echo   CRM PRESUPUESTOS - PRUEBAS AUTOMATICAS
echo ==============================================
call mvnw.cmd clean test
set "RESULTADO=%ERRORLEVEL%"
echo.
if "%RESULTADO%"=="0" (
    echo PRUEBAS FINALIZADAS CORRECTAMENTE
) else (
    echo ERROR EN PRUEBAS O COMPILACION. Codigo: %RESULTADO%
)
echo Informes: target\surefire-reports
pause
exit /b %RESULTADO%
