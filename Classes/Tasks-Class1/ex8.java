public class ex8 {
    // Power Calculator
    // Write a method power(int base, int exponent) that calculates base raised to the power of exponent using a loop.
    
    static int power(int base, int exponent) {
        int result = base;
        for (int i = 2; i <= exponent; i++) {
            result *= base;
        }
        return result;
    }

    public static void main() {
        System.out.println(power(2, 4)); // 2^4 = 16
        System.out.println(power(3, 7)); // 3^7 = 2187
    }
}
