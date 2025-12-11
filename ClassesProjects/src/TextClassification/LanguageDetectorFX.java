package TextClassification;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;
import java.util.Map;

public class LanguageDetectorFX extends Application {

    private final DocumentParser parser;
    private final Map<String, Map<String, Double>> unigramKnowledgeBase;
    private final Map<String, Map<String, Double>> bigramKnowledgeBase;
    private final Set<String> validBigrams;

    private TextArea inputArea;
    private Label resultLabel;
    private Stage primaryStage;
    private BarChart<String, Number> barChart;
    private ListView<String> statsList;
    private ComboBox<String> algorithmBox;

    public final String enterMsg = "Enter text to classify...";

    public LanguageDetectorFX() {

        this.parser = new DocumentParser();

        LanguageData unigramLoader = new LanguageData();
        unigramLoader.loadCsv("./ClassesProjects/src/TextClassification/letterFrequency.csv");
        this.unigramKnowledgeBase = unigramLoader.getProfiles();

        LanguageData bigramLoader = new LanguageData();
        bigramLoader.loadCsv("./ClassesProjects/src/TextClassification/bigramsFrequency.csv");
        this.bigramKnowledgeBase = bigramLoader.getProfiles();

        if (!bigramKnowledgeBase.isEmpty()) {
            validBigrams = bigramKnowledgeBase.values().iterator().next().keySet();
        } else {
            validBigrams = new java.util.HashSet<>();
        }
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        if (unigramKnowledgeBase.isEmpty() || bigramKnowledgeBase.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Critical Error");
            alert.setHeaderText("Database Empty");
            alert.setContentText("All or one of language bases is empty. Check the CSV file paths.");
            alert.showAndWait();
            Platform.exit();
            System.exit(1);
            return;
        }

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        resultLabel = new Label(enterMsg);
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        BorderPane.setAlignment(resultLabel, Pos.CENTER);
        BorderPane.setMargin(resultLabel, new Insets(0, 0, 10, 0));
        root.setTop(resultLabel);

        VBox topPanel = new VBox(5);
        topPanel.setAlignment(Pos.CENTER);
        algorithmBox = new ComboBox<>();
        algorithmBox.getItems().addAll("Unigrams (1-letter)", "Bigrams (2-letters)");
        algorithmBox.getSelectionModel().selectFirst();

        topPanel.getChildren().addAll(resultLabel, algorithmBox);
        root.setTop(topPanel);

        SplitPane splitPane = new SplitPane();
        inputArea = new TextArea();
        inputArea.setWrapText(true);
        inputArea.setFont(Font.font("Monospaced", 14));
        VBox leftBox = new VBox(5, new Label("Input Text:"), inputArea);
        VBox.setVgrow(inputArea, Priority.ALWAYS);
        leftBox.setPadding(new Insets(10));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Letter");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Frequency (%)");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Language Fingerprint Comparison");
        barChart.setAnimated(false);
        barChart.setLegendVisible(true);
        barChart.setPrefHeight(300);

        statsList = new ListView<>();
        statsList.setPrefHeight(150);
        statsList.setStyle("-fx-font-family: 'Monospaced';");

        VBox rightBox = new VBox(10,
                barChart,
                new Label("Distance statistics:"),
                statsList
        );
        rightBox.setPadding(new Insets(10));
        VBox.setVgrow(barChart, Priority.ALWAYS);

        splitPane.getItems().addAll(leftBox, rightBox);
        splitPane.setDividerPositions(0.4);
        root.setCenter(splitPane);

        Button loadFileButton = new Button("Load File");
        Button analyzeButton = new Button("Analyze");
        analyzeButton.setStyle("-fx-background-color: #2E8B57; -fx-text-fill: white; -fx-font-weight: bold;");

        Button helpButton = new Button("?");
        helpButton.setStyle(
                "-fx-background-radius: 50%; " +
                        "-fx-min-width: 30px; " +
                        "-fx-min-height: 30px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #e0e0e0;"
        );
        helpButton.setTooltip(new Tooltip("Show help"));

        HBox centerButtons = new HBox(15, loadFileButton, analyzeButton);
        centerButtons.setAlignment(Pos.CENTER);
        centerButtons.setPadding(new Insets(10));
        centerButtons.setPickOnBounds(false); // so the mouse isn't blocked

        StackPane bottomPanel = new StackPane();
        bottomPanel.setPadding(new Insets(10));
        bottomPanel.getChildren().addAll(helpButton, centerButtons);
        StackPane.setAlignment(helpButton, Pos.CENTER_LEFT);
        StackPane.setAlignment(centerButtons, Pos.CENTER);
        root.setBottom(bottomPanel);

