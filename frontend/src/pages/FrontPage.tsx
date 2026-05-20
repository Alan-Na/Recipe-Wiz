import { Box, Button, Grid, Heading, HStack, Stack, Text, VStack } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { motion } from 'framer-motion';

const MotionBox = motion(Box);

const FEATURE_ICONS = ['🔍', '📊', '📅', '⚖️'];

const FOOD_ITEMS = [
  { emoji: '🍜', cls: 'float-a', top: '10%',  left: '60%',  size: '4xl' },
  { emoji: '🥗', cls: 'float-b', top: '30%',  left: '75%',  size: '5xl' },
  { emoji: '🍝', cls: 'float-c', top: '55%',  left: '62%',  size: '3xl' },
  { emoji: '🥘', cls: 'float-a', top: '70%',  left: '78%',  size: '4xl' },
  { emoji: '🍱', cls: 'float-b', top: '15%',  left: '88%',  size: '3xl' },
  { emoji: '🥑', cls: 'float-c', top: '45%',  left: '90%',  size: '2xl' },
  { emoji: '🫕', cls: 'float-a', top: '80%',  left: '55%',  size: '3xl' },
];

const stagger = {
  hidden: {},
  show: { transition: { staggerChildren: 0.12 } },
};
const fadeUp = {
  hidden: { opacity: 0, y: 28 },
  show:   { opacity: 1, y: 0, transition: { duration: 0.55, ease: 'easeOut' } },
};

