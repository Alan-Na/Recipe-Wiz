import {
  Box,
  Button,
  FormControl,
  FormLabel,
  HStack,
  Input,
  Tag,
  TagCloseButton,
  TagLabel,
  Wrap,
  WrapItem,
} from '@chakra-ui/react';
import { useState } from 'react';

interface IngredientManagerProps {
  ingredients: string[];
  onAdd: (ingredient: string) => void;
  onRemove: (ingredient: string) => void;
}

export const IngredientManager = ({ ingredients, onAdd, onRemove }: IngredientManagerProps) => {
  const [value, setValue] = useState('');

  const handleAdd = () => {
    const trimmed = value.trim();
    if (!trimmed) {
      return;
    }
    onAdd(trimmed);
    setValue('');
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === 'Enter') {
      event.preventDefault();
      handleAdd();
    }
  };

  return (
    <Box bg="white" borderRadius="xl" p={6} shadow="sm">
      <FormControl>
        <FormLabel fontWeight="semibold" color="teal.600">
          Ingredients
        </FormLabel>
        <HStack spacing={4}>
          <Input
            placeholder="e.g., chicken breast"
            value={value}
            onChange={(event) => setValue(event.target.value)}
            onKeyDown={handleKeyDown}
          />
          <Button colorScheme="teal" onClick={handleAdd}>
            Add
          </Button>
        </HStack>
      </FormControl>

      <Wrap spacing={3} mt={4}>
        {ingredients.map((ingredient) => (
          <WrapItem key={ingredient}>
            <Tag size="lg" colorScheme="teal" variant="subtle">
              <TagLabel>{ingredient}</TagLabel>
              <TagCloseButton onClick={() => onRemove(ingredient)} />
            </Tag>
          </WrapItem>
        ))}
      </Wrap>
    </Box>
  );
};
