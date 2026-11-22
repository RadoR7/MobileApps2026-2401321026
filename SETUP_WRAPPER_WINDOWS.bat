@echo off
cd /d "%~dp0"
powershell.exe -NoLogo -NoProfile -ExecutionPolicy Bypass -File "%~dp0SETUP_WRAPPER_WINDOWS.ps1"
if errorlevel 1 (
  echo.
  echo Wrapper setup did not finish. Follow FIRST_RUN_WINDOWS.md for manual steps.
  pause
  exit /b 1
)
echo.
echo Setup completed. In Android Studio select File ^> Open and choose this FitTrack folder.
pause
