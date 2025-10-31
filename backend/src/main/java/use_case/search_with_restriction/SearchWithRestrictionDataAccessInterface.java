package use_case.search_with_restriction;

import entity.Recipe;

import java.util.List;

public interface SearchWithRestrictionDataAccessInterface {
    /**
     * Get searchedRecipe name from a POST request from the API.
     *
     * @param foodName the ingredient name from user's input.
     * @param diet the diet plan user chose.
     * @param health the health plan user chose.
     * @param cuisineType the cuisine type user chose.
     * @return A list of Recipe entity
     */
    List<Recipe> searchRecipesByRestriction(String foodName, String diet, String health, String
            cuisineType);
}
