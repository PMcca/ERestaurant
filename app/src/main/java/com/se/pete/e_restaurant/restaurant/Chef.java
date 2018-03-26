package com.se.pete.e_restaurant.restaurant;

import android.support.annotation.Nullable;

/**

 */

public class Chef extends Staff
{
    public Chef(int id, String nme, String pw)
    {
        super(id, nme, pw);
    }

    public void setOrderReady(Order o)
    {
        o.updateStatus("READY");
    }

    @Nullable
    public Order getNextOrder()
    {
        for(Order o : Registry.getInstance().getOrderList())
        {
            if(o.getStatus().equals("PENDING"))
                return o;
        }
        return null;
    }

}
