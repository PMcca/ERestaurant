package com.se.pete.e_restaurant;

import android.text.Editable;

import com.se.pete.e_restaurant.restaurant.Chef;
import com.se.pete.e_restaurant.restaurant.FoodItem;
import com.se.pete.e_restaurant.restaurant.Manager;
import com.se.pete.e_restaurant.restaurant.Menu;
import com.se.pete.e_restaurant.restaurant.Registry;
import com.se.pete.e_restaurant.restaurant.Staff;
import com.se.pete.e_restaurant.restaurant.Waiter;

import java.util.List;
import java.util.Map;

/**
 * General set of tools used by multiple parts of program
 */

public final class Res
{
    /*
    * Check for any empty input
    * True = at least one Editable is empty
    * False = all have content
    */
    public static boolean isEmpty(Editable... content)
    {
        for(Editable e : content)
        {
            if(e.toString().matches("")) //Check for empty string
                return true;
        }
        return false;
    }

    // Load the menu with initial FoodItems
    // DESTRUCTIVE on menu
    public static void loadMenu(Menu menu)
    {
        menu.addItem(new FoodItem("Steamed Ham", 8.75f, 8));
        menu.addItem(new FoodItem("Fries", 1.5f, 15));
        menu.addItem(new FoodItem("Salad", 2.3f, 6));
        menu.addItem(new FoodItem("Chicken Burger", 7.0f, 11));
        menu.addItem(new FoodItem("Toast", 0.95f, 14));
        menu.addItem(new FoodItem("Full English", 5.5f, 10));
        menu.addItem(new FoodItem("Coca Cola", 0.99f, 42));
        menu.addItem(new FoodItem("Fanta", 0.99f, 19));
        menu.addItem(new FoodItem("Water", 0f, 99));
        menu.addItem(new FoodItem("Cheesecake", 6.6f, 3));
        menu.addItem(new FoodItem("Steamed Clam", 8.2f, 1));
    }

    public static void loadStaff()
    {
        if(Registry.getInstance().getStaffList().isEmpty()) {
            List<Staff> sList = Registry.getInstance().getStaffList();
            sList.add(new Manager(0, "root", "a"));
            sList.add(new Manager(Staff.getIdCounter(), "Simon", "Password123"));
            sList.add(new Waiter(Staff.getIdCounter(), "Peggy", "brucewayne"));
            sList.add(new Waiter(Staff.getIdCounter(), "Colin", "penOverPencil"));
            sList.add(new Chef(Staff.getIdCounter(), "Greg", " "));
            sList.add(new Chef(Staff.getIdCounter(), "Pequad", "increaser"));
        }
    }

    // Load amountInOrder map with all 0s
    // DESTRUCTIVE on amountsInOrder
    public static void loadAmountInOrder(Map<FoodItem, Integer> amounts, Menu menu)
    {
        for(FoodItem item : menu.getItems())
        {
            amounts.put(item, 0);
        }
    }
}
