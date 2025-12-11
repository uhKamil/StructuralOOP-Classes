package TextClassification;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LanguageData {
    public static final Map<String, Map<String, Double>> profiles = new HashMap<>();

    public void loadCsv(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = null;

            if ((line = br.readLine()) != null) {
                headers = line.split(",");
                for (int i = 1; i < headers.length; i++) {
                    profiles.putIfAbsent(headers[i].trim(), new HashMap<>());
                }
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String feature = values[0].trim();

                for (int i = 1; i < values.length; i++) {
                    if (headers != null && i < headers.length) {
                        String lang = headers[i].trim();
                        try {
                            double frequency = Double.parseDouble(values[i]);
                            profiles.get(lang).put(feature, frequency);
                        } catch (NumberFormatException _) {
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Couldn't read the file. Reason: " + e.getMessage());
            System.out.println("The program tried to look for the file in: " + System.getProperty("user.dir"));
        }
    }

    public Map<String, Map<String, Double>> getProfiles() {
        return profiles;
    }
}
