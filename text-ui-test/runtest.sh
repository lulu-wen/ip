#!/usr/bin/env bash

# Wipe the previous build so that stale .class files from an earlier package
# layout can never be picked up instead of the freshly compiled ones.
rm -rf ../bin
mkdir ../bin

# delete output from previous run
if [ -e "./ACTUAL.TXT" ]
then
    rm ACTUAL.TXT
fi

# compile the code into the bin folder, terminates if error occurred
# find is used instead of a glob because the sources live in nested packages
if ! javac -cp ../src/main/java -Xlint:none -d ../bin $(find ../src/main/java -name "*.java")
then
    echo "********** BUILD FAILURE **********"
    exit 1
fi

# run the program, feed commands from input.txt and redirect the output to ACTUAL.TXT
java -classpath ../bin lumi.Lumi < input.txt > ACTUAL.TXT

# convert to UNIX format
cp EXPECTED.TXT EXPECTED-UNIX.TXT
dos2unix ACTUAL.TXT EXPECTED-UNIX.TXT

# compare the output to the expected output
diff ACTUAL.TXT EXPECTED-UNIX.TXT
if [ $? -eq 0 ]
then
    echo "Test result: PASSED"
    exit 0
else
    echo "Test result: FAILED"
    exit 1
fi
