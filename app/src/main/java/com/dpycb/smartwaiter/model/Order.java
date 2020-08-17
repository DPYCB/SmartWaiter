package com.dpycb.smartwaiter.model;

public class Order {
    private int ID;
    private String FoodID;
    private String FoodName;
    private String Quantity;
    private String Price;
    private String Time;

    public Order() {
    }

    public Order(String foodID, String foodName, String quantity, String price, String time) {
        FoodID = foodID;
        FoodName = foodName;
        Quantity = quantity;
        Price = price;
        Time = time;
    }

    public Order(int ID, String foodID, String foodName, String quantity, String price, String time) {
        this.ID = ID;
        FoodID = foodID;
        FoodName = foodName;
        Quantity = quantity;
        Price = price;
        Time = time;
    }

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String foodID) {
        FoodID = foodID;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
