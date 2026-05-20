package use_case.ai_meal_plan;

import entity.Recipe;
import java.time.LocalDate;
import java.util.List;

/**
 * Port for the external LLM call. Implementations (adapters) live in data_access/.
 */
public interface AiGenerationGateway {

    /**
     * Asks the LLM to produce a meal plan.
     *
     * @param profile  the user's physical profile and goal
     * @param recipes  saved recipes with nutrition context
     * @param dateFrom first day of the plan (inclusive)
     * @param dateTo   last day of the plan (inclusive)
     * @return raw list of suggested entries (not yet validated)
     */
    List<RawEntry> generatePlan(UserProfile profile, List<Recipe> recipes,
                                LocalDate dateFrom, LocalDate dateTo) throws Exception;

    record UserProfile(String gender, int heightCm, int weightKg, int age, String goal) {}

    record RawEntry(int recipeId, LocalDate mealDate, String mealType) {}
}
