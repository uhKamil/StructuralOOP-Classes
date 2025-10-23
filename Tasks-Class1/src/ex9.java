public class ex9 {
    // Multiplication Table
    // Create a method printMultiplicationTable(int n) that prints the multiplication table for number n (from 1 to 10).

    static char printMultiplicationTable(int n) {
        for (int i = 1; i <= n; i++) {
            System.out.print(i + "\t");
        }
        for (int i = 1; i <= n; i++) {
            System.out.println();
            for (int j = 1; j <= n; j++) {
                System.out.print(i * j + "\t");
            }
        }
        return ' ';
    }

    public static void main() {
        System.out.println(printMultiplicationTable(5) + "\n");
        System.out.println(printMultiplicationTable(10));
    }
}
