// Kamil Wolbach (280161)

// Exercise 5 — Temperature Conversion
// Write a program that reads a temperature in Celsius and converts it
// to Fahrenheit using the formula: F = C * 9/5 + 32

import java.util.Scanner;

public class JAVA_01_05 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter temperature in Celsius: ");
        double celsius = scanner.nextDouble();
        double fahrenheit = celsius * 9 / 5 + 32;

        System.out.printf("Temperature in Fahrenheit: %.2f\n", fahrenheit);
        scanner.close();
    }
}
