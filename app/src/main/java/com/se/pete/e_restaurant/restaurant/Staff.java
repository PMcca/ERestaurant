package com.se.pete.e_restaurant.restaurant;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a single staff member. A staff member must be specialised into either a chef,
 * manager or waiter.
 */

public abstract class Staff implements Serializable
{
    private static int idCounter = 0;
    private int staffID;
    private String name, password;

    protected List<Order> orders;

    public Staff(int id, String nme, String pw)
    {
        staffID = id;
        name = nme;
        password = pw;
        orders = Registry.getInstance().getOrderList();
    }

    public static void decrementCounter(){idCounter--;}


    public List<Order> getOrders(){return this.orders;}

    public int getID(){return this.staffID;}

    public String getName(){return this.name;}

    public String getPassword(){return this.password;}

    public static int getIdCounter(){idCounter += 1; return idCounter;}

}
