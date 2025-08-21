package use_case.nutrition_analysis;

import java.util.List;

import entity.Nutrient;

/**
 * Output boundary for the Nutrition Analysis use case.
 */
public interface NutritionAnalysisOutputBoundary {
    /**
     * Present the Nutrition information to the user.
     * @param NutritionInfo List of recipes to present
     */
    void presentNutritionInfo(List<Nutrient> NutritionInfo);

    /**
     * Present an error message to the user.
     * @param error Error message to present
     */
    void presentError(String error);
}
