public class task4_numberreverse {
    public static String reverseNum (String num) {
        String reversed = "";
        for (int i = 0; i < num.length(); i++) {
            reversed += num.charAt(num.length()-i-1);
        }
        return reversed;
    }
    static void main(String[] args) {
        String number = "456";
        System.out.println(reverseNum(number));
    }
}
