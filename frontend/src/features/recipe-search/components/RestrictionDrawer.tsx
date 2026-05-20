import {
  Box, Button, Checkbox, CheckboxGroup, Drawer, DrawerBody,
  DrawerCloseButton, DrawerContent, DrawerFooter, DrawerHeader,
  DrawerOverlay, Flex, Stack, Tag, Text,
} from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { CUISINE_OPTIONS, DIET_OPTIONS, HEALTH_OPTIONS } from '../constants';
import type { SearchRestrictions } from '../types';

interface RestrictionDrawerProps {
  isOpen: boolean;
  onClose: () => void;
  restrictions: SearchRestrictions;
  onApply: (restrictions: SearchRestrictions) => void;
}

const SECTION_META = {
  diet:    { emoji: '🥗', color: 'green',  bg: '#E8F5E9', dot: '#43A047' },
  health:  { emoji: '💚', color: 'teal',   bg: '#E0F2F1', dot: '#00897B' },
  cuisine: { emoji: '🌍', color: 'purple', bg: '#F3E5F5', dot: '#8E24AA' },
};

export const RestrictionDrawer = ({
  isOpen, onClose, restrictions, onApply,
}: RestrictionDrawerProps) => {
  const [local, setLocal] = useState<SearchRestrictions>(restrictions);
  const { t } = useTranslation();

  useEffect(() => {
    if (isOpen) setLocal(restrictions);
  }, [isOpen, restrictions]);

  const handleApply = () => { onApply(local); onClose(); };

  const totalSelected = local.diet.length + local.health.length + local.cuisine.length;

  const renderGroup = (
    title: string,
    options: string[],
    key: keyof SearchRestrictions,
  ) => {
    const meta = SECTION_META[key];
    return (
      <Box>
        <Flex align="center" gap={2} mb={3}>
          <Text fontSize="lg">{meta.emoji}</Text>
          <Text fontWeight={800} fontSize="sm" color="gray.700" textTransform="uppercase" letterSpacing="wider">
            {title}
          </Text>
          {local[key].length > 0 && (
            <Tag size="sm" bg={meta.dot} color="white" borderRadius="full" fontWeight={700}>
              {local[key].length}
            </Tag>
          )}
        </Flex>
        <Box bg={meta.bg} borderRadius="xl" p={4}>
          <CheckboxGroup
            value={local[key]}
            onChange={(values) =>
              setLocal((prev) => ({ ...prev, [key]: values as string[] }))
            }
          >
            <Stack spacing={2} maxH="200px" overflowY="auto"
              css={{ '&::-webkit-scrollbar': { width: '4px' }, '&::-webkit-scrollbar-thumb': { background: meta.dot, borderRadius: '4px' } }}
            >
              {options.map((option) => (
                <Checkbox
                  key={option}
                  value={option}
                  colorScheme={meta.color}
                  _checked={{ '& .chakra-checkbox__control': { bg: meta.dot, borderColor: meta.dot } }}
                >
                  <Text fontSize="sm" color="gray.700" fontWeight={500}>{option}</Text>
                </Checkbox>
              ))}
            </Stack>
          </CheckboxGroup>
        </Box>
      </Box>
    );
  };

  return (
    <Drawer isOpen={isOpen} placement="right" onClose={onClose} size="md">
      <DrawerOverlay bg="blackAlpha.400" backdropFilter="blur(4px)" />
      <DrawerContent borderLeftRadius="2xl" overflow="hidden">
        <DrawerCloseButton color="white" top={4} right={4} />

        {/* Gradient header */}
        <DrawerHeader p={0}>
          <Box bgGradient="linear(135deg, teal.500, teal.400)" px={6} pt={6} pb={5}>
            <Text fontSize="2xl" mb={1}>⚙️</Text>
            <Text fontWeight={800} color="white" fontSize="lg">
              {t('restrictionDrawer.title')}
            </Text>
            {totalSelected > 0 && (
              <Text color="whiteAlpha.800" fontSize="sm" mt={1}>
                {totalSelected} filter{totalSelected > 1 ? 's' : ''} active
              </Text>
            )}
          </Box>
        </DrawerHeader>

        <DrawerBody p={5} overflowY="auto">
          <Stack spacing={6}>
            {renderGroup(t('restrictionDrawer.dietLabels'), DIET_OPTIONS, 'diet')}
            {renderGroup(t('restrictionDrawer.healthLabels'), HEALTH_OPTIONS, 'health')}
            {renderGroup(t('restrictionDrawer.cuisineTypes'), CUISINE_OPTIONS, 'cuisine')}
          </Stack>
        </DrawerBody>

        <DrawerFooter borderTop="1px solid" borderColor="gray.100" gap={3}>
          <Button
            variant="ghost"
            borderRadius="xl"
            fontWeight={600}
            onClick={() => setLocal({ diet: [], health: [], cuisine: [] })}
            color="gray.500"
          >
            Clear all
          </Button>
          <Button variant="outline" borderRadius="xl" fontWeight={600} onClick={onClose} flex={1}>
            {t('restrictionDrawer.cancel')}
          </Button>
          <Button
            colorScheme="teal"
            borderRadius="xl"
            fontWeight={700}
            onClick={handleApply}
            flex={1}
            _hover={{ transform: 'translateY(-1px)', shadow: 'md' }}
            transition="all 0.2s"
          >
            {t('restrictionDrawer.apply')}
          </Button>
        </DrawerFooter>
      </DrawerContent>
    </Drawer>
  );
};
