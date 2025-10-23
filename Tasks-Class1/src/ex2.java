public class ex2 {
    // Factorial Calculator
    // Create a method factorial(int n) that returns the factorial of n using a loop.
    static int factorial(int n) {
        int a = 1;
        for (int i = 2; i <= n; i++) {
            System.out.println(i);
            a *= i;
        }
        return a;
    }
    public static void main() {
        System.out.println(factorial(5)); // 120
    }
}
