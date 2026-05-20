import {
  Badge, Box, Flex, IconButton, Select, SimpleGrid,
  Spinner, Text, VStack,
} from '@chakra-ui/react';
import { DeleteIcon } from '@chakra-ui/icons';
import type { Dayjs } from 'dayjs';
import dayjs from 'dayjs';
import { useTranslation } from 'react-i18next';
import { motion, AnimatePresence } from 'framer-motion';
import type { MealPlanEntryDto } from '../../../types/api';
import { MEAL_STATUSES } from '../utils';

const MotionBox = motion(Box);

interface MealPlanCalendarProps {
  weekDays: Dayjs[];
  entries: MealPlanEntryDto[];
  isLoading: boolean;
  processingEntryId: number | null;
  removingEntryId: number | null;
  onUpdateStatus: (entryId: number, status: string) => void;
  onRemove: (entryId: number) => void;
}

const getEntriesForDate = (entries: MealPlanEntryDto[], date: Dayjs) =>
  entries.filter((entry) => dayjs(entry.mealDate).isSame(date, 'day'));

// Color per meal type
const MEAL_TYPE_COLORS: Record<string, { bg: string; border: string; badge: string }> = {
  breakfast: { bg: '#FFF8E1', border: '#FFD54F', badge: 'yellow' },
  lunch:     { bg: '#E8F5E9', border: '#66BB6A', badge: 'green' },
  dinner:    { bg: '#E3F2FD', border: '#42A5F5', badge: 'blue' },
  snack:     { bg: '#FCE4EC', border: '#EC407A', badge: 'pink' },
};
const defaultColors = { bg: 'teal.50', border: '#80CBC4', badge: 'teal' };

const getMealColors = (mealType: string) =>
  MEAL_TYPE_COLORS[mealType?.toLowerCase()] ?? defaultColors;

export const MealPlanCalendar = ({
  weekDays, entries, isLoading, processingEntryId, removingEntryId, onUpdateStatus, onRemove,
}: MealPlanCalendarProps) => {
  const { t, i18n } = useTranslation();
  const isZh = i18n.language === 'zh';

  if (isLoading) {
    return (
      <Flex justify="center" align="center" bg="white" borderRadius="2xl" p={10}
        shadow="md" flex="1" border="1px solid" borderColor="teal.100" direction="column" gap={4}>
        <Spinner size="xl" color="teal.500" thickness="3px" />
        <Text color="gray.400" fontSize="sm">Loading your meal plan…</Text>
      </Flex>
    );
  }

  return (
    <SimpleGrid columns={{ base: 2, md: 4, lg: weekDays.length }} spacing={3} flex="1">
      {weekDays.map((day, i) => {
        const dayEntries = getEntriesForDate(entries, day);
        const isToday = day.isSame(dayjs(), 'day');
        const dateLabel = isZh ? day.format('M月D日') : day.format('MMM D');
        const dayLabel  = isZh ? day.format('ddd')   : day.format('ddd');

        return (
          <MotionBox
            key={day.toISOString()}
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.35, delay: i * 0.05 }}
          >
            <Box
              borderRadius="2xl"
              border="2px solid"
              borderColor={isToday ? 'teal.400' : 'gray.100'}
              bg="white"
              overflow="hidden"
              shadow={isToday ? 'lg' : 'sm'}
              minH="220px"
              transition="shadow 0.2s"
              _hover={{ shadow: 'md' }}
            >
              {/* Day header */}
              <Box
                bgGradient={isToday
                  ? 'linear(135deg, teal.500, teal.400)'
                  : 'linear(135deg, gray.100, gray.50)'}
                px={3}
                py={3}
              >
                <Text
                  fontSize="xs"
                  fontWeight={800}
                  textTransform="uppercase"
                  letterSpacing="wider"
                  color={isToday ? 'whiteAlpha.800' : 'gray.400'}
                >
                  {dayLabel}
                </Text>
                <Text
                  fontWeight={800}
                  fontSize="md"
                  color={isToday ? 'white' : 'gray.700'}
                  lineHeight={1.2}
                >
                  {dateLabel}
                </Text>
                {isToday && (
                  <Badge
                    bg="whiteAlpha.300"
                    color="white"
                    fontSize="2xs"
                    borderRadius="full"
                    px={2}
                    mt={1}
                    fontWeight={700}
                  >
                    {t('mealPlanCalendar.today')}
                  </Badge>
                )}
              </Box>

              {/* Entries */}
              <VStack align="stretch" spacing={2} p={2}>
                <AnimatePresence>
                  {dayEntries.length === 0 ? (
                    <Flex align="center" justify="center" py={6} direction="column" gap={1}>
                      <Text fontSize="xl">🍽️</Text>
                      <Text color="gray.300" fontSize="xs" textAlign="center">
                        {t('mealPlanCalendar.noMeals')}
                      </Text>
                    </Flex>
                  ) : (
                    dayEntries.map((entry) => {
                      const colors = getMealColors(entry.mealType);
                      return (
                        <MotionBox
                          key={entry.entryId}
                          initial={{ opacity: 0, scale: 0.9 }}
                          animate={{ opacity: 1, scale: 1 }}
                          exit={{ opacity: 0, scale: 0.8 }}
                          transition={{ duration: 0.2 }}
                        >
                          <Box
                            bg={colors.bg}
                            border="1px solid"
                            borderColor={colors.border}
                            borderRadius="xl"
                            p={2.5}
                          >
                            <Flex justify="space-between" align="flex-start" mb={1.5}>
                              <Box flex={1} minW={0}>
                                <Text
                                  fontWeight={700}
                                  fontSize="xs"
                                  color="gray.700"
                                  noOfLines={2}
                                  lineHeight={1.3}
                                  mb={1}
                                >
                                  {entry.recipe.title}
                                </Text>
                                <Badge
                                  colorScheme={colors.badge}
                                  fontSize="2xs"
                                  borderRadius="full"
                                  px={2}
                                  fontWeight={700}
                                >
                                  {t(`mealTypes.${entry.mealType}`, entry.mealType)}
                                </Badge>
                              </Box>
                              <IconButton
                                aria-label={t('mealPlanCalendar.removeMeal')}
                                icon={<DeleteIcon />}
                                size="xs"
                                variant="ghost"
                                colorScheme="red"
                                borderRadius="lg"
                                onClick={() => onRemove(entry.entryId)}
                                isLoading={removingEntryId === entry.entryId}
                                ml={1}
                                flexShrink={0}
                              />
                            </Flex>
                            <Select
                              size="xs"
                              value={entry.status}
                              onChange={(e) => onUpdateStatus(entry.entryId, e.target.value)}
                              isDisabled={processingEntryId === entry.entryId}
                              borderRadius="lg"
                              borderColor={colors.border}
                              bg="white"
                              fontSize="xs"
                              fontWeight={600}
                            >
                              {MEAL_STATUSES.map((status) => (
                                <option key={status} value={status}>
                                  {t(`mealStatuses.${status}`, status)}
                                </option>
                              ))}
                            </Select>
                          </Box>
                        </MotionBox>
                      );
                    })
                  )}
                </AnimatePresence>
              </VStack>
            </Box>
          </MotionBox>
        );
      })}
    </SimpleGrid>
  );
};
