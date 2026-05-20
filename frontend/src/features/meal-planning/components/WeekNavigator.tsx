import { Box, Button, Flex, HStack, IconButton, Text } from '@chakra-ui/react';
import { ArrowBackIcon, ArrowForwardIcon, RepeatIcon } from '@chakra-ui/icons';
import type { Dayjs } from 'dayjs';
import { useTranslation } from 'react-i18next';
import { motion } from 'framer-motion';
import { formatWeekRange } from '../utils';

const MotionBox = motion(Box);

interface WeekNavigatorProps {
  weekStart: Dayjs;
  onPreviousWeek: () => void;
  onNextWeek: () => void;
  onResetWeek: () => void;
}

export const WeekNavigator = ({
  weekStart, onPreviousWeek, onNextWeek, onResetWeek,
}: WeekNavigatorProps) => {
  const { t } = useTranslation();

  return (
    <Box
      bg="white"
      borderRadius="2xl"
      shadow="md"
      border="1px solid"
      borderColor="teal.100"
      overflow="hidden"
    >
      {/* Gradient accent strip */}
      <Box h="4px" bgGradient="linear(to-r, teal.400, orange.300)" />

      <Flex align="center" justify="space-between" px={6} py={4} flexWrap="wrap" gap={3}>
        <HStack spacing={3}>
          <Text fontSize="xl">📅</Text>
          <MotionBox
            key={weekStart.toISOString()}
            initial={{ opacity: 0, y: -8 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.3 }}
          >
            <Text
              fontWeight={800}
              fontSize={{ base: 'md', md: 'lg' }}
              bgGradient="linear(to-r, teal.600, teal.400)"
              bgClip="text"
            >
              {t('weekNavigator.weekOf', { range: formatWeekRange(weekStart) })}
            </Text>
          </MotionBox>
        </HStack>

        <HStack spacing={2}>
          <IconButton
            aria-label={t('weekNavigator.previousWeek')}
            icon={<ArrowBackIcon />}
            size="sm"
            variant="ghost"
            colorScheme="teal"
            borderRadius="xl"
            onClick={onPreviousWeek}
            _hover={{ bg: 'teal.50', transform: 'translateX(-2px)' }}
            transition="all 0.2s"
          />
          <Button
            leftIcon={<RepeatIcon />}
            size="sm"
            colorScheme="teal"
            variant="outline"
            borderRadius="xl"
            fontWeight={700}
            onClick={onResetWeek}
            _hover={{ bg: 'teal.50' }}
          >
            {t('weekNavigator.today')}
          </Button>
          <IconButton
            aria-label={t('weekNavigator.nextWeek')}
            icon={<ArrowForwardIcon />}
            size="sm"
            variant="ghost"
            colorScheme="teal"
            borderRadius="xl"
            onClick={onNextWeek}
            _hover={{ bg: 'teal.50', transform: 'translateX(2px)' }}
            transition="all 0.2s"
          />
        </HStack>
      </Flex>
    </Box>
  );
};
