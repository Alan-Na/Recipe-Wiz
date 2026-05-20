import {
  Badge, Box, Button, Collapse, Flex, Heading, HStack,
  NumberInput, NumberInputField, Stack, Text, VStack,
} from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { motion, AnimatePresence } from 'framer-motion';
import type { RecipeDto } from '../../../types/api';

const MotionBox = motion(Box);

const CARD_GRADIENTS = [
  'linear(135deg, teal.400, teal.600)',
  'linear(135deg, orange.400, red.400)',
  'linear(135deg, purple.400, blue.500)',
  'linear(135deg, green.400, teal.500)',
  'linear(135deg, pink.400, purple.500)',
  'linear(135deg, yellow.400, orange.500)',
];
const cardGradient = (title: string) =>
  CARD_GRADIENTS[title.charCodeAt(0) % CARD_GRADIENTS.length];

const MEAL_TYPE_MAX: Record<string, number> = {
  calories: 2000, protein: 50, fat: 65, carbohydrates: 300,
};

const NutritionBar = ({ label, value, max, color }: {
  label: string; value: number; max: number; color: string;
}) => {
  const pct = Math.min(100, (value / max) * 100);
  return (
    <Box>
      <Flex justify="space-between" mb={1}>
        <Text fontSize="xs" color="gray.500">{label}</Text>
        <Text fontSize="xs" fontWeight={700} color="gray.700">{Math.round(value)}</Text>
      </Flex>
      <Box bg="gray.100" borderRadius="full" h="5px" overflow="hidden">
        <MotionBox
          initial={{ width: 0 }}
          animate={{ width: `${pct}%` }}
          transition={{ duration: 0.8, ease: 'easeOut' }}
          h="full"
          bgGradient={`linear(to-r, ${color}.300, ${color}.500)`}
          borderRadius="full"
        />
      </Box>
    </Box>
  );
};

const ServingAdjustField = ({ defaultValue, onSubmit }: {
  defaultValue: number; onSubmit: (v: number) => void;
}) => {
  const [value, setValue] = useState(String(defaultValue));
  const { t } = useTranslation();
  useEffect(() => { setValue(String(defaultValue)); }, [defaultValue]);
  return (
    <HStack spacing={2}>
      <NumberInput value={value} onChange={(v) => setValue(v)} min={1} maxW="80px" size="sm">
        <NumberInputField borderRadius="lg" borderColor="gray.200" />
      </NumberInput>
      <Button
        size="sm"
        variant="ghost"
        colorScheme="teal"
        borderRadius="lg"
        fontWeight={600}
        onClick={() => {
          const p = Number(value);
          onSubmit(Number.isFinite(p) && p > 0 ? p : defaultValue);
        }}
      >
        {t('recipeResults.updateServings')}
      </Button>
    </HStack>
  );
};

interface RecipeResultsProps {
  recipes: RecipeDto[];
  isLoading: boolean;
  onAnalyze: (r: RecipeDto) => void;
  onSave: (r: RecipeDto) => void;
  onAdjust: (r: RecipeDto, servings: number) => void;
}

