public class ex10 {
    // Palindrome Number Check
    // Write a method isPalindrome(int number) that returns true if the number reads the same backward and forward.
    
    static boolean isPalindrome(int number) {
        int reversedNumber = ex5.reverseNumber(number);
        return (reversedNumber == number);
    }
    
    public static void main() {
        System.out.println(isPalindrome(500)); // false
        System.out.println(isPalindrome(5445)); // true
    }
}
