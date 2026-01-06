#!/bin/bash
javac -d "../../out/production/Lab5" \
      -cp "../../../out/production/Lab5:./libs/term/jline.jar:./libs" \
      ./src/Ex9/JAVA_05_09.java

java --enable-native-access=ALL-UNNAMED \
     -cp "../../../out/production/Lab5:./libs/term/jline.jar:./libs" \
     ./src/Ex9/JAVA_05_09.java

read -p "Press enter to continue..."

