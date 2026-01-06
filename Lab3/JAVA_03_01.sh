#!/bin/zsh
javac -d "../../out/production/Lab3" -cp "../../../out/production/Lab3" ./src/JAVA_03_01.java
java --enable-native-access=ALL-UNNAMED -cp "../../../out/production/Lab3" ./src/JAVA_03_01.java
echo "Press any key to continue..."
read
