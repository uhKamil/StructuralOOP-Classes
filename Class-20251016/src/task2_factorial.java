public class task2_factorial {
    public static int factorial(int n) {
        int factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
            System.out.println("Factorial of " + i + " = " + factorial);
        }
        return factorial;
    }
    static void main(String[] args) {int fact = factorial(6); }
}
