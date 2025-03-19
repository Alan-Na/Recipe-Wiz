package data_access;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entity.MealPlanEntry;
import entity.Recipe;
import use_case.meal_planning.MealPlanningDataAccessInterface;

/**
 * Implementation of MealPlanningDataAccessInterface that manages meal plan entries using JSON file storage.
 */
public class MealPlanningDataAccessObject implements MealPlanningDataAccessInterface {

    private final Map<Integer, MealPlanEntry> mealPlanEntries = new HashMap<>();
    private final String filePath = "meal_plan.json";
    private final SavedRecipesDataAccessInterface savedRecipesDataAccessInterface;
    private int nextEntryId = 1;

    /**
     * Constructs a new MealPlanningDataAccessObject.
     *
     * @param savedRecipesDataAccessInterface the data access object for saved recipes
     */
    public MealPlanningDataAccessObject(SavedRecipesDataAccessInterface savedRecipesDataAccessInterface) {
        this.savedRecipesDataAccessInterface = savedRecipesDataAccessInterface;
        loadFromJsonFile();
    }

    @Override
    public MealPlanEntry getMealPlanEntry(int userId, int entryId) {
        final MealPlanEntry entry = mealPlanEntries.get(entryId);
        final MealPlanEntry result;
        if (entry != null && entry.getUserId() == userId) {
            result = entry;
        }
        else {
            result = null;
        }
        return result;
    }

    @Override
    public void updateMealStatus(int userId, int entryId, String status) {
        final MealPlanEntry entry = mealPlanEntries.get(entryId);
        if (entry != null && entry.getUserId() == userId) {
            entry.setStatus(status);
            saveToJsonFile();
        }
        else {
            throw new IllegalArgumentException("Entry not found or unauthorized access");
        }
    }

    @Override
    public void addMealPlanEntry(int userId, int recipeId, LocalDate date, String mealType) {
        final Recipe recipe = savedRecipesDataAccessInterface.getSavedRecipe(userId, recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe not found in saved recipes");
        }

        final MealPlanEntry entry = new MealPlanEntry(nextEntryId++, recipe, date, userId, mealType);
        mealPlanEntries.put(entry.getEntryId(), entry);
        saveToJsonFile();
    }

    @Override
    public void removeMealPlanEntry(int userId, int mealPlanEntryId) {
        final MealPlanEntry entry = mealPlanEntries.get(mealPlanEntryId);
        if (entry != null && entry.getUserId() == userId) {
            mealPlanEntries.remove(mealPlanEntryId);
            saveToJsonFile();
        }
    }

    @Override
    public List<MealPlanEntry> getWeeklyPlan(int userId, LocalDate weekStart) {
        final int daysInWeek = 7;
        return mealPlanEntries.values().stream()
                .filter(entry -> entry.getUserId() == userId)
                .filter(entry -> {
                    final LocalDate date = entry.getDate();
                    return !date.isBefore(weekStart) && date.isBefore(weekStart.plusDays(daysInWeek));
                })
                .toList();
    }

    private void saveToJsonFile() {
        try (Writer writer = new FileWriter(filePath)) {
            final Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(JSONArray.class, new JSONArrayTypeAdapter())
                    .create();
            gson.toJson(mealPlanEntries, writer);
        }
        catch (IOException exception) {
            System.err.println("Error saving to JSON file: " + exception.getMessage());
        }
    }

    private void loadFromJsonFile() {
        try (Reader reader = new FileReader(filePath)) {
            final Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .registerTypeAdapter(JSONArray.class, new JSONArrayTypeAdapter())
                    .create();

            final Type type = new TypeToken<Map<Integer, MealPlanEntry>>() { }.getType();
            final Map<Integer, MealPlanEntry> loadedEntries = gson.fromJson(reader, type);

            if (loadedEntries != null) {
                mealPlanEntries.putAll(loadedEntries);
                nextEntryId = loadedEntries.keySet().stream().max(Integer::compareTo).orElse(0) + 1;
            }
        }
        catch (IOException exception) {
            System.err.println("Error loading from JSON file: " + exception.getMessage());
        }
    }
}
