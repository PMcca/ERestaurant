package com.se.pete.e_restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.se.pete.e_restaurant.restaurant.Customer;
import com.se.pete.e_restaurant.restaurant.FoodItem;
import com.se.pete.e_restaurant.restaurant.Order;
import com.se.pete.e_restaurant.restaurant.Registry;

import java.util.List;

public class atvOrderConfirm extends AppCompatActivity
{
    private Customer customer;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_order_confirm);

        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra(atvCustomerMain.KEY_CUSTOMER_OBJECT);
        order = (Order) intent.getSerializableExtra(atvCustomerMain.KEY_ORDER_OBJECT);

        TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
        TextView tvSummary = (TextView) findViewById(R.id.tvSummary);

        // Print the summary and total cost of order
        tvSummary.setText(calcSummary(order));
        tvTotal.setText("£" + order.getTotalPrice());
    }

    // Return to previous screen if up button (top left) pressed
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Confirm the order and send off the chef's and system.
    public void confirmOrder(View v)
    {
        order.setID();

        // Bind customer to order
        customer.submitOrder(order);

        // Decrease the stock of each item in order
        order.finaliseStockDecrease();

        //Bind this order to the bill
        order.setBill(customer.getBill());

        // Update Bill price
        customer.getBill().calculatePrice(order);

        // Add order, bill and customer to system
        Registry.getInstance().addOrder(order);
        Registry.getInstance().addCustomer(customer);
        Registry.getInstance().addOBill(customer.getBill());


        List<Customer> customers = Registry.getInstance().getCustomerList();

        order.updateStatus("PENDING");

        Intent in = new Intent(this, atvCustomerMain.class);
        in.putExtra(atvCustomerMain.KEY_CUSTOMER_OBJECT, customer);
        in.putExtra(atvCustomerMain.KEY_CONFIRM_FLAG, true); //Confirm flag

        in.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(in);
    }

    // Get string of summary to show to user
    private static String calcSummary(Order order)
    {
        StringBuffer summaryStr = new StringBuffer();

        for(FoodItem item : order.getOrderItems())
        {
            summaryStr.append(item.getName() + " : £" + item.getPrice() + "\n");
        }
        return summaryStr.toString();
    }

}
