// Kamil Wolbach (280161)

// Exercise 10 — Simple Calculator (Menu)
// Write a program that:
// 1. Reads two numbers.
// 2. Displays a menu:
// 1 - addition 2 - subtraction 3 - multiplication 4 - division
// 3. Reads the user’s choice and performs the corresponding operation using a switch statement.

import java.util.Scanner;

public class JAVA_01_10 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        float a = 0, b = 0;
        System.out.print("Enter the first number: ");
        a = scanner.nextFloat();
        System.out.print("Enter the second number: ");
        b = scanner.nextFloat();
        
        System.out.println("To perform calculations, please enter a number: \n" +
                "1 - Addition \n2 - Subtraction \n3 - Multiplication \n" +
                "4 - Division");
        
        int choice = 0;
        while (choice < 1 || choice > 4) {
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
        }
        
        switch (choice) {
            case 1:
                System.out.println("Addition result: " + (a + b));
                break;
            case 2:
                System.out.println("Subtraction result: " + (a - b));
                break;
            case 3:
                System.out.println("Multiplication result: " + (a * b));
                break;
            case 4:
                System.out.println("Division result: " + (a / b));
                break;
        }
    }
}
