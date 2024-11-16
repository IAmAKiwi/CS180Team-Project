@echo off
echo Running Checkstyle...

:: Set paths
set CHECKSTYLE_JAR="%USERPROFILE%\checkstyle\checkstyle.jar"
set CONFIG_FILE="cs180_checks.xml"
set SOURCE_DIR="src\main\java"

:: Run Checkstyle
java -jar %CHECKSTYLE_JAR% -c %CONFIG_FILE% %SOURCE_DIR%\*.java

echo Done!
pause