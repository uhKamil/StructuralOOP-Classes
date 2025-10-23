public class ex3 {
    // Even or Odd Checker
    // Write a method isEven(int number) that returns true if the number is even, otherwise false.
    static boolean isEven(int n) {
        return n % 2 == 0;
    }
    public static void main() {
        System.out.println(isEven(50)); // true
        System.out.println(isEven(127)); // false
    }
}
