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
import { useTranslation } from 'react-i18next';

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
  const { t } = useTranslation();

  return (
    <Modal isOpen={isOpen} onClose={onClose} size="lg" isCentered>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>
          {t('nutritionModal.title')}{recipeName ? ` - ${recipeName}` : ''}
        </ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          {isLoading ? (
            <Box display="flex" justifyContent="center" py={12}>
              <Spinner size="lg" color="teal.500" />
            </Box>
          ) : error ? (
            <Text color="red.500">{error}</Text>
          ) : nutritionInfo.length === 0 ? (
            <Text color="gray.600">{t('nutritionModal.noData')}</Text>
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
          <Button onClick={onClose}>{t('nutritionModal.close')}</Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};
