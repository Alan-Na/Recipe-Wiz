import {
  Badge,
  Box,
  Button,
  Card,
  CardBody,
  CardFooter,
  CardHeader,
  Heading,
  HStack,
  NumberInput,
  NumberInputField,
  Stack,
  Text,
  VStack,
} from '@chakra-ui/react';
import { useEffect, useMemo, useState } from 'react';
import type { RecipeDto } from '../../../types/api';

interface RecipeResultsProps {
  recipes: RecipeDto[];
  isLoading: boolean;
  onAnalyze: (recipe: RecipeDto) => void;
  onSave: (recipe: RecipeDto) => void;
  onAdjust: (recipe: RecipeDto, servings: number) => void;
}

const ServingAdjustField = ({ defaultValue, onSubmit }: { defaultValue: number; onSubmit: (value: number) => void }) => {
  const [value, setValue] = useState(String(defaultValue));
  useEffect(() => {
    setValue(String(defaultValue));
  }, [defaultValue]);

  return (
    <HStack spacing={2}>
      <NumberInput value={value} onChange={(val) => setValue(val)} min={1} maxW="90px">
        <NumberInputField />
      </NumberInput>
      <Button
        size="sm"
        onClick={() => {
          const parsed = Number(value);
          onSubmit(Number.isFinite(parsed) && parsed > 0 ? parsed : defaultValue);
        }}
      >
        Update Servings
      </Button>
    </HStack>
  );
};

export const RecipeResults = ({
  recipes,
  isLoading,
  onAnalyze,
  onSave,
  onAdjust,
}: RecipeResultsProps) => {
  const highlightText = useMemo(
    () => (text: string) => text.replace(/([A-Z])/g, ' $1').trim(),
    [],
  );

  if (isLoading) {
    return (
      <Box bg="white" borderRadius="xl" p={8} textAlign="center" shadow="sm">
        <Text color="teal.600" fontWeight="medium">
          Searching for delicious recipes...
        </Text>
      </Box>
    );
  }

  if (recipes.length === 0) {
    return (
      <Box bg="white" borderRadius="xl" p={8} textAlign="center" shadow="sm">
        <Text color="gray.600">
          Add ingredients and press search to discover matching recipes. Apply restrictions to tailor results
          to your needs.
        </Text>
      </Box>
    );
  }

  return (
    <Stack spacing={4}>
      {recipes.map((recipe) => (
        <Card key={recipe.recipeId} variant="outline" shadow="sm">
          <CardHeader>
            <HStack justify="space-between" align="flex-start">
              <Heading size="md" color="teal.700">
                {recipe.title}
              </Heading>
              <Badge colorScheme="teal" fontSize="0.8em">
                Servings: {recipe.servings}
              </Badge>
            </HStack>
            <Text mt={2} color="gray.600">
              {recipe.description}
            </Text>
          </CardHeader>
          <CardBody pt={0}>
            <VStack align="stretch" spacing={3}>
              <Box>
                <Text fontWeight="semibold" color="teal.600">
                  Ingredients
                </Text>
                <Text color="gray.600" mt={1}>
                  {recipe.ingredientLines.join(' • ')}
                </Text>
              </Box>
              <Box>
                <Text fontWeight="semibold" color="teal.600">
                  Instructions
                </Text>
                <Text color="gray.600" mt={1}>
                  {recipe.instructions}
                </Text>
              </Box>
              <Box>
                <Text fontWeight="semibold" color="teal.600">
                  Nutrition Overview
                </Text>
                <Text color="gray.600" mt={1}>
                  {highlightText(`Calories ${recipe.nutrition.calories.toFixed(0)} kcal, Protein ${
                    recipe.nutrition.protein
                  } g, Fat ${recipe.nutrition.fat} g, Carbs ${
                    recipe.nutrition.carbohydrates
                  } g`)}
                </Text>
              </Box>
            </VStack>
          </CardBody>
          <CardFooter justify="space-between" flexWrap="wrap" gap={4}>
            <HStack spacing={3}>
              <Button colorScheme="teal" variant="solid" onClick={() => onAnalyze(recipe)}>
                Analyze Nutrition
              </Button>
              <Button variant="outline" colorScheme="teal" onClick={() => onSave(recipe)}>
                Save Recipe
              </Button>
            </HStack>
            <ServingAdjustField
              defaultValue={recipe.servings}
              onSubmit={(servings) => onAdjust(recipe, servings)}
            />
          </CardFooter>
        </Card>
      ))}
    </Stack>
  );
};
