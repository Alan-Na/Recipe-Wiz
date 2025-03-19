package use_case.nutrition_analysis;

import entity.Recipe;

/**
 * Interface for the Nutrition Analysis use case.
 */
public interface NutritionAnalysisInputBoundary {

    /**
     * Searches for recipes based on a list of ingredients.
     *
     * @param recipe the title of recipe to analyze.
     * @throws NutritionAnalysisException if search fails
     */
    void analyzeNutrition(Recipe recipe) throws NutritionAnalysisException;
}
