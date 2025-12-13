public class ex13 {
    // Find Maximum
    // Write a method findMax(int a, int b, int c) that returns the largest of three integers.
    
    static int findMax(int a, int b, int c) {
        int max = a;
        if (a < b) {
            max = b;
        }
        if (b < c) {
            max = c;
        }
        if (c < a) {
            max = a;
        }
        return max;
    }

    public static void main() {
        System.out.println(findMax(1, 2, 3)); // 3
        System.out.println(findMax(4, 125, 6)); // 125
    }
}
