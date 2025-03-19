package interface_adapter.nutrition_analysis;

import entity.Recipe;
import use_case.nutrition_analysis.NutritionAnalysisInputBoundary;

/**
 * Controller for the Nutrition Analysis functionality.
 */
public class NutritionAnalysisController {
    private final NutritionAnalysisInputBoundary nutritionAnalysisInputBoundaryUseCase;

    public NutritionAnalysisController(NutritionAnalysisInputBoundary nutritionAnalysisInputBoundaryUsecase) {
        this.nutritionAnalysisInputBoundaryUseCase = nutritionAnalysisInputBoundaryUsecase;
    }

    /**
     * Execute a nutrition analysis with the given recipe name.
     * @param recipe the recipe the user wants to analyze.
     */
    public void executeAnalysis(Recipe recipe) {
        nutritionAnalysisInputBoundaryUseCase.analyzeNutrition(recipe);
    }
}

