import {
  Box,
  List,
  ListIcon,
  ListItem,
  Modal,
  ModalBody,
  ModalCloseButton,
  ModalContent,
  ModalFooter,
  ModalHeader,
  ModalOverlay,
  Spinner,
  Text,
  Button,
} from '@chakra-ui/react';
import { CheckCircleIcon } from '@chakra-ui/icons';

interface NutritionModalProps {
  isOpen: boolean;
  onClose: () => void;
  recipeName?: string;
  nutritionInfo: string[];
  isLoading: boolean;
  error?: string | null;
}

export const NutritionModal = ({
  isOpen,
  onClose,
  recipeName,
  nutritionInfo,
  isLoading,
  error,
}: NutritionModalProps) => {
  return (
    <Modal isOpen={isOpen} onClose={onClose} size="lg" isCentered>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Nutrition Analysis {recipeName ? `- ${recipeName}` : ''}</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          {isLoading ? (
            <Box display="flex" justifyContent="center" py={12}>
              <Spinner size="lg" color="teal.500" />
            </Box>
          ) : error ? (
            <Text color="red.500">{error}</Text>
          ) : nutritionInfo.length === 0 ? (
            <Text color="gray.600">No nutrition data returned for this recipe.</Text>
          ) : (
            <List spacing={2}>
              {nutritionInfo.map((info) => (
                <ListItem key={info} display="flex" alignItems="center" color="gray.700">
                  <ListIcon as={CheckCircleIcon} color="teal.500" />
                  {info}
                </ListItem>
              ))}
            </List>
          )}
        </ModalBody>
        <ModalFooter>
          <Button onClick={onClose}>Close</Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};
