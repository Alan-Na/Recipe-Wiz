package app;

import data_access.RecipeSearchDataAccessObject;
import data_access.SearchWithRestrictionDataAccessObject;

/**
 * An application where users can search for recipes based on ingredients.
 * <p>
 * This application allows users to input ingredients and search for matching recipes
 * using the Edamam Recipe Search API. Users can view recipe details including
 * ingredients and instructions.
 * </p>
 */
public class MainRecipeApplication {
    /**
     * Main method for the Recipe Application.
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        // Create the data access object for recipe search
        final RecipeSearchDataAccessObject recipeSearchDataAccessObject = new RecipeSearchDataAccessObject();
        final SearchWithRestrictionDataAccessObject searchWithRestrictionDataAccessObject = new SearchWithRestrictionDataAccessObject();

        // Create and configure the application using the builder
        final RecipeAppBuilder builder = new RecipeAppBuilder();

        // Build the application with correct order
        builder.addRecipeSearchAPI(recipeSearchDataAccessObject, searchWithRestrictionDataAccessObject)
                .addMealPlanningView()
                .addMealPlanningUseCase()
                .addNutritionAnalysisView()
                .addRecipeSearchView()
                .addRecipeSearchUseCase()
                .addRestrictionSearchUseCase()
                .addNutritionAnalysisUseCase()
                .addServingAdjustUseCase()
                .addFrontpageView();

        // Build and show the frame
        builder.build();
    }
}
