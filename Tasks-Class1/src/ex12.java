public class ex12 {
    // Fibonacci Sequence
    // Write a method printFibonacci(int n) that prints the first n numbers in the Fibonacci sequence.
    
    static int printFibonacci(int n) {
        int result = 0; int f0 = 0; int f1 = 1;
        if (n == 0) {
            return f0;
        }
        else if (n == 1) {
            return f1;
        }
        for (int i = 2; i <= n; i++) {
            result = f0 + f1;
            f0 = f1; f1 = result;
        }
        return result;
    }

    public static void main() {
        System.out.println(printFibonacci(3)); // 2
        System.out.println(printFibonacci(5)); // 5
        System.out.println(printFibonacci(8)); // 21
    }
}
