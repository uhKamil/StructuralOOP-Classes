package TextClassification;

import java.util.Map;
import java.util.Scanner;

public class LanguageDetectorTerminal {
    static void main() {
        LanguageData loader = new LanguageData();
        loader.loadCsv("letterFrequency.csv");
        Map<String, Map<String, Double>> knowledgeBase = loader.getProfiles();

        System.out.println("The program can detect languages: " + knowledgeBase.keySet());

        DocumentParser parser = new DocumentParser();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Give me some text to analyze (or write 'exit' or 'quit' to leave): ---");
            String input = scanner.nextLine();

            while (input.isEmpty()) input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input) || "quit".equalsIgnoreCase(input)) break;

            if (input.length() < 20) {
                System.out.println("[CAUTION] Your text is very short. The result might be inaccurate.");
            }

            Map<String, Double> docProfile = parser.analyzeUnigrams(input);
            String detectedLang = parser.classifyLanguage(docProfile, knowledgeBase);
            System.out.println(">>> Detected language: " + detectedLang);
        }
        scanner.close();
    }
}
