import { useEffect, useState } from 'react';
import {
  Button,
  FormControl,
  FormLabel,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Select,
  Stack,
  Text,
} from '@chakra-ui/react';
import type { Dayjs } from 'dayjs';
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

export const AddMealModal = ({
  isOpen,
  onClose,
  recipe,
  weekDays,
  onSubmit,
  isSubmitting,
}: AddMealModalProps) => {
  const [selectedDate, setSelectedDate] = useState<string>(() => weekDays[0]?.format('YYYY-MM-DD') ?? '');
  const [mealType, setMealType] = useState<string>(MEAL_TYPES[0]);

  useEffect(() => {
    if (isOpen && weekDays.length > 0) {
      setSelectedDate(weekDays[0].format('YYYY-MM-DD'));
      setMealType(MEAL_TYPES[0]);
    }
  }, [isOpen, weekDays]);

  if (!recipe) {
    return null;
  }

  const handleSubmit = async () => {
    if (!selectedDate) {
      return;
    }
    await onSubmit({
      recipeId: recipe.recipeId,
      mealDate: selectedDate,
      mealType,
    });
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} isCentered>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Add to Meal Plan</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <Stack spacing={4}>
            <Text color="gray.600">Add <strong>{recipe.title}</strong> to your calendar.</Text>
            <FormControl>
              <FormLabel>Meal Date</FormLabel>
              <Select value={selectedDate} onChange={(event) => setSelectedDate(event.target.value)}>
                {weekDays.map((day) => (
                  <option key={day.format('YYYY-MM-DD')} value={day.format('YYYY-MM-DD')}>
                    {day.format('dddd, MMM D')}
                  </option>
                ))}
              </Select>
            </FormControl>
            <FormControl>
              <FormLabel>Meal Type</FormLabel>
              <Select value={mealType} onChange={(event) => setMealType(event.target.value)}>
                {MEAL_TYPES.map((type) => (
                  <option key={type} value={type}>
                    {type}
                  </option>
                ))}
              </Select>
            </FormControl>
          </Stack>
        </ModalBody>
        <ModalFooter>
          <Button variant="ghost" mr={3} onClick={onClose}>
            Cancel
          </Button>
          <Button colorScheme="teal" onClick={handleSubmit} isLoading={isSubmitting}>
            Add Meal
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};
