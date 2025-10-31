import { Dayjs } from 'dayjs';

export const MEAL_TYPES = ['Breakfast', 'Lunch', 'Dinner', 'Snack'] as const;
export const MEAL_STATUSES = ['planned', 'in progress', 'completed'] as const;

export type MealStatus = (typeof MEAL_STATUSES)[number];
export type MealType = (typeof MEAL_TYPES)[number];

export const getWeekStart = (date: Dayjs): Dayjs => {
  const dayIndex = (date.day() + 6) % 7; // convert Sunday=0 to Monday=0
  return date.subtract(dayIndex, 'day').startOf('day');
};

export const getWeekDays = (weekStart: Dayjs): Dayjs[] => {
  return Array.from({ length: 7 }, (_, index) => weekStart.add(index, 'day'));
};

export const formatWeekRange = (weekStart: Dayjs): string => {
  const weekEnd = weekStart.add(6, 'day');
  const startFormat = weekStart.format('MMM D');
  const endFormat =
    weekStart.month() === weekEnd.month()
      ? weekEnd.format('D, YYYY')
      : weekEnd.format('MMM D, YYYY');
  return `${startFormat} - ${endFormat}`;
};
