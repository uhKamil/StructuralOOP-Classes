public class Lecture1Ex7 {
    public static void main(String[] args) {
        byte b = 127; // max value for byte
        b++;          // overflow!

        System.out.println("Overflowed byte value: " + b); // Output: -128
    }
}

