import {
  Badge,
  Box,
  Flex,
  Heading,
  IconButton,
  Select,
  SimpleGrid,
  Spinner,
  Stack,
  Text,
  VStack,
} from '@chakra-ui/react';
import { DeleteIcon } from '@chakra-ui/icons';
import type { Dayjs } from 'dayjs';
import dayjs from 'dayjs';
import { useTranslation } from 'react-i18next';
import type { MealPlanEntryDto } from '../../../types/api';
import { MEAL_STATUSES } from '../utils';

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

export const MealPlanCalendar = ({
  weekDays,
  entries,
  isLoading,
  processingEntryId,
  removingEntryId,
  onUpdateStatus,
  onRemove,
}: MealPlanCalendarProps) => {
  const { t, i18n } = useTranslation();
  const isZh = i18n.language === 'zh';

  if (isLoading) {
    return (
      <Flex justify="center" align="center" bg="white" borderRadius="xl" p={10} shadow="sm" flex="1">
        <Spinner size="xl" color="teal.500" />
      </Flex>
    );
  }

  return (
    <SimpleGrid columns={weekDays.length} spacing={4} flex="1">
      {weekDays.map((day) => {
        const dayEntries = getEntriesForDate(entries, day);
        const isToday = day.isSame(dayjs(), 'day');
        // Format date header based on language
        const dateLabel = isZh
          ? day.format('M月D日 ddd')
          : day.format('ddd, MMM D');

        return (
          <Box
            key={day.toISOString()}
            borderRadius="xl"
            border="1px solid"
            borderColor={isToday ? 'teal.300' : 'teal.100'}
            bg="white"
            p={4}
            shadow="sm"
            minH="220px"
          >
            <Heading size="sm" color="teal.700">
              {dateLabel}
            </Heading>
            {isToday && (
              <Badge colorScheme="teal" mt={1}>
                {t('mealPlanCalendar.today')}
              </Badge>
            )}
            <VStack align="stretch" spacing={3} mt={4}>
              {dayEntries.length === 0 ? (
                <Text color="gray.400" fontSize="sm">
                  {t('mealPlanCalendar.noMeals')}
                </Text>
              ) : (
                dayEntries.map((entry) => (
                  <Box
                    key={entry.entryId}
                    border="1px solid"
                    borderColor="teal.100"
                    borderRadius="lg"
                    p={3}
                    bg="teal.50"
                  >
                    <Flex justify="space-between" align="start">
                      <Stack spacing={1}>
                        <Text fontWeight="semibold" color="teal.700">
                          {entry.recipe.title}
                        </Text>
                        <Badge colorScheme="teal" width="fit-content">
                          {t(`mealTypes.${entry.mealType}`, entry.mealType)}
                        </Badge>
                      </Stack>
                      <IconButton
                        aria-label={t('mealPlanCalendar.removeMeal')}
                        icon={<DeleteIcon />}
                        size="sm"
                        variant="ghost"
                        onClick={() => onRemove(entry.entryId)}
                        isLoading={removingEntryId === entry.entryId}
                      />
                    </Flex>
                    <Select
                      size="sm"
                      mt={3}
                      value={entry.status}
                      onChange={(event) => onUpdateStatus(entry.entryId, event.target.value)}
                      isDisabled={processingEntryId === entry.entryId}
                    >
                      {MEAL_STATUSES.map((status) => (
                        <option key={status} value={status}>
                          {t(`mealStatuses.${status}`, status)}
                        </option>
                      ))}
                    </Select>
                  </Box>
                ))
              )}
            </VStack>
          </Box>
        );
      })}
    </SimpleGrid>
  );
};
