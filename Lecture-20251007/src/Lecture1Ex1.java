public class Lecture1Ex1 {
    public static void main(String[] args) {
        byte b = 100;                // 1 byte, range: -128 to 127
        short s = 10000;             // 2 bytes
        int i = 100000;              // 4 bytes
        long l = 10000000000L;       // 8 bytes (notice the 'L' at the end)

        float f = 5.75f;             // 4 bytes (requires 'f' suffix)
        double d = 19.99;            // 8 bytes

        char c = 'A';                // 2 bytes (Unicode character)
        boolean isJavaFun = true;    // 1 bit (true or false)

        // Print values
        System.out.println("byte: " + b);
        System.out.println("short: " + s);
        System.out.println("int: " + i);
        System.out.println("long: " + l);
        System.out.println("float: " + f);
        System.out.println("double: " + d);
        System.out.println("char: " + c);
        System.out.println("boolean: " + isJavaFun);
    }
}
