package TextClassification;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class LanguageDetectorSwing extends JFrame {
    private final DocumentParser parser;
    private final Map<String, Map<String, Double>> knowledgeBase;

    private JTextArea inputArea;
    private JLabel resultLabel;

    public LanguageDetectorSwing() {
        LanguageData loader = new LanguageData();
        loader.loadCsv("letterFrequency.csv");
        this.knowledgeBase = loader.getProfiles();
        this.parser = new DocumentParser();

        if (knowledgeBase.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Language base is empty – can't perform the analysis. Check the CSV file.", "Critical error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } else {
            setTitle("Language Detector");
            setSize(600, 450);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout(10, 10));
            initComponents();
        }
    }

    private void initComponents() {
        JPanel topPanel = new JPanel();
        resultLabel = new JLabel("Enter some text or load a file...");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(resultLabel);
        add(topPanel, BorderLayout.NORTH);

        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(inputArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        JButton loadFileButton = new JButton("Load a file (.txt)");
        JButton analyzeButton = new JButton("Analyze");
        analyzeButton.setFont(new Font("Arial", Font.BOLD, 12));
        analyzeButton.setBackground(new Color(70, 130, 180));
        analyzeButton.setForeground(Color.WHITE);

        bottomPanel.add(loadFileButton);
        bottomPanel.add(analyzeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        analyzeButton.addActionListener(_ -> performAnalysis());
        loadFileButton.addActionListener(_ -> chooseFile());
    }

    private void performAnalysis() {
        String text = inputArea.getText();

        if (text.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pole tekstowe jest puste!", "Error", JOptionPane.ERROR_MESSAGE);
            resultLabel.setText("Enter some text or load a file...");
            resultLabel.setForeground(Color.DARK_GRAY);
            return;
        }

        else if (text.trim().length() < 20) {
            JOptionPane.showMessageDialog(this, "Your text is very short. The result might be inaccurate.", "Warning", JOptionPane.WARNING_MESSAGE);
        }

        Map<String, Double> docProfile = parser.analyzeUnigrams(text);
        String detectedLang = parser.classifyLanguage(docProfile, knowledgeBase);

        resultLabel.setText("Detected language: " + detectedLang);
        resultLabel.setForeground(new Color(0, 100, 0));
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Text files (*.txt, *.md)", "txt", "md"));

        fileChooser.setCurrentDirectory(new File("."));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String content = Files.readString(selectedFile.toPath());
                inputArea.setText(content);
                resultLabel.setText("Loaded a file: " + selectedFile.getName());
                resultLabel.setForeground(Color.BLACK);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Couldn't read the file " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static void main() {
        SwingUtilities.invokeLater(() -> new LanguageDetectorSwing().setVisible(true));
    }
}