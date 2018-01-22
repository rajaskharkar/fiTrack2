package com.trial.rajas.fitrack;

/**
 * Created by rajas on 1/3/2018.
 */

public class Ingredient {

    public Ingredient(String name, Float quantity, String unit, Float calorieCount) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.calorie_count= calorieCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getCalorieCount() {
        return calorie_count;
    }

    public void setCalorieCount(Float calorieCount) {
        this.calorie_count = calorie_count;
    }

    String name;
    Float quantity;
    String unit;
    Float calorie_count;
}
