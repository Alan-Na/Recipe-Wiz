import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import type { Dayjs } from 'dayjs';
import {
  addMealPlanEntry,
  confirmAiMealPlan,
  generateAiMealPlan,
  getMealPlanWeek,
  getSavedRecipesForMealPlan,
  removeMealPlanEntry,
  updateMealStatus,
} from '../../../api/mealPlanning';
import { deleteSavedRecipe } from '../../../api/recipes';
import type {
  AddMealPlanEntryRequest,
  AiMealPlanConfirmRequest,
  AiMealPlanRequest,
  MealPlanEntryDto,
} from '../../../types/api';

const USER_ID = 1;

export const MEAL_PLAN_QUERY_KEY = 'meal-plan';
export const SAVED_RECIPES_QUERY_KEY = 'saved-recipes';

export const useMealPlanning = (weekStart: Dayjs) => {
  const queryClient = useQueryClient();
  const weekStartKey = weekStart.format('YYYY-MM-DD');

  const mealPlanQuery = useQuery({
    queryKey: [MEAL_PLAN_QUERY_KEY, USER_ID, weekStartKey],
    queryFn: () => getMealPlanWeek(USER_ID, weekStartKey),
  });

  const savedRecipesQuery = useQuery({
    queryKey: [SAVED_RECIPES_QUERY_KEY, USER_ID],
    queryFn: () => getSavedRecipesForMealPlan(USER_ID),
  });

  const invalidateMealPlan = () =>
    queryClient.invalidateQueries({ queryKey: [MEAL_PLAN_QUERY_KEY, USER_ID] });

  const addMealMutation = useMutation({
    mutationFn: (payload: AddMealPlanEntryRequest) => addMealPlanEntry(USER_ID, payload),
    onSuccess: invalidateMealPlan,
  });

  const removeMealMutation = useMutation({
    mutationFn: (entryId: number) => removeMealPlanEntry(USER_ID, entryId),
    onSuccess: invalidateMealPlan,
  });

  const updateStatusMutation = useMutation({
    mutationFn: ({ entryId, status }: { entryId: number; status: string }) =>
      updateMealStatus(USER_ID, entryId, { status }),
    onSuccess: invalidateMealPlan,
  });

  const invalidateSavedRecipes = () =>
    queryClient.invalidateQueries({ queryKey: [SAVED_RECIPES_QUERY_KEY, USER_ID] });

  const deleteSavedRecipeMutation = useMutation({
    mutationFn: (recipeId: number) => deleteSavedRecipe(USER_ID, recipeId),
    onSuccess: invalidateSavedRecipes,
  });

  const generateAiPlanMutation = useMutation({
    mutationFn: (payload: AiMealPlanRequest) => generateAiMealPlan(USER_ID, payload),
  });

  const confirmAiPlanMutation = useMutation({
    mutationFn: (payload: AiMealPlanConfirmRequest) => confirmAiMealPlan(USER_ID, payload),
    onSuccess: invalidateMealPlan,
  });

  return {
    mealPlan: (mealPlanQuery.data ?? []) as MealPlanEntryDto[],
    isMealPlanLoading: mealPlanQuery.isLoading,
    isMealPlanPending: mealPlanQuery.isPending,
    savedRecipes: savedRecipesQuery.data ?? [],
    isSavedRecipesLoading: savedRecipesQuery.isLoading,
    addMeal: addMealMutation.mutateAsync,
    removeMeal: removeMealMutation.mutateAsync,
    updateMealStatus: updateStatusMutation.mutateAsync,
    addMealStatus: addMealMutation.status,
    removeMealStatus: removeMealMutation.status,
    updateMealStatusState: updateStatusMutation.status,
    deleteSavedRecipe: deleteSavedRecipeMutation.mutateAsync,
    isDeletingSavedRecipe: deleteSavedRecipeMutation.isPending,
    deletingSavedRecipeId: deleteSavedRecipeMutation.variables ?? null,
    generateAiPlan: generateAiPlanMutation.mutateAsync,
    confirmAiPlan: confirmAiPlanMutation.mutateAsync,
    isGeneratingAiPlan: generateAiPlanMutation.isPending,
    isConfirmingAiPlan: confirmAiPlanMutation.isPending,
  };
};
