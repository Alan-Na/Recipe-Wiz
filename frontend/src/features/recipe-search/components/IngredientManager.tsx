import {
  Box, Button, Flex, HStack, Input, InputGroup, InputLeftElement,
  Tag, TagCloseButton, TagLabel, Text, Wrap, WrapItem,
} from '@chakra-ui/react';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { motion, AnimatePresence } from 'framer-motion';

const MotionBox = motion(Box);

interface IngredientManagerProps {
  ingredients: string[];
  onAdd: (ingredient: string) => void;
  onRemove: (ingredient: string) => void;
}

const TAG_COLORS = ['teal', 'green', 'blue', 'purple', 'pink', 'orange'];
const tagColor = (s: string) => TAG_COLORS[s.charCodeAt(0) % TAG_COLORS.length];

export const IngredientManager = ({ ingredients, onAdd, onRemove }: IngredientManagerProps) => {
  const [value, setValue] = useState('');
  const { t } = useTranslation();

  const handleAdd = () => {
    const trimmed = value.trim();
    if (!trimmed) return;
    onAdd(trimmed);
    setValue('');
  };

  return (
    <Box bg="white" borderRadius="2xl" p={6} shadow="md" border="1px solid" borderColor="orange.100">
      <Text fontWeight={700} color="gray.700" mb={4} fontSize="sm" textTransform="uppercase" letterSpacing="wider">
        🥕 {t('ingredientManager.label')}
      </Text>

      <HStack spacing={3}>
        <InputGroup>
          <InputLeftElement pointerEvents="none" color="gray.400" fontSize="lg">
            🔎
          </InputLeftElement>
          <Input
            pl={10}
            placeholder={t('ingredientManager.placeholder')}
            value={value}
            onChange={(e) => setValue(e.target.value)}
            onKeyDown={(e) => { if (e.key === 'Enter') { e.preventDefault(); handleAdd(); } }}
            borderRadius="xl"
            borderColor="gray.200"
            _focus={{ borderColor: 'teal.400', boxShadow: '0 0 0 3px rgba(44,122,123,0.15)' }}
            bg="#fffaf5"
          />
        </InputGroup>
        <Button
          colorScheme="teal"
          borderRadius="xl"
          px={6}
          fontWeight={700}
          onClick={handleAdd}
          _hover={{ transform: 'translateY(-1px)', shadow: 'md' }}
          transition="all 0.2s"
        >
          {t('ingredientManager.addButton')}
        </Button>
      </HStack>

      <AnimatePresence>
        {ingredients.length > 0 && (
          <MotionBox
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            exit={{ opacity: 0, height: 0 }}
            mt={4}
          >
            <Wrap spacing={2}>
              <AnimatePresence>
                {ingredients.map((ing) => (
                  <WrapItem key={ing}>
                    <MotionBox
                      initial={{ opacity: 0, scale: 0.7 }}
                      animate={{ opacity: 1, scale: 1 }}
                      exit={{ opacity: 0, scale: 0.7 }}
                      transition={{ type: 'spring', stiffness: 400, damping: 25 }}
                    >
                      <Tag
                        size="lg"
                        colorScheme={tagColor(ing)}
                        variant="subtle"
                        borderRadius="full"
                        px={4}
                        py={2}
                        fontWeight={600}
                      >
                        <TagLabel>{ing}</TagLabel>
                        <TagCloseButton onClick={() => onRemove(ing)} />
                      </Tag>
                    </MotionBox>
                  </WrapItem>
                ))}
              </AnimatePresence>
            </Wrap>
          </MotionBox>
        )}
      </AnimatePresence>

      {ingredients.length === 0 && (
        <Flex align="center" justify="center" mt={4} py={3} borderRadius="xl" bg="gray.50">
          <Text color="gray.400" fontSize="sm">
            {t('ingredientManager.placeholder')} ↑
          </Text>
        </Flex>
      )}
    </Box>
  );
};
