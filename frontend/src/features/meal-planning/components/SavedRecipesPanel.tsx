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
import type { RecipeDto } from '../../../types/api';

interface SavedRecipesPanelProps {
  recipes: RecipeDto[];
  isLoading: boolean;
  onAdd: (recipe: RecipeDto) => void;
}

export const SavedRecipesPanel = ({ recipes, isLoading, onAdd }: SavedRecipesPanelProps) => {
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
        Saved Recipes
      </Heading>
      <Text fontSize="sm" color="gray.500" mt={2} mb={4}>
        Select a recipe to add it to the weekly calendar.
      </Text>
      <Divider mb={4} />
      {isLoading ? (
        <Stack align="center" py={10}>
          <Spinner size="lg" color="teal.500" />
        </Stack>
      ) : recipes.length === 0 ? (
        <Text color="gray.600">Save recipes from the search page to plan your week.</Text>
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
                  Add to Calendar
                </Button>
              </Box>
            </ListItem>
          ))}
        </List>
      )}
    </Box>
  );
};
