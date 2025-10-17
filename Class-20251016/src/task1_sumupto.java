public class task1_sumupto {
    static int sumUpTo(int N) {
        int sum = 0;
        for (int i = 1; i <= N; i++) {
            sum += i;
        }
        System.out.println("Sum from 1 to " + N + " is: " + sum);
        return sum;
    }
    static void main(String[] args) {
        int ResultSum = sumUpTo(5);
        System.out.println(ResultSum);
    }
}
