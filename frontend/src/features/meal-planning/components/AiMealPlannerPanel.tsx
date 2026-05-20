import {
  Badge, Box, Button, Collapse, Flex, FormControl, FormLabel,
  HStack, Select, Stack, Text, Textarea, Tooltip, VStack,
} from '@chakra-ui/react';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { motion, AnimatePresence } from 'framer-motion';
import type { AiMealPlanConfirmRequest, AiMealPlanPreviewDto, AiMealPlanRequest } from '../../../types/api';

const MotionBox = motion(Box);

interface AiMealPlannerPanelProps {
  savedRecipesCount: number;
  onGenerate: (req: AiMealPlanRequest) => Promise<AiMealPlanPreviewDto>;
  onConfirm: (req: AiMealPlanConfirmRequest) => Promise<void>;
  isGenerating: boolean;
  isConfirming: boolean;
}

type PanelState = 'form' | 'preview' | 'confirmed';

const MEAL_TYPE_COLORS: Record<string, string> = {
  Breakfast: 'yellow', Lunch: 'green', Dinner: 'blue', Snack: 'pink',
};
const MEAL_TYPE_EMOJIS: Record<string, string> = {
  Breakfast: '🌅', Lunch: '☀️', Dinner: '🌙', Snack: '🍎',
};

// Helper to generate a range of numbers
const range = (start: number, end: number, step = 1) => {
  const result: number[] = [];
  for (let i = start; i <= end; i += step) result.push(i);
  return result;
};

// Group preview entries by date
const groupByDate = (entries: AiMealPlanPreviewDto['entries']) => {
  const map = new Map<string, typeof entries>();
  for (const e of entries) {
    if (!map.has(e.mealDate)) map.set(e.mealDate, []);
    map.get(e.mealDate)!.push(e);
  }
  return Array.from(map.entries()).sort(([a], [b]) => a.localeCompare(b));
};

const formatDate = (dateStr: string, isZh: boolean): string => {
  const d = new Date(dateStr + 'T00:00:00');
  if (isZh) {
    return `${d.getMonth() + 1}月${d.getDate()}日 ${['日', '一', '二', '三', '四', '五', '六'][d.getDay()]}`;
  }
  return d.toLocaleDateString('en-US', { weekday: 'short', month: 'short', day: 'numeric' });
};

