import { Button, ButtonGroup, Heading, HStack, IconButton } from '@chakra-ui/react';
import { ArrowBackIcon, ArrowForwardIcon, RepeatIcon } from '@chakra-ui/icons';
import type { Dayjs } from 'dayjs';
import { useTranslation } from 'react-i18next';
import { formatWeekRange } from '../utils';

interface WeekNavigatorProps {
  weekStart: Dayjs;
  onPreviousWeek: () => void;
  onNextWeek: () => void;
  onResetWeek: () => void;
}

export const WeekNavigator = ({
  weekStart,
  onPreviousWeek,
  onNextWeek,
  onResetWeek,
}: WeekNavigatorProps) => {
  const { t } = useTranslation();

  return (
    <HStack justify="space-between" align="center" bg="white" borderRadius="xl" p={6} shadow="sm">
      <Heading size="md" color="teal.700">
        {t('weekNavigator.weekOf', { range: formatWeekRange(weekStart) })}
      </Heading>
      <ButtonGroup variant="outline" size="sm" spacing={2}>
        <IconButton
          aria-label={t('weekNavigator.previousWeek')}
          icon={<ArrowBackIcon />}
          onClick={onPreviousWeek}
        />
        <Button leftIcon={<RepeatIcon />} onClick={onResetWeek}>
          {t('weekNavigator.today')}
        </Button>
        <IconButton
          aria-label={t('weekNavigator.nextWeek')}
          icon={<ArrowForwardIcon />}
          onClick={onNextWeek}
        />
      </ButtonGroup>
    </HStack>
  );
};
