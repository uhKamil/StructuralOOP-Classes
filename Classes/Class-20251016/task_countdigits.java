public class task_countdigits {
    public static int countDigits(int number) {
        int count = 0;
        if (number == 0) {
            return 0;
        }
        else {
            while (number > 0) {
                count++;
                number /= 10;
            }
        }
        return count;
    }
    public static void main(String[] args) {
        System.out.println(countDigits(12115));
    }
}
