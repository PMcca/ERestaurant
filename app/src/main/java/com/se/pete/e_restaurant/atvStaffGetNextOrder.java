/**
 * Used by Chefs to get the next order. Selects the next "PENDING" order and renders it
 * to user.
 */

package com.se.pete.e_restaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.se.pete.e_restaurant.restaurant.Chef;
import com.se.pete.e_restaurant.restaurant.FoodItem;
import com.se.pete.e_restaurant.restaurant.Order;

public class atvStaffGetNextOrder extends AppCompatActivity
{
    private Chef chef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_staff_get_next_order);

        // Chef who pressed button. Get the next order.
        chef = (Chef) getIntent().getSerializableExtra(atvStaffMain.KEY_STAFF_OBJECT);
        Order nextOrder = chef.getNextOrder();

        // View to show order contents and ID
        TextView tvContent = (TextView) findViewById(R.id.tvNextOrderContents);
        TextView tvID = (TextView) findViewById(R.id.tvNextOrderID);

        // Ensure there is an order to prepare
        if(nextOrder != null)
        {
            // Load stringbuffer with food item names
            StringBuffer orderContent = new StringBuffer();
            for (FoodItem item : nextOrder.getOrderItems()) {
                orderContent.append(item.getName() + "\n");
            }

            // Show contents and ID
            tvContent.setText(orderContent.toString());
            tvID.setText("ID: " + nextOrder.getOrderID());
        }

        // No orders- notify chef
        else
        {
            TextView tvHeader = (TextView) findViewById(R.id.tvNextOrderHeader);
            tvHeader.setText("No orders to prepare.");
            tvContent.setText("");
            tvID.setText("");
        }
    }
}
