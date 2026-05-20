import { useState, useMemo } from 'react';
import {
  Box, Flex, HStack, Stack, Text, useToast, VStack,
} from '@chakra-ui/react';
import dayjs from 'dayjs';
import { useTranslation } from 'react-i18next';
import { motion } from 'framer-motion';
import type { RecipeDto } from '../types/api';
import { useMealPlanning } from '../features/meal-planning/hooks/useMealPlanning';
import { getWeekStart, getWeekDays } from '../features/meal-planning/utils';
import { WeekNavigator } from '../features/meal-planning/components/WeekNavigator';
import { MealPlanCalendar } from '../features/meal-planning/components/MealPlanCalendar';
import { SavedRecipesPanel } from '../features/meal-planning/components/SavedRecipesPanel';
import { AddMealModal } from '../features/meal-planning/components/AddMealModal';
import { AiMealPlannerPanel } from '../features/meal-planning/components/AiMealPlannerPanel';

const MotionBox = motion(Box);

export const MealPlannerPage = () => {
  const [weekStart, setWeekStart] = useState(() => getWeekStart(dayjs()));
  const [selectedRecipe, setSelectedRecipe] = useState<RecipeDto | null>(null);
  const [processingEntryId, setProcessingEntryId] = useState<number | null>(null);
  const [removingEntryId, setRemovingEntryId] = useState<number | null>(null);
  const [isAddingMeal, setIsAddingMeal] = useState<boolean>(false);
  const toast = useToast();
  const { t } = useTranslation();

  const weekDays = useMemo(() => getWeekDays(weekStart), [weekStart]);

  const {
    mealPlan, isMealPlanLoading, savedRecipes, isSavedRecipesLoading,
    addMeal, removeMeal, updateMealStatus,
    generateAiPlan, confirmAiPlan, isGeneratingAiPlan, isConfirmingAiPlan,
  } = useMealPlanning(weekStart);

  const handlePreviousWeek = () => setWeekStart((prev) => getWeekStart(prev.subtract(7, 'day')));
  const handleNextWeek    = () => setWeekStart((prev) => getWeekStart(prev.add(7, 'day')));
  const handleResetWeek   = () => setWeekStart(getWeekStart(dayjs()));

  const handleAddMeal = async ({ recipeId, mealDate, mealType }: { recipeId: number; mealDate: string; mealType: string }) => {
    setIsAddingMeal(true);
    try {
      await addMeal({ recipeId, mealDate, mealType });
      toast({ title: t('mealPlanner.toast.mealAdded'), status: 'success', duration: 3000, isClosable: true });
      setSelectedRecipe(null);
    } catch (error) {
      toast({ title: t('mealPlanner.toast.mealAddFailed'), description: error instanceof Error ? error.message : t('mealPlanner.toast.tryAgain'), status: 'error', duration: 4000, isClosable: true });
    } finally {
      setIsAddingMeal(false);
    }
  };

  const handleUpdateStatus = async (entryId: number, status: string) => {
    setProcessingEntryId(entryId);
    try {
      await updateMealStatus({ entryId, status });
      toast({ title: t('mealPlanner.toast.statusUpdated'), status: 'success', duration: 2500, isClosable: true });
    } catch (error) {
      toast({ title: t('mealPlanner.toast.statusFailed'), description: error instanceof Error ? error.message : t('mealPlanner.toast.tryAgain'), status: 'error', duration: 4000, isClosable: true });
    } finally {
      setProcessingEntryId(null);
    }
  };

  const handleRemoveEntry = async (entryId: number) => {
    setRemovingEntryId(entryId);
    try {
      await removeMeal(entryId);
      toast({ title: t('mealPlanner.toast.mealRemoved'), status: 'success', duration: 2500, isClosable: true });
    } catch (error) {
      toast({ title: t('mealPlanner.toast.mealRemoveFailed'), description: error instanceof Error ? error.message : t('mealPlanner.toast.tryAgain'), status: 'error', duration: 4000, isClosable: true });
    } finally {
      setRemovingEntryId(null);
    }
  };

  return (
    <>
      <Stack spacing={6} w="full">

        {/* Page header */}
        <MotionBox initial={{ opacity: 0, y: -16 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.4 }}>
          <HStack spacing={3} align="flex-end">
            <VStack align="flex-start" spacing={0.5}>
              <HStack spacing={2}>
                <Text fontSize="2xl">📅</Text>
                <Box
                  fontSize={{ base: '2xl', md: '3xl' }}
                  fontWeight={900}
                  letterSpacing="-0.5px"
                  bgGradient="linear(to-r, orange.500, orange.400)"
                  bgClip="text"
                  as="h1"
                >
                  {t('mealPlanner.pageTitle')}
                </Box>
              </HStack>
              <Text color="gray.500" fontSize="sm" pl={9}>
                {t('mealPlanner.pageDescription')}
              </Text>
            </VStack>
          </HStack>
        </MotionBox>

        {/* Quick stats strip */}
        <MotionBox initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.4, delay: 0.08 }}>
          <Flex gap={3} flexWrap="wrap">
            {[
              { emoji: '🍽️', label: t('mealPlanner.mealsThisWeek', 'Meals this week'), value: mealPlan.length },
              { emoji: '💾', label: t('mealPlanner.savedRecipes', 'Saved recipes'), value: savedRecipes.length },
            ].map((stat) => (
              <Box
                key={stat.label}
                bg="white"
                borderRadius="xl"
                px={5}
                py={3}
                shadow="sm"
                border="1px solid"
                borderColor="orange.100"
                display="flex"
                alignItems="center"
                gap={3}
              >
                <Text fontSize="lg">{stat.emoji}</Text>
                <Box>
                  <Text fontSize="xl" fontWeight={800} color="gray.800" lineHeight={1}>{stat.value}</Text>
                  <Text fontSize="xs" color="gray.500" fontWeight={500}>{stat.label}</Text>
                </Box>
              </Box>
            ))}
          </Flex>
        </MotionBox>

        {/* AI Meal Planner Panel */}
        <MotionBox initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.4, delay: 0.1 }}>
          <AiMealPlannerPanel
            savedRecipesCount={savedRecipes.length}
            onGenerate={generateAiPlan}
            onConfirm={confirmAiPlan}
            isGenerating={isGeneratingAiPlan}
            isConfirming={isConfirmingAiPlan}
          />
        </MotionBox>

        {/* Week navigator */}
        <MotionBox initial={{ opacity: 0, y: 8 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.4, delay: 0.15 }}>
          <WeekNavigator
            weekStart={weekStart}
            onPreviousWeek={handlePreviousWeek}
            onNextWeek={handleNextWeek}
            onResetWeek={handleResetWeek}
          />
        </MotionBox>

        {/* Calendar + panel */}
        <Flex gap={5} direction={{ base: 'column', lg: 'row' }} align="flex-start">
          <Box flex={1} minW={0}>
            <MealPlanCalendar
              weekDays={weekDays}
              entries={mealPlan}
              isLoading={isMealPlanLoading}
              processingEntryId={processingEntryId}
              removingEntryId={removingEntryId}
              onUpdateStatus={handleUpdateStatus}
              onRemove={handleRemoveEntry}
            />
          </Box>
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
