package com.se.pete.e_restaurant.restaurant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Customer represents a diner (or group of diners) at the restaurant. A customer may make orders,
 * which will get tallied up into a payable Bill at the end of their time at the restaurant.
 */

public class Customer implements Serializable
{
    // Used to set the instance of this specific build per table
    private static int tableNumberCounter;
    private static int customerIDCounter = 1;

    private int tableNumber;
    private int customerID;

    private Waiter waiter;
    private Bill bill;
    private Menu menu;

    private List<Order> orders;

    public Customer()
    {
        tableNumber = tableNumberCounter;
        orders = new ArrayList<>();
        menu = Menu.getInstance();
        bill = new Bill(orders, this);
        customerID = customerIDCounter++;
    }

    public Order makeOrder()
    {
        Order o = new Order();
        return o;
    }

    public void submitOrder(Order order)
    {
        // Bind customer to submitted order
        this.orders.add(order);
        order.addCustomer(this);
    }

    /* Due to the way A-Studio handles objects, this is
     * required to update the customer object of the order
     * whose status was updated.
    */
    public void updateChangedOrder(Order newOrder)
    {
        for(int i = 0; i < orders.size(); i++)
        {
            if(orders.get(i).getOrderID() == newOrder.getOrderID())
            {
                // Update order list and Registry list of customers.
                orders.set(i, newOrder);
                Registry.getInstance().addCustomer(this);
            }

        }
    }

    public void requestWaiter()
    {
         // Stub- UI thread draws DialogWindow to screen in atvCustomerMain
    }

    public void payBill()
    {

    }

    public void checkOrderStatus()
    {

    }

    public void giveFeedback()
    {
        // Stub
    }

    // If a customer does not continue with an order/bill, decrement the counter
    public static void decrementCounter()
    {customerIDCounter--;}

    public static void setTableNum(int n){tableNumberCounter = n;}

    public int getTableNumber(){return this.tableNumber;}

    public Menu getMenu(){return this.menu;}

    public List<Order> getOrders(){return this.orders;}

    public Bill getBill(){return this.bill;}

    public int getCustomerID(){return this.customerID;}
}
