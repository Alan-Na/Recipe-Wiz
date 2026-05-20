import { useEffect, useState } from 'react';
import {
  Box, Button, Flex, FormControl, FormLabel, Modal, ModalBody,
  ModalCloseButton, ModalContent, ModalFooter, ModalHeader,
  ModalOverlay, Select, Stack, Text,
} from '@chakra-ui/react';
import type { Dayjs } from 'dayjs';
import { useTranslation } from 'react-i18next';
import { MEAL_TYPES } from '../utils';
import type { RecipeDto } from '../../../types/api';

interface AddMealModalProps {
  isOpen: boolean;
  onClose: () => void;
  recipe: RecipeDto | null;
  weekDays: Dayjs[];
  onSubmit: (payload: { recipeId: number; mealDate: string; mealType: string }) => Promise<void>;
  isSubmitting: boolean;
}

const MEAL_TYPE_EMOJIS: Record<string, string> = {
  breakfast: '🌅',
  lunch: '☀️',
  dinner: '🌙',
  snack: '🍎',
};

export const AddMealModal = ({
  isOpen, onClose, recipe, weekDays, onSubmit, isSubmitting,
}: AddMealModalProps) => {
  const [selectedDate, setSelectedDate] = useState<string>(() => weekDays[0]?.format('YYYY-MM-DD') ?? '');
  const [mealType, setMealType] = useState<string>(MEAL_TYPES[0]);
  const { t, i18n } = useTranslation();
  const isZh = i18n.language === 'zh';

  useEffect(() => {
    if (isOpen && weekDays.length > 0) {
      setSelectedDate(weekDays[0].format('YYYY-MM-DD'));
      setMealType(MEAL_TYPES[0]);
    }
  }, [isOpen, weekDays]);

  if (!recipe) return null;

  const handleSubmit = async () => {
    if (!selectedDate) return;
    await onSubmit({ recipeId: recipe.recipeId, mealDate: selectedDate, mealType });
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} isCentered size="md">
      <ModalOverlay bg="blackAlpha.500" backdropFilter="blur(6px)" />
      <ModalContent borderRadius="2xl" overflow="hidden" shadow="2xl">
        {/* Gradient header */}
        <ModalHeader p={0}>
          <Box bgGradient="linear(135deg, orange.400, orange.300)" px={6} pt={6} pb={5}>
            <ModalCloseButton color="white" top={4} right={4} />
            <Text fontSize="2xl" mb={1}>📅</Text>
            <Text fontWeight={800} color="white" fontSize="lg">
              {t('addMealModal.title')}
            </Text>
            <Text color="whiteAlpha.800" fontSize="sm" mt={1} noOfLines={1}>
              {recipe.title}
            </Text>
          </Box>
        </ModalHeader>

        <ModalBody p={5}>
          <Stack spacing={5}>
            {/* Recipe preview */}
            <Box bg="#fffaf5" borderRadius="xl" p={4} border="1px solid" borderColor="orange.100">
              <Flex gap={3} align="flex-start">
                <Text fontSize="2xl">🍽️</Text>
                <Box>
                  <Text fontWeight={700} fontSize="sm" color="gray.800" mb={0.5}>
                    {recipe.title}
                  </Text>
                  <Text fontSize="xs" color="gray.500" noOfLines={2}>
                    {recipe.description}
                  </Text>
                </Box>
              </Flex>
            </Box>

            {/* Date picker */}
            <FormControl>
              <FormLabel fontWeight={700} fontSize="sm" color="gray.600" mb={2}>
                📆 {t('addMealModal.mealDate')}
              </FormLabel>
              <Select
                value={selectedDate}
                onChange={(e) => setSelectedDate(e.target.value)}
                borderRadius="xl"
                borderColor="gray.200"
                fontWeight={500}
                _focus={{ borderColor: 'orange.400', boxShadow: '0 0 0 3px rgba(251,146,60,0.15)' }}
              >
                {weekDays.map((day) => (
                  <option key={day.format('YYYY-MM-DD')} value={day.format('YYYY-MM-DD')}>
                    {isZh ? day.format('M月D日 dddd') : day.format('dddd, MMM D')}
                  </option>
                ))}
              </Select>
            </FormControl>

            {/* Meal type */}
            <FormControl>
              <FormLabel fontWeight={700} fontSize="sm" color="gray.600" mb={2}>
                🍴 {t('addMealModal.mealType')}
              </FormLabel>
              <Select
                value={mealType}
                onChange={(e) => setMealType(e.target.value)}
                borderRadius="xl"
                borderColor="gray.200"
                fontWeight={500}
                _focus={{ borderColor: 'orange.400', boxShadow: '0 0 0 3px rgba(251,146,60,0.15)' }}
              >
                {MEAL_TYPES.map((type) => (
                  <option key={type} value={type}>
                    {MEAL_TYPE_EMOJIS[type] ?? '🍽️'} {t(`mealTypes.${type}`, type)}
                  </option>
                ))}
              </Select>
            </FormControl>
          </Stack>
        </ModalBody>

        <ModalFooter borderTop="1px solid" borderColor="gray.100" py={4} gap={3}>
          <Button
            variant="ghost"
            borderRadius="xl"
            fontWeight={600}
            onClick={onClose}
            flex={1}
          >
            {t('addMealModal.cancel')}
          </Button>
          <Button
            colorScheme="orange"
            borderRadius="xl"
            fontWeight={700}
            onClick={handleSubmit}
            isLoading={isSubmitting}
            flex={1}
            leftIcon={<Text as="span">📅</Text>}
            _hover={{ transform: 'translateY(-1px)', shadow: 'md' }}
            transition="all 0.2s"
          >
            {t('addMealModal.addMeal')}
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};
