// Kamil Wolbach (280161)

// Exercise 7 — Factorial
// Write a program that calculates the factorial
// of a number n (n! = 1 * 2 * 3 * ... * n). Use a for or while loop.

import java.util.Scanner;

public class JAVA_01_07 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int number = scanner.nextInt();
        int factorial = 1;
        
        for (int i = 1; i <= number; i++) {
            factorial *= i;
        }
        
        System.out.println(number + "! = " + factorial);
        scanner.close();
    }
}
