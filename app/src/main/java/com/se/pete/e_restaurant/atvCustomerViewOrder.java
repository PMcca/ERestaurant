package com.se.pete.e_restaurant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.se.pete.e_restaurant.restaurant.Customer;
import com.se.pete.e_restaurant.restaurant.FoodItem;
import com.se.pete.e_restaurant.restaurant.Order;

import java.util.List;

public class atvCustomerViewOrder extends AppCompatActivity
{
    ListView lvOrder;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_customer_view_order);

        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra(atvCustomerMain.KEY_CUSTOMER_OBJECT);

        lvOrder = (ListView) findViewById(R.id.lvOrder);

        final OrderStatusAdapter foodAdapter = new OrderStatusAdapter(getApplicationContext(),
                R.layout.menurow, customer.getOrders());
        lvOrder.setAdapter(foodAdapter);


        // Check if there are any orders
        if(customer.getOrders().isEmpty())
        {
            AlertDialog.Builder dialogNoOrders = new AlertDialog.Builder(atvCustomerViewOrder.this);

            dialogNoOrders.setCancelable(true);
            dialogNoOrders.setTitle("No Orders");
            dialogNoOrders.setMessage("Make an order before viewing its status.");

            dialogNoOrders.setNegativeButton("Okay", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.cancel();
                    finish();
                }
            });
            dialogNoOrders.show();
        }
    }

    private class OrderStatusAdapter extends ArrayAdapter
    {
        private List<Order> orders;
        private int resource;
        private LayoutInflater inflater;

        public OrderStatusAdapter(@NonNull Context context, int resourceP, @NonNull List objects)
        {
            super(context, resourceP, objects);
            orders = objects;
            resource = resourceP;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = inflater.inflate(resource, null);
            }

            TextView tvFoodName = (TextView) convertView.findViewById(R.id.tvFoodName);
            TextView tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            TextView tvStock = convertView.findViewById(R.id.tvStock);
            TextView tvPriceHids = (TextView) convertView.findViewById(R.id.tvFoodPrice);

            Order order = orders.get(position);

            StringBuffer contents = new StringBuffer();
            for(FoodItem item : order.getOrderItems())
            {
                contents.append(item.getName() + "\n");
            }

            tvFoodName.setText(contents.toString());
            tvStatus.setText(order.getStatus());
            tvPriceHids.setVisibility(View.INVISIBLE);
            tvStock.setVisibility(View.INVISIBLE);

            return convertView;
        }
    }
}
