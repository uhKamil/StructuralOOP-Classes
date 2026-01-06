#!/bin/bash
java --enable-native-access=ALL-UNNAMED \
     -cp "../../../out/production/Lab6:./libs/term/jline.jar:./libs" \
     ./src/Ex3/JAVA_06_03.java

read -p "Press enter to continue..."

