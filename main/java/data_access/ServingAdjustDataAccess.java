package data_access;

import entity.Recipe;
import use_case.serving_adjust.ServingAdjustDataAccessInterface;
import use_case.serving_adjust.ServingAdjustException;
import java.util.List;

/**
 * Implementation of ServingAdjustDataAccessInterface for data persistence.
 */
public class ServingAdjustDataAccess implements ServingAdjustDataAccessInterface {
    @Override
    public void saveUpdatedRecipes(List<Recipe> recipes) throws ServingAdjustException {
        try {
            // Implement your data saving logic here
            // For demonstration, we'll just print the recipes
            for (Recipe recipe : recipes) {
                System.out.println("Saving updated recipe: " + recipe.getTitle());
            }
        } catch (Exception e) {
            throw new ServingAdjustException("Failed to save updated recipes.", e);
        }
    }
}