export const RecipeResults = ({ recipes, isLoading, onAnalyze, onSave, onAdjust }: RecipeResultsProps) => {
  const { t } = useTranslation();
  const [expanded, setExpanded] = useState<Record<number, boolean>>({});

  if (isLoading) {
    return (
      <Stack spacing={4}>
        {[1, 2, 3].map((i) => (
          <Box key={i} bg="white" borderRadius="2xl" overflow="hidden" shadow="md">
            <Box h="80px" bgGradient="linear(135deg, gray.200, gray.300)" />
            <Box p={6}>
              <Box h={4} bg="gray.200" borderRadius="full" mb={3} w="60%" />
              <Box h={3} bg="gray.100" borderRadius="full" mb={2} />
              <Box h={3} bg="gray.100" borderRadius="full" w="80%" />
            </Box>
          </Box>
        ))}
      </Stack>
    );
  }

  if (recipes.length === 0) {
    return (
      <Box
        bg="white"
        borderRadius="2xl"
        p={14}
        textAlign="center"
        shadow="md"
        border="2px dashed"
        borderColor="teal.200"
      >
        <Text fontSize="5xl" mb={4}>🍽️</Text>
        <Heading fontSize="xl" color="gray.600" mb={2} fontWeight={700}>
          {t('recipeResults.emptyHint').split('.')[0]}
        </Heading>
        <Text color="gray.400" fontSize="sm" maxW="sm" mx="auto">
          {t('recipeResults.emptyHint')}
        </Text>
      </Box>
    );
  }

  const toggleExpand = (id: number) =>
    setExpanded((prev) => ({ ...prev, [id]: !prev[id] }));

  return (
    <Stack spacing={5}>
      <AnimatePresence>
        {recipes.map((recipe, i) => (
          <MotionBox
            key={recipe.recipeId}
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -20 }}
            transition={{ duration: 0.4, delay: i * 0.07 }}
          >
            <Box
              bg="white"
              borderRadius="2xl"
              overflow="hidden"
              shadow="md"
              border="1px solid"
              borderColor="gray.100"
              _hover={{ shadow: 'xl' }}
              transition="shadow 0.25s"
            >
              {/* Gradient header */}
              <Box bgGradient={cardGradient(recipe.title)} px={6} py={5} position="relative">
                <HStack justify="space-between" align="flex-start">
                  <VStack align="flex-start" spacing={1}>
                    <Heading fontSize="lg" color="white" fontWeight={800} letterSpacing="-0.3px">
                      {recipe.title}
                    </Heading>
                    <Text fontSize="xs" color="whiteAlpha.800" noOfLines={1}>
                      {recipe.description}
                    </Text>
                  </VStack>
                  <Badge
                    bg="whiteAlpha.300"
                    color="white"
                    borderRadius="full"
                    px={3}
                    py={1}
                    fontSize="xs"
                    fontWeight={700}
                    flexShrink={0}
                  >
                    {t('recipeResults.servings', { count: recipe.servings })}
                  </Badge>
                </HStack>
              </Box>

              <Box px={6} pt={5} pb={6}>
                {/* Nutrition mini bars */}
                {(recipe.nutrition.calories > 0 || recipe.nutrition.protein > 0) && (
                  <Box mb={5} p={4} bg="#fffaf5" borderRadius="xl">
                    <Text fontSize="xs" fontWeight={800} color="gray.500" textTransform="uppercase" letterSpacing="wider" mb={3}>
                      📊 {t('recipeResults.nutritionOverview')}
                    </Text>
                    <Stack spacing={2}>
                      <NutritionBar label={`Calories (kcal)`} value={recipe.nutrition.calories} max={MEAL_TYPE_MAX.calories} color="orange" />
                      <NutritionBar label={`Protein (g)`} value={recipe.nutrition.protein} max={MEAL_TYPE_MAX.protein} color="blue" />
                      <NutritionBar label={`Fat (g)`} value={recipe.nutrition.fat} max={MEAL_TYPE_MAX.fat} color="pink" />
                      <NutritionBar label={`Carbs (g)`} value={recipe.nutrition.carbohydrates} max={MEAL_TYPE_MAX.carbohydrates} color="green" />
                    </Stack>
                  </Box>
                )}

                {/* Ingredients */}
                <Box mb={4}>
                  <Button
                    variant="ghost"
                    size="sm"
                    fontWeight={700}
                    color="teal.600"
                    px={0}
                    mb={2}
                    _hover={{ bg: 'transparent', color: 'teal.800' }}
                    onClick={() => toggleExpand(recipe.recipeId)}
                    rightIcon={<Text as="span">{expanded[recipe.recipeId] ? '▲' : '▼'}</Text>}
                  >
                    🥦 {t('recipeResults.ingredientsLabel')}
                  </Button>
                  <Collapse in={!!expanded[recipe.recipeId]} animateOpacity>
                    <Box bg="gray.50" borderRadius="xl" p={4}>
                      <Text color="gray.600" fontSize="sm" lineHeight={1.8}>
                        {recipe.ingredientLines.join(' · ')}
                      </Text>
                    </Box>
                  </Collapse>
                </Box>

                {/* Instructions link */}
                <Box mb={5}>
                  <Text fontSize="xs" fontWeight={700} color="gray.400" textTransform="uppercase" letterSpacing="wider" mb={1}>
                    📖 {t('recipeResults.instructionsLabel')}
                  </Text>
                  <Text
                    as="a"
                    href={recipe.instructions}
                    target="_blank"
                    rel="noopener noreferrer"
                    color="teal.500"
                    fontSize="sm"
                    fontWeight={600}
                    _hover={{ color: 'teal.700', textDecoration: 'underline' }}
                  >
                    {recipe.description} ↗
                  </Text>
                </Box>

                {/* Actions */}
                <Flex justify="space-between" align="center" flexWrap="wrap" gap={3}>
                  <HStack spacing={2} flexWrap="wrap">
                    <Button
                      colorScheme="teal"
                      size="sm"
                      borderRadius="xl"
                      fontWeight={700}
                      leftIcon={<Text as="span">📊</Text>}
                      onClick={() => onAnalyze(recipe)}
                      _hover={{ transform: 'translateY(-1px)', shadow: 'md' }}
                      transition="all 0.2s"
                    >
                      {t('recipeResults.analyzeNutrition')}
                    </Button>
                    <Button
                      variant="outline"
                      colorScheme="orange"
                      size="sm"
                      borderRadius="xl"
                      fontWeight={700}
                      leftIcon={<Text as="span">💾</Text>}
                      onClick={() => onSave(recipe)}
                      _hover={{ transform: 'translateY(-1px)', shadow: 'md' }}
                      transition="all 0.2s"
                    >
                      {t('recipeResults.saveRecipe')}
                    </Button>
                  </HStack>
                  <ServingAdjustField
                    defaultValue={recipe.servings}
                    onSubmit={(servings) => onAdjust(recipe, servings)}
                  />
                </Flex>
              </Box>
            </Box>
          </MotionBox>
        ))}
      </AnimatePresence>
    </Stack>
  );
};
