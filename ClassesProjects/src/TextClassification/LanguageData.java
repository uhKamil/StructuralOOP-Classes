package TextClassification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LanguageData {
    public static final Map<String, Map<Character, Double>> profiles = new HashMap<>();

    public void loadCsv(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = null;

            if ((line = br.readLine()) != null) {
                headers = line.split(",");
                for (int i = 1; i < headers.length; i++) {
                    profiles.put(headers[i], new HashMap<>());
                }
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                char letter = values[0].charAt(0);

                for (int i = 1; i < values.length; i++) {
                    assert headers != null;
                    String lang = headers[i];
                    double frequency = Double.parseDouble(values[i]);
                    profiles.get(lang).put(letter, frequency);
                }
            }
        } catch (IOException e) {
            System.err.println("Couldn't read the file. Reason: " + e.getMessage());
            System.out.println("The program tried to look for the file in: " + System.getProperty("user.dir"));
        }
    }

    public Map<String, Map<Character, Double>> getProfiles() {
        return profiles;
    }
}
