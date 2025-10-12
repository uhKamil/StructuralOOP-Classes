// Kamil Wolbach (280161)

// Exercise 2 — Simple Calculations
// Write a program that:
// 1. Reads two integers from the user.
// 2. Calculates and prints their sum, difference, product, and quotient.
// Example output: Enter the first number: 8 Enter the second number: 2 Sum: 10 Difference: 6 Product: 16

import java.util.Scanner;

public class JAVA_01_02 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the first number: ");
        int a = scanner.nextInt();
        System.out.print("Enter the second number: ");
        int b = scanner.nextInt();

        int sum = a + b; int diff = a - b; int prod = a * b; int quot = a / b;

        System.out.println("Sum: " + sum);
        System.out.println("Difference: " + diff);
        System.out.println("Product: " + prod);
        System.out.println("Quotient: " + quot); // notice the fractional part is truncated

        scanner.close();
    }
}
