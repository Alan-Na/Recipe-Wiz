package data_access;

import entity.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.recipe_search.RecipeSearchDataAccessInterface;

public class RecipeSearchDataAccessObject implements RecipeSearchDataAccessInterface {
    private static final String APP_ID = "aaac3ad0";
    private static final String APP_KEY = "3862f2800864a629947f0f79f4da280d";
    private static final String BASE_URL = "https://api.edamam.com/api/recipes/v2";
    private static final String NA_URL = "https://api.edamam.com/api/nutrition-details";
    private final OkHttpClient httpClient = new OkHttpClient();

    @Override
    public List<Recipe> searchRecipesByFoodName(String foodName) {
        return searchRecipes(foodName, null, null, null);
    }

    /**
     * Fetch recipes from Edamam API.
     */
    private List<Recipe> searchRecipes(String foodName, String diet, String health, String cuisineType) {
        List<Recipe> recipes = new ArrayList<>();

        // Build API URL
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("type", "public");
        urlBuilder.addQueryParameter("q", foodName);
        urlBuilder.addQueryParameter("app_id", APP_ID);
        urlBuilder.addQueryParameter("app_key", APP_KEY);

        // Add optional filters if provided
        if (diet != null && !diet.isEmpty()) {
            urlBuilder.addQueryParameter("diet", diet);
        }
        if (health != null && !health.isEmpty()) {
            urlBuilder.addQueryParameter("health", health);
        }
        if (cuisineType != null && !cuisineType.isEmpty()) {
            urlBuilder.addQueryParameter("cuisineType", cuisineType);
        }

        Request request = new Request.Builder().url(urlBuilder.build().toString()).build();

        // API Call
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray hitsArray = jsonObject.getJSONArray("hits");

                for (int i = 0; i < hitsArray.length(); i++) {
                    JSONObject recipeJson = hitsArray.getJSONObject(i).getJSONObject("recipe");

                    // Extract details
                    String name = recipeJson.getString("label");
                    JSONArray jsonIngredient = recipeJson.getJSONArray("ingredientLines");
                    String description = recipeJson.optString("source", "No description available");
                    String instructions = recipeJson.optString("url", "Instructions not available");
                    List<Ingredient> ingredients = extractIngredients(recipeJson.getJSONArray("ingredients"));

                    // Provide default nutrition if not available
                    Nutrition nutrition = new Nutrition(0, 0, 0, 0, 0, 0);

                    // Add a default food list and servings
                    List<Food> food = new ArrayList<>();
                    int servings = 1;

                    // Create Recipe
                    Recipe recipe = new Recipe(
                            i + 1,
                            name,
                            description,
                            ingredients,
                            instructions,
                            nutrition,
                            food,
                            jsonIngredient,
                            servings
                    );

                    recipes.add(recipe);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return recipes;
    }

    /**
     * Extract detailed ingredients from a JSON array.
     */
    private List<Ingredient> extractIngredients(JSONArray ingredientsArray) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientsArray.length(); i++) {
            JSONObject ingredientJson = ingredientsArray.getJSONObject(i);

            String food = ingredientJson.getString("food");
            double quantity = ingredientJson.optDouble("quantity", 0.0);
            String unit = ingredientJson.optString("measure", "");

            ingredients.add(new Ingredient(i + 1, food, quantity, unit));
        }
        return ingredients;
    }

    /**
     * Retrieve detailed recipe information by recipe ID.
     */
    public Recipe getRecipeById(String recipeId) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("type", "public");
        urlBuilder.addQueryParameter("id", recipeId);
        urlBuilder.addQueryParameter("app_id", APP_ID);
        urlBuilder.addQueryParameter("app_key", APP_KEY);

        Request request = new Request.Builder().url(urlBuilder.build().toString()).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject recipeJson = jsonObject.getJSONObject("recipe");

                // Extract details
                String name = recipeJson.getString("label");
                String description = recipeJson.optString("source", "No description available");
                String instructions = recipeJson.optString("url", "Instructions not available");
                List<Ingredient> ingredients = extractIngredients(recipeJson.getJSONArray("ingredients"));

                // Extract nutrition
                JSONObject nutritionJson = recipeJson.optJSONObject("totalNutrients");
                Nutrition nutrition = (nutritionJson != null) ? new Nutrition(
                        nutritionJson.optJSONObject("ENERC_KCAL").optDouble("quantity", 0),
                        nutritionJson.optJSONObject("PROCNT").optDouble("quantity", 0),
                        nutritionJson.optJSONObject("FAT").optDouble("quantity", 0),
                        nutritionJson.optJSONObject("CHOCDF").optDouble("quantity", 0),
                        nutritionJson.optJSONObject("FIBTG").optDouble("quantity", 0),
                        nutritionJson.optJSONObject("SUGAR").optDouble("quantity", 0)
                ) : new Nutrition(0, 0, 0, 0, 0, 0);

                List<Food> food = new ArrayList<>();
                int servings = 1;

                return new Recipe(
                        Integer.parseInt(recipeId),
                        name,
                        description,
                        ingredients,
                        instructions,
                        nutrition,
                        food,
                        new JSONArray(),
                        servings

                );
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return null;
    }
}
