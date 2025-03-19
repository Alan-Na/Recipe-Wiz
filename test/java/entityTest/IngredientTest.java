package entityTest;

import entity.Ingredient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class IngredientTest {

    @Test
    public void testConstructorAndGetters() {
        // Create an Ingredient object
        Ingredient ingredient = new Ingredient(1, "Flour", 2.0, "cups");

        // Test getters
        assertEquals(1, ingredient.getIngredientId());
        assertEquals("Flour", ingredient.getName());
        assertEquals(2.0, ingredient.getQuantity(), 0.01);
        assertEquals("cups", ingredient.getUnit());
    }

    @Test
    public void testSetQuantity() {
        // Create an Ingredient object
        Ingredient ingredient = new Ingredient(1, "Flour", 2.0, "cups");

        // Set a new quantity
        ingredient.setQuantity(3.5);

        // Verify the updated quantity
        assertEquals(3.5, ingredient.getQuantity(), 0.01);
    }

    @Test
    public void testScaleQuantity() {
        // Create an Ingredient object
        Ingredient ingredient = new Ingredient(1, "Flour", 2.0, "cups");

        // Scale the quantity
        Ingredient scaledIngredient = ingredient.scaleQuantity(2.5);

        // Verify that the original ingredient is unchanged
        assertEquals(2.0, ingredient.getQuantity(), 0.01);

        // Verify the new ingredient's scaled quantity
        assertEquals(5.0, scaledIngredient.getQuantity(), 0.01);
        assertEquals(ingredient.getName(), scaledIngredient.getName());
        assertEquals(ingredient.getUnit(), scaledIngredient.getUnit());
        assertEquals(ingredient.getIngredientId(), scaledIngredient.getIngredientId());
    }
}
