@echo off
setlocal

set "port=8080"

for /F "tokens=5" %%P in ('netstat -ano ^| findstr "LISTENING" ^| findstr ":8080 "') do (
    echo Terminating process with PID %%P running on port %port%
    taskkill /F /PID %%P >nul
)

if %errorlevel% equ 0 (
    echo Process terminated successfully.
) else (
    echo No processes found running on port %port%.
)

endlocal

PAUSE