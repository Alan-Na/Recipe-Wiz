import { Box, Button, Heading, SimpleGrid, Stack, Text } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

export const FrontPage = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();

  const features = t('frontPage.features', { returnObjects: true }) as Array<{
    title: string;
    description: string;
  }>;

  return (
    <Stack w="full" spacing={12}>
      <Box
        bgGradient="linear(to-r, teal.500, teal.300)"
        color="white"
        borderRadius="3xl"
        px={{ base: 8, md: 16 }}
        py={{ base: 14, md: 20 }}
        shadow="xl"
      >
        <Stack spacing={6} maxW="3xl">
          <Heading fontSize={{ base: '3xl', md: '5xl' }} fontWeight="extrabold">
            {t('frontPage.headline')}
          </Heading>
          <Text fontSize={{ base: 'md', md: 'lg' }} opacity={0.85}>
            {t('frontPage.subheadline')}
          </Text>
          <Stack direction={{ base: 'column', sm: 'row' }} spacing={4}>
            <Button
              colorScheme="whiteAlpha"
              variant="solid"
              size="lg"
              onClick={() => navigate('/recipes')}
            >
              {t('frontPage.exploreRecipes')}
            </Button>
            <Button
              colorScheme="blackAlpha"
              variant="outline"
              size="lg"
              onClick={() => navigate('/meal-planner')}
            >
              {t('frontPage.openMealPlanner')}
            </Button>
          </Stack>
        </Stack>
      </Box>

      <Stack spacing={6}>
        <Heading fontSize={{ base: '2xl', md: '3xl' }} color="teal.700">
          {t('frontPage.featuresTitle')}
        </Heading>
        <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
          {features.map((feature) => (
            <Box
              key={feature.title}
              bg="white"
              borderRadius="2xl"
              p={8}
              shadow="md"
              border="1px solid"
              borderColor="teal.50"
            >
              <Heading fontSize="xl" color="teal.600" mb={3}>
                {feature.title}
              </Heading>
              <Text color="gray.600">{feature.description}</Text>
            </Box>
          ))}
        </SimpleGrid>
      </Stack>
    </Stack>
  );
};
