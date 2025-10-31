package use_case.search_with_restriction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import entity.Food;
import entity.Nutrition;
import entity.Recipe;

/**
 * Interactor for recipe search with restriction.
 */
public class RecipeSearchWithRestrictionInteractor implements SearchWithRestrictionInputBoundary {
    private static final String DIET_LABEL = "Diet Label";
    private static final String HEALTH_LABEL = "Health Label";
    private static final String CUISINE_TYPE = "Cuisine Type";
    private static final String DELIMETER = ",";

    private final SearchWithRestrictionDataAccessInterface searchWithRestrictionDataAccessInterface;
    private final SearchWithRestrictionOutputBoundary outputBoundary;

    public RecipeSearchWithRestrictionInteractor(SearchWithRestrictionDataAccessInterface searchWithRestrictionDataAccessInterface,
                                                 SearchWithRestrictionOutputBoundary outputBoundary) {
        this.searchWithRestrictionDataAccessInterface = searchWithRestrictionDataAccessInterface;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void searchRestrictionRecipes(Map<String, List<String>> restrictions) throws SearchWithRestrictionException {
        try {
            final String searchFoodQuery = String.join(DELIMETER, restrictions.get("Food Name"));

            String searchDietQuery = null;
            if (restrictions.get(DIET_LABEL) != null && !restrictions.get(DIET_LABEL).isEmpty()) {
                searchDietQuery = String.join(DELIMETER, restrictions.get(DIET_LABEL));
            }

            String searchHealthQuery = null;
            if (restrictions.get(HEALTH_LABEL) != null && !restrictions.get(HEALTH_LABEL).isEmpty()) {
                searchHealthQuery = String.join(DELIMETER, restrictions.get(HEALTH_LABEL));
            }

            String searchCuisineQuery = null;
            if (restrictions.get(CUISINE_TYPE) != null && !restrictions.get(CUISINE_TYPE).isEmpty()) {
                searchCuisineQuery = String.join(DELIMETER, restrictions.get(CUISINE_TYPE));
            }
            final List<Recipe> searchResults = searchWithRestrictionDataAccessInterface.searchRecipesByRestriction(
                    searchFoodQuery, searchDietQuery, searchHealthQuery, searchCuisineQuery);
            final List<Recipe> recipes = convertToRecipes(searchResults);
            outputBoundary.presentRecipes(recipes);
        }
        catch (Exception exception) {
            outputBoundary.presentError("Failed to search recipes: " + exception.getMessage());
            throw new SearchWithRestrictionException("Recipe search failed", exception);
        }
    }

    private List<Recipe> convertToRecipes(List<Recipe> searchResults) {
        final List<Recipe> recipes = new ArrayList<>();
        int recipeId = 1;

        for (Recipe result : searchResults) {
            final String description = defaultIfNull(result.getDescription(), "No description available");
            final String instructions = defaultIfNull(result.getInstructions(), "Instructions not available");
            final Nutrition nutrition = defaultIfNull(result.getNutrition(), new Nutrition(0, 0, 0, 0, 0, 0));
            final List<Food> food = defaultIfNull(result.getFood(), new ArrayList<>());
            final int servings = Math.max(result.getServings(), 1);

            final Recipe recipe = new Recipe(
                    recipeId++,
                    result.getTitle(),
                    description,
                    result.getIngredients(),
                    instructions,
                    nutrition,
                    food,
                    result.getJsonIngredient(),
                    servings
            );

            recipes.add(recipe);
        }

        return recipes;
    }

    private <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }
}
