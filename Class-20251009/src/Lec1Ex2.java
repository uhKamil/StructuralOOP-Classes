public class Lec1Ex2 {
    public static void main(String[] args) {
        int a = 10; int b = 3;

        System.out.println("Addition: " + (a + b));
        System.out.println("Subtraction: " + (a - b));
        System.out.println("Multiplication: " + (a * b));
        System.out.println("Division (int): " + (a / b));               // !! notice w/out "float" the result is int
        System.out.println("Division (float): " + ((float) a / b));     // only this way you get the result in float
        System.out.println("Remainder: " + (a % b));

    }
}
