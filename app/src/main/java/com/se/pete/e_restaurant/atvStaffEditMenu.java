package com.se.pete.e_restaurant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.se.pete.e_restaurant.restaurant.FoodItem;
import com.se.pete.e_restaurant.restaurant.Manager;
import com.se.pete.e_restaurant.restaurant.Menu;
import com.se.pete.e_restaurant.restaurant.Registry;
import com.se.pete.e_restaurant.restaurant.Staff;

import java.util.List;

public class atvStaffEditMenu extends AppCompatActivity
{
    ListView lvFoodItems;
    MenuEditAdapter editAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_staff_edit_menu);

        lvFoodItems = (ListView) findViewById(R.id.lvStaffMenu);
        FloatingActionButton fabAddItem = findViewById(R.id.fabAddItem);

        editAdapter = new MenuEditAdapter(getApplicationContext()
                , R.layout.menurow
                , Menu.getInstance().getItems());

        lvFoodItems.setAdapter(editAdapter);

        lvFoodItems.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                final FoodItem selectedItem = (FoodItem) adapterView.getItemAtPosition(i);
                AlertDialog.Builder dialogChooseOption = new AlertDialog.Builder(atvStaffEditMenu.this);

                dialogChooseOption.setCancelable(true);
                dialogChooseOption.setTitle(selectedItem.getName());
                dialogChooseOption.setMessage("What would you like to do to this item?");

                dialogChooseOption.setNegativeButton("Remove Item", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                        removeItemConfirmation(selectedItem);
                    }
                });

                dialogChooseOption.setPositiveButton("Change Stock", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        dialogInterface.cancel();
                        changeStock(selectedItem);
                    }
                });
                dialogChooseOption.show();
            }
        });

        fabAddItem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent addItem = new Intent(getApplicationContext(), atvStaffAddItem.class);
                startActivity(addItem);
            }
        });
    }

    private class MenuEditAdapter extends ArrayAdapter
    {
        private List<FoodItem> foodList;
        private int resource;
        private LayoutInflater inflater;

        public MenuEditAdapter(@NonNull Context context, int resourceP, @NonNull List objects)
        {
            super(context, resourceP, objects);
            foodList = objects;
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

            TextView tvStaffName = (TextView) convertView.findViewById(R.id.tvFoodName);
            TextView tvHide = (TextView) convertView.findViewById(R.id.tvStatus);
            TextView tvStock = convertView.findViewById(R.id.tvStock);
            TextView tvHide2 = (TextView) convertView.findViewById(R.id.tvFoodPrice);

            FoodItem item = foodList.get(position);

            tvStaffName.setText(item.getName().toString());

            // Get food's stock
            tvStock.setText(String.valueOf(item.getStock()));

            tvHide.setVisibility(View.INVISIBLE);
            tvHide2.setVisibility(View.INVISIBLE);

            return convertView;
        }

        public FoodItem getItemAtPosition(int position){return foodList.get(position);}
    }

    private void removeItemConfirmation(final FoodItem item)
    {
        AlertDialog.Builder dialogRemoveItem = new AlertDialog.Builder(atvStaffEditMenu.this);

        dialogRemoveItem.setCancelable(true);
        dialogRemoveItem.setTitle("Remove " + item.getName() + "?");
        dialogRemoveItem.setMessage("Are you sure you want to remove " + item.getName()
                + " from the menu?");

        dialogRemoveItem.setNegativeButton("No", new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialogInterface, int i)
        {
            dialogInterface.cancel();
        }
    });
        dialogRemoveItem.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Menu.getInstance().removeItem(item);
                editAdapter.notifyDataSetChanged();
                dialogInterface.dismiss();

                Snackbar.make(lvFoodItems, item.getName() + " removed from menu.",
                        Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        dialogRemoveItem.show();
    }

    private void changeStock(final FoodItem item)
    {
        AlertDialog.Builder dialogChangeStock = new AlertDialog.Builder(atvStaffEditMenu.this);

        LayoutInflater inflater = atvStaffEditMenu.this.getLayoutInflater();

        final View dialogRef = inflater.inflate(R.layout.dialoglayout, null);
        dialogChangeStock.setView(dialogRef);

        final EditText numNewStock = (EditText) dialogRef.findViewById(R.id.numEditText);
        numNewStock.setHint("New Stock Amount");

        dialogChangeStock.setCancelable(true);
        dialogChangeStock.setTitle(item.getName() + "- stock change");
        dialogChangeStock.setMessage("Enter the amount to change it to below.\nCurrent stock:   "
        + item.getStock());

        dialogChangeStock.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        dialogChangeStock.setPositiveButton("Set", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {

                // Set the item's new stock to user inputted number
                try {
                    Menu.getInstance().updateItemStock(item.getName(),
                            Integer.parseInt(numNewStock.getText().toString()));
                    editAdapter.notifyDataSetChanged();
                    dialogInterface.dismiss();

                    Snackbar.make(lvFoodItems, item.getName() + " stock changed.",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                // Catch no number entered exception
                catch(NumberFormatException e)
                {
                    dialogInterface.cancel();
                    Snackbar.make(lvFoodItems, "Error- enter a valid number.",
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        dialogChangeStock.show();
    }
}
