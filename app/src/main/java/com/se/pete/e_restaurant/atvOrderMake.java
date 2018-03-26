package com.se.pete.e_restaurant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.se.pete.e_restaurant.restaurant.Customer;
import com.se.pete.e_restaurant.restaurant.FoodItem;
import com.se.pete.e_restaurant.restaurant.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class atvOrderMake extends AppCompatActivity
{
    ListView lvFoodItems;
    FloatingActionButton fabSubmitOrder;

    Customer customer;
    Order order;
    Map<FoodItem, Integer> amountInOrder; // Used for tracking # of specific items in order

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_order_make);

        // Get customer object passed to this
        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra(atvCustomerMain.KEY_CUSTOMER_OBJECT);

        amountInOrder = new HashMap<>();
        Res.loadAmountInOrder(amountInOrder, customer.getMenu());

        order = customer.makeOrder();
        lvFoodItems = (ListView) findViewById(R.id.lvFoodMenu);
        fabSubmitOrder = (FloatingActionButton) findViewById(R.id.fabSubmitOrder);

        // Set the adapter for the list view
        final FoodListAdapter foodAdapter = new FoodListAdapter(getApplicationContext(),
                R.layout.menurow, customer.getMenu().getItems());
        lvFoodItems.setAdapter(foodAdapter);


        // FAB OnClick for SUBMIT
        fabSubmitOrder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // If order is empty, display error message
                if(!(order.getOrderSize() > 0))
                {
                    AlertDialog.Builder dialogEmptyOrder = new AlertDialog.Builder(atvOrderMake.this);

                    dialogEmptyOrder.setCancelable(true);
                    dialogEmptyOrder.setTitle("Empty Order");
                    dialogEmptyOrder.setMessage("Please add some items to your order before submitting.");

                    dialogEmptyOrder.setNegativeButton("Okay", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialogInterface.cancel();
                        }
                    });
                    dialogEmptyOrder.show();
                }
                // Order is not empty, confirm order
                else
                {
                    Intent confirm = new Intent(getApplicationContext(), atvOrderConfirm.class);
                    confirm.putExtra(atvCustomerMain.KEY_CUSTOMER_OBJECT, customer);
                    confirm.putExtra(atvCustomerMain.KEY_ORDER_OBJECT, order);
                    startActivity(confirm);
                }
            }
        });

        // Add items to order
        lvFoodItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                // Get selected item
                FoodItem item = (FoodItem) adapterView.getItemAtPosition(i);

                // Check if item is in stock ("virtual" stock, given by it - amount in basket)
                if ((item.getStock() - amountInOrder.get(item) > 0))
                {
                    order.addItem(item);
                    addToOrderAmount(item);

                    // Notify user via Snackbar
                    Snackbar.make(view, item.getName() + " added to order.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    // Update Adapter to show new # of items in order
                    foodAdapter.notifyDataSetChanged();
                }

                // Item out of stock- notify user
                else
                {
                    Snackbar.make(view, item.getName() + " is out of stock. Not added to order.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }
        });

        // Used for removing items from order
        lvFoodItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                FoodItem item = (FoodItem) adapterView.getItemAtPosition(i);

                // Check if an instance of the item is in the order
                if(order.hasItem(item))
                {
                    order.removeItem(item);
                    Snackbar.make(view, item.getName() + " removed from order."
                            , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    // Attempt to remove item from # in list
                    removeOrderFromAmount(item);

                    //Update for GUI
                    foodAdapter.notifyDataSetChanged();
                }

                // Item not found- show error Snackbar
                else
                {
                    Snackbar.make(view, item.getName() + " was never in your order. Nothing changed."
                            , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                return true;
            }
        });

        introMessage();
    }

    // Adapter used for handling List rendering
    private class FoodListAdapter extends ArrayAdapter
    {
        private List<FoodItem> foodItems;
        private int resource;
        private LayoutInflater inflater;

        public FoodListAdapter(@NonNull Context context, int resourceP, @NonNull List objects)
        {
            super(context, resourceP, objects);
            foodItems = objects;
            resource = resourceP;
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                convertView = inflater.inflate(resource, null);
            }

            TextView tvFoodName = (TextView) convertView.findViewById(R.id.tvFoodName);
            TextView tvFoodPrice = (TextView) convertView.findViewById(R.id.tvFoodPrice);
            TextView tvFoodStock = (TextView) convertView.findViewById(R.id.tvStock);
            FoodItem item = foodItems.get(position);

            // Set values for each food item
            tvFoodName.setText(item.getName());
            tvFoodPrice.setText("£" + String.valueOf(item.getPrice()));

            // Handle null pointer exception
            if(amountInOrder.get(item) == null)
                tvFoodStock.setText("0");

            else
                tvFoodStock.setText(String.valueOf(amountInOrder.get(item)));

            return convertView;
        }

        public FoodItem getItemAtPosition(int position)
        {
            return foodItems.get(position);
        }
    }


    // Update the number of a specific item in the order (used for GUI)
    private void addToOrderAmount(FoodItem item)
    {
            int currentAmount = amountInOrder.get(item);
            currentAmount += 1;
            amountInOrder.put(item, currentAmount);

    }
    // Update the number of a specific item removed from order (used for GUI)
    private void removeOrderFromAmount(FoodItem item)
    {
        // Check if it's > 0
        int currentAmount = amountInOrder.get(item);

        if(currentAmount > 0)
        {
            currentAmount--;
            amountInOrder.put(item, currentAmount);
        }

        return;
    }

    // Intro message, conveying controls to user.
    private void introMessage()
    {
        AlertDialog.Builder dialogIntroMessage = new AlertDialog.Builder(atvOrderMake.this);

        dialogIntroMessage.setCancelable(true);
        dialogIntroMessage.setTitle("Create your Order");
        dialogIntroMessage.setMessage("To add an item, simply tap it.\nTo remove, hold-press on that item." +
                "\nWhen ready, tap the \"Submit\" button at the top.");

        dialogIntroMessage.setNegativeButton("Okay", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        dialogIntroMessage.show();
    }

}
