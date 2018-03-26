package com.se.pete.e_restaurant.restaurant;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry represents the general database for all information in the system.
 * Allows for staff to search and retrieve various pieces of info such as orders or logging in
 */

public class Registry
{
    // Singleton
    private static final Registry ourInstance = new Registry();
    public static Registry getInstance() {
        return ourInstance;
    }
    private Registry() {restaurantID = 1;}

    private int restaurantID ;

    private List<Staff> staffList = new ArrayList<Staff>();
    private List<Order> orderList = new ArrayList<Order>();
    private List<Bill> billList = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();

    public void addStaff(Staff s)
    {staffList.add(s);}

    public boolean removeStaff(Staff s)
    {
        return staffList.remove(s);
    }

    public void addOrder(Order o)
    {orderList.add(o);}

    public void addCustomer(Customer c)
    {
        for(int i = 0; i < customerList.size(); i++)
        {
            if(customerList.get(i).getCustomerID() == c.getCustomerID())
            {
                customerList.set(i, c);
                return;
            }
        }
        customerList.add(c);
    }

    // Add or update the list of bills
    public void addOBill(Bill b)
    {
        // Check if the bill is already in the list
        for(int i = 0; i < billList.size(); i++)
        {
            if(b.getBillID() == billList.get(i).getBillID())
            {
                // If it is, simply replace the object (updated bill)
                billList.set(i, b);
                return;
            }
        }
        // Or add the new bill to the list
        billList.add(b);
    }

    public Order searchOrderByID(int id)
    {
        return orderList.get(id);
    }

    public Staff searchStaffByID(int id)
    {
        for(Staff s : staffList)
        {
            if(s.getID() == id)
                return s;
        }
        return null;
    }

    public Customer searchCustomerByID(int id)
    {
        for(Customer c : customerList)
        {
            if(c.getCustomerID() == id)
                return c;
        }
        return null;
    }


    //Check if given login details are correct
    @Nullable
    public Staff correctLogin(int staffID, String password)
    {
        for(Staff s : staffList)
        {
            if(s.getID() == staffID)
            {
                if(s.getPassword().equals(password))
                    return s; // Correct ID and matching password
            }
        }
        return null; // No matches
    }

    public List<Staff> getStaffList(){return this.staffList;}

    public List<Order> getOrderList(){return this.orderList;}

    public List<Bill> getBillList(){return this.billList;}

    public List<Customer> getCustomerList(){return this.customerList;}
}
