package use_case.search_with_restriction;

import java.util.List;
import java.util.Map;

import use_case.recipe_search.RecipeSearchException;

/**
 * Interface for the recipe search with restriction use case.
 */
public interface SearchWithRestrictionInputBoundary {
    /**
     * Searches for recipes based on a map of restrictions.
     * @param restrictions Map of restrictions to search with
     * @throws RecipeSearchException if search fails
     */
    void searchRestrictionRecipes(Map<String, List<String>> restrictions) throws RecipeSearchException;

}
