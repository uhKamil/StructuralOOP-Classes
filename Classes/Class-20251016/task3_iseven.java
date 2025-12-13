public class task3_iseven {
    public static boolean isEven(int number) {
        return number % 2 == 0;
    }
    static void main(String[] args) {
        boolean num = isEven(12);
        System.out.println(num);
    }
}
