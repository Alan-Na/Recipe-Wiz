import {
  Box, Button, Flex, Heading, HStack, Icon, Stack, Tag,
  TagCloseButton, TagLabel, Text, useDisclosure, useToast, VStack,
} from '@chakra-ui/react';
import { useMutation } from '@tanstack/react-query';
import { useState } from 'react';
import { FaFilter } from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import { motion } from 'framer-motion';
import { IngredientManager } from '../features/recipe-search/components/IngredientManager';
import { RestrictionDrawer } from '../features/recipe-search/components/RestrictionDrawer';
import { RecipeResults } from '../features/recipe-search/components/RecipeResults';
import { NutritionModal } from '../features/recipe-search/components/NutritionModal';
import type { SearchRestrictions } from '../features/recipe-search/types';
import { adjustServings, saveRecipe, searchRecipes, searchRecipesWithRestrictions } from '../api/recipes';
import type { RecipeDto } from '../types/api';
import { analyzeNutrition } from '../api/nutrition';

const MotionBox = motion(Box);

const DEFAULT_RESTRICTIONS: SearchRestrictions = { diet: [], health: [], cuisine: [] };
const USER_ID = 1;

const TAG_TYPE_COLORS: Record<string, string> = {
  diet: 'green', health: 'teal', cuisine: 'purple',
};

