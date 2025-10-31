package data_access;

import entity.Recipe;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import use_case.serving_adjust.ServingAdjustDataAccessInterface;
import use_case.serving_adjust.ServingAdjustException;

/**
 * Implementation of ServingAdjustDataAccessInterface for data persistence.
 */
@Component
public class ServingAdjustDataAccess implements ServingAdjustDataAccessInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServingAdjustDataAccess.class);

    @Override
    public void saveUpdatedRecipes(List<Recipe> recipes) throws ServingAdjustException {
        try {
            for (Recipe recipe : recipes) {
                LOGGER.info("Saving updated recipe: {}", recipe.getTitle());
            }
        }
        catch (Exception e) {
            throw new ServingAdjustException("Failed to save updated recipes.", e);
        }
    }
}