export const FrontPage = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();

  const features = t('frontPage.features', { returnObjects: true }) as Array<{
    title: string; description: string;
  }>;

  return (
    <Stack spacing={16}>

      {/* ── Hero ── */}
      <Box
        position="relative"
        minH="520px"
        borderRadius="3xl"
        overflow="hidden"
        bgGradient="linear(135deg, teal.600 0%, teal.400 50%, orange.300 100%)"
        px={{ base: 8, md: 16 }}
        py={{ base: 14, md: 20 }}
        shadow="2xl"
      >
        {/* Floating food decorations */}
        {FOOD_ITEMS.map((f) => (
          <Box
            key={f.emoji}
            className={f.cls}
            position="absolute"
            top={f.top}
            left={f.left}
            fontSize={f.size}
            opacity={0.85}
            pointerEvents="none"
            userSelect="none"
          >
            {f.emoji}
          </Box>
        ))}

        {/* Hero text */}
        <Box position="relative" zIndex={2} maxW="xl">
          <MotionBox
            variants={stagger}
            initial="hidden"
            animate="show"
          >
            <MotionBox variants={fadeUp}>
              <HStack mb={4}>
                <Box bg="whiteAlpha.300" px={3} py={1} borderRadius="full">
                  <Text color="white" fontSize="xs" fontWeight={700} letterSpacing="wider">
                    ✨ RECIPE WIZ
                  </Text>
                </Box>
              </HStack>
            </MotionBox>

            <MotionBox variants={fadeUp}>
              <Heading
                fontSize={{ base: '3xl', md: '5xl' }}
                fontWeight={900}
                color="white"
                lineHeight={1.1}
                letterSpacing="-1px"
                mb={5}
              >
                {t('frontPage.headline')}
              </Heading>
            </MotionBox>

            <MotionBox variants={fadeUp}>
              <Text
                fontSize={{ base: 'md', md: 'lg' }}
                color="whiteAlpha.900"
                maxW="lg"
                lineHeight={1.7}
                mb={8}
              >
                {t('frontPage.subheadline')}
              </Text>
            </MotionBox>

            <MotionBox variants={fadeUp}>
              <HStack spacing={4} flexWrap="wrap">
                <Button
                  size="lg"
                  bg="white"
                  color="teal.700"
                  fontWeight={800}
                  borderRadius="2xl"
                  px={8}
                  _hover={{ bg: 'orange.50', transform: 'translateY(-2px)', shadow: 'xl' }}
                  transition="all 0.2s"
                  onClick={() => navigate('/recipes')}
                  leftIcon={<span>🔍</span>}
                >
                  {t('frontPage.exploreRecipes')}
                </Button>
                <Button
                  size="lg"
                  variant="outline"
                  borderColor="whiteAlpha.700"
                  color="white"
                  fontWeight={700}
                  borderRadius="2xl"
                  px={8}
                  _hover={{ bg: 'whiteAlpha.200', transform: 'translateY(-2px)' }}
                  transition="all 0.2s"
                  onClick={() => navigate('/meal-planner')}
                  leftIcon={<span>📅</span>}
                >
                  {t('frontPage.openMealPlanner')}
                </Button>
              </HStack>
            </MotionBox>
          </MotionBox>
        </Box>

        {/* Decorative bottom wave */}
        <Box
          position="absolute"
          bottom={0}
          left={0}
          right={0}
          h="60px"
          bgGradient="linear(to-t, #fffaf5, transparent)"
        />
      </Box>

      {/* ── Features ── */}
      <Stack spacing={8}>
        <VStack spacing={2} textAlign="center">
          <Text
            fontSize="xs"
            fontWeight={800}
            letterSpacing="widest"
            color="orange.400"
            textTransform="uppercase"
          >
            ★ FEATURES
          </Text>
          <Heading fontSize={{ base: '2xl', md: '3xl' }} fontWeight={800} letterSpacing="-0.5px">
            {t('frontPage.featuresTitle')}
          </Heading>
        </VStack>

        <Grid templateColumns={{ base: '1fr', md: 'repeat(2, 1fr)' }} gap={6}>
          {features.map((feature, i) => (
            <MotionBox
              key={feature.title}
              initial={{ opacity: 0, y: 30 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ duration: 0.5, delay: i * 0.1 }}
              whileHover={{ y: -6, transition: { duration: 0.2 } }}
            >
              <Box
                bg="white"
                borderRadius="2xl"
                p={8}
                shadow="md"
                border="1px solid"
                borderColor="orange.100"
                h="full"
                cursor="default"
                _hover={{ shadow: 'xl', borderColor: 'teal.200' }}
                transition="all 0.25s"
              >
                <HStack spacing={4} mb={4} align="flex-start">
                  <Box
                    fontSize="3xl"
                    bg="teal.50"
                    w={14}
                    h={14}
                    borderRadius="2xl"
                    display="flex"
                    alignItems="center"
                    justifyContent="center"
                    flexShrink={0}
                  >
                    {FEATURE_ICONS[i]}
                  </Box>
                  <Heading fontSize="lg" fontWeight={700} color="gray.800" pt={1}>
                    {feature.title}
                  </Heading>
                </HStack>
                <Text color="gray.500" lineHeight={1.7} fontSize="sm">
                  {feature.description}
                </Text>
              </Box>
            </MotionBox>
          ))}
        </Grid>
      </Stack>

      {/* ── CTA Banner ── */}
      <MotionBox
        initial={{ opacity: 0, scale: 0.97 }}
        whileInView={{ opacity: 1, scale: 1 }}
        viewport={{ once: true }}
        transition={{ duration: 0.5 }}
      >
        <Box
          bgGradient="linear(135deg, orange.400, teal.500)"
          borderRadius="3xl"
          p={{ base: 10, md: 14 }}
          textAlign="center"
          shadow="xl"
        >
          <Text fontSize="4xl" mb={3}>🥘</Text>
          <Heading fontSize={{ base: 'xl', md: '3xl' }} color="white" fontWeight={800} mb={3}>
            {t('frontPage.exploreRecipes')} →
          </Heading>
          <Text color="whiteAlpha.900" mb={7} fontSize="md">
            {t('frontPage.subheadline')}
          </Text>
          <HStack justify="center" spacing={4} flexWrap="wrap">
            <Button
              size="lg"
              bg="white"
              color="teal.700"
              fontWeight={800}
              borderRadius="2xl"
              px={10}
              _hover={{ bg: 'orange.50', transform: 'translateY(-2px)', shadow: 'xl' }}
              transition="all 0.2s"
              onClick={() => navigate('/recipes')}
            >
              {t('frontPage.exploreRecipes')}
            </Button>
            <Button
              size="lg"
              variant="outline"
              borderColor="whiteAlpha.700"
              color="white"
              fontWeight={700}
              borderRadius="2xl"
              px={10}
              _hover={{ bg: 'whiteAlpha.200', transform: 'translateY(-2px)' }}
              transition="all 0.2s"
              onClick={() => navigate('/meal-planner')}
            >
              {t('frontPage.openMealPlanner')}
            </Button>
          </HStack>
        </Box>
      </MotionBox>

    </Stack>
  );
};
