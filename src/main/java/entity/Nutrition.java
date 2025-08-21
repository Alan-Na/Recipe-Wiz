package entity;

/**
 * The class represents nutrition for food.
 */
public class Nutrition {

    private double calories;
    private double protein;
    private double fat;
    private double carbohydrates;
    private double fiber;
    private double sugar;

    public Nutrition(double calories, double protein, double fat, double carbohydrates,
                     double fiber, double sugar) {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.sugar = sugar;
    }

    /**
     * A method that formats the Nutrition Info into the structure required (A String).
     * @return String, a simple structure that illustrates all nutrition information.
     */
    public String formatNutritionInfo() {
        return String.format(
                "Calories: %.2f, Protein: %.2f g, Fat: %.2f g, Carbohydrates: %.2f g, Fiber: %.2f g, Sugar: %.2f g",
                calories, protein, fat, carbohydrates, fiber, sugar
        );
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getFiber() {
        return fiber;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getSugar() {
        return sugar;
    }

    public void setSugar(double sugar) {
        this.sugar = sugar;
    }
}
