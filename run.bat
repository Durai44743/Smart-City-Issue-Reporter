@echo off
if not exist out mkdir out
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt && java -ea -cp "out;lib/*" com.smartcity.ui.Main
