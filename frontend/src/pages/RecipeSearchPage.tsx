import {
  Button,
  Flex,
  Heading,
  HStack,
  Icon,
  Stack,
  Tag,
  TagCloseButton,
  TagLabel,
  useDisclosure,
  useToast,
} from '@chakra-ui/react';
import { useMutation } from '@tanstack/react-query';
import { useState } from 'react';
import { FaFilter } from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import { IngredientManager } from '../features/recipe-search/components/IngredientManager';
import { RestrictionDrawer } from '../features/recipe-search/components/RestrictionDrawer';
import { RecipeResults } from '../features/recipe-search/components/RecipeResults';
import { NutritionModal } from '../features/recipe-search/components/NutritionModal';
import type { SearchRestrictions } from '../features/recipe-search/types';
import { adjustServings, saveRecipe, searchRecipes, searchRecipesWithRestrictions } from '../api/recipes';
import type { RecipeDto } from '../types/api';
import { analyzeNutrition } from '../api/nutrition';

const DEFAULT_RESTRICTIONS: SearchRestrictions = {
  diet: [],
  health: [],
  cuisine: [],
};

const USER_ID = 1;

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
      const hasRestrictions =
        rest.diet.length > 0 || rest.health.length > 0 || rest.cuisine.length > 0;

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
        toast({
          title: t('recipeSearch.toast.notFound'),
          description: t('recipeSearch.toast.notFoundDesc'),
          status: 'info',
          duration: 4000,
          isClosable: true,
        });
      }
    },
    onError: (error: unknown) => {
      toast({
        title: t('recipeSearch.toast.searchFailed'),
        description: error instanceof Error ? error.message : t('recipeSearch.toast.tryAgain'),
        status: 'error',
        duration: 4000,
        isClosable: true,
      });
    },
  });

  const saveMutation = useMutation({
    mutationFn: (recipe: RecipeDto) => saveRecipe(USER_ID, recipe),
    onSuccess: () => {
      toast({
        title: t('recipeSearch.toast.saved'),
        description: t('recipeSearch.toast.savedDesc'),
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
    },
    onError: (error: unknown) => {
      toast({
        title: t('recipeSearch.toast.saveFailed'),
        description: error instanceof Error ? error.message : t('recipeSearch.toast.tryAgain'),
        status: 'error',
        duration: 4000,
        isClosable: true,
      });
    },
  });

  const adjustMutation = useMutation({
    mutationFn: async ({ recipe, servings }: { recipe: RecipeDto; servings: number }) => {
      const updated = await adjustServings({ newServings: servings, recipes: [recipe] });
      return updated[0];
    },
    onSuccess: (updatedRecipe) => {
      setRecipes((prev) =>
        prev.map((recipe) => (recipe.recipeId === updatedRecipe.recipeId ? updatedRecipe : recipe)),
      );
      toast({
        title: t('recipeSearch.toast.servingsUpdated'),
        description: t('recipeSearch.toast.servingsUpdatedDesc'),
        status: 'success',
        duration: 3000,
        isClosable: true,
      });
    },
    onError: (error: unknown) => {
      toast({
        title: t('recipeSearch.toast.servingsFailed'),
        description: error instanceof Error ? error.message : t('recipeSearch.toast.tryAgain'),
        status: 'error',
        duration: 4000,
        isClosable: true,
      });
    },
  });

  const nutritionMutation = useMutation({
    mutationFn: (recipe: RecipeDto) => analyzeNutrition(recipe),
    onError: (error: unknown) => {
      toast({
        title: t('recipeSearch.toast.nutritionFailed'),
        description: error instanceof Error ? error.message : t('recipeSearch.toast.tryAgain'),
        status: 'error',
        duration: 4000,
        isClosable: true,
      });
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
      toast({
        title: t('recipeSearch.toast.addIngredient'),
        status: 'warning',
        duration: 3000,
        isClosable: true,
      });
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

  const appliedRestrictionTags = [
    ...restrictions.diet.map((item) => ({ type: 'diet' as const, label: TAG_TYPE_KEYS.diet, value: item })),
    ...restrictions.health.map((item) => ({ type: 'health' as const, label: TAG_TYPE_KEYS.health, value: item })),
    ...restrictions.cuisine.map((item) => ({ type: 'cuisine' as const, label: TAG_TYPE_KEYS.cuisine, value: item })),
  ];

  return (
    <>
      <Stack w="full" spacing={6}>
        <Heading color="teal.700">{t('recipeSearch.pageTitle')}</Heading>

        <IngredientManager
          ingredients={ingredients}
          onAdd={handleAddIngredient}
          onRemove={handleRemoveIngredient}
        />

        <Flex
          bg="white"
          borderRadius="xl"
          p={6}
          align="center"
          justify="space-between"
          shadow="sm"
          wrap="wrap"
          gap={4}
        >
          <HStack spacing={3} flexWrap="wrap">
            {appliedRestrictionTags.length === 0 ? (
              <Tag colorScheme="gray" variant="subtle">
                {t('recipeSearch.noRestrictions')}
              </Tag>
            ) : (
              appliedRestrictionTags.map((tag) => (
                <Tag key={`${tag.type}-${tag.value}`} colorScheme="teal" variant="subtle">
                  <TagLabel>
                    {tag.label}: {tag.value}
                  </TagLabel>
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

          <HStack spacing={3}>
            <Button
              leftIcon={<Icon as={FaFilter} />}
              variant="outline"
              onClick={restrictionDrawer.onOpen}
            >
              {t('recipeSearch.filtersButton')}
            </Button>
            <Button
              colorScheme="teal"
              onClick={handleSearch}
              isLoading={searchMutation.isPending}
              loadingText={t('recipeSearch.searching')}
            >
              {t('recipeSearch.searchButton')}
            </Button>
          </HStack>
        </Flex>

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
        onApply={(updatedRestrictions) => setRestrictions(updatedRestrictions)}
      />

      <NutritionModal
        isOpen={nutritionModal.isOpen}
        onClose={() => {
          nutritionModal.onClose();
          setSelectedRecipe(undefined);
        }}
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
