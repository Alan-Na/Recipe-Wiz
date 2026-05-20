import { Button, Flex, HStack, Link as ChakraLink, Spacer, Text } from '@chakra-ui/react';
import { NavLink, Outlet } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

export const AppLayout = () => {
  const { t, i18n } = useTranslation();
  const isZh = i18n.language === 'zh';

  const NAV_ITEMS = [
    { label: t('nav.home'), to: '/' },
    { label: t('nav.recipeSearch'), to: '/recipes' },
    { label: t('nav.mealPlanner'), to: '/meal-planner' },
  ];

  return (
    <Flex direction="column" minH="100vh" bg="gray.50">
      <Flex
        as="header"
        bg="white"
        shadow="sm"
        px={{ base: 4, md: 10 }}
        py={4}
        align="center"
        position="sticky"
        top={0}
        zIndex={10}
      >
        <Text fontSize="xl" fontWeight="bold" color="teal.600">
          Recipe Wiz
        </Text>
        <Spacer />
        <HStack spacing={4}>
          {NAV_ITEMS.map((item) => (
            <ChakraLink
              key={item.to}
              as={NavLink}
              to={item.to}
              px={3}
              py={2}
              borderRadius="md"
              _hover={{ textDecoration: 'none', bg: 'teal.50' }}
              _activeLink={{ bg: 'teal.500', color: 'white' }}
            >
              {item.label}
            </ChakraLink>
          ))}
          <Button
            size="sm"
            variant="outline"
            colorScheme="teal"
            onClick={() => i18n.changeLanguage(isZh ? 'en' : 'zh')}
            minW="60px"
            fontWeight="semibold"
          >
            {isZh ? 'EN' : '中文'}
          </Button>
        </HStack>
      </Flex>
      <Flex as="main" flex="1" px={{ base: 4, md: 10 }} py={8}>
        <Outlet />
      </Flex>
      <Flex as="footer" bg="white" py={6} justify="center" mt="auto" shadow="inner">
        <Text color="gray.500" fontSize="sm">
          {t('footer.rights', { year: new Date().getFullYear() })}
        </Text>
      </Flex>
    </Flex>
  );
};
