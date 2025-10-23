public class ex6 {
    // Prime Number Test
    // Write a method isPrime(int number) that returns true if the number is prime, otherwise false.
    
    static boolean isPrime(int number) {
        if (number == 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main() {
        System.out.println(isPrime(2)); // true
        System.out.println(isPrime(10)); // false
        System.out.println(isPrime(19)); // true
    }
}
