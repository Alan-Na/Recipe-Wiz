package data_access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Recipe;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import use_case.serving_adjust.ServingAdjustDataAccessInterface;
import use_case.serving_adjust.ServingAdjustException;

/**
 * Persists serving-adjusted recipe data for a user by updating both the
 * {@code saved_recipes} table and any matching {@code meal_plan_entries} rows.
 */
@Component
public class ServingAdjustDataAccess implements ServingAdjustDataAccessInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServingAdjustDataAccess.class);

    private static final String UPDATE_SAVED =
            "UPDATE saved_recipes SET recipe_data = ? WHERE user_id = ? AND recipe_id = ?";
    private static final String UPDATE_ENTRIES =
            "UPDATE meal_plan_entries SET recipe_data = ? WHERE user_id = ? AND recipe_id = ?";

    private final DatabaseManager databaseManager;
    private final Gson gson;

    public ServingAdjustDataAccess(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(JSONArray.class, new JSONArrayTypeAdapter())
                .create();
    }

    @Override
    public void saveUpdatedRecipes(int userId, List<Recipe> recipes) throws ServingAdjustException {
        try (Connection conn = databaseManager.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement savedStmt = conn.prepareStatement(UPDATE_SAVED);
                 PreparedStatement entryStmt = conn.prepareStatement(UPDATE_ENTRIES)) {

                for (Recipe recipe : recipes) {
                    final String json = gson.toJson(recipe);
                    final int recipeId = recipe.getRecipeId();

                    savedStmt.setString(1, json);
                    savedStmt.setInt(2, userId);
                    savedStmt.setInt(3, recipeId);
                    savedStmt.addBatch();

                    entryStmt.setString(1, json);
                    entryStmt.setInt(2, userId);
                    entryStmt.setInt(3, recipeId);
                    entryStmt.addBatch();

                    LOGGER.debug("Queued serving update for user={} recipe={} ({})", userId, recipeId, recipe.getTitle());
                }

                savedStmt.executeBatch();
                entryStmt.executeBatch();
                conn.commit();
                LOGGER.info("Persisted serving adjustment for user={}, {} recipe(s)", userId, recipes.size());

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new ServingAdjustException("Failed to persist serving adjustments: " + e.getMessage(), e);
        }
    }
}