        analyzeButton.setOnAction(_ -> performAnalysis());
        loadFileButton.setOnAction(_ -> chooseFile());
        helpButton.setOnAction(_ -> showHelp());

        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Language Detector");
        stage.setScene(scene);
        stage.show();
    }

    private void performAnalysis() {
        String text = inputArea.getText();

        String selectedAlgo = algorithmBox.getValue();
        String detectedLang;
        Map<String, Double> profile;
        Map<String, Map<String, Double>> currentKnowledgeBase;

        if (selectedAlgo.contains("Bigrams")) {
            profile = parser.analyzeBigrams(text, validBigrams);
            currentKnowledgeBase = bigramKnowledgeBase;
        } else {
            profile = parser.analyzeUnigrams(text);
            currentKnowledgeBase = unigramKnowledgeBase;
        }

        detectedLang = parser.classifyLanguage(profile, currentKnowledgeBase);

        resultLabel.setText("Detected Language: " + detectedLang);
        resultLabel.setTextFill(Color.web("#006400"));

        updateChart(profile, detectedLang, currentKnowledgeBase);
        updateStatsList();
    }

    private void updateChart(Map<String, Double> textProfile, String lang, Map<String, Map<String, Double>> kb) {
        barChart.getData().clear();
        barChart.getXAxis().setLabel(algorithmBox.getValue().contains("Bigram") ? "Bigrams" : "Letters");

        XYChart.Series<String, Number> seriesInput = new XYChart.Series<>();
        seriesInput.setName("Input");

        XYChart.Series<String, Number> seriesRef = new XYChart.Series<>();
        seriesRef.setName(lang);

        barChart.getData().add(seriesRef);
        barChart.getData().add(seriesInput);

        textProfile.keySet().stream().sorted().limit(26).forEach(key -> {
            XYChart.Data<String, Number> inputData = new XYChart.Data<>(key, textProfile.get(key));
            XYChart.Data<String, Number> refData = new XYChart.Data<>(key, kb.get(lang).getOrDefault(key, 0.0));
            seriesInput.getData().add(inputData);
            seriesRef.getData().add(refData);
        });

        hoverOverData(seriesRef);
        hoverOverData(seriesInput);
    }

    /**
     * Makes it possible to view percentages in series when mouse is hovered over data
     */
    private static void hoverOverData(XYChart.Series<String, Number> seriesInput) {
        for (XYChart.Data<String, Number> data : seriesInput.getData()) {
            Tooltip t = new Tooltip(data.getYValue() + "%");
            Tooltip.install(data.getNode(), t);
            data.getNode().setOnMouseEntered(_ -> data.getNode().setStyle("-fx-bar-fill: cyan;"));
            data.getNode().setOnMouseExited(_ -> data.getNode().setStyle(""));
        }
    }

    private void updateStatsList() {
        statsList.getItems().clear();
        Map<String, Double> distances = parser.getLanguageDist();

        // Sort by distance
        distances.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> {
                    String lang = entry.getKey();
                    Double dist = entry.getValue();

                    String line = String.format("%-5s : %.4f", lang, dist);
                    if (dist.equals(distances.values().stream().min(Double::compare).orElse(0.0))) {
                        line += "  <-- CLOSEST MATCH";
                    }

                    statsList.getItems().add(line);
                });
    }

    private void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.md"));
        fileChooser.setInitialDirectory(new File("."));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile != null) {
            try {
                inputArea.setText(Files.readString(selectedFile.toPath()));
                resultLabel.setText("Loaded: " + selectedFile.getName());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Cannot read file.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("How to use Language Detector");

        String content = """
        Welcome to Language Detector. This tool uses statistical fingerprinting to classify text based on letter frequency.
        
        HOW TO USE:
        1. Choose the Algorithm:
           - Unigrams (1-letter): Analyzes frequency of single characters (A-Z). Efficient for longer documents.
           - Bigrams (2-letters): Analyzes pairs of adjacent letters. Superior accuracy for short texts and similar languages (e.g., Norwegian vs. Danish).
        
        2. Input Data:
        Type or paste text directly into the main area or use 'Load File' to import .txt / .md files.
        
        3. Interpret Results:
           - The Chart visualizes the match: compare your Input bars with the Reference bars.
           - The Statistics List shows the Euclidean distance (error rate computed using Least Squares Method). The closer to 0, the more reliable the match.
        """;

        alert.setContentText(content);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}