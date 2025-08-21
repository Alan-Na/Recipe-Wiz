package interface_adapter.recipe_search;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state for recipe search functionality.
 */
public class RecipeSearchState {
    private List<String> ingredients = new ArrayList<>();
    private List<String> recipeResults = new ArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();
    private String error;
    private String message;

    /**
     * Copy constructor for RecipeSearchState.
     * @param copy The state to copy from
     */
    public RecipeSearchState(RecipeSearchState copy) {
        if (copy != null) {
            ingredients = new ArrayList<>(copy.ingredients);
            recipeResults = new ArrayList<>(copy.recipeResults);
            recipes = new ArrayList<>(copy.recipes);
            error = copy.error;
            message = copy.message;
        }
    }

    /**
     * Default constructor for RecipeSearchState.
     */
    public RecipeSearchState() {

    }

    /**
     * Getter for ingredients.
     * @return A copy of the ingredients list
     */
    public List<String> getIngredients() {
        return new ArrayList<>(ingredients);
    }

    /**
     * Setter for ingredients.
     * @param ingredients The ingredients to set
     */
    public void setIngredients(List<String> ingredients) {
        this.ingredients = new ArrayList<>(ingredients);
    }

    /**
     * Getter for recipeResults.
     * @return A copy of the recipeResults list
     */
    public List<String> getRecipeResults() {
        return new ArrayList<>(recipeResults);
    }

    /**
     * Setter for recipeResults.
     * @param recipeResults The recipeResults to set
     */
    public void setRecipeResults(List<String> recipeResults) {
        this.recipeResults = new ArrayList<>(recipeResults);
    }

    /**
     * Getter for error.
     * @return The error message
     */
    public String getError() {
        return error;
    }

    /**
     * Setter for error.
     * @param error The error message to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Getter for message.
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter for message.
     * @param message The message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for recipes.
     * @return A copy of the recipes list
     */
    public List<Recipe> getRecipes() {
        return new ArrayList<>(recipes);
    }

    /**
     * Setter for recipes.
     * @param recipes The recipes to set
     */
    public void setRecipes(List<Recipe> recipes) {
        this.recipes = new ArrayList<>(recipes);
    }
}