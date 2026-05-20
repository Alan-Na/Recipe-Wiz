import {
  Box, Button, Flex, Spinner, Stack, Text, VStack,
} from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';
import { motion, AnimatePresence } from 'framer-motion';
import type { RecipeDto } from '../../../types/api';

const MotionBox = motion(Box);

interface SavedRecipesPanelProps {
  recipes: RecipeDto[];
  isLoading: boolean;
  onAdd: (recipe: RecipeDto) => void;
}

const PANEL_GRADIENTS = [
  'linear(135deg, teal.400, teal.600)',
  'linear(135deg, orange.400, red.400)',
  'linear(135deg, purple.400, blue.500)',
  'linear(135deg, green.400, teal.500)',
  'linear(135deg, pink.400, purple.500)',
];
const cardGradient = (title: string) =>
  PANEL_GRADIENTS[title.charCodeAt(0) % PANEL_GRADIENTS.length];

export const SavedRecipesPanel = ({ recipes, isLoading, onAdd }: SavedRecipesPanelProps) => {
  const { t } = useTranslation();

  return (
    <Box
      bg="white"
      borderRadius="2xl"
      shadow="md"
      border="1px solid"
      borderColor="orange.100"
      minW={{ base: 'full', lg: '300px' }}
      maxW={{ base: 'full', lg: '340px' }}
      overflow="hidden"
      display="flex"
      flexDirection="column"
    >
      {/* Header */}
      <Box bgGradient="linear(135deg, orange.400, orange.300)" px={5} pt={5} pb={4}>
        <Text fontSize="xl" mb={1}>💾</Text>
        <Text fontWeight={800} color="white" fontSize="md">
          {t('savedRecipesPanel.title')}
        </Text>
        <Text color="whiteAlpha.800" fontSize="xs" mt={0.5}>
          {t('savedRecipesPanel.subtitle')}
        </Text>
      </Box>

      {/* Body */}
      <Box p={4} flex={1} overflowY="auto" maxH="600px">
        {isLoading ? (
          <Flex justify="center" align="center" py={10} direction="column" gap={3}>
            <Spinner size="lg" color="orange.400" thickness="3px" />
            <Text color="gray.400" fontSize="sm">Loading saved recipes…</Text>
          </Flex>
        ) : recipes.length === 0 ? (
          <VStack spacing={2} py={8}>
            <Text fontSize="3xl">📭</Text>
            <Text color="gray.400" fontSize="sm" textAlign="center">
              {t('savedRecipesPanel.empty')}
            </Text>
          </VStack>
        ) : (
          <Stack spacing={3}>
            <AnimatePresence>
              {recipes.map((recipe, i) => (
                <MotionBox
                  key={recipe.recipeId}
                  initial={{ opacity: 0, x: 20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ duration: 0.3, delay: i * 0.06 }}
                >
                  <Box
                    borderRadius="xl"
                    overflow="hidden"
                    border="1px solid"
                    borderColor="gray.100"
                    _hover={{ shadow: 'md', transform: 'translateY(-2px)' }}
                    transition="all 0.2s"
                  >
                    {/* Mini gradient accent */}
                    <Box h="3px" bgGradient={cardGradient(recipe.title)} />
                    <Box p={3}>
                      <Text fontWeight={700} fontSize="sm" color="gray.800" noOfLines={1} mb={0.5}>
                        {recipe.title}
                      </Text>
                      <Text fontSize="xs" color="gray.400" noOfLines={2} lineHeight={1.5} mb={3}>
                        {recipe.description}
                      </Text>
                      <Button
                        size="sm"
                        colorScheme="orange"
                        variant="outline"
                        borderRadius="lg"
                        fontWeight={700}
                        onClick={() => onAdd(recipe)}
                        w="full"
                        leftIcon={<Text as="span" fontSize="xs">📅</Text>}
                        _hover={{ bg: 'orange.50', transform: 'translateY(-1px)' }}
                        transition="all 0.2s"
                      >
                        {t('savedRecipesPanel.addToCalendar')}
                      </Button>
                    </Box>
                  </Box>
                </MotionBox>
              ))}
            </AnimatePresence>
          </Stack>
        )}
      </Box>
    </Box>
  );
};
