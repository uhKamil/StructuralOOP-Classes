// Kamil Wolbach (280161)

// Exercise 6 — Sum from 1 to n
// Write a program that:
// - Reads a number n.
// - Calculates and prints the sum of all numbers from 1 to n.
// (For example, if n = 5, the result is 15.)

import java.util.Scanner;

public class JAVA_01_06 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter a number: ");
        int number = scanner.nextInt();
        int sum = 0;
        
        for (int i = 1; i <= number; i++) {
            sum += i;
        }
        
        System.out.println("Sum of all numbers from 1 to " + number + " is " + sum);
        scanner.close();
    }
}