export const AiMealPlannerPanel = ({
  savedRecipesCount,
  onGenerate,
  onConfirm,
  isGenerating,
  isConfirming,
}: AiMealPlannerPanelProps) => {
  const { t, i18n } = useTranslation();
  const isZh = i18n.language === 'zh';

  const [isExpanded, setIsExpanded] = useState(false);
  const [panelState, setPanelState] = useState<PanelState>('form');
  const [preview, setPreview] = useState<AiMealPlanPreviewDto | null>(null);
  const [error, setError] = useState<string | null>(null);

  // Form state
  const [gender, setGender] = useState(1);
  const [heightCm, setHeightCm] = useState(170);
  const [weightKg, setWeightKg] = useState(65);
  const [age, setAge] = useState(30);
  const [goal, setGoal] = useState('');

  const canGenerate = savedRecipesCount >= 3;

  const handleGenerate = async () => {
    setError(null);
    try {
      const result = await onGenerate({ gender, heightCm, weightKg, age, goal });
      setPreview(result);
      setPanelState('preview');
    } catch (err) {
      setError(err instanceof Error ? err.message : t('aiMealPlanner.toast.tryAgain'));
    }
  };

  const handleConfirm = async () => {
    if (!preview) return;
    setError(null);
    try {
      await onConfirm({
        entries: preview.entries.map((e) => ({
          recipeId: e.recipeId,
          mealDate: e.mealDate,
          mealType: e.mealType,
        })),
      });
      setPanelState('confirmed');
    } catch (err) {
      setError(err instanceof Error ? err.message : t('aiMealPlanner.toast.tryAgain'));
    }
  };

  const handleReset = () => {
    setPanelState('form');
    setPreview(null);
    setError(null);
  };

  return (
    <Box
      bg="white"
      borderRadius="2xl"
      shadow="md"
      border="1px solid"
      borderColor="purple.100"
      overflow="hidden"
    >
      {/* Gradient accent strip */}
      <Box h="4px" bgGradient="linear(to-r, purple.400, pink.400, orange.300)" />

      {/* Toggle header */}
      <Flex
        align="center"
        justify="space-between"
        px={6}
        py={4}
        cursor="pointer"
        onClick={() => setIsExpanded((v) => !v)}
        _hover={{ bg: 'purple.50' }}
        transition="background 0.2s"
      >
        <HStack spacing={3}>
          <Text fontSize="xl">🤖</Text>
          <Box>
            <HStack spacing={2} align="center">
              <Text fontWeight={800} fontSize="md" color="gray.800">
                {t('aiMealPlanner.panelTitle')}
              </Text>
              <Badge
                bg="purple.500"
                color="white"
                borderRadius="full"
                px={2}
                py={0.5}
                fontSize="2xs"
                fontWeight={700}
                letterSpacing="wider"
              >
                AI
              </Badge>
              {!canGenerate && (
                <Badge colorScheme="orange" variant="subtle" borderRadius="full" fontSize="2xs" px={2}>
                  {t('aiMealPlanner.needMoreRecipesShort', { count: 3 - savedRecipesCount })}
                </Badge>
              )}
            </HStack>
            <Text fontSize="xs" color="gray.400" mt={0.5}>
              {t('aiMealPlanner.panelSubtitle')}
            </Text>
          </Box>
        </HStack>
        <Text fontSize="xs" color="gray.400" fontWeight={700}>
          {isExpanded ? '▲' : '▼'}
        </Text>
      </Flex>

      {/* Collapsible body */}
      <Collapse in={isExpanded} animateOpacity>
        <Box px={6} pb={6} pt={2}>
          <AnimatePresence mode="wait">

            {/* ── FORM STATE ─────────────────────────────── */}
            {panelState === 'form' && (
              <MotionBox
                key="form"
                initial={{ opacity: 0, y: 8 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -8 }}
                transition={{ duration: 0.25 }}
              >
                <Stack spacing={5}>
                  {/* Profile dropdowns */}
                  <Box bg="purple.50" borderRadius="xl" p={4}>
                    <Text fontSize="xs" fontWeight={800} color="purple.600"
                      textTransform="uppercase" letterSpacing="wider" mb={3}>
                      👤 {t('aiMealPlanner.profileSection')}
                    </Text>
                    <Flex gap={4} flexWrap="wrap">
                      {/* Gender */}
                      <FormControl flex="1" minW="120px">
                        <FormLabel fontSize="xs" fontWeight={700} color="gray.600" mb={1}>
                          {t('aiMealPlanner.gender')}
                        </FormLabel>
                        <Select
                          size="sm"
                          borderRadius="lg"
                          value={gender}
                          onChange={(e) => setGender(Number(e.target.value))}
                          _focus={{ borderColor: 'purple.400' }}
                        >
                          <option value={1}>{t('aiMealPlanner.male')}</option>
                          <option value={2}>{t('aiMealPlanner.female')}</option>
                        </Select>
                      </FormControl>

                      {/* Age */}
                      <FormControl flex="1" minW="100px">
                        <FormLabel fontSize="xs" fontWeight={700} color="gray.600" mb={1}>
                          {t('aiMealPlanner.age')}
                        </FormLabel>
                        <Select
                          size="sm"
                          borderRadius="lg"
                          value={age}
                          onChange={(e) => setAge(Number(e.target.value))}
                          _focus={{ borderColor: 'purple.400' }}
                        >
                          {range(10, 99).map((v) => (
                            <option key={v} value={v}>{v}</option>
                          ))}
                        </Select>
                      </FormControl>

                      {/* Height */}
                      <FormControl flex="1" minW="130px">
                        <FormLabel fontSize="xs" fontWeight={700} color="gray.600" mb={1}>
                          {t('aiMealPlanner.height')}
                        </FormLabel>
                        <Select
                          size="sm"
                          borderRadius="lg"
                          value={heightCm}
                          onChange={(e) => setHeightCm(Number(e.target.value))}
                          _focus={{ borderColor: 'purple.400' }}
                        >
                          {range(140, 220).map((v) => (
                            <option key={v} value={v}>{v} cm</option>
                          ))}
                        </Select>
                      </FormControl>

                      {/* Weight */}
                      <FormControl flex="1" minW="130px">
                        <FormLabel fontSize="xs" fontWeight={700} color="gray.600" mb={1}>
                          {t('aiMealPlanner.weight')}
                        </FormLabel>
                        <Select
                          size="sm"
                          borderRadius="lg"
                          value={weightKg}
                          onChange={(e) => setWeightKg(Number(e.target.value))}
                          _focus={{ borderColor: 'purple.400' }}
                        >
                          {range(30, 150).map((v) => (
                            <option key={v} value={v}>{v} kg</option>
                          ))}
                        </Select>
                      </FormControl>
                    </Flex>
                  </Box>

                  {/* Goal input */}
                  <FormControl>
                    <FormLabel fontSize="xs" fontWeight={700} color="gray.600" mb={1}>
                      🎯 {t('aiMealPlanner.goal')}
                    </FormLabel>
                    <Textarea
                      placeholder={t('aiMealPlanner.goalPlaceholder')}
                      value={goal}
                      onChange={(e) => setGoal(e.target.value)}
                      maxLength={300}
                      rows={3}
                      borderRadius="xl"
                      borderColor="gray.200"
                      fontSize="sm"
                      resize="none"
                      _focus={{ borderColor: 'purple.400', boxShadow: '0 0 0 3px rgba(128,90,213,0.15)' }}
                    />
                    <Text fontSize="xs" color="gray.400" textAlign="right" mt={1}>
                      {goal.length}/300
                    </Text>
                  </FormControl>

                  {/* Error */}
                  {error && (
                    <Box bg="red.50" borderRadius="xl" p={3} border="1px solid" borderColor="red.200">
                      <Text fontSize="sm" color="red.500">⚠️ {error}</Text>
                    </Box>
                  )}

                  {/* Generate button */}
                  <Tooltip
                    label={t('aiMealPlanner.needMoreRecipes')}
                    isDisabled={canGenerate}
                    hasArrow
                  >
                    <Button
                      bgGradient="linear(135deg, purple.500, pink.400)"
                      color="white"
                      borderRadius="xl"
                      fontWeight={700}
                      size="lg"
                      w="full"
                      isDisabled={!canGenerate}
                      isLoading={isGenerating}
                      loadingText={t('aiMealPlanner.generating')}
                      onClick={handleGenerate}
                      _hover={{ bgGradient: 'linear(135deg, purple.600, pink.500)', transform: 'translateY(-1px)', shadow: 'md' }}
                      _disabled={{ opacity: 0.5, cursor: 'not-allowed' }}
                      transition="all 0.2s"
                      leftIcon={<Text as="span">✨</Text>}
                    >
                      {t('aiMealPlanner.generateButton')}
                    </Button>
                  </Tooltip>
                </Stack>
              </MotionBox>
            )}

            {/* ── PREVIEW STATE ──────────────────────────── */}
            {panelState === 'preview' && preview && (
              <MotionBox
                key="preview"
                initial={{ opacity: 0, y: 8 }}
                animate={{ opacity: 1, y: 0 }}
                exit={{ opacity: 0, y: -8 }}
                transition={{ duration: 0.25 }}
              >
                <Stack spacing={4}>
                  <HStack spacing={2}>
                    <Text fontSize="lg">📋</Text>
                    <Text fontWeight={800} fontSize="md" color="gray.700">
                      {t('aiMealPlanner.previewTitle')}
                    </Text>
                    <Badge colorScheme="purple" borderRadius="full" fontSize="xs" fontWeight={700}>
                      {preview.entries.length} {t('aiMealPlanner.mealsCount', 'meals')}
                    </Badge>
                  </HStack>

                  {/* Preview cards grouped by date */}
                  <Stack spacing={3}>
                    {groupByDate(preview.entries).map(([date, entries], di) => (
                      <MotionBox
                        key={date}
                        initial={{ opacity: 0, x: -12 }}
                        animate={{ opacity: 1, x: 0 }}
                        transition={{ delay: di * 0.05, duration: 0.3 }}
                      >
                        <Box borderRadius="xl" border="1px solid" borderColor="gray.100" overflow="hidden">
                          {/* Date header */}
                          <Box bg="gray.50" px={4} py={2} borderBottom="1px solid" borderColor="gray.100">
                            <Text fontWeight={800} fontSize="sm" color="gray.600">
                              📅 {formatDate(date, isZh)}
                            </Text>
                          </Box>
                          {/* Meal entries */}
                          <Stack spacing={0} divider={<Box h="1px" bg="gray.50" />}>
                            {entries.map((entry) => {
                              const color = MEAL_TYPE_COLORS[entry.mealType] ?? 'gray';
                              const emoji = MEAL_TYPE_EMOJIS[entry.mealType] ?? '🍽️';
                              return (
                                <Flex
                                  key={`${date}-${entry.mealType}`}
                                  align="center"
                                  gap={3}
                                  px={4}
                                  py={3}
                                >
                                  <Badge
                                    colorScheme={color}
                                    borderRadius="full"
                                    px={2}
                                    py={0.5}
                                    fontSize="2xs"
                                    fontWeight={700}
                                    flexShrink={0}
                                    minW="70px"
                                    textAlign="center"
                                  >
                                    {emoji} {entry.mealType}
                                  </Badge>
                                  <Text fontSize="sm" color="gray.700" fontWeight={600} flex={1} noOfLines={1}>
                                    {entry.recipe.title}
                                  </Text>
                                  {entry.recipe.nutrition.calories > 0 && (
                                    <Text fontSize="xs" color="gray.400" flexShrink={0}>
                                      {Math.round(entry.recipe.nutrition.calories)} kcal
                                    </Text>
                                  )}
                                </Flex>
                              );
                            })}
                          </Stack>
                        </Box>
                      </MotionBox>
                    ))}
                  </Stack>

                  {/* Error */}
                  {error && (
                    <Box bg="red.50" borderRadius="xl" p={3} border="1px solid" borderColor="red.200">
                      <Text fontSize="sm" color="red.500">⚠️ {error}</Text>
                    </Box>
                  )}

                  {/* Action buttons */}
                  <Flex gap={3} flexWrap="wrap">
                    <Button
                      variant="outline"
                      colorScheme="purple"
                      borderRadius="xl"
                      fontWeight={700}
                      flex={1}
                      isDisabled={isGenerating || isConfirming}
                      isLoading={isGenerating}
                      loadingText={t('aiMealPlanner.generating')}
                      onClick={handleGenerate}
                      leftIcon={<Text as="span">🔄</Text>}
                    >
                      {t('aiMealPlanner.regenerateButton')}
                    </Button>
                    <Button
                      bgGradient="linear(135deg, purple.500, pink.400)"
                      color="white"
                      borderRadius="xl"
                      fontWeight={700}
                      flex={2}
                      isLoading={isConfirming}
                      loadingText={t('aiMealPlanner.confirming')}
                      onClick={handleConfirm}
                      _hover={{ bgGradient: 'linear(135deg, purple.600, pink.500)', transform: 'translateY(-1px)', shadow: 'md' }}
                      transition="all 0.2s"
                      leftIcon={<Text as="span">✅</Text>}
                    >
                      {t('aiMealPlanner.confirmButton')}
                    </Button>
                  </Flex>
                </Stack>
              </MotionBox>
            )}

            {/* ── CONFIRMED STATE ────────────────────────── */}
            {panelState === 'confirmed' && (
              <MotionBox
                key="confirmed"
                initial={{ opacity: 0, scale: 0.95 }}
                animate={{ opacity: 1, scale: 1 }}
                exit={{ opacity: 0 }}
                transition={{ duration: 0.3 }}
              >
                <VStack spacing={4} py={4} textAlign="center">
                  <Text fontSize="4xl">🎉</Text>
                  <Box>
                    <Text fontWeight={800} fontSize="lg" color="gray.700" mb={1}>
                      {t('aiMealPlanner.confirmedTitle')}
                    </Text>
                    <Text fontSize="sm" color="gray.400">
                      {t('aiMealPlanner.confirmedSubtitle')}
                    </Text>
                  </Box>
                  <Button
                    variant="outline"
                    colorScheme="purple"
                    borderRadius="xl"
                    fontWeight={700}
                    onClick={handleReset}
                    leftIcon={<Text as="span">🤖</Text>}
                  >
                    {t('aiMealPlanner.resetButton')}
                  </Button>
                </VStack>
              </MotionBox>
            )}

          </AnimatePresence>
        </Box>
      </Collapse>
    </Box>
  );
};
