import {
  Box,
  Button,
  Checkbox,
  CheckboxGroup,
  Drawer,
  DrawerBody,
  DrawerCloseButton,
  DrawerContent,
  DrawerFooter,
  DrawerHeader,
  DrawerOverlay,
  Heading,
  Stack,
} from '@chakra-ui/react';
import { useEffect, useState } from 'react';
import { CUISINE_OPTIONS, DIET_OPTIONS, HEALTH_OPTIONS } from '../constants';
import type { SearchRestrictions } from '../types';

interface RestrictionDrawerProps {
  isOpen: boolean;
  onClose: () => void;
  restrictions: SearchRestrictions;
  onApply: (restrictions: SearchRestrictions) => void;
}

export const RestrictionDrawer = ({
  isOpen,
  onClose,
  restrictions,
  onApply,
}: RestrictionDrawerProps) => {
  const [localRestrictions, setLocalRestrictions] = useState<SearchRestrictions>(restrictions);

  useEffect(() => {
    if (isOpen) {
      setLocalRestrictions(restrictions);
    }
  }, [isOpen, restrictions]);

  const handleApply = () => {
    onApply(localRestrictions);
    onClose();
  };

  const renderGroup = (title: string, options: string[], key: keyof SearchRestrictions) => (
    <Box>
      <Heading as="h3" fontSize="md" mb={3} color="teal.600">
        {title}
      </Heading>
      <CheckboxGroup
        value={localRestrictions[key]}
        onChange={(values) =>
          setLocalRestrictions((prev) => ({ ...prev, [key]: values as string[] }))
        }
      >
        <Stack spacing={2}>
          {options.map((option) => (
            <Checkbox key={option} value={option}>
              {option}
            </Checkbox>
          ))}
        </Stack>
      </CheckboxGroup>
    </Box>
  );

  return (
    <Drawer isOpen={isOpen} placement="right" onClose={onClose} size="md">
      <DrawerOverlay />
      <DrawerContent>
        <DrawerCloseButton />
        <DrawerHeader borderBottomWidth="1px">Apply Restrictions</DrawerHeader>
        <DrawerBody>
          <Stack spacing={6}>
            {renderGroup('Diet Labels', DIET_OPTIONS, 'diet')}
            {renderGroup('Health Labels', HEALTH_OPTIONS, 'health')}
            {renderGroup('Cuisine Types', CUISINE_OPTIONS, 'cuisine')}
          </Stack>
        </DrawerBody>
        <DrawerFooter borderTopWidth="1px">
          <Button variant="outline" mr={3} onClick={onClose}>
            Cancel
          </Button>
          <Button colorScheme="teal" onClick={handleApply}>
            Apply
          </Button>
        </DrawerFooter>
      </DrawerContent>
    </Drawer>
  );
};
