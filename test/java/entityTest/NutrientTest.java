package entityTest;

import entity.Nutrient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NutrientTest {

    private Nutrient nutrient;

    @Before
    public void setUp() {
        // Initialize the Nutrient object before each test
        nutrient = new Nutrient("Vitamin A");
    }

    @Test
    public void testConstructor() {
        // Verify that the constructor correctly sets the nutrientInfo field
        assertNotNull(nutrient);
        assertEquals("Vitamin A", nutrient.getNutrientInfo());
    }

    @Test
    public void testGetNutrientInfo() {
        // Verify that the getter returns the correct nutrientInfo
        assertEquals("Vitamin A", nutrient.getNutrientInfo());
    }

    @Test
    public void testSetNutrientInfo() {
        // Set a new nutrientInfo value and verify that it's updated
        nutrient.setNutrientInfo("Vitamin C");
        assertEquals("Vitamin C", nutrient.getNutrientInfo());
    }

    @Test
    public void testSetNutrientInfo_emptyString() {
        // Set an empty string and verify that the setter works correctly
        nutrient.setNutrientInfo("");
        assertEquals("", nutrient.getNutrientInfo());
    }

    @Test
    public void testSetNutrientInfo_nullValue() {
        // Set null and verify that the setter allows null values
        nutrient.setNutrientInfo(null);
        assertNull(nutrient.getNutrientInfo());
    }
}
