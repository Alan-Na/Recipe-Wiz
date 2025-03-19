package use_caseTest.serving_adjust;

import entity.Ingredient;
import entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.serving_adjust.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for ServingAdjustInteractor.
 */
class ServingAdjustInteractorTest {

    private TestServingAdjustOutputBoundary outputBoundary;
    private TestServingAdjustDataAccess dataAccess;
    private ServingAdjustInteractor interactor;

    @BeforeEach
    void setUp() {

        outputBoundary = new TestServingAdjustOutputBoundary();
        dataAccess = new TestServingAdjustDataAccess();

        interactor = new ServingAdjustInteractor(outputBoundary, dataAccess);
    }

    @Test
    void testAdjustServings_singleRecipe() throws ServingAdjustException {

        Ingredient ingredient1 = new Ingredient(1, "Flour", 200, "grams");
        Ingredient ingredient2 = new Ingredient(2, "Sugar", 100, "grams");
        List<Ingredient> ingredients = Arrays.asList(ingredient1, ingredient2);

        Recipe recipe = new Recipe(
                1,
                "Cake",
                "Delicious cake",
                ingredients,
                "Mix ingredients and bake.",
                null,
                null,
                null,
                4
        );

        List<Recipe> recipes = Collections.singletonList(recipe);

        ServingAdjustInputData inputData = new ServingAdjustInputData(8, recipes);

        interactor.adjustServings(inputData);

        assertEquals(8, recipe.getServings());
        assertEquals(400, ingredient1.getQuantity());
        assertEquals(200, ingredient2.getQuantity());

        assertTrue(outputBoundary.wasCalled);
        assertEquals(recipes, outputBoundary.outputData.getUpdatedRecipes());

        assertTrue(dataAccess.wasCalled);
        assertEquals(recipes, dataAccess.savedRecipes);
    }

    @Test
    void testAdjustServings_invalidServings() {

        Ingredient ingredient = new Ingredient(1, "Salt", 10, "grams");
        List<Ingredient> ingredients = Collections.singletonList(ingredient);

        Recipe recipe = new Recipe(
                2,
                "Soup",
                "Tasty soup",
                ingredients,
                "Boil water and add salt.",
                null,
                null,
                null,
                2
        );

        List<Recipe> recipes = Collections.singletonList(recipe);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServingAdjustInputData(0, recipes);
        });

        assertEquals("New servings must be greater than zero.", exception.getMessage());
    }

    @Test
    void testAdjustServings_multipleRecipes() throws ServingAdjustException {

        Ingredient ingredient1 = new Ingredient(1, "Pasta", 100, "grams");
        Recipe recipe1 = new Recipe(
                3,
                "Spaghetti",
                "Classic spaghetti",
                Collections.singletonList(ingredient1),
                "Cook pasta.",
                null,
                null,
                null,
                2
        );

        Ingredient ingredient2 = new Ingredient(2, "Rice", 200, "grams");
        Recipe recipe2 = new Recipe(
                4,
                "Fried Rice",
                "Delicious fried rice",
                Collections.singletonList(ingredient2),
                "Fry rice.",
                null,
                null,
                null,
                4
        );

        List<Recipe> recipes = Arrays.asList(recipe1, recipe2);

        ServingAdjustInputData inputData = new ServingAdjustInputData(8, recipes);

        interactor.adjustServings(inputData);

        assertEquals(8, recipe1.getServings());
        assertEquals(400, ingredient1.getQuantity());

        assertEquals(8, recipe2.getServings());
        assertEquals(400, ingredient2.getQuantity());

        assertTrue(outputBoundary.wasCalled);
        assertEquals(recipes, outputBoundary.outputData.getUpdatedRecipes());

        assertTrue(dataAccess.wasCalled);
        assertEquals(recipes, dataAccess.savedRecipes);
    }

    @Test
    void testAdjustServings_recipeWithInvalidCurrentServings() {

        Ingredient ingredient = new Ingredient(1, "Milk", 500, "ml");
        Recipe recipe = new Recipe(
                7,
                "Smoothie",
                "Fruit smoothie",
                Collections.singletonList(ingredient),
                "Blend ingredients.",
                null,
                null,
                null,
                0
        );

        List<Recipe> recipes = Collections.singletonList(recipe);

        ServingAdjustInputData inputData = new ServingAdjustInputData(4, recipes);

        ServingAdjustException exception = assertThrows(
                ServingAdjustException.class,
                () -> interactor.adjustServings(inputData)
        );

        assertEquals("Current servings must be greater than zero.", exception.getMessage());
    }

    @Test
    void testAdjustServings_dataAccessThrowsException() {

        dataAccess.throwExceptionOnSave = true;

        Ingredient ingredient = new Ingredient(1, "Eggs", 3, "pieces");
        Recipe recipe = new Recipe(
                8,
                "Omelette",
                "Simple omelette",
                Collections.singletonList(ingredient),
                "Beat eggs and cook.",
                null,
                null,
                null,
                1
        );

        List<Recipe> recipes = Collections.singletonList(recipe);

        ServingAdjustInputData inputData = new ServingAdjustInputData(2, recipes);

        ServingAdjustException exception = assertThrows(
                ServingAdjustException.class,
                () -> interactor.adjustServings(inputData)
        );

        assertEquals("Failed to save updated recipes.", exception.getMessage());
    }

    /**
     * Test implementation of ServingAdjustOutputBoundary for capturing method calls.
     */
    static class TestServingAdjustOutputBoundary implements ServingAdjustOutputBoundary {
        boolean wasCalled = false;
        ServingAdjustOutputData outputData = null;

        @Override
        public void presentUpdatedRecipes(ServingAdjustOutputData outputData) {
            this.wasCalled = true;
            this.outputData = outputData;
        }
    }

    /**
     * Test implementation of ServingAdjustDataAccessInterface for capturing method calls.
     */
    private static class TestServingAdjustDataAccess implements ServingAdjustDataAccessInterface {
        public boolean wasCalled = false;
        public boolean throwExceptionOnSave = false;
        public List<Recipe> savedRecipes = null;

        @Override
        public void saveUpdatedRecipes(List<Recipe> recipes) throws ServingAdjustException {
            if (throwExceptionOnSave) {
                throw new ServingAdjustException("Failed to save updated recipes.");
            }
            this.wasCalled = true;
            this.savedRecipes = recipes;
        }
    }

    @Test
    void testAdjustServings_nullRecipesList() {
        int newServings = 4;
        List<Recipe> recipes = null;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServingAdjustInputData(newServings, recipes);
        });
        assertEquals("Recipes list cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testAdjustServings_emptyRecipesList() {

        int newServings = 4;
        List<Recipe> recipes = Collections.emptyList();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new ServingAdjustInputData(newServings, recipes);
        });
        assertEquals("Recipes list cannot be null or empty.", exception.getMessage());
    }

    @Test
    void testAdjustServings_recipeListContainsNull() {

        Ingredient ingredient = new Ingredient(1, "Sugar", 100, "grams");
        Recipe recipe = new Recipe(
                1,
                "Cake",
                "Delicious cake",
                Collections.singletonList(ingredient),
                "Mix ingredients and bake.",
                null,
                null,
                null,
                4
        );

        List<Recipe> recipes = Arrays.asList(recipe, null);

        ServingAdjustInputData inputData = new ServingAdjustInputData(8, recipes);

        ServingAdjustException exception = assertThrows(
                ServingAdjustException.class,
                () -> interactor.adjustServings(inputData)
        );

        assertEquals("Recipe cannot be null.", exception.getMessage());
    }

}
