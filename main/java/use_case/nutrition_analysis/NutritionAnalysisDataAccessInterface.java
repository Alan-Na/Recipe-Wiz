package use_case.nutrition_analysis;

import entity.Nutrient;
import entity.Recipe;

import java.io.IOException;
import java.util.List;

/**
 * Interface for accessing nutritional analysis data from an external API.
 * <p>
 * Provides a method to analyze the nutritional content of a given recipe
 * and return a list of Nutrient.
 * </p>
 */
public interface NutritionAnalysisDataAccessInterface {
    /**
     * Get totalNutrients from a POST request from the API.
     *
     * @param recipe the recipe we want to analyze the nutrition of.
     * @return A list of Nutrient entity
     * @throws IOException If an I/O error occurs.
     */
    List<Nutrient> analyzeNutrition(Recipe recipe) throws IOException;
    // Helper methods are private and are not in the interface according to
    // the encapsulation principle and interface Segregation (from SOLID).
}
