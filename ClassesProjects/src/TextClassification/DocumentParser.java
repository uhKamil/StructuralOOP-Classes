package TextClassification;

import java.util.HashMap;
import java.util.Map;

public class DocumentParser {
    /**
     * Document classification based on Least Squares Method
     *
     * @param text the document's text
     * @param knowledgeBase a CSV map (language -> (letter, frequency))
     * @return ISO two-letter language codes such as "PL" or "EN"
     */
    public String classifyLanguage(String text, Map<String, Map<Character, Double>> knowledgeBase) {
        Map<Character, Double> documentProfile = analyzeText(text);

        String bestMatch = "Unknown";
        double minDist = Double.MAX_VALUE;

        for (String langCode : knowledgeBase.keySet()) {
            Map<Character, Double> langProfile = knowledgeBase.get(langCode);
            double dist = calculateEuclideanDistance(documentProfile, langProfile);

            if (dist < minDist) {
                minDist = dist;
                bestMatch = langCode;
            }
        }
        return bestMatch;
    }

    /**
     * Converts a String to a frequency map (%).
     */
    private Map<Character, Double> analyzeText(String text) {
        Map<Character, Integer> counts = new HashMap<>();
        long totalLetters = 0;

        String cleanText = text.toLowerCase();

        for (char c : cleanText.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                counts.put(c, counts.getOrDefault(c, 0) + 1);
                totalLetters++;
            }
        }

        Map<Character, Double> frequencies = new HashMap<>();
        if (totalLetters == 0) return frequencies;

        for (char c = 'a'; c <= 'z'; c++) {
            int count = counts.getOrDefault(c, 0);
            double percentage = (count * 100.0) / totalLetters;
            frequencies.put(c, percentage);
        }

        return frequencies;
    }
    
    private double calculateEuclideanDistance(Map<Character, Double> docProfile, Map<Character, Double> langProfile) {
        double sum = 0.0;
        for (char c = 'a'; c <= 'z'; c++) {
            double p1 = docProfile.getOrDefault(c, 0.0);
            double p2 = langProfile.getOrDefault(c, 0.0);
            sum += (p1 - p2) * (p1 - p2);
        }
        return Math.sqrt(sum);
    }
}
