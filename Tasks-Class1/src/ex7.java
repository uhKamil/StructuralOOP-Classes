public class ex7 {
    // Sum of Even Numbers
    // Write a method sumEvenNumbers(int n) that returns the sum of all even numbers from 1 to n.
    static int sumEvenNumbers(int n) {
        int sum = 0;
        for (int i = 2; i <= n; i++) {
            if (i % 2 == 0) {
                sum += i;
            }
        }
        return sum;
    }
    public static void main() {
        System.out.println(sumEvenNumbers(10)); // 2+4+6+8+10 = 30
        System.out.println(sumEvenNumbers(6)); // 2+4+6 = 12
    }
}
