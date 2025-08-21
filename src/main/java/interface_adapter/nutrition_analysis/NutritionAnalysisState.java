package interface_adapter.nutrition_analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state for Nutrition Analysis functionality.
 */
public class NutritionAnalysisState {
    private List<String> nutritionResults = new ArrayList<>();
    private String error;

    /**
     * Default constructor for NutritionAnalysisState.
     */
    public NutritionAnalysisState() {

    }

    public List<String> getNutritionResults() {
        return nutritionResults;
    }

    public String getError() {
        return error;
    }

    public void setNutritionResults(List<String> nutritionResults) {
        this.nutritionResults = nutritionResults;
    }

    public void setError(String error) {
        this.error = error;
    }
}
