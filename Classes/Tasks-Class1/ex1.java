public class ex1 {
    // Sum of Numbers
    // Write a method sumUpTo(int n) that returns the sum of all numbers from 1 to n.
    static int sumUpTo(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
        }
        return sum;
    }
    public static void main() {
        System.out.println(sumUpTo(5)); // 15
        System.out.println(sumUpTo(4)); // 10
    }
}
