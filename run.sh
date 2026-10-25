#!/bin/sh
# Needs JDK 11+ and lib/sqlite-jdbc-*.jar
mkdir -p out
javac -d out $(find src -name "*.java") && java -ea -cp "out:lib/*" com.smartcity.ui.Main
