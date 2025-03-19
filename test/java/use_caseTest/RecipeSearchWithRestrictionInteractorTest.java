package use_caseTest;

import entity.Nutrition;
import entity.Recipe;
import org.junit.jupiter.api.Test;
import use_case.search_with_restriction.RecipeSearchWithRestrictionInteractor;
import data_access.SearchWithRestrictionDataAccessObject;
import use_case.search_with_restriction.SearchWithRestrictionException;
import use_case.search_with_restriction.SearchWithRestrictionOutputBoundary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RecipeSearchWithRestrictionInteractorTest {

    @Test
    void testSearchWithRestrictionsSuccess() throws SearchWithRestrictionException {
        // Arrange
        InMemorySearchWithRestrictionDataAccess dataAccessObject = new InMemorySearchWithRestrictionDataAccess();
        TestOutputBoundary outputBoundary = new TestOutputBoundary();
        RecipeSearchWithRestrictionInteractor interactor = new RecipeSearchWithRestrictionInteractor(dataAccessObject, outputBoundary);

        Map<String, List<String>> restrictions = new HashMap<>();
        restrictions.put("Food Name", List.of("apple", "banana"));
        restrictions.put("Diet Label", List.of("vegan"));
        restrictions.put("Health Label", List.of("low-sugar"));
        restrictions.put("Cuisine Type", List.of("Asian"));

        // Act
        interactor.searchRestrictionRecipes(restrictions);

        // Assert
        assertEquals(1, outputBoundary.presentedRecipes.size());
        assertEquals("Test Recipe", outputBoundary.presentedRecipes.get(0).getTitle());
    }

    @Test
    void testSearchWithRestrictionsFailure() {
        // Arrange
        FaultySearchWithRestrictionDataAccess dataAccessObject = new FaultySearchWithRestrictionDataAccess();
        TestOutputBoundary outputBoundary = new TestOutputBoundary();
        RecipeSearchWithRestrictionInteractor interactor = new RecipeSearchWithRestrictionInteractor(dataAccessObject, outputBoundary);

        Map<String, List<String>> restrictions = new HashMap<>();
        restrictions.put("Food Name", List.of("apple"));

        // Act & Assert
        SearchWithRestrictionException exception = assertThrows(SearchWithRestrictionException.class,
                () -> interactor.searchRestrictionRecipes(restrictions));
        assertEquals("Recipe search failed", exception.getMessage());
        assertEquals("Failed to search recipes: Search operation failed", outputBoundary.errorMessage);
    }

    // Stub for SearchWithRestrictionDataAccessObject
    private static class InMemorySearchWithRestrictionDataAccess extends SearchWithRestrictionDataAccessObject {
        @Override
        public List<Recipe> searchRecipesByRestriction(String foodQuery, String dietQuery, String healthQuery, String cuisineQuery) {
            List<Recipe> recipes = new ArrayList<>();
            recipes.add(new Recipe(
                    1,
                    "Test Recipe",
                    "Test description",
                    new ArrayList<>(),
                    "Test instructions",
                    new Nutrition(0, 0, 0, 0, 0, 0),
                    new ArrayList<>(),
                    null,
                    2
            ));
            return recipes;
        }
    }

    private static class FaultySearchWithRestrictionDataAccess extends SearchWithRestrictionDataAccessObject {
        @Override
        public List<Recipe> searchRecipesByRestriction(String foodQuery, String dietQuery, String healthQuery, String cuisineQuery) {
            throw new RuntimeException("Search operation failed");
        }
    }

    // Test Output Boundary
    private static class TestOutputBoundary implements SearchWithRestrictionOutputBoundary {
        List<Recipe> presentedRecipes;
        String errorMessage;

        @Override
        public void presentRecipes(List<Recipe> recipes) {
            this.presentedRecipes = recipes;
        }

        @Override
        public void presentError(String message) {
            this.errorMessage = message;
        }
    }
}
