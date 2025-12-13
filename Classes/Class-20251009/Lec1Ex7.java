public class Lec1Ex7 {
    public static void main(String[] args) {
        byte b = 127; // max value for byte
        b++;          // overflow!!!!!!!!

        System.out.println("Overfloated byte value: " + b);  // notice we start over, because the result is -128
    }
}
