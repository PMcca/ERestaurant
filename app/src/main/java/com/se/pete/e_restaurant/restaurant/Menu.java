package com.se.pete.e_restaurant.restaurant;

import android.support.annotation.Nullable;

import com.se.pete.e_restaurant.Res;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu is the list of available items of food that the restaurant offers.
 * It is composed of a list of FoodItem objects.
 */

public class Menu implements Serializable
{
    // Singleton
    private static final Menu ourInstance = new Menu();
    public static Menu getInstance() {
        return ourInstance;
    }

    private Menu()
    {
        items = new ArrayList<>();
        Res.loadMenu(this);
    }

    private static List<FoodItem> items;

    public List<FoodItem> getItems(){return this.items;}

    public void addItem(FoodItem item)
    {
        items.add(item);
    }

    public void removeItem(FoodItem item)
    {
        items.remove(item);
    }

    /*
    * Due to the way A-Studio handles object references, this method
    * is needed to reflect any changes to stock with order confirms.
     */
    public void updateItemStock(String name, int newStock)
    {
        for(FoodItem item : items)
        {
            if(item.getName().equals(name))
            {
                item.setStock(newStock);
                return;
            }
        }
    }

    @Nullable
    public FoodItem searchItemByName(String name)
    {
        for(FoodItem fi : items)
        {
            if(fi.getName().equals(name))
                return fi;
        }

        return null;
    }


}
