public class ex4 {
    // Count Digits
    // Write a method countDigits(int number) that returns how many digits the given number has.
    static int countDigits(int n) {
        int digits = 0;
        while (n > 1) {
            n /= 10;
            digits += 1;
        }
        return digits;
    }
    public static void main() {
        System.out.println(countDigits(3125)); // 4
        System.out.println(countDigits(312515)); // 6
    }
}
