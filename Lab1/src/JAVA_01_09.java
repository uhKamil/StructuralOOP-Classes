// Kamil Wolbach (280161)

// Exercise 9 — Smallest of Three Numbers
// Write a program that reads three integers and prints the smallest one.

import java.util.Scanner;

public class JAVA_01_09 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the first number: ");
        int num1 = scanner.nextInt();
        System.out.print("Enter the second number: ");
        int num2 = scanner.nextInt();
        System.out.print("Enter the third number: ");
        int num3 = scanner.nextInt();
        
        int smallest = num1;
        if (num2 < smallest) {
            smallest = num2;
        }
        else if (num3 < smallest) {
            smallest = num3;
        }
        
        System.out.println("The smallest number is " + smallest + ".");
    }
}
