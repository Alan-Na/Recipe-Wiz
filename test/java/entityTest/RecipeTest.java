package entityTest;

import entity.*;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import org.json.JSONObject;

import java.util.ArrayList;
public class RecipeTest {

    private Recipe recipe;
    private List<Ingredient> ingredients;
    private Nutrition nutrition;
    private List<Food> food;
    private JSONArray jsonIngredients;

    @Before
    public void setUp() {
        // Create dummy data for testing
        ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "Flour", 2.0, "cups"));
        ingredients.add(new Ingredient(2, "Sugar", 1.5, "cups"));

        nutrition = new Nutrition(200, 5, 10, 30, 2, 15);

        food = new ArrayList<>();
        food.add(new Food(1, "Wheat", "Grain", "kg", 1.0, nutrition));
        food.add(new Food(2, "Sugarcane", "Plant", "kg", 0.5, nutrition));

        jsonIngredients = new JSONArray();
        jsonIngredients.put(new JSONObject().put("name", "Flour").put("quantity", 2.0).put("unit", "cups"));
        jsonIngredients.put(new JSONObject().put("name", "Sugar").put("quantity", 1.5).put("unit", "cups"));

        // Create a Recipe object
        recipe = new Recipe(101, "Pancakes", "Delicious pancakes",
                ingredients, "Mix and cook.", nutrition, food, jsonIngredients, 4);
    }

    @Test
    public void testGetters() {
        assertEquals(101, recipe.getRecipeId());
        assertEquals("Pancakes", recipe.getTitle());
        assertEquals("Delicious pancakes", recipe.getDescription());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals("Mix and cook.", recipe.getInstructions());
        assertEquals(nutrition, recipe.getNutrition());
        assertEquals(food, recipe.getFood());
        assertEquals(jsonIngredients.toString(), recipe.getJsonIngredient().toString());
        assertEquals(4, recipe.getServings());
    }

    @Test
    public void testSetters() {
        recipe.setRecipeId(202);
        assertEquals(202, recipe.getRecipeId());

        recipe.setTitle("Waffles");
        assertEquals("Waffles", recipe.getTitle());

        recipe.setDescription("Crispy waffles");
        assertEquals("Crispy waffles", recipe.getDescription());

        List<Ingredient> newIngredients = new ArrayList<>();
        newIngredients.add(new Ingredient(3, "Milk", 1.0, "cups"));
        recipe.setIngredients(newIngredients);
        assertEquals(newIngredients, recipe.getIngredients());

        recipe.setInstructions("Blend and bake.");
        assertEquals("Blend and bake.", recipe.getInstructions());

        Nutrition newNutrition = new Nutrition(150, 4, 6, 20, 1, 10);
        recipe.setNutrition(newNutrition);
        assertEquals(newNutrition, recipe.getNutrition());

        List<Food> newFood = new ArrayList<>();
        newFood.add(new Food(3, "Butter", "Dairy", "grams", 200, newNutrition));
        recipe.setFood(newFood);
        assertEquals(newFood, recipe.getFood());

        JSONArray newJsonIngredients = new JSONArray();
        newJsonIngredients.put(new JSONObject().put("name", "Butter").put("quantity", 200).put("unit", "grams"));
        recipe.setJsonIngredient(newJsonIngredients);
        assertEquals(newJsonIngredients.toString(), recipe.getJsonIngredient().toString());

        recipe.setServings(2);
        assertEquals(2, recipe.getServings());
    }

    @Test
    public void testToString() {
        String expected = "Recipe: Pancakes (Servings: 4)\n" +
                "Description: Delicious pancakes\n" +
                "Ingredients: " + ingredients.toString() + "\n" +
                "Instructions: Mix and cook.";
        assertEquals(expected, recipe.toString());
    }
}