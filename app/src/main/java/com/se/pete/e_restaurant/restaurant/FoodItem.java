package com.se.pete.e_restaurant.restaurant;

import java.io.Serializable;

/**
 * A FoodItem represents a single item of food available at the restaurant. A FoodItem has a
 * name, price and an amount of stock at any given time.
 * Created by Pete on 19/03/2018.
 */

public class FoodItem implements Serializable
{
    private String name;
    private float price;
    private int stock;

    private Menu menu;

    public FoodItem(String nme, float prce, int stk)
    {
        this.name = nme;
        this.price = prce;

        if(stk > 0)
            this.stock = stk;
        else
            this.stock = 1;

        menu = Menu.getInstance();
    }

    public void increaseStock()
    {this.stock++;}

    public void decreaseStock()
    {this.stock--;}

    public boolean isInStock()
    {return this.stock>0;}

    public String getName() {return name;}

    public float getPrice() {return price;}

    public int getStock() {return stock;}

    public void setStock(int newStock){this.stock = newStock;}
}
