@ECHO OFF

REM wipe the previous build so stale .class files cannot be picked up
if exist ..\bin rmdir /s /q ..\bin
mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM compile the code into the bin folder, terminates if error occurred
REM sources are listed to a file because they live in nested packages
dir /s /B ..\src\main\java\*.java > sources.txt
javac -cp ..\src\main\java -Xlint:none -d ..\bin @sources.txt
IF ERRORLEVEL 1 (
    del sources.txt
    echo ********** BUILD FAILURE **********
    exit /b 1
)
del sources.txt

REM run the program, feed commands from input.txt and redirect the output to ACTUAL.TXT
java -classpath ..\bin lumi.Lumi < input.txt > ACTUAL.TXT

REM compare the output to the expected output
FC ACTUAL.TXT EXPECTED.TXT
