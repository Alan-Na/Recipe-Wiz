package use_case.ai_meal_plan;

import entity.Recipe;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Core use-case: generate a validated AI meal plan preview, then optionally confirm it.
 */
public class AiMealPlanInteractor {

    private static final int MIN_SAVED_RECIPES = 3;
    private static final Set<String> VALID_MEAL_TYPES = Set.of("Breakfast", "Lunch", "Dinner", "Snack");

    private final AiMealPlanDataAccessInterface dao;
    private final AiGenerationGateway gateway;

    public AiMealPlanInteractor(AiMealPlanDataAccessInterface dao, AiGenerationGateway gateway) {
        this.dao = dao;
        this.gateway = gateway;
    }

    /**
     * Generates and validates a meal plan preview.
     * Performs one automatic retry if the first LLM response yields too few valid entries.
     *
     * @param userId user requesting the plan
     * @param profile physical profile + goal
     * @param today  start date (plan runs from today → end of ISO week/Sunday)
     * @return validated, non-empty list of bulk entries ready for commit
     */
    public List<AiMealPlanDataAccessInterface.BulkEntry> generatePreview(
            int userId,
            AiGenerationGateway.UserProfile profile,
            LocalDate today) throws Exception {

        List<Recipe> saved = dao.getSavedRecipes(userId);
        if (saved.size() < MIN_SAVED_RECIPES) {
            throw new IllegalStateException(
                    "At least " + MIN_SAVED_RECIPES + " saved recipes are required to use AI planning.");
        }

        // Plan to end of ISO week (Sunday inclusive)
        LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        Set<Integer> validIds = saved.stream()
                .map(Recipe::getRecipeId)
                .collect(Collectors.toSet());

        long daysRemaining = today.until(weekEnd).getDays() + 1;

        // First attempt
        List<AiMealPlanDataAccessInterface.BulkEntry> valid =
                filterValid(gateway.generatePlan(profile, saved, today, weekEnd), validIds, today, weekEnd);

        // Retry once if we have fewer valid entries than days remaining
        if (valid.size() < daysRemaining) {
            valid = filterValid(gateway.generatePlan(profile, saved, today, weekEnd), validIds, today, weekEnd);
        }

        if (valid.isEmpty()) {
            throw new IllegalStateException(
                    "AI could not generate a valid meal plan. Please try again or adjust your saved recipes.");
        }
        return valid;
    }

    /**
     * Bulk-writes the confirmed preview entries to the meal plan table.
     */
    public void confirmPlan(int userId, List<AiMealPlanDataAccessInterface.BulkEntry> entries) {
        dao.bulkAddMealEntries(userId, entries);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private List<AiMealPlanDataAccessInterface.BulkEntry> filterValid(
            List<AiGenerationGateway.RawEntry> raw,
            Set<Integer> validIds,
            LocalDate from,
            LocalDate to) {
        return raw.stream()
                .filter(e -> validIds.contains(e.recipeId()))
                .filter(e -> VALID_MEAL_TYPES.contains(e.mealType()))
                .filter(e -> !e.mealDate().isBefore(from) && !e.mealDate().isAfter(to))
                .map(e -> new AiMealPlanDataAccessInterface.BulkEntry(e.recipeId(), e.mealDate(), e.mealType()))
                .distinct()
                .collect(Collectors.toList());
    }
}
