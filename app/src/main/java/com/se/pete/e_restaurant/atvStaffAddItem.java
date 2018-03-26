/*
* Add a new FoodItem to the menu (from StaffEditMenu)
* A simple form for the manager to fill out to add a new item to the menu.
* Returns to StaffEditMenu on completion.
 */

package com.se.pete.e_restaurant;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.se.pete.e_restaurant.restaurant.FoodItem;
import com.se.pete.e_restaurant.restaurant.Menu;

public class atvStaffAddItem extends AppCompatActivity
{
    EditText etName;
    EditText numPrice;
    EditText numStock ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_staff_add_item);

        etName = findViewById(R.id.etItemName);
        numPrice = findViewById(R.id.etItemPrice);
        numStock = findViewById(R.id.etItemInitialStock);
    }

    public void addNewItem(View v)
    {
        try {
            String name = etName.getText().toString();

            // Ensure "Name" field has data in it
            if(name.equals(""))
                throw new NumberFormatException();

            float price = Float.valueOf(numPrice.getText().toString());
            int stock = Integer.parseInt(numStock.getText().toString());

            Menu.getInstance().addItem(new FoodItem(name, price, stock));

            Intent finishedItemAdd = new Intent(getApplicationContext(), atvStaffEditMenu.class);
            finishedItemAdd.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(finishedItemAdd);
        }

        catch (NumberFormatException e)
        {
            Snackbar.make(etName, "Error- empty fields. Please enter valid data.",
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }
    }
}
