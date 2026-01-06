#!/bin/bash
java --enable-native-access=ALL-UNNAMED \
     -cp "../../../out/production/Lab7:./libs/term/jline.jar:./libs" \
     ./src/Ex1/JAVA_07_01.java

read -p "Press enter to continue..."