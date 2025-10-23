public class ex15 {
    // Perfect Number Checker
    // Write a method isPerfectNumber(int number) that checks whether a number is perfect (equal to the sum of its proper divisors, e.g., 6 = 1 + 2 + 3).
    
    static boolean isPerfectNumber(int number) {
        if (number <= 1) return false;
        int sum = 0;
        for (int i = 1; i <= number / 2; i++) {
            if (number % i == 0) {
                sum += i;
            }
        }
        return number == sum;
    }

    public static void main() {
        System.out.println(isPerfectNumber(6)); // true
        System.out.println(isPerfectNumber(27)); // false
    }
}
