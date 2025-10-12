// Kamil Wolbach (280161)

// Exercise 4 — Greater Number
// Write a program that reads two numbers and prints which one is greater.
// If they are equal, print an appropriate message.
import java.util.Scanner;

public class JAVA_01_04 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the first number: ");
        double num1 = scanner.nextDouble();
        System.out.print("Enter the second number: ");
        double num2 = scanner.nextDouble();

        if (num1 > num2) {
            System.out.println("The first number (" + num1 + ") is greater than the second number (" + num2 + ").");
        }
        else if (num2 > num1) {
            System.out.println("The second number (" + num2 + ") is greater than the first number (" + num1 + ").");
        }
        else {
            System.out.println("Both numbers are equal (" + num1 + ").");
        }
        scanner.close();
    }
}
