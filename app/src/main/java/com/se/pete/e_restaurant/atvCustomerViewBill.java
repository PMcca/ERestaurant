package com.se.pete.e_restaurant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

public class atvCustomerViewBill extends AppCompatActivity
{
    ListView lvBill;
    FloatingActionButton fabPayBill;

    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_customer_view_bill);

        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra(atvCustomerMain.KEY_CUSTOMER_OBJECT);

        lvBill = (ListView) findViewById(R.id.lvBill);
        fabPayBill = (FloatingActionButton) findViewById(R.id.fabPayBill);
        TextView tvTotalBillCost = (TextView) findViewById(R.id.tvTotalBillCost);

        final ViewBillAdapter foodAdapter = new ViewBillAdapter(getApplicationContext(),
                R.layout.menurow, customer.getOrders());
        lvBill.setAdapter(foodAdapter);

        tvTotalBillCost.setText("£" + String.valueOf(customer.getBill().getPrice()));

        // Check if there are any orders
        if(customer.getOrders().isEmpty())
        {
            AlertDialog.Builder dialogNoOrders = new AlertDialog.Builder(atvCustomerViewBill.this);

            dialogNoOrders.setCancelable(true);
            dialogNoOrders.setTitle("No Orders");
            dialogNoOrders.setMessage("Make an order before viewing or paying the bill.");

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

        else
        {
            fabPayBill.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent confirm = new Intent(getApplicationContext(), atvPayBill.class);
                    confirm.putExtra(atvCustomerMain.KEY_CUSTOMER_OBJECT, customer);
                    startActivity(confirm);
                }
            });
        }
    }

    // Adapter used for handling List rendering
    private class ViewBillAdapter extends ArrayAdapter
    {
        private List<Order> orders;
        private int resource;
        private LayoutInflater inflater;

        public ViewBillAdapter(@NonNull Context context, int resourceP, @NonNull List objects)
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
            TextView tvFoodStock = (TextView) convertView.findViewById(R.id.tvStock);
            TextView tvPriceHids = (TextView) convertView.findViewById(R.id.tvFoodPrice);
            Order order = orders.get(position);

            StringBuffer contents = new StringBuffer();
            for(FoodItem item : order.getOrderItems())
            {
                contents.append(item.getName() + "\n");
            }

            tvFoodName.setText(contents.toString());
            tvFoodStock.setText("£" + String.valueOf(order.getTotalPrice()));
            tvPriceHids.setVisibility(View.INVISIBLE);

            return convertView;
        }
    }
}