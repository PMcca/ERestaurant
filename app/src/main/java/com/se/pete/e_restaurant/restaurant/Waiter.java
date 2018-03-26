package com.se.pete.e_restaurant.restaurant;

import java.util.List;

/**
 * Created by Pete on 08/03/2018.
 * Represents a single waiter working at the restaurant. Waiters are able to view orders and are
 * responsible for responding to them.
 */

public class Waiter extends Staff
{
    private List<Customer> customers;

    public Waiter(int id, String nme, String pw)
    {
        super(id, nme, pw);
    }
}
