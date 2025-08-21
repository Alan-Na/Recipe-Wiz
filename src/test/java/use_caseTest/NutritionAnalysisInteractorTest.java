package use_caseTest;

import data_access.*;
import entity.*;
import interface_adapter.nutrition_analysis.NutritionAnalysisPresenter;
import interface_adapter.nutrition_analysis.NutritionAnalysisState;
import interface_adapter.nutrition_analysis.NutritionAnalysisViewModel;
import interface_adapter.recipe_search.RecipeSearchPresenter;
import interface_adapter.recipe_search.RecipeSearchViewModel;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import use_case.nutrition_analysis.NutritionAnalysisException;
import use_case.nutrition_analysis.NutritionAnalysisInteractor;
import use_case.recipe_search.RecipeSearchInteractor;

import static org.junit.Assert.*;

import java.util.List;

public class NutritionAnalysisInteractorTest {

    private NutritionAnalysisInteractor nutritionAnalysis;
    private NutritionAnalysisViewModel viewModel;
    private Recipe recipe;

    @Before
    public void setUp() {

        // Initialize necessary components
        NutritionAnalysisDataAccessObject mockDAO = new NutritionAnalysisDataAccessObject(); // Custom implementation
        viewModel = new NutritionAnalysisViewModel();
        NutritionAnalysisPresenter presenter = new NutritionAnalysisPresenter(viewModel);
        nutritionAnalysis = new NutritionAnalysisInteractor(mockDAO, presenter);

        // Establishment of a system that produce a valid Recipe Object
        SavedRecipesDataAccessObject savedRecipesDataAccess = new SavedRecipesDataAccessObject();
        RecipeSearchViewModel recipeSearchViewModel = new RecipeSearchViewModel();
        RecipeSearchPresenter recipeSearchPresenter = new RecipeSearchPresenter(recipeSearchViewModel);
        RecipeSearchDataAccessObject recipeSearchDataAccessObject = new RecipeSearchDataAccessObject();

        RecipeSearchInteractor recipeSearchInputBoundaryUseCase = new RecipeSearchInteractor(
                recipeSearchDataAccessObject,
                savedRecipesDataAccess,
                recipeSearchPresenter
        );

        // Assume RecipeSearch use case (RecipeSearchInteractor) is well tested (independence principle).
        final String exampleFoodName = "egg";
        List<String> exampleIngredients = List.of(exampleFoodName);
        recipeSearchInputBoundaryUseCase.searchRecipes(exampleIngredients);
        recipe = recipeSearchViewModel.getState().getRecipes().get(1);

    }

    @Test
    public void testAnalyzeNutrition_Success() {


        // Call the method
        nutritionAnalysis.analyzeNutrition(recipe);

        // Get the state from the viewModel
        NutritionAnalysisState state = viewModel.getState();

        // Ensure that the state is of type NutritionAnalysisState
        assertNotNull("Expected state to be an instance of NutritionAnalysisState", state);

        // Assert the result
        assertNotNull(state);
        assertEquals(33, state.getNutritionResults().size());
        assertTrue(state.getNutritionResults().toString().contains("Vitamin B-6"));
        assertTrue(state.getNutritionResults().toString().contains("Fatty acids, total monounsaturated"));
    }

    @Test
    public void testAnalyzeNutrition_ErrorHandling() {

        try {
            recipe.setJsonIngredient(new JSONArray());
            nutritionAnalysis.analyzeNutrition(recipe);
        }
        catch (NutritionAnalysisException e) {
            assertEquals("Error processing nutrition analysis", e.getMessage());
            assertTrue(e.getCause() instanceof RuntimeException);
        }
    }
}
