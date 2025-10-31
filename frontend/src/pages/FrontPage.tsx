import { Box, Button, Heading, SimpleGrid, Stack, Text } from '@chakra-ui/react';
import { useNavigate } from 'react-router-dom';

const FEATURES = [
  {
    title: 'Smart Recipe Search',
    description: 'Discover recipes from your ingredients and tailor them with dietary filters.',
  },
  {
    title: 'Detailed Nutrition Insights',
    description: 'Understand calories and key nutrients before you start cooking.',
  },
  {
    title: 'Personal Meal Planner',
    description: 'Organize meals for the whole week and stay on track effortlessly.',
  },
  {
    title: 'Serving Adjustments',
    description: 'Scale recipes up or down with a single click and keep ingredient ratios balanced.',
  },
];

export const FrontPage = () => {
  const navigate = useNavigate();

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
            Plan meals, discover recipes, and eat smarter with Recipe Wiz.
          </Heading>
          <Text fontSize={{ base: 'md', md: 'lg' }} opacity={0.85}>
            We help you keep the experience you loved in the desktop app, now delivered through a modern,
            responsive web experience. Start by searching for delicious recipes or build your weekly plan.
          </Text>
          <Stack direction={{ base: 'column', sm: 'row' }} spacing={4}>
            <Button
              colorScheme="whiteAlpha"
              variant="solid"
              size="lg"
              onClick={() => navigate('/recipes')}
            >
              Explore Recipes
            </Button>
            <Button
              colorScheme="blackAlpha"
              variant="outline"
              size="lg"
              onClick={() => navigate('/meal-planner')}
            >
              Open Meal Planner
            </Button>
          </Stack>
        </Stack>
      </Box>

      <Stack spacing={6}>
        <Heading fontSize={{ base: '2xl', md: '3xl' }} color="teal.700">
          Everything you need, now on the web
        </Heading>
        <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
          {FEATURES.map((feature) => (
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
