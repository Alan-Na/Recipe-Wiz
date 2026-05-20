import {
  Box, Button, Flex, List, ListItem, Modal, ModalBody, ModalCloseButton,
  ModalContent, ModalFooter, ModalHeader, ModalOverlay, Spinner, Text,
} from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';
import { motion } from 'framer-motion';

const MotionBox = motion(Box);

interface NutritionModalProps {
  isOpen: boolean;
  onClose: () => void;
  recipeName?: string;
  nutritionInfo: string[];
  isLoading: boolean;
  error?: string | null;
}

// Assign a color category based on nutrient keyword
const nutrientColor = (label: string): string => {
  const l = label.toLowerCase();
  if (l.includes('energy') || l.includes('calorie')) return 'orange';
  if (l.includes('protein')) return 'blue';
  if (l.includes('fat') || l.includes('monounsat') || l.includes('polyunsat') || l.includes('saturated') || l.includes('trans')) return 'pink';
  if (l.includes('carb') || l.includes('sugar') || l.includes('fiber')) return 'green';
  if (l.includes('vitamin')) return 'purple';
  if (l.includes('sodium') || l.includes('calcium') || l.includes('potassium') || l.includes('iron') || l.includes('zinc')) return 'teal';
  return 'gray';
};

const COLOR_BG: Record<string, string> = {
  orange: '#FFF3E0', blue: '#E3F2FD', pink: '#FCE4EC', green: '#E8F5E9',
  purple: '#F3E5F5', teal: '#E0F2F1', gray: '#F5F5F5',
};
const COLOR_DOT: Record<string, string> = {
  orange: '#FB8C00', blue: '#1E88E5', pink: '#E91E63', green: '#43A047',
  purple: '#8E24AA', teal: '#00897B', gray: '#757575',
};

export const NutritionModal = ({
  isOpen, onClose, recipeName, nutritionInfo, isLoading, error,
}: NutritionModalProps) => {
  const { t } = useTranslation();

  return (
    <Modal isOpen={isOpen} onClose={onClose} size="lg" isCentered scrollBehavior="inside">
      <ModalOverlay bg="blackAlpha.500" backdropFilter="blur(6px)" />
      <ModalContent borderRadius="2xl" overflow="hidden" shadow="2xl" maxH="85vh">
        <Box bgGradient="linear(135deg, teal.500, teal.400)" px={6} pt={6} pb={5}>
          <ModalCloseButton color="white" top={4} right={4} />
          <Text fontSize="2xl" mb={1}>📊</Text>
          <Text fontWeight={800} color="white" fontSize="lg">
            {t('nutritionModal.title')}
          </Text>
          {recipeName && (
            <Text color="whiteAlpha.800" fontSize="sm" mt={1} noOfLines={1}>
              {recipeName}
            </Text>
          )}
        </Box>

        <ModalBody p={5} overflowY="auto">
          {isLoading ? (
            <Flex justify="center" align="center" py={16} direction="column" gap={4}>
              <Spinner size="xl" color="teal.500" thickness="3px" />
              <Text color="gray.400" fontSize="sm">Analyzing nutrients…</Text>
            </Flex>
          ) : error ? (
            <Box textAlign="center" py={10}>
              <Text fontSize="3xl" mb={3}>⚠️</Text>
              <Text color="red.400" fontWeight={600}>{error}</Text>
            </Box>
          ) : nutritionInfo.length === 0 ? (
            <Box textAlign="center" py={10}>
              <Text fontSize="3xl" mb={3}>🔬</Text>
              <Text color="gray.400">{t('nutritionModal.noData')}</Text>
            </Box>
          ) : (
            <List spacing={2}>
              {nutritionInfo.map((info, i) => {
                const color = nutrientColor(info);
                return (
                  <MotionBox
                    key={info}
                    as={ListItem}
                    initial={{ opacity: 0, x: -12 }}
                    animate={{ opacity: 1, x: 0 }}
                    transition={{ delay: i * 0.03, duration: 0.3 }}
                  >
                    <Flex
                      align="center"
                      bg={COLOR_BG[color]}
                      borderRadius="xl"
                      px={4}
                      py={2.5}
                      gap={3}
                    >
                      <Box
                        w={2.5}
                        h={2.5}
                        borderRadius="full"
                        bg={COLOR_DOT[color]}
                        flexShrink={0}
                      />
                      <Text fontSize="sm" color="gray.700" fontWeight={500}>
                        {info}
                      </Text>
                    </Flex>
                  </MotionBox>
                );
              })}
            </List>
          )}
        </ModalBody>

        <ModalFooter borderTop="1px solid" borderColor="gray.100" py={4}>
          <Button
            onClick={onClose}
            colorScheme="teal"
            borderRadius="xl"
            px={8}
            fontWeight={700}
          >
            {t('nutritionModal.close')}
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};
