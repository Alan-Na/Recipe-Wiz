package entity;

/**
 * The class of food.
 */
public class Food {

    private int id;
    private String name;
    private String category;
    private String unit;
    private double servingSize;

    private Nutrition nutrition;

    public Food(int id, String name, String category, String unit, double servingSize, Nutrition nutrition) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.servingSize = servingSize;
        this.nutrition = nutrition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getServingSize() {
        return servingSize;
    }

    public void setServingSize(double servingSize) {
        this.servingSize = servingSize;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }
}

