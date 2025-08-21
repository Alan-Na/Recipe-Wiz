package interface_adapter.search_with_restriction;

import java.util.List;
import java.util.Map;

import use_case.search_with_restriction.SearchWithRestrictionInputBoundary;

/**
 * Controller for the recipe search with restriction functionality.
 */
public class RestrictionController {
    private final SearchWithRestrictionInputBoundary searchWithRestrictionInputBoundaryUseCase;

    public RestrictionController(SearchWithRestrictionInputBoundary searchWithRestrictionInputBoundary) {
        this.searchWithRestrictionInputBoundaryUseCase = searchWithRestrictionInputBoundary;
    }

    /**
     * Execute a recipe search with the given restrictions.
     * @param ingredients Map of ingredients to search for
     */
    public void executeRestrictionSearch(Map<String, List<String>> ingredients) {
        try {
            searchWithRestrictionInputBoundaryUseCase.searchRestrictionRecipes(ingredients);
        }
        catch (Exception exception) {
            // Error will be handled by the presenter through output boundary
        }
    }
}
