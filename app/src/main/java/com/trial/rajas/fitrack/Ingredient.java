package com.trial.rajas.fitrack;

/**
 * Created by rajas on 1/3/2018.
 */

public class Ingredient {

    public Ingredient(String name, Float quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
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

    String name;
    Float quantity;
    String unit;
}
