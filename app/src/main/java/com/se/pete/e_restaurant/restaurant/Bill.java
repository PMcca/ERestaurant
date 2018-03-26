package com.se.pete.e_restaurant.restaurant;

import java.io.Serializable;
import java.util.List;

/**
 * A Bill is a tally of the sum of orders a customer has made at a particular "session".
 */

public class Bill implements Serializable
{
    private int billID;
    private boolean wasRefunded;
    private static int billIDCounter = 1;

    private float price;
    private float tip;

    private List<Order> orders;

    private String paymentMethod;

    private Manager manager;

    private Waiter waiter;

    private Customer customer;

    public Bill(List<Order> custOrders, Customer cust)
    {
        orders = custOrders;
        price = 0;
        tip = 0;
        billID = billIDCounter++;
        customer = cust;
    }

    public void addOrder(Order order)
    {
        orders.add(order);
    }

    public void removeOrder(Order order)
    {
        orders.remove(order);
    }

    public void addTip(float amount)
    {
        tip = amount;
        price += tip;
    }

    public void refundBill()
    {
        // Negative amount represents refund
        this.price *= -1;
        wasRefunded = true;
    }

    public boolean wasRefunded(){return this.wasRefunded;}

    public void calculatePrice(Order o)
    {
        price += o.getTotalPrice();
    }


    public float getPrice()
    {
        return price;
    }

    public void setPaymentMethod(String s) {this.paymentMethod = s;}

    public float getTip() {return tip;}

    public String getPaymentMethod(){return paymentMethod;}

    public List<Order> getOrders(){return orders;}

    public int getBillID(){return this.billID;}

    public Customer getCustomer(){return this.customer;}


}
