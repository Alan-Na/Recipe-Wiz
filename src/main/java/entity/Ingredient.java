package entity;

/**
 * The class represents an ingredient in a recipe.
 */
public final class Ingredient {

    private final int ingredientId;
    private final String name;
    private double quantity;
    private final String unit;

    /**
     * Constructor for the Ingredient class.
     *
     * @param ingredientId The unique identifier of the ingredient.
     * @param name         The name of the ingredient.
     * @param quantity     The quantity of the ingredient.
     * @param unit         The unit of measurement for the ingredient.
     * @throws IllegalArgumentException if quantity is negative or name/unit is null or empty.
     */
    public Ingredient(int ingredientId, String name, double quantity, String unit) {

        this.ingredientId = ingredientId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    /**
     * Scales the ingredient quantity by a given factor.
     *
     * @param factor The scaling factor (e.g., 2.0 for doubling the quantity).
     * @return A new Ingredient object with the scaled quantity.
     * @throws IllegalArgumentException if factor is negative.
     */
    public Ingredient scaleQuantity(double factor) {
        return new Ingredient(ingredientId, name, quantity * factor, unit);
    }

}
