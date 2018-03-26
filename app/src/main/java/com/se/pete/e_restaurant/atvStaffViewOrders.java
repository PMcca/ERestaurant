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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.se.pete.e_restaurant.restaurant.Bill;
import com.se.pete.e_restaurant.restaurant.Chef;
import com.se.pete.e_restaurant.restaurant.FoodItem;
import com.se.pete.e_restaurant.restaurant.Order;
import com.se.pete.e_restaurant.restaurant.Registry;

import java.util.List;

public class atvStaffViewOrders extends AppCompatActivity
{
    ListView lvOrders;
    Chef chef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_staff_view_orders);

        // Get intent anc check for flags
        Intent flags = getIntent();
        Bill bill = (Bill) flags
                .getSerializableExtra(atvStaffViewBills.KEY_SPECIFIC_ORDER);

        lvOrders = (ListView) findViewById(R.id.lvOrders);

        // If this was started from main staff menu, view all orders in system
        if(bill != null)
        {
            final StaffOrderStatusAdapter ordersAdapter = new StaffOrderStatusAdapter(getApplicationContext(),
                    R.layout.menurow, bill.getOrders());
            lvOrders.setAdapter(ordersAdapter);
        }

        // Check if this was started from setOrderReady from chef
        else if(flags.getBooleanExtra(atvStaffMain.KEY_ORDER_READY, false))
        {
            // Get a list of orders which are "PENDING"
            chef = (Chef) flags.getSerializableExtra(atvStaffMain.KEY_CHEF_OBJECT);
            List<Order> pendingOrders = Order.getPendingOrders(Registry.getInstance().getOrderList());


            final StaffOrderStatusAdapter ordersAdapter = new StaffOrderStatusAdapter(getApplicationContext(),
                    R.layout.menurow, pendingOrders);
            lvOrders.setAdapter(ordersAdapter);

            lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Order selectedOrder = (Order) adapterView.getItemAtPosition(i);

                    chef.setOrderReady(selectedOrder);
                    ordersAdapter.notifyDataSetChanged();
                }
            });
        }

        // Else, view a specific bill's orders instead.
        else
        {
            final StaffOrderStatusAdapter ordersAdapter = new StaffOrderStatusAdapter(getApplicationContext(),
                    R.layout.menurow, Registry.getInstance().getOrderList());
            lvOrders.setAdapter(ordersAdapter);
        }


        if(Registry.getInstance().getOrderList().isEmpty())
        {
            AlertDialog.Builder dialogNoOrders = new AlertDialog.Builder(atvStaffViewOrders.this);

            dialogNoOrders.setCancelable(true);
            dialogNoOrders.setTitle("No Orders");
            dialogNoOrders.setMessage("No orders have been made in the system.");

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


    private class StaffOrderStatusAdapter extends ArrayAdapter
    {
        private List<Order> orders;
        private int resource;
        private LayoutInflater inflater;

        public StaffOrderStatusAdapter(@NonNull Context context, int resourceP, @NonNull List objects)
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
            TextView tvOrderID = (TextView) convertView.findViewById(R.id.tvFoodPrice);

            Order order = orders.get(position);

            StringBuffer contents = new StringBuffer();
            for(FoodItem item : order.getOrderItems())
            {
                contents.append(item.getName() + "\n");
            }

            tvFoodName.setText(contents.toString());
            tvStatus.setText("£" + String.valueOf(order.getTotalPrice()));
            tvOrderID.setText("Order ID: " + String.valueOf(order.getOrderID()));
            tvStock.setText(order.getStatus());

            return convertView;
        }

        public Order getItemAtPosition(int position){return orders.get(position);}
    }
}
