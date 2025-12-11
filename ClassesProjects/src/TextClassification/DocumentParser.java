package TextClassification;

import java.util.HashMap;
import java.util.Map;

public class DocumentParser {
    private final Map<String, Double> languageDist = new HashMap<>();

    /**
     * Document classification based on Least Squares Method
     *
     * @param docProfile a CSV map (letter/bigram -> frequency)
     * @param knowledgeBase a CSV map (language -> (letter, frequency))
     * @return ISO two-letter language codes such as "PL" or "EN"
     */
    public String classifyLanguage(Map<String, Double> docProfile, Map<String, Map<String, Double>> knowledgeBase) {
        languageDist.clear();
        String bestMatch = "Unknown";
        double minDist = Double.MAX_VALUE;

        for (String langCode : knowledgeBase.keySet()) {
            Map<String, Double> langProfile = knowledgeBase.get(langCode);
            double dist = calculateEuclideanDistance(docProfile, langProfile);
            languageDist.put(langCode, dist);

            if (dist < minDist) {
                minDist = dist;
                bestMatch = langCode;
            }
        }
        return bestMatch;
    }

    public Map<String, Double> getLanguageDist() {
        return new HashMap<>(languageDist);
    }

    // Algorithm 1: Unigrams
    public Map<String, Double> analyzeUnigrams(String text) {
        Map<String, Integer> counts = new HashMap<>();
        long totalLetters = 0;
        String cleanText = text.toLowerCase();

        for (char c : cleanText.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                String key = String.valueOf(c);
                counts.put(key, counts.getOrDefault(key, 0) + 1);
                totalLetters++;
            }
        }
        return normalize(counts, totalLetters);
    }

    // Algorithm 2: Bigrams
    public Map<String, Double> analyzeBigrams(String text) {
        Map<String, Integer> counts = new HashMap<>();
        long totalBigrams = 0;
        String cleanText = text.toLowerCase().replaceAll("[^a-z]", "");

        for (int i = 0; i < cleanText.length() - 1; i++) {
            String bigram = cleanText.substring(i, i + 2);
            counts.put(bigram, counts.getOrDefault(bigram, 0) + 1);
            totalBigrams++;
        }
        return normalize(counts, totalBigrams);
    }

    private Map<String, Double> normalize(Map<String, Integer> counts, long total) {
        Map<String, Double> frequencies = new HashMap<>();
        if (total == 0) return frequencies;

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / total;
            frequencies.put(entry.getKey(), percentage);
        }
        return frequencies;
    }

    public double calculateEuclideanDistance(Map<String, Double> docProfile, Map<String, Double> langProfile) {
        double sum = 0.0;
        for (String feature : docProfile.keySet()) {
            double p1 = docProfile.get(feature);
            double p2 = langProfile.getOrDefault(feature, 0.0);
            sum += (p1 - p2) * (p1 - p2);
        }
        return Math.sqrt(sum);
    }
}
