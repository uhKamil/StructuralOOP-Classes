public class lec2ex1 {
    public static void main(String[] args) {
        // The for loop
        for (int i = 1; i <= 10; i++) {
            System.out.println("Number: " + i);
        }

        // Way 1 for the while loop
        int i = 1;
        while (i <= 5) {
            System.out.println("This will print as long as the condition is true.");
            i++;
        }

        // Way 2 for the while loop
        int j = 1;
        do {
            System.out.println("This will print at least once.");
            j++;
        }
        while (j <= 5);
    }
}
