package data_access;

import entity.Nutrient;
import entity.Recipe;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import use_case.nutrition_analysis.NutritionAnalysisDataAccessInterface;
import use_case.nutrition_analysis.NutritionAnalysisException;

/**
 * The DAO that implements the interface.
 * <p>
 * Contains the main method of the interface, which extract information from an api.
 * Contains several helper methods.
 * </p>
 */
@Component
public class NutritionAnalysisDataAccessObject implements NutritionAnalysisDataAccessInterface {
    private static final String NA_URL = "https://api.edamam.com/api/nutrition-details";
    private final OkHttpClient httpClient;
    private final String appId;
    private final String appKey;

    public NutritionAnalysisDataAccessObject(
            OkHttpClient httpClient,
            @Value("${recipewiz.edamam.nutrition-analysis.app-id}") String appId,
            @Value("${recipewiz.edamam.nutrition-analysis.app-key}") String appKey
    ) {
        this.httpClient = httpClient;
        this.appId = appId;
        this.appKey = appKey;
    }

    @Override
    public List<Nutrient> analyzeNutrition(Recipe recipe) throws IOException {
        List<Nutrient> nutrientsList = new ArrayList<>();

        // Build the HTTP request
        final Request request = buildNutritionRequest(recipe);

        // Process the response
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.body() != null) {
                nutrientsList = parseNutrientsResponse(response.body().string());
            }
        }
        catch (IOException exception) {
            throw new IOException("Error processing nutrition analysis", exception);
        }

        return nutrientsList;
    }

    private Request buildNutritionRequest(Recipe recipe) {
        if (appId == null || appId.isBlank() || appKey == null || appKey.isBlank()) {
            throw new IllegalStateException("Edamam credentials are not configured");
        }

        final HttpUrl urlBuilder = Objects.requireNonNull(HttpUrl.parse(NA_URL)).newBuilder()
                .addQueryParameter("app_id", appId)
                .addQueryParameter("app_key", appKey)
                .build();

        final JSONObject requestBody = new JSONObject();
        requestBody.put("title", recipe.getTitle());
        requestBody.put("ingr", recipe.getJsonIngredient());

        return new Request.Builder()
                .url(urlBuilder)
                .post(RequestBody.create(
                        requestBody.toString(),
                        MediaType.get("application/json; charset=utf-8")
                ))
                .build();
    }

    private List<Nutrient> parseNutrientsResponse(String jsonData) throws NutritionAnalysisException {
        final List<Nutrient> nutrientsList = new ArrayList<>();
        try {
            final JSONObject jsonObject = new JSONObject(jsonData);
            final JSONObject totalNutrients = jsonObject.getJSONObject("totalNutrients");

            for (String key : totalNutrients.keySet()) {
                final JSONObject nutrient = totalNutrients.getJSONObject(key);
                final String aNutrient = nutrient.getString("label")
                        + ": "
                        + nutrient.getInt("quantity")
                        + nutrient.getString("unit");

                nutrientsList.add(new Nutrient(aNutrient));
            }
        }
        catch (JSONException exception) {
            throw new NutritionAnalysisException("Error processing nutrition analysis", exception);
        }
        return nutrientsList;
    }
}
