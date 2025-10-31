package data_access;

import entity.Food;
import entity.Ingredient;
import entity.Nutrition;
import entity.Recipe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import use_case.search_with_restriction.SearchWithRestrictionDataAccessInterface;

@Component
public class SearchWithRestrictionDataAccessObject implements SearchWithRestrictionDataAccessInterface {

    private static final String BASE_URL = "https://api.edamam.com/api/recipes/v2";

    private final OkHttpClient httpClient;
    private final String appId;
    private final String appKey;

    public SearchWithRestrictionDataAccessObject(
            OkHttpClient httpClient,
            @Value("${recipewiz.edamam.recipe-search.app-id}") String appId,
            @Value("${recipewiz.edamam.recipe-search.app-key}") String appKey
    ) {
        this.httpClient = httpClient;
        this.appId = appId;
        this.appKey = appKey;
    }

    @Override
    public List<Recipe> searchRecipesByRestriction(String foodName, String diet, String health, String
            cuisineType) {
        return searchRecipes(foodName, diet, health, cuisineType);
    }

    /**
     * Fetch recipes from Edamam API.
     */
    private List<Recipe> searchRecipes(String foodName, String diet, String health, String cuisineType) {
        final List<Recipe> recipes = new ArrayList<>();

        // Build API URL
        if (appId == null || appId.isBlank() || appKey == null || appKey.isBlank()) {
            throw new IllegalStateException("Edamam credentials are not configured");
        }

        final HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("type", "public");
        urlBuilder.addQueryParameter("q", foodName);
        urlBuilder.addQueryParameter("app_id", appId);
        urlBuilder.addQueryParameter("app_key", appKey);

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
}
