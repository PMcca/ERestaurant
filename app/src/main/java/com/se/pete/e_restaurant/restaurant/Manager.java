package com.se.pete.e_restaurant.restaurant;

import java.util.List;

/**
 * A manager at the restaurant. While it is assumed there can be only one manager at any single
 * restaurant, two Manager objects will exist in the system, one of which represents Root.
 */

public class Manager extends Staff
{
    private List<Bill> bills;

    public Manager(int id, String nme, String pw)
    {
        super(id, nme, pw);
    }

    public void addStaff(Staff s)
    {

    }

    public void removeStaff(Staff s)
    {
        Registry.getInstance().removeStaff(s);
    }
}
