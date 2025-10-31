import { useState, useMemo } from 'react';
import {
  Flex,
  Heading,
  Stack,
  Text,
  useToast,
} from '@chakra-ui/react';
import dayjs from 'dayjs';
import type { RecipeDto } from '../types/api';
import { useMealPlanning } from '../features/meal-planning/hooks/useMealPlanning';
import {
  getWeekStart,
  getWeekDays,
} from '../features/meal-planning/utils';
import { WeekNavigator } from '../features/meal-planning/components/WeekNavigator';
import { MealPlanCalendar } from '../features/meal-planning/components/MealPlanCalendar';
import { SavedRecipesPanel } from '../features/meal-planning/components/SavedRecipesPanel';
import { AddMealModal } from '../features/meal-planning/components/AddMealModal';

export const MealPlannerPage = () => {
  const [weekStart, setWeekStart] = useState(() => getWeekStart(dayjs()));
  const [selectedRecipe, setSelectedRecipe] = useState<RecipeDto | null>(null);
  const [processingEntryId, setProcessingEntryId] = useState<number | null>(null);
  const [removingEntryId, setRemovingEntryId] = useState<number | null>(null);
  const [isAddingMeal, setIsAddingMeal] = useState<boolean>(false);
  const toast = useToast();

  const weekDays = useMemo(() => getWeekDays(weekStart), [weekStart]);

  const {
    mealPlan,
    isMealPlanLoading,
    savedRecipes,
    isSavedRecipesLoading,
    addMeal,
    removeMeal,
    updateMealStatus,
  } = useMealPlanning(weekStart);

  const handlePreviousWeek = () => {
    setWeekStart((prev) => getWeekStart(prev.subtract(7, 'day')));
  };

  const handleNextWeek = () => {
    setWeekStart((prev) => getWeekStart(prev.add(7, 'day')));
  };

  const handleResetWeek = () => {
    setWeekStart(getWeekStart(dayjs()));
  };

  const handleAddMeal = async ({
    recipeId,
    mealDate,
    mealType,
  }: {
    recipeId: number;
    mealDate: string;
    mealType: string;
  }) => {
    setIsAddingMeal(true);
    try {
      await addMeal({ recipeId, mealDate, mealType });
      toast({
        title: 'Meal added to calendar',
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
      setSelectedRecipe(null);
    }
    catch (error) {
      toast({
        title: 'Failed to add meal',
        description: error instanceof Error ? error.message : 'Please try again later.',
        status: 'error',
        duration: 4000,
        isClosable: true,
      });
    }
    finally {
      setIsAddingMeal(false);
    }
  };

  const handleUpdateStatus = async (entryId: number, status: string) => {
    setProcessingEntryId(entryId);
    try {
      await updateMealStatus({ entryId, status });
      toast({
        title: 'Meal status updated',
        status: 'success',
        duration: 2500,
        isClosable: true,
      });
    }
    catch (error) {
      toast({
        title: 'Failed to update status',
        description: error instanceof Error ? error.message : 'Please try again later.',
        status: 'error',
        duration: 4000,
        isClosable: true,
      });
    }
    finally {
      setProcessingEntryId(null);
    }
  };

  const handleRemoveEntry = async (entryId: number) => {
    setRemovingEntryId(entryId);
    try {
      await removeMeal(entryId);
      toast({
        title: 'Meal removed from calendar',
        status: 'success',
        duration: 2500,
        isClosable: true,
      });
    }
    catch (error) {
      toast({
        title: 'Failed to remove meal',
        description: error instanceof Error ? error.message : 'Please try again later.',
        status: 'error',
        duration: 4000,
        isClosable: true,
      });
    }
    finally {
      setRemovingEntryId(null);
    }
  };

  return (
    <>
      <Stack spacing={6} w="full">
        <Heading color="teal.700">Meal Planner</Heading>
        <Text color="gray.600">
          Plan your week by assigning saved recipes to specific days and meal types. Update meal status as
          you cook and stay in sync across all your devices.
        </Text>

        <WeekNavigator
          weekStart={weekStart}
          onPreviousWeek={handlePreviousWeek}
          onNextWeek={handleNextWeek}
          onResetWeek={handleResetWeek}
        />

        <Flex gap={6} direction={{ base: 'column', lg: 'row' }} align="stretch">
          <MealPlanCalendar
            weekDays={weekDays}
            entries={mealPlan}
            isLoading={isMealPlanLoading}
            processingEntryId={processingEntryId}
            removingEntryId={removingEntryId}
            onUpdateStatus={handleUpdateStatus}
            onRemove={handleRemoveEntry}
          />
          <SavedRecipesPanel
            recipes={savedRecipes}
            isLoading={isSavedRecipesLoading}
            onAdd={(recipe) => setSelectedRecipe(recipe)}
          />
        </Flex>
      </Stack>

      <AddMealModal
        isOpen={Boolean(selectedRecipe)}
        onClose={() => setSelectedRecipe(null)}
        recipe={selectedRecipe}
        weekDays={weekDays}
        onSubmit={handleAddMeal}
        isSubmitting={isAddingMeal}
      />
    </>
  );
};
