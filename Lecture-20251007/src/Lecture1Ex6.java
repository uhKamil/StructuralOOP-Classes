public class Lecture1Ex6 {
    public static void main(String[] args) {
        boolean isSunny = true;
        boolean isRaining = false;

        boolean goOutside = isSunny && !isRaining;

        System.out.println("Can I go outside? " + goOutside);
    }
}

