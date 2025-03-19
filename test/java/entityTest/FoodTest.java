package entityTest;

import entity.Food;
import entity.Nutrition;
import org.junit.Test;

import static org.junit.Assert.*;

public class FoodTest {

    @Test
    public void testFoodInitialization() {
        // Use a simple Nutrition object
        Nutrition nutrition = new Nutrition(250.0, 10.0, 8.0, 10.0, 10.0, 10.0);

        // Create a Food object
        Food food = new Food(1, "Apple", "Fruit", "grams", 100.0, nutrition);

        // Assert Food attributes
        assertEquals(1, food.getId());
        assertEquals("Apple", food.getName());
        assertEquals("Fruit", food.getCategory());
        assertEquals("grams", food.getUnit());
        assertEquals(100.0, food.getServingSize(), 0.0); // Use delta for double comparison

        // Assert that the Nutrition object is correctly stored
        assertSame(nutrition, food.getNutrition());
    }

    @Test
    public void testSettersAndGetters() {
        // Use a simple Nutrition object
        Nutrition initialNutrition = new Nutrition(300.0, 20.0, 15.0, 10.0, 10.0, 10.0);

        // Create a Food object
        Food food = new Food(1, "Banana", "Fruit", "grams", 150.0, initialNutrition);

        // Update Food attributes
        food.setId(2);
        food.setName("Orange");
        food.setCategory("Citrus");
        food.setUnit("pieces");
        food.setServingSize(200.0);

        // Update Nutrition object
        Nutrition updatedNutrition = new Nutrition(400.0, 25.0, 20.0, 10.0, 10.0, 10.0);
        food.setNutrition(updatedNutrition);

        // Assert updated Food attributes
        assertEquals(2, food.getId());
        assertEquals("Orange", food.getName());
        assertEquals("Citrus", food.getCategory());
        assertEquals("pieces", food.getUnit());
        assertEquals(200.0, food.getServingSize(), 0.0); // Use delta for double comparison

        // Assert updated Nutrition object
        assertSame(updatedNutrition, food.getNutrition());
    }

    @Test
    public void testNullValues() {
        // Create a Food object with null values for optional attributes
        Food food = new Food(3, null, null, null, 50.0, null);

        // Assert null attributes
        assertNull(food.getName());
        assertNull(food.getCategory());
        assertNull(food.getUnit());
        assertNull(food.getNutrition());

        // Assert non-null attributes
        assertEquals(3, food.getId());
        assertEquals(50.0, food.getServingSize(), 0.0); // Use delta for double comparison
    }

    @Test
    public void testNegativeServingSize() {
        // Test Food with a negative serving size
        Food food = new Food(4, "Ice Cream", "Dessert", "grams", -150.0, null);

        // Assert negative serving size (if not validated)
        assertEquals(-150.0, food.getServingSize(), 0.0); // Use delta for double comparison
    }

    @Test
    public void testSetInvalidServingSize() {
        // Create a Food object
        Food food = new Food(5, "Yogurt", "Dairy", "grams", 200.0, null);

        // Update to an invalid serving size
        food.setServingSize(-50.0);

        // Assert that the serving size was updated (assuming no validation logic exists)
        assertEquals(-50.0, food.getServingSize(), 0.0); // Use delta for double comparison
    }
}