export const RecipeSearchPage = () => {
  const [ingredients, setIngredients] = useState<string[]>([]);
  const [restrictions, setRestrictions] = useState<SearchRestrictions>(DEFAULT_RESTRICTIONS);
  const [recipes, setRecipes] = useState<RecipeDto[]>([]);
  const [selectedRecipe, setSelectedRecipe] = useState<RecipeDto | undefined>(undefined);

  const restrictionDrawer = useDisclosure();
  const nutritionModal = useDisclosure();
  const toast = useToast();
  const { t } = useTranslation();

  const searchMutation = useMutation({
    mutationFn: async (payload: { ingredients: string[]; restrictions: SearchRestrictions }) => {
      const { ingredients: ing, restrictions: rest } = payload;
      const hasRestrictions = rest.diet.length > 0 || rest.health.length > 0 || rest.cuisine.length > 0;
      if (hasRestrictions) {
        return searchRecipesWithRestrictions({
          foodName: ing.join(', '),
          dietLabels: rest.diet,
          healthLabels: rest.health,
          cuisineTypes: rest.cuisine,
        });
      }
      return searchRecipes(ing);
    },
    onSuccess: (data) => {
      setRecipes(data);
      if (data.length === 0) {
        toast({ title: t('recipeSearch.toast.notFound'), description: t('recipeSearch.toast.notFoundDesc'), status: 'info', duration: 4000, isClosable: true });
      }
    },
    onError: (error: unknown) => {
      toast({ title: t('recipeSearch.toast.searchFailed'), description: error instanceof Error ? error.message : t('recipeSearch.toast.tryAgain'), status: 'error', duration: 4000, isClosable: true });
    },
  });

  const saveMutation = useMutation({
    mutationFn: (recipe: RecipeDto) => saveRecipe(USER_ID, recipe),
    onSuccess: () => {
      toast({ title: t('recipeSearch.toast.saved'), description: t('recipeSearch.toast.savedDesc'), status: 'success', duration: 3000, isClosable: true });
    },
    onError: (error: unknown) => {
      toast({ title: t('recipeSearch.toast.saveFailed'), description: error instanceof Error ? error.message : t('recipeSearch.toast.tryAgain'), status: 'error', duration: 4000, isClosable: true });
    },
  });

  const adjustMutation = useMutation({
    mutationFn: async ({ recipe, servings }: { recipe: RecipeDto; servings: number }) => {
      const updated = await adjustServings({ newServings: servings, recipes: [recipe] });
      return updated[0];
    },
    onSuccess: (updatedRecipe) => {
      setRecipes((prev) => prev.map((r) => (r.recipeId === updatedRecipe.recipeId ? updatedRecipe : r)));
      toast({ title: t('recipeSearch.toast.servingsUpdated'), description: t('recipeSearch.toast.servingsUpdatedDesc'), status: 'success', duration: 3000, isClosable: true });
    },
    onError: (error: unknown) => {
      toast({ title: t('recipeSearch.toast.servingsFailed'), description: error instanceof Error ? error.message : t('recipeSearch.toast.tryAgain'), status: 'error', duration: 4000, isClosable: true });
    },
  });

  const nutritionMutation = useMutation({
    mutationFn: (recipe: RecipeDto) => analyzeNutrition(recipe),
    onError: (error: unknown) => {
      toast({ title: t('recipeSearch.toast.nutritionFailed'), description: error instanceof Error ? error.message : t('recipeSearch.toast.tryAgain'), status: 'error', duration: 4000, isClosable: true });
    },
  });

  const handleAddIngredient = (ingredient: string) => {
    setIngredients((prev) => (prev.includes(ingredient) ? prev : [...prev, ingredient]));
  };
  const handleRemoveIngredient = (ingredient: string) => {
    setIngredients((prev) => prev.filter((item) => item !== ingredient));
  };
  const handleSearch = () => {
    if (ingredients.length === 0) {
      toast({ title: t('recipeSearch.toast.addIngredient'), status: 'warning', duration: 3000, isClosable: true });
      return;
    }
    searchMutation.mutate({ ingredients, restrictions });
  };
  const handleAnalyze = (recipe: RecipeDto) => {
    setSelectedRecipe(recipe);
    nutritionMutation.reset();
    nutritionModal.onOpen();
    nutritionMutation.mutate(recipe);
  };

  const TAG_TYPE_KEYS = {
    diet: t('recipeSearch.tagType.diet'),
    health: t('recipeSearch.tagType.health'),
    cuisine: t('recipeSearch.tagType.cuisine'),
  };

  const appliedTags = [
    ...restrictions.diet.map((v) => ({ type: 'diet' as const, label: TAG_TYPE_KEYS.diet, value: v })),
    ...restrictions.health.map((v) => ({ type: 'health' as const, label: TAG_TYPE_KEYS.health, value: v })),
    ...restrictions.cuisine.map((v) => ({ type: 'cuisine' as const, label: TAG_TYPE_KEYS.cuisine, value: v })),
  ];

  return (
    <>
      <Stack w="full" spacing={6}>

        {/* Page header */}
        <MotionBox initial={{ opacity: 0, y: -16 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.4 }}>
          <VStack align="flex-start" spacing={1}>
            <HStack spacing={2}>
              <Text fontSize="2xl">🔍</Text>
              <Heading
                fontSize={{ base: '2xl', md: '3xl' }}
                fontWeight={900}
                letterSpacing="-0.5px"
                bgGradient="linear(to-r, teal.600, teal.400)"
                bgClip="text"
              >
                {t('recipeSearch.pageTitle')}
              </Heading>
            </HStack>
            <Text color="gray.500" fontSize="sm" pl={9}>
              {t('recipeSearch.pageSubtitle', 'Add ingredients and find the perfect recipe')}
            </Text>
          </VStack>
        </MotionBox>

        {/* Ingredient manager */}
        <MotionBox initial={{ opacity: 0, y: 16 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.4, delay: 0.1 }}>
          <IngredientManager
            ingredients={ingredients}
            onAdd={handleAddIngredient}
            onRemove={handleRemoveIngredient}
          />
        </MotionBox>

        {/* Filter & Search bar */}
        <MotionBox initial={{ opacity: 0, y: 16 }} animate={{ opacity: 1, y: 0 }} transition={{ duration: 0.4, delay: 0.15 }}>
          <Box bg="white" borderRadius="2xl" p={5} shadow="md" border="1px solid" borderColor="teal.100">
            <Flex align="center" justify="space-between" flexWrap="wrap" gap={4}>
              {/* Active filter tags */}
              <HStack spacing={2} flexWrap="wrap" flex={1}>
                {appliedTags.length === 0 ? (
                  <HStack spacing={2}>
                    <Box w={2} h={2} borderRadius="full" bg="gray.300" />
                    <Text color="gray.400" fontSize="sm">{t('recipeSearch.noRestrictions')}</Text>
                  </HStack>
                ) : (
                  appliedTags.map((tag) => (
                    <Tag
                      key={`${tag.type}-${tag.value}`}
                      colorScheme={TAG_TYPE_COLORS[tag.type]}
                      variant="subtle"
                      borderRadius="full"
                      px={3}
                      py={1}
                      fontWeight={600}
                      fontSize="xs"
                    >
                      <TagLabel>{tag.label}: {tag.value}</TagLabel>
                      <TagCloseButton
                        onClick={() =>
                          setRestrictions((prev) => ({
                            ...prev,
                            [tag.type]: prev[tag.type].filter((v) => v !== tag.value),
                          }))
                        }
                      />
                    </Tag>
                  ))
                )}
              </HStack>

              {/* Action buttons */}
              <HStack spacing={3}>
                <Button
                  leftIcon={<Icon as={FaFilter} />}
                  variant="outline"
                  colorScheme="teal"
                  borderRadius="xl"
                  fontWeight={600}
                  fontSize="sm"
                  onClick={restrictionDrawer.onOpen}
                  position="relative"
                >
                  {t('recipeSearch.filtersButton')}
                  {appliedTags.length > 0 && (
                    <Box
                      position="absolute"
                      top="-6px"
                      right="-6px"
                      bg="teal.500"
                      color="white"
                      borderRadius="full"
                      w={5}
                      h={5}
                      fontSize="xs"
                      fontWeight={800}
                      display="flex"
                      alignItems="center"
                      justifyContent="center"
                    >
                      {appliedTags.length}
                    </Box>
                  )}
                </Button>
                <Button
                  colorScheme="teal"
                  borderRadius="xl"
                  fontWeight={700}
                  px={8}
                  onClick={handleSearch}
                  isLoading={searchMutation.isPending}
                  loadingText={t('recipeSearch.searching')}
                  leftIcon={<Text as="span">🔍</Text>}
                  _hover={{ transform: 'translateY(-1px)', shadow: 'md' }}
                  transition="all 0.2s"
                >
                  {t('recipeSearch.searchButton')}
                </Button>
              </HStack>
            </Flex>
          </Box>
        </MotionBox>

        {/* Results */}
        <RecipeResults
          recipes={recipes}
          isLoading={searchMutation.isPending}
          onAnalyze={handleAnalyze}
          onSave={(recipe) => saveMutation.mutate(recipe)}
          onAdjust={(recipe, servings) => adjustMutation.mutate({ recipe, servings })}
        />
      </Stack>

      <RestrictionDrawer
        isOpen={restrictionDrawer.isOpen}
        onClose={restrictionDrawer.onClose}
        restrictions={restrictions}
        onApply={(r) => setRestrictions(r)}
      />

      <NutritionModal
        isOpen={nutritionModal.isOpen}
        onClose={() => { nutritionModal.onClose(); setSelectedRecipe(undefined); }}
        recipeName={selectedRecipe?.title}
        nutritionInfo={nutritionMutation.data ?? []}
        isLoading={nutritionMutation.isPending}
        error={
          nutritionMutation.isError && nutritionMutation.error instanceof Error
            ? nutritionMutation.error.message
            : undefined
        }
      />
    </>
  );
};
