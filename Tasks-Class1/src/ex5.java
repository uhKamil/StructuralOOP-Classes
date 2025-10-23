public class ex5 {
    // Reverse a Number
    // Create a method reverseNumber(int number) that returns the number with digits reversed (e.g., 123 → 321).
    
    static int reverseNumber(int number) {
        int reversedNum = 0;
        while (number > 0) {
            int digit = number % 10;
            reversedNum += digit;
            number /= 10;
            if (number >= 1) {
                reversedNum *= 10;
            }
        }
        return reversedNum;
    }
    
    public static void main() {
        System.out.println(reverseNumber(1256));
        System.out.println(reverseNumber(5000));
    }
}
