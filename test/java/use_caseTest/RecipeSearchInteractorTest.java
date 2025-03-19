package use_caseTest;

import data_access.RecipeSearchDataAccessObject;
import data_access.SavedRecipesDataAccessObject;
import entity.Nutrition;
import entity.Recipe;
import org.junit.jupiter.api.Test;
import use_case.recipe_search.RecipeSearchInteractor;
import use_case.recipe_search.RecipeSearchOutputBoundary;
import use_case.recipe_search.RecipeSearchException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeSearchInteractorTest {

    @Test
    void testSaveRecipeSuccess() throws RecipeSearchException {
        // Arrange
        InMemorySavedRecipesDataAccess savedRecipesDataAccess = new InMemorySavedRecipesDataAccess();
        TestRecipeSearchOutputBoundary outputBoundary = new TestRecipeSearchOutputBoundary();
        RecipeSearchInteractor interactor = new RecipeSearchInteractor(null, savedRecipesDataAccess, outputBoundary);

        Recipe recipe = createTestRecipe();

        // Act
        interactor.saveRecipe(1, recipe);

        // Assert
        assertTrue(savedRecipesDataAccess.isRecipeSaved(1, recipe));
        assertEquals(recipe, outputBoundary.savedRecipe);
    }

    @Test
    void testSaveRecipeFailure() {
        // Arrange
        FaultySavedRecipesDataAccess savedRecipesDataAccess = new FaultySavedRecipesDataAccess();
        TestRecipeSearchOutputBoundary outputBoundary = new TestRecipeSearchOutputBoundary();
        RecipeSearchInteractor interactor = new RecipeSearchInteractor(null, savedRecipesDataAccess, outputBoundary);

        Recipe recipe = createTestRecipe();

        // Act & Assert
        RecipeSearchException exception = assertThrows(RecipeSearchException.class, () -> interactor.saveRecipe(1, recipe));
        assertEquals("Failed to save recipe", exception.getMessage());
        assertEquals("Failed to save recipe: Save operation failed", outputBoundary.errorMessage);
    }

    @Test
    void testSearchRecipesSuccess() throws RecipeSearchException {
        // Arrange
        InMemoryRecipeSearchDataAccessObject searchDataAccessObject = new InMemoryRecipeSearchDataAccessObject();
        TestRecipeSearchOutputBoundary outputBoundary = new TestRecipeSearchOutputBoundary();
        RecipeSearchInteractor interactor = new RecipeSearchInteractor(searchDataAccessObject, null, outputBoundary);

        List<String> ingredients = List.of("ingredient1", "ingredient2");

        // Act
        interactor.searchRecipes(ingredients);

        // Assert
        assertEquals(1, outputBoundary.presentedRecipes.size());
        assertEquals("Test Recipe", outputBoundary.presentedRecipes.get(0).getTitle());
    }

    @Test
    void testSearchRecipesFailure() {
        // Arrange
        FaultyRecipeSearchDataAccessObject searchDataAccessObject = new FaultyRecipeSearchDataAccessObject();
        TestRecipeSearchOutputBoundary outputBoundary = new TestRecipeSearchOutputBoundary();
        RecipeSearchInteractor interactor = new RecipeSearchInteractor(searchDataAccessObject, null, outputBoundary);

        // Act & Assert
        RecipeSearchException exception = assertThrows(RecipeSearchException.class, () -> interactor.searchRecipes(List.of("ingredient1")));
        assertEquals("Recipe search failed", exception.getMessage());
        assertEquals("Failed to search recipes: Search operation failed", outputBoundary.errorMessage);
    }

    // Helper to create a test recipe
    private Recipe createTestRecipe() {
        return new Recipe(
                1,
                "Test Recipe",
                "Description",
                new ArrayList<>(),
                "Instructions",
                new Nutrition(100, 10, 10, 10, 10, 10),
                new ArrayList<>(),
                null,
                4
        );
    }

    // Stub for SavedRecipesDataAccessInterface
    private static class InMemorySavedRecipesDataAccess extends SavedRecipesDataAccessObject {
        private final List<Recipe> savedRecipes = new ArrayList<>();

        @Override
        public void saveRecipe(int userId, Recipe recipe) {
            savedRecipes.add(recipe);
        }

        public boolean isRecipeSaved(int userId, Recipe recipe) {
            return savedRecipes.contains(recipe);
        }
    }

    private static class FaultySavedRecipesDataAccess extends SavedRecipesDataAccessObject {
        @Override
        public void saveRecipe(int userId, Recipe recipe) {
            throw new RuntimeException("Save operation failed");
        }
    }

    // Stub for RecipeSearchDataAccessObject
    private static class InMemoryRecipeSearchDataAccessObject extends RecipeSearchDataAccessObject {
        @Override
        public List<Recipe> searchRecipesByFoodName(String searchQuery) {
            List<Recipe> recipes = new ArrayList<>();
            recipes.add(new Recipe(1, "Test Recipe", "Test description", new ArrayList<>(), "Test instructions", new Nutrition(0, 0, 0, 0, 0, 0), new ArrayList<>(), null, 2));
            return recipes;
        }
    }

    private static class FaultyRecipeSearchDataAccessObject extends RecipeSearchDataAccessObject {
        @Override
        public List<Recipe> searchRecipesByFoodName(String searchQuery) {
            throw new RuntimeException("Search operation failed");
        }
    }

    // Test Output Boundary
    private static class TestRecipeSearchOutputBoundary implements RecipeSearchOutputBoundary {
        Recipe savedRecipe;
        String errorMessage;
        List<Recipe> presentedRecipes;

        @Override
        public void presentSaveSuccess(Recipe recipe) {
            this.savedRecipe = recipe;
        }

        @Override
        public void presentError(String message) {
            this.errorMessage = message;
        }

        @Override
        public void presentRecipes(List<Recipe> recipes) {
            this.presentedRecipes = recipes;
        }
    }
}