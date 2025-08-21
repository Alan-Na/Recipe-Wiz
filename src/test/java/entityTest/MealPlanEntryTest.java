package entityTest;

import entity.MealPlanEntry;
import entity.Recipe;
import entity.Ingredient;
import entity.Food;
import entity.Nutrition;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;

public class MealPlanEntryTest {

    private MealPlanEntry mealPlanEntry;
    private Recipe recipe;

    @Before
    public void setUp() {
        // Create mock or basic instances for the complex attributes of Recipe

        // Mock or create Ingredients (just basic test data for demonstration)
        Ingredient ingredient1 = new Ingredient(1, "Flour", 500, "grams");
        Ingredient ingredient2 = new Ingredient(2, "Sugar", 200, "grams");
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);

        // Create Nutrition object (assuming it's another class with minimal attributes)
        Nutrition nutrition = new Nutrition(100.0, 5.0, 2.0, 30.0, 10.0, 15.0);  // Mock values for the example

        // Create Food objects (mocking them with minimal attributes)
        Food food1 = new Food(1, "Pizza", "Main Dish", "slice", 1.0, nutrition);
        Food food2 = new Food(2, "Pasta", "Main Dish", "bowl", 1.5, nutrition);
        List<Food> foodList = new ArrayList<>();
        foodList.add(food1);
        foodList.add(food2);

        // Create a JSONArray for jsonIngredient (assuming you need some mock data)
        JSONArray jsonIngredients = new JSONArray();
        jsonIngredients.put("ingredient1");
        jsonIngredients.put("ingredient2");

        // Create a Recipe instance with the mocked/constructed data
        recipe = new Recipe(1, "Spaghetti Bolognese", "A classic Italian pasta dish.", ingredients, "Cook spaghetti, then add sauce.", nutrition, foodList, jsonIngredients, 4);

        // Now create the MealPlanEntry with the mock Recipe
        mealPlanEntry = new MealPlanEntry(1, recipe, LocalDate.of(2024, 11, 26), 101, "Dinner");
    }

    @Test
    public void testConstructor_validInput() {
        // Check that the object is created correctly with valid parameters
        assertNotNull(mealPlanEntry);
        assertEquals(1, mealPlanEntry.getEntryId());
        assertEquals(recipe, mealPlanEntry.getRecipe());
        assertEquals(LocalDate.of(2024, 11, 26), mealPlanEntry.getDate());
        assertEquals(101, mealPlanEntry.getUserId());
        assertEquals("Dinner", mealPlanEntry.getMealType());
        assertEquals("planned", mealPlanEntry.getStatus());  // Default status should be "planned"
    }

    @Test
    public void testGetEntryId() {
        assertEquals(1, mealPlanEntry.getEntryId());
    }

    @Test
    public void testGetRecipe() {
        assertEquals(recipe, mealPlanEntry.getRecipe());
    }

    @Test
    public void testGetDate() {
        assertEquals(LocalDate.of(2024, 11, 26), mealPlanEntry.getDate());
    }

    @Test
    public void testGetUserId() {
        assertEquals(101, mealPlanEntry.getUserId());
    }

    @Test
    public void testGetMealType() {
        assertEquals("Dinner", mealPlanEntry.getMealType());
    }

    @Test
    public void testGetStatus() {
        assertEquals("planned", mealPlanEntry.getStatus());
    }

    @Test
    public void testSetEntryId() {
        mealPlanEntry.setEntryId(2);
        assertEquals(2, mealPlanEntry.getEntryId());
    }

    @Test
    public void testSetRecipe() {
        // Create a new Recipe to replace the old one
        Recipe newRecipe = new Recipe(2, "Chicken Salad", "A fresh chicken salad.", new ArrayList<>(), "Mix ingredients", new Nutrition(100.0, 5.0, 2.0, 30.0, 10.0, 15.0), new ArrayList<>(), new JSONArray(), 2);
        mealPlanEntry.setRecipe(newRecipe);
        assertEquals(newRecipe, mealPlanEntry.getRecipe());
    }

    @Test
    public void testSetDate() {
        mealPlanEntry.setDate(LocalDate.of(2025, 12, 31));
        assertEquals(LocalDate.of(2025, 12, 31), mealPlanEntry.getDate());
    }

    @Test
    public void testSetUserId() {
        mealPlanEntry.setUserId(102);
        assertEquals(102, mealPlanEntry.getUserId());
    }

    @Test
    public void testSetMealType() {
        mealPlanEntry.setMealType("Lunch");
        assertEquals("Lunch", mealPlanEntry.getMealType());
    }

    @Test
    public void testSetStatus() {
        mealPlanEntry.setStatus("completed");
        assertEquals("completed", mealPlanEntry.getStatus());
    }

    @Test
    public void testStatusDefaultValue() {
        // Ensure the default value of 'status' is "planned" when created
        MealPlanEntry entry = new MealPlanEntry(2, recipe, LocalDate.of(2024, 11, 27), 103, "Breakfast");
        assertEquals("planned", entry.getStatus());
    }
}