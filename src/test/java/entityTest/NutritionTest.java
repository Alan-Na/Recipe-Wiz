package entityTest;

import entity.Nutrition;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NutritionTest {

    private Nutrition nutrition;

    @Before
    public void setUp() {
        // Initialize the Nutrition object with sample data before each test
        nutrition = new Nutrition(100.0, 5.0, 2.0, 30.0, 10.0, 15.0);
    }

    @Test
    public void testConstructor() {
        // Verify that the object is correctly initialized by the constructor
        assertNotNull(nutrition);
        assertEquals(100.0, nutrition.getCalories(), 0.0);
        assertEquals(5.0, nutrition.getProtein(), 0.0);
        assertEquals(2.0, nutrition.getFat(), 0.0);
        assertEquals(30.0, nutrition.getCarbohydrates(), 0.0);
        assertEquals(10.0, nutrition.getFiber(), 0.0);
        assertEquals(15.0, nutrition.getSugar(), 0.0);
    }

    @Test
    public void testFormatNutritionInfo() {
        // Verify the formatNutritionInfo method produces the correct formatted string
        String expectedInfo = "Calories: 100.00, Protein: 5.00 g, Fat: 2.00 g, Carbohydrates: 30.00 g, Fiber: 10.00 g, Sugar: 15.00 g";
        assertEquals(expectedInfo, nutrition.formatNutritionInfo());
    }

    @Test
    public void testGetCalories() {
        // Verify that the getter for calories returns the correct value
        assertEquals(100.0, nutrition.getCalories(), 0.0);
    }

    @Test
    public void testSetCalories() {
        // Set new value for calories and verify it's updated correctly
        nutrition.setCalories(200.0);
        assertEquals(200.0, nutrition.getCalories(), 0.0);
    }

    @Test
    public void testGetProtein() {
        // Verify that the getter for protein returns the correct value
        assertEquals(5.0, nutrition.getProtein(), 0.0);
    }

    @Test
    public void testSetProtein() {
        // Set new value for protein and verify it's updated correctly
        nutrition.setProtein(10.0);
        assertEquals(10.0, nutrition.getProtein(), 0.0);
    }

    @Test
    public void testGetFat() {
        // Verify that the getter for fat returns the correct value
        assertEquals(2.0, nutrition.getFat(), 0.0);
    }

    @Test
    public void testSetFat() {
        // Set new value for fat and verify it's updated correctly
        nutrition.setFat(4.0);
        assertEquals(4.0, nutrition.getFat(), 0.0);
    }

    @Test
    public void testGetCarbohydrates() {
        // Verify that the getter for carbohydrates returns the correct value
        assertEquals(30.0, nutrition.getCarbohydrates(), 0.0);
    }

    @Test
    public void testSetCarbohydrates() {
        // Set new value for carbohydrates and verify it's updated correctly
        nutrition.setCarbohydrates(40.0);
        assertEquals(40.0, nutrition.getCarbohydrates(), 0.0);
    }

    @Test
    public void testGetFiber() {
        // Verify that the getter for fiber returns the correct value
        assertEquals(10.0, nutrition.getFiber(), 0.0);
    }

    @Test
    public void testSetFiber() {
        // Set new value for fiber and verify it's updated correctly
        nutrition.setFiber(15.0);
        assertEquals(15.0, nutrition.getFiber(), 0.0);
    }

    @Test
    public void testGetSugar() {
        // Verify that the getter for sugar returns the correct value
        assertEquals(15.0, nutrition.getSugar(), 0.0);
    }

    @Test
    public void testSetSugar() {
        // Set new value for sugar and verify it's updated correctly
        nutrition.setSugar(20.0);
        assertEquals(20.0, nutrition.getSugar(), 0.0);
    }
}