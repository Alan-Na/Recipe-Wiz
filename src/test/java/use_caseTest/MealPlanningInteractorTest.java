package use_caseTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import data_access.SavedRecipesDataAccessInterface;
import entity.MealPlanEntry;
import entity.Recipe;
import use_case.meal_planning.MealPlanningDataAccessInterface;
import use_case.meal_planning.MealPlanningInteractor;
import use_case.meal_planning.MealPlanningOutputBoundary;

class MealPlanningInteractorTest {

    @Mock
    private MealPlanningDataAccessInterface dataAccessInterface;

    @Mock
    private SavedRecipesDataAccessInterface savedRecipesDataAccessInterface;

    @Mock
    private MealPlanningOutputBoundary outputBoundary;

    private MealPlanningInteractor interactor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interactor = new MealPlanningInteractor(
                dataAccessInterface,
                savedRecipesDataAccessInterface,
                outputBoundary
        );
    }

    @Test
    void testGetSavedRecipes_Success() {
        int userId = 1;
        List<Recipe> expectedRecipes = new ArrayList<>();
        when(savedRecipesDataAccessInterface.getSavedRecipes(userId)).thenReturn(expectedRecipes);

        List<Recipe> result = interactor.getSavedRecipes(userId);

        assertEquals(expectedRecipes, result);
        verify(outputBoundary).presentSavedRecipes(expectedRecipes);
    }

    @Test
    void testGetSavedRecipes_InvalidUserId() {
        int invalidUserId = -1;

        List<Recipe> result = interactor.getSavedRecipes(invalidUserId);

        assertTrue(result.isEmpty());
        verify(outputBoundary).presentError(any());
    }

    @Test
    void testUpdateMealStatus_Success() {
        int userId = 1;
        int entryId = 1;
        String status = "completed";

        interactor.updateMealStatus(userId, entryId, status);

        verify(dataAccessInterface).updateMealStatus(userId, entryId, status);
        verify(outputBoundary).presentStatusUpdateSuccess("Meal status updated");
    }

    @Test
    void testUpdateMealStatus_InvalidUserId() {
        int invalidUserId = -1;
        int entryId = 1;
        String status = "completed";

        interactor.updateMealStatus(invalidUserId, entryId, status);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).updateMealStatus(anyInt(), anyInt(), anyString());
    }

    @Test
    void testUpdateMealStatus_InvalidEntryId() {
        int userId = 1;
        int invalidEntryId = -1;
        String status = "completed";

        interactor.updateMealStatus(userId, invalidEntryId, status);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).updateMealStatus(anyInt(), anyInt(), anyString());
    }

    @Test
    void testUpdateMealStatus_InvalidStatus() {
        int userId = 1;
        int entryId = 1;
        String invalidStatus = "";

        interactor.updateMealStatus(userId, entryId, invalidStatus);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).updateMealStatus(anyInt(), anyInt(), anyString());
    }

    @Test
    void testAddToCalendar_Success() {
        int userId = 1;
        int recipeId = 1;
        LocalDate date = LocalDate.now();
        String mealType = "lunch";

        interactor.addToCalendar(userId, recipeId, date, mealType);

        verify(dataAccessInterface).addMealPlanEntry(userId, recipeId, date, mealType);
        verify(outputBoundary).presentAddSuccess("Recipe added to calendar");
    }

    @Test
    void testAddToCalendar_InvalidUserId() {
        int invalidUserId = -1;
        int recipeId = 1;
        LocalDate date = LocalDate.now();
        String mealType = "lunch";

        interactor.addToCalendar(invalidUserId, recipeId, date, mealType);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).addMealPlanEntry(anyInt(), anyInt(), any(), anyString());
    }

    @Test
    void testAddToCalendar_InvalidRecipeId() {
        int userId = 1;
        int invalidRecipeId = -1;
        LocalDate date = LocalDate.now();
        String mealType = "lunch";

        interactor.addToCalendar(userId, invalidRecipeId, date, mealType);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).addMealPlanEntry(anyInt(), anyInt(), any(), anyString());
    }

    @Test
    void testAddToCalendar_NullDate() {
        int userId = 1;
        int recipeId = 1;
        LocalDate nullDate = null;
        String mealType = "lunch";

        interactor.addToCalendar(userId, recipeId, nullDate, mealType);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).addMealPlanEntry(anyInt(), anyInt(), any(), anyString());
    }

    @Test
    void testAddToCalendar_InvalidMealType() {
        int userId = 1;
        int recipeId = 1;
        LocalDate date = LocalDate.now();
        String invalidMealType = "";

        interactor.addToCalendar(userId, recipeId, date, invalidMealType);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).addMealPlanEntry(anyInt(), anyInt(), any(), anyString());
    }

    @Test
    void testRemoveFromCalendar_Success() {
        int userId = 1;
        int entryId = 1;

        interactor.removeFromCalendar(userId, entryId);

        verify(dataAccessInterface).removeMealPlanEntry(userId, entryId);
        verify(outputBoundary).presentRemoveSuccess("Recipe removed from calendar");
    }

    @Test
    void testRemoveFromCalendar_InvalidUserId() {
        int invalidUserId = -1;
        int entryId = 1;

        interactor.removeFromCalendar(invalidUserId, entryId);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).removeMealPlanEntry(anyInt(), anyInt());
    }

    @Test
    void testRemoveFromCalendar_InvalidEntryId() {
        int userId = 1;
        int invalidEntryId = -1;

        interactor.removeFromCalendar(userId, invalidEntryId);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).removeMealPlanEntry(anyInt(), anyInt());
    }

    @Test
    void testGetCalendarWeek_Success() {
        int userId = 1;
        LocalDate weekStart = LocalDate.now();
        List<MealPlanEntry> expectedEntries = new ArrayList<>();
        when(dataAccessInterface.getWeeklyPlan(userId, weekStart)).thenReturn(expectedEntries);

        interactor.getCalendarWeek(userId, weekStart);

        verify(dataAccessInterface).getWeeklyPlan(userId, weekStart);
        verify(outputBoundary).presentCalendarWeek(expectedEntries);
    }

    @Test
    void testGetCalendarWeek_InvalidUserId() {
        int invalidUserId = -1;
        LocalDate weekStart = LocalDate.now();

        interactor.getCalendarWeek(invalidUserId, weekStart);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).getWeeklyPlan(anyInt(), any());
    }

    @Test
    void testGetCalendarWeek_NullDate() {
        int userId = 1;
        LocalDate nullDate = null;

        interactor.getCalendarWeek(userId, nullDate);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).getWeeklyPlan(anyInt(), any());
    }

    @Test
    void testInitializeMealPlanning_Success() {
        int userId = 1;
        List<MealPlanEntry> expectedEntries = new ArrayList<>();
        when(dataAccessInterface.getWeeklyPlan(eq(userId), any(LocalDate.class)))
                .thenReturn(expectedEntries);

        interactor.initializeMealPlanning(userId);

        verify(dataAccessInterface).getWeeklyPlan(eq(userId), any(LocalDate.class));
        verify(outputBoundary).presentCalendarWeek(expectedEntries);
    }

    @Test
    void testInitializeMealPlanning_InvalidUserId() {
        int invalidUserId = -1;

        interactor.initializeMealPlanning(invalidUserId);

        verify(outputBoundary).presentError(any());
        verify(dataAccessInterface, never()).getWeeklyPlan(anyInt(), any());
    }
}