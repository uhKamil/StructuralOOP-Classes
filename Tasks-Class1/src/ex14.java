public class ex14 {
    // Sum of Digits
    // Create a method sumOfDigits(int number) that returns the sum of all digits in the number.
    
    static int sumOfDigits(int number) {
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }
    
    public static void main() {
        System.out.println(sumOfDigits(12115)); // 10
    }
}
