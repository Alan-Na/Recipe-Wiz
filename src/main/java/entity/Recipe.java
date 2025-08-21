package entity;

import java.util.List;
import java.util.Objects;

import org.json.JSONArray;

/**
 * The unified Recipe entity containing all necessary details.
 */
public class Recipe {

    private int recipeId;
    private String title;
    private String description;
    private List<Ingredient> ingredients;
    private String instructions;
    private Nutrition nutrition;
    private List<Food> food;
    private JSONArray jsonIngredient;
    private int servings;

    /**
     * Constructor for Recipe with all fields.
     * @param recipeId the Id of the recipe in the api.
     * @param title the name of the recipe.
     * @param description a brief description of this recipe.
     * @param ingredients a list of all ingredients of this recipe.
     * @param instructions the instruction of making this meal.
     * @param nutrition the nutrition information of this recipe given by the api.
     * @param food a list of all food required for this meal.
     * @param jsonIngredient the list of ingredients in JSONArray form.
     * @param servings the number of people this meal is serving.
     */
    public Recipe(int recipeId, String title, String description, List<Ingredient> ingredients,
                  String instructions, Nutrition nutrition, List<Food> food, JSONArray jsonIngredient, int servings) {
        this.recipeId = recipeId;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = Objects.requireNonNullElse(instructions, "Instructions not available");
        this.nutrition = nutrition;
        this.food = food;
        this.jsonIngredient = jsonIngredient;
        this.servings = servings;
    }

    // Getters and setters for all fields
    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public List<Food> getFood() {
        return food;
    }

    public void setFood(List<Food> food) {
        this.food = food;
    }

    public JSONArray getJsonIngredient() {
        return jsonIngredient;
    }

    public void setJsonIngredient(JSONArray jsonIngredient) {
        this.jsonIngredient = jsonIngredient;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    @Override
    public String toString() {
        return String.format("Recipe: %s (Servings: %d)%nDescription: %s%nIngredients: %s%nInstructions: %s",
                title, servings, description, ingredients, instructions);
    }
}
