package data_access;

import entity.Nutrition;
import entity.Recipe;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import use_case.ai_meal_plan.AiGenerationGateway;

/**
 * Adapter that calls the DeepSeek Chat Completions API to generate structured meal plans.
 * Uses OkHttp (same pattern as NutritionAnalysisDataAccessObject) and org.json for parsing.
 */
@Component
public class DeepSeekAiGateway implements AiGenerationGateway {

    private static final String DEEPSEEK_URL = "https://api.deepseek.com/chat/completions";
    private static final String MODEL = "deepseek-chat";
    private static final MediaType JSON_MEDIA = MediaType.get("application/json; charset=utf-8");

    private static final String SYSTEM_PROMPT = """
            You are a professional nutritionist and meal planner.
            You will receive a user's physical profile, their health goal, and a list of available recipes with nutritional data.
            Your task is to create a structured meal plan covering the specified date range.

            Rules:
            - Use ONLY recipeIds from the provided recipe list.
            - mealType MUST be exactly one of: Breakfast, Lunch, Dinner, Snack (case-sensitive).
            - mealDate MUST be in ISO-8601 format (YYYY-MM-DD) and within the requested date range (inclusive).
            - Assign 2-3 meals per day (e.g. Breakfast + Lunch + Dinner, optionally Snack).
            - Do NOT assign the same recipe more than once per day.
            - Distribute meals across the week to ensure nutritional variety.
            - Balance macronutrients according to the user's health goal.
            - Return ONLY a valid JSON object with this exact structure:
              { "mealPlan": [ { "recipeId": <integer>, "mealDate": "<YYYY-MM-DD>", "mealType": "<string>" } ] }
            - Return no prose, no markdown fences, only the JSON object.
            """;

    private final OkHttpClient httpClient;
    private final String apiKey;

    public DeepSeekAiGateway(
            OkHttpClient httpClient,
            @Value("${recipewiz.deepseek.api-key}") String apiKey) {
        this.httpClient = httpClient;
        this.apiKey = apiKey;
    }

    @PostConstruct
    public void validateApiKey() {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                "DeepSeek API key is not configured. " +
                "Set the DEEPSEEK_API_KEY environment variable before starting the application.");
        }
    }

    @Override
    public List<RawEntry> generatePlan(UserProfile profile, List<Recipe> recipes,
                                       LocalDate dateFrom, LocalDate dateTo) throws Exception {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("DeepSeek API key is not configured.");
        }

        String userMessage = buildUserMessage(profile, recipes, dateFrom, dateTo);
        String requestBodyJson = buildRequestBody(userMessage);

        Request request = new Request.Builder()
                .url(DEEPSEEK_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(RequestBody.create(requestBodyJson, JSON_MEDIA))
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("DeepSeek API returned HTTP " + response.code());
            }
            return parseResponse(response.body().string());
        }
    }

    // ── Request builder ───────────────────────────────────────────────────────

    private String buildRequestBody(String userMessage) {
        JSONObject systemMsg = new JSONObject()
                .put("role", "system")
                .put("content", SYSTEM_PROMPT);

        JSONObject userMsg = new JSONObject()
                .put("role", "user")
                .put("content", userMessage);

        return new JSONObject()
                .put("model", MODEL)
                .put("response_format", new JSONObject().put("type", "json_object"))
                .put("temperature", 0.7)
                .put("max_tokens", 2000)
                .put("messages", new JSONArray().put(systemMsg).put(userMsg))
                .toString();
    }

    private String buildUserMessage(UserProfile p, List<Recipe> recipes,
                                    LocalDate from, LocalDate to) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== User Profile ===\n");
        sb.append("Gender: ").append(p.gender()).append("\n");
        sb.append("Height: ").append(p.heightCm()).append(" cm\n");
        sb.append("Weight: ").append(p.weightKg()).append(" kg\n");
        sb.append("Age: ").append(p.age()).append(" years\n");
        sb.append("Health Goal: ").append(p.goal()).append("\n\n");

        sb.append("=== Meal Plan Date Range ===\n");
        sb.append("From: ").append(from).append(" (inclusive)\n");
        sb.append("To:   ").append(to).append(" (inclusive)\n\n");

        sb.append("=== Available Recipes (use ONLY these recipeIds) ===\n");
        for (Recipe r : recipes) {
            sb.append("recipeId=").append(r.getRecipeId())
              .append(" | title=\"").append(r.getTitle()).append("\"");
            Nutrition n = r.getNutrition();
            if (n != null) {
                sb.append(" | cal=").append(Math.round(n.getCalories()))
                  .append("kcal, protein=").append(Math.round(n.getProtein()))
                  .append("g, fat=").append(Math.round(n.getFat()))
                  .append("g, carbs=").append(Math.round(n.getCarbohydrates())).append("g");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // ── Response parser ───────────────────────────────────────────────────────

    private List<RawEntry> parseResponse(String responseBody) throws Exception {
        JSONObject root = new JSONObject(responseBody);
        String content = root
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");

        JSONArray mealPlan = new JSONObject(content).getJSONArray("mealPlan");
        List<RawEntry> result = new ArrayList<>();
        for (int i = 0; i < mealPlan.length(); i++) {
            JSONObject entry = mealPlan.getJSONObject(i);
            result.add(new RawEntry(
                    entry.getInt("recipeId"),
                    LocalDate.parse(entry.getString("mealDate")),
                    entry.getString("mealType")));
        }
        return result;
    }
}
