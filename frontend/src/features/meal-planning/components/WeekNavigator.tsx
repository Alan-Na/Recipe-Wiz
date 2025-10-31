import { Button, ButtonGroup, Heading, HStack, IconButton } from '@chakra-ui/react';
import { ArrowBackIcon, ArrowForwardIcon, RepeatIcon } from '@chakra-ui/icons';
import type { Dayjs } from 'dayjs';
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
  return (
    <HStack justify="space-between" align="center" bg="white" borderRadius="xl" p={6} shadow="sm">
      <Heading size="md" color="teal.700">
        Week of {formatWeekRange(weekStart)}
      </Heading>
      <ButtonGroup variant="outline" size="sm" spacing={2}>
        <IconButton aria-label="Previous week" icon={<ArrowBackIcon />} onClick={onPreviousWeek} />
        <Button leftIcon={<RepeatIcon />} onClick={onResetWeek}>
          Today
        </Button>
        <IconButton aria-label="Next week" icon={<ArrowForwardIcon />} onClick={onNextWeek} />
      </ButtonGroup>
    </HStack>
  );
};
