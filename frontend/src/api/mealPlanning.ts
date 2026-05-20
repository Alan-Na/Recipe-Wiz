import { apiClient } from './client';
import type {
  AddMealPlanEntryRequest,
  AiMealPlanConfirmRequest,
  AiMealPlanPreviewDto,
  AiMealPlanRequest,
  MealPlanEntryDto,
  RecipeDto,
  UpdateMealStatusRequest,
} from '../types/api';

export const getMealPlanWeek = async (
  userId: number,
  weekStart: string,
): Promise<MealPlanEntryDto[]> => {
  const params = new URLSearchParams({ weekStart });
  const response = await apiClient.get<MealPlanEntryDto[]>(
    `/api/users/${userId}/meal-plan?${params.toString()}`,
  );
  return response.data;
};

export const addMealPlanEntry = async (
  userId: number,
  payload: AddMealPlanEntryRequest,
): Promise<void> => {
  await apiClient.post(`/api/users/${userId}/meal-plan`, payload);
};

export const removeMealPlanEntry = async (userId: number, entryId: number): Promise<void> => {
  await apiClient.delete(`/api/users/${userId}/meal-plan/${entryId}`);
};

export const updateMealStatus = async (
  userId: number,
  entryId: number,
  payload: UpdateMealStatusRequest,
): Promise<void> => {
  await apiClient.patch(`/api/users/${userId}/meal-plan/${entryId}`, payload);
};

export const getSavedRecipesForMealPlan = async (userId: number): Promise<RecipeDto[]> => {
  const response = await apiClient.get<RecipeDto[]>(`/api/users/${userId}/meal-plan/saved-recipes`);
  return response.data;
};

// ── AI Meal Planner ────────────────────────────────────────────────────────

export const generateAiMealPlan = async (
  userId: number,
  payload: AiMealPlanRequest,
): Promise<AiMealPlanPreviewDto> => {
  const response = await apiClient.post<AiMealPlanPreviewDto>(
    `/api/users/${userId}/meal-plan/ai/generate`,
    payload,
    { timeout: 90_000 }, // DeepSeek may be slow; override default 15s
  );
  return response.data;
};

export const confirmAiMealPlan = async (
  userId: number,
  payload: AiMealPlanConfirmRequest,
): Promise<void> => {
  await apiClient.post(`/api/users/${userId}/meal-plan/ai/confirm`, payload);
};
