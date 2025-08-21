package use_case.nutrition_analysis;

import java.io.IOException;
import java.util.List;

import entity.Nutrient;
import entity.Recipe;

/**
 * Implementation of the Nutrition Analysis use case.
 */
public class NutritionAnalysisInteractor implements NutritionAnalysisInputBoundary {

    private final NutritionAnalysisDataAccessInterface nutritionAnalysisDAO;
    private final NutritionAnalysisOutputBoundary outputBoundary;

    public NutritionAnalysisInteractor(NutritionAnalysisDataAccessInterface NutritionAnalysisDAO,
                                       NutritionAnalysisOutputBoundary outputBoundary) {
        this.nutritionAnalysisDAO = NutritionAnalysisDAO;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void analyzeNutrition(Recipe recipe) throws NutritionAnalysisException {
        try {
            // Get NutritionInfo from Edamam API by the RecipeName.
            final List<Nutrient> nutritionInfo = nutritionAnalysisDAO.analyzeNutrition(recipe);

            // Present success
            outputBoundary.presentNutritionInfo(nutritionInfo);
        }
        catch (IOException exception) {
            // Present error
            outputBoundary.presentError("Failed to analyze the recipe: " + exception.getMessage());
            throw new NutritionAnalysisException("Analysis failed", exception);
        }
    }
}
