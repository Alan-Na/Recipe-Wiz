import {
  Box, Flex, HStack, Link as ChakraLink, Spacer, Text, Button, Container,
} from '@chakra-ui/react';
import { NavLink, Outlet } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { motion, AnimatePresence } from 'framer-motion';

const MotionBox = motion(Box);

export const AppLayout = () => {
  const { t, i18n } = useTranslation();
  const isZh = i18n.language === 'zh';

  const NAV_ITEMS = [
    { label: t('nav.home'), to: '/' },
    { label: t('nav.recipeSearch'), to: '/recipes' },
    { label: t('nav.mealPlanner'), to: '/meal-planner' },
  ];

  return (
    <Flex direction="column" minH="100vh">
      {/* ── Navbar ── */}
      <Box
        as="header"
        position="sticky"
        top={0}
        zIndex={100}
        bg="rgba(255,250,245,0.85)"
        backdropFilter="blur(12px)"
        borderBottom="1px solid"
        borderColor="orange.100"
        px={{ base: 4, md: 10 }}
        py={3}
      >
        <Flex align="center" maxW="1400px" mx="auto">
          {/* Logo */}
          <HStack spacing={2}>
            <Text fontSize="2xl">🍳</Text>
            <Text fontSize="lg" fontWeight="800" letterSpacing="-0.5px">
              <Box as="span" color="teal.600">Recipe</Box>
              <Box as="span" color="orange.400">Wiz</Box>
            </Text>
          </HStack>

          <Spacer />

          {/* Nav links */}
          <HStack spacing={1} mr={4}>
            {NAV_ITEMS.map((item) => (
              <ChakraLink
                key={item.to}
                as={NavLink}
                to={item.to}
                end={item.to === '/'}
                px={4}
                py={2}
                borderRadius="xl"
                fontWeight={500}
                fontSize="sm"
                color="gray.600"
                _hover={{ textDecoration: 'none', bg: 'teal.50', color: 'teal.700' }}
                _activeLink={{ bg: 'teal.500', color: 'white', fontWeight: 700 }}
                transition="all 0.2s"
              >
                {item.label}
              </ChakraLink>
            ))}
          </HStack>

          {/* Language switcher */}
          <AnimatePresence mode="wait">
            <MotionBox
              key={i18n.language}
              initial={{ opacity: 0, y: -6 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: 6 }}
              transition={{ duration: 0.2 }}
            >
              <Button
                size="sm"
                variant="outline"
                colorScheme="orange"
                borderRadius="full"
                px={4}
                fontWeight={700}
                fontSize="xs"
                onClick={() => i18n.changeLanguage(isZh ? 'en' : 'zh')}
                _hover={{ bg: 'orange.50' }}
              >
                {isZh ? '🇺🇸 EN' : '🇨🇳 中文'}
              </Button>
            </MotionBox>
          </AnimatePresence>
        </Flex>
      </Box>

      {/* ── Main ── */}
      <Box as="main" flex="1" bg="#fffaf5">
        <Container maxW="1400px" px={{ base: 4, md: 10 }} py={8}>
          <Outlet />
        </Container>
      </Box>

      {/* ── Footer ── */}
      <Box
        as="footer"
        borderTop="1px solid"
        borderColor="orange.100"
        bg="rgba(255,250,245,0.9)"
        py={5}
        textAlign="center"
      >
        <HStack justify="center" spacing={2}>
          <Text fontSize="xl">🍳</Text>
          <Text color="gray.400" fontSize="sm">
            {t('footer.rights', { year: new Date().getFullYear() })}
          </Text>
        </HStack>
      </Box>
    </Flex>
  );
};
