import {
  Box,
  Button,
  Divider,
  Heading,
  List,
  ListItem,
  Spinner,
  Stack,
  Text,
} from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';
import type { RecipeDto } from '../../../types/api';

interface SavedRecipesPanelProps {
  recipes: RecipeDto[];
  isLoading: boolean;
  onAdd: (recipe: RecipeDto) => void;
}

export const SavedRecipesPanel = ({ recipes, isLoading, onAdd }: SavedRecipesPanelProps) => {
  const { t } = useTranslation();

  return (
    <Box
      bg="white"
      borderRadius="xl"
      p={6}
      shadow="sm"
      border="1px solid"
      borderColor="teal.50"
      minW={{ base: 'full', lg: '320px' }}
    >
      <Heading size="md" color="teal.700">
        {t('savedRecipesPanel.title')}
      </Heading>
      <Text fontSize="sm" color="gray.500" mt={2} mb={4}>
        {t('savedRecipesPanel.subtitle')}
      </Text>
      <Divider mb={4} />
      {isLoading ? (
        <Stack align="center" py={10}>
          <Spinner size="lg" color="teal.500" />
        </Stack>
      ) : recipes.length === 0 ? (
        <Text color="gray.600">{t('savedRecipesPanel.empty')}</Text>
      ) : (
        <List spacing={3}>
          {recipes.map((recipe) => (
            <ListItem key={recipe.recipeId}>
              <Box
                border="1px solid"
                borderColor="teal.100"
                borderRadius="lg"
                p={3}
                bg="teal.50"
              >
                <Text fontWeight="semibold" color="teal.700">
                  {recipe.title}
                </Text>
                <Text fontSize="sm" color="gray.600" noOfLines={2} mt={1}>
                  {recipe.description}
                </Text>
                <Button
                  size="sm"
                  colorScheme="teal"
                  mt={3}
                  onClick={() => onAdd(recipe)}
                  width="full"
                >
                  {t('savedRecipesPanel.addToCalendar')}
                </Button>
              </Box>
            </ListItem>
          ))}
        </List>
      )}
    </Box>
  );
};
