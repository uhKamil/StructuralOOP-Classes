public class ex11 {
    // Count Vowels
    // Create a method countVowels(String text) that returns how many vowels are in the given string.
    
    static int countVowels(String text) {
        int vowelsSum = 0;
        for (char letter : text.toLowerCase().toCharArray()) {
            if (letter == 'a' || letter == 'e' || letter == 'i' || letter == 'o' || letter == 'u') {
                vowelsSum += 1;
            }
        }
        return vowelsSum;
    }

    public static void main() {
        System.out.println(countVowels("Kamil")); // 2
        System.out.println(countVowels("kajaki")); // 3
    }
}