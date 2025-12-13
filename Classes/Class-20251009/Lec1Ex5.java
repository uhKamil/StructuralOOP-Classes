public class Lec1Ex5 {
    public static void main(String[] args) {
        char letter = 'Z';
        int unicode = letter;

        System.out.println("Unicode value of " + letter + " is " + unicode);

        // You can also do
        System.out.println("Unicode value of " + letter + " is " + (int) letter);
    }
}
