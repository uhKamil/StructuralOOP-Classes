// Kamil Wolbach (280161)

// Exercise 3 — Even or Odd
// Write a program that reads an integer and checks if it is even or odd using the % operator.

import java.util.Scanner;

public class JAVA_01_03 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number and I'll tell you if it's even or odd: ");
        int number = scanner.nextInt();
        if (number % 2 == 0) {
            System.out.println("The number is even.");
        }
        else {
            System.out.println("The number is odd.");
        }
        scanner.close();
    }
}
