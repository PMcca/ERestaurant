package com.se.pete.e_restaurant.restaurant;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single Order made by a diner. Consists of multiple items of food, which sum together
 * to form a final Bill.
 */

public class Order implements Serializable
{
    private static int idCounter = 1;

    private String orderStatus;
    private int orderID;

    private float totalPrice;
    private int estWaitTime;

    private Registry registry;

    private Customer customer;

    private Bill bill;

    private List<FoodItem> orderItems; // Items of food in order

    private List<Staff> staffList;

    public Order()
    {
        this.orderItems = new ArrayList<>();
        this.staffList = new ArrayList<>();
        this.orderStatus = "CREATING";
    }

    public void addItem(FoodItem item)
    {
        orderItems.add(item);
        increasePrice(item);
    }

    public void removeItem(FoodItem item)
    {
        orderItems.remove(item);
        decreasePrice(item);
    }

    // Decrease the actual stock of the items in the order
    public void finaliseStockDecrease()
    {
        for(FoodItem item : this.orderItems)
        {
            item.decreaseStock();

            // Change menu as well (A-Studio workaround)
            Menu.getInstance().updateItemStock(item.getName(), item.getStock());
        }
    }

    // Returns a list of orders which are "PENDING" in status
    public static List<Order> getPendingOrders(List<Order> orders)
    {
        List<Order> sortedOrders = new ArrayList<Order>();
        for(Order o : orders)
        {
            if(o.getStatus().equals("PENDING"))
                sortedOrders.add(o);
        }
        return sortedOrders;
    }

    public void setBill(Bill b)
    {
        this.bill = b;
    }


    public void updateStatus(String s)
    {
        this.orderStatus = s;
        customer.updateChangedOrder(this);
    }

    public void calculateWaitTime()
    {} //STUB

    public void addCustomer(Customer c) {this.customer = c;}


    private void increasePrice(FoodItem item)
    {
        this.totalPrice += item.getPrice();
    }

    private void decreasePrice(FoodItem item)
    {
        this.totalPrice -= item.getPrice();
    }

    public void setID() {this.orderID = idCounter++;}



    public boolean hasItem(FoodItem item)
    {
        return this.orderItems.contains(item);
    }

    public int getOrderID(){return this.orderID;}

    public List<FoodItem> getOrderItems(){return this.orderItems;}

    public float getTotalPrice() {return this.totalPrice;}

    public String getStatus(){return this.orderStatus;}

    public int getOrderSize(){return this.orderItems.size();}
}
