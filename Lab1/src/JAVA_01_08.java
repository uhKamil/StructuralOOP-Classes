// Kamil Wolbach (280161)

// Exercise 8 — Even Numbers
// Write a program that reads a number n and prints all even numbers from 1 to n.

import java.util.Scanner;

public class JAVA_01_08 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int number = scanner.nextInt();
        
        for (int i = 1; i <= number; i++) {
            if (i % 2 == 0) {
                System.out.println(i);
            }
        }
    }    
}
