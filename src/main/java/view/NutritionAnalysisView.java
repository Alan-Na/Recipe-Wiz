package view;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import interface_adapter.nutrition_analysis.NutritionAnalysisState;
import interface_adapter.nutrition_analysis.NutritionAnalysisViewModel;

/**
 * View for the nutrition analysis use case.
 */
public class NutritionAnalysisView extends JFrame {

    private final NutritionAnalysisViewModel viewModel;
    private final JTextArea resultArea;

    public NutritionAnalysisView(NutritionAnalysisViewModel nutritionAnalysisViewModel) {
        super("Nutrition Analysis result");

        this.viewModel = nutritionAnalysisViewModel;
        this.resultArea = new JTextArea();

        initializeUi();
        observeViewModelState();
    }

    private void initializeUi() {
        // Set size and default close operation for the JFrame
        final int width = 450;
        final int height = 650;
        setSize(width, height);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create and set up the panel with result area
        final JPanel panel = createPanelWithResultArea();
        add(panel);
    }

    private JPanel createPanelWithResultArea() {
        final JPanel panel = new JPanel(new BorderLayout());

        // Configure the result area (JTextArea)
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);

        final JScrollPane scrollPane = new JScrollPane(resultArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void observeViewModelState() {
        // Observe changes to the "state" property of the view model
        viewModel.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName())) {
                updateResultArea((NutritionAnalysisState) evt.getNewValue());
            }
        });
    }

    private void updateResultArea(NutritionAnalysisState state) {
        if (state != null) {
            final String displayedNutritionInfo = classifyNutrients(state.getNutritionResults());
            resultArea.setText(displayedNutritionInfo);
        }
        else {
            resultArea.setText("State is Null");
        }
    }

    /**
     * The method handling the classification of all nutrient of the recipe.
     * @param nutrientInfos a list of String containing all nutrients information of the recipe.
     * @return a String containing all nutrients well classified.
     */
    public static String classifyNutrients(List<String> nutrientInfos) {
        // Categorize nutrients
        final Map<String, List<String>> categorizedNutrients = new HashMap<>();
        categorizedNutrients.put("Macronutrients", new ArrayList<>());
        categorizedNutrients.put("Micronutrients", new ArrayList<>());
        categorizedNutrients.put("Vitamins", new ArrayList<>());
        categorizedNutrients.put("Minerals", new ArrayList<>());
        categorizedNutrients.put("Others", new ArrayList<>());

        for (String info : nutrientInfos) {
            categorizeNutrient(info, categorizedNutrients);
        }

        // Build result string
        return buildResultString(categorizedNutrients).trim();
    }

    private static void categorizeNutrient(String info, Map<String, List<String>> categorizedNutrients) {
        final String lowerInfo = info.trim().toLowerCase();

        if (containsAny(lowerInfo, "carbohydrate", "protein", "fat", "energy", "fiber", "water")) {
            categorizedNutrients.get("Macronutrients").add(info);
        }
        else if (lowerInfo.contains("vitamin")) {
            if (containsAny(lowerInfo, "a", "b", "c", "d", "e", "k")) {
                categorizedNutrients.get("Vitamins").add(info);
            }
            else {
                categorizedNutrients.get("Micronutrients").add(info);
            }
        }
        else if (containsAny(lowerInfo, "calcium", "iron", "magnesium", "phosphorus", "potassium", "sodium", "zinc")) {
            categorizedNutrients.get("Minerals").add(info);
        }
        else {
            categorizedNutrients.get("Others").add(info);
        }
    }

    private static boolean containsAny(String input, String... keywords) {
        boolean contains = false;
        for (String keyword : keywords) {
            if (input.contains(keyword)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    private static String buildResultString(Map<String, List<String>> categorizedNutrients) {
        final StringBuilder result = new StringBuilder();

        for (Map.Entry<String, List<String>> entry : categorizedNutrients.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                result.append(entry.getKey()).append(":\n");
                for (String info : entry.getValue()) {
                    result.append("    - ").append(info).append("\n");
                }
            }
        }

        return result.toString();
    }
}
