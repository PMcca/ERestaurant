package com.se.pete.e_restaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.se.pete.e_restaurant.restaurant.Customer;
import com.se.pete.e_restaurant.restaurant.Manager;
import com.se.pete.e_restaurant.restaurant.Menu;
import com.se.pete.e_restaurant.restaurant.Registry;
import com.se.pete.e_restaurant.restaurant.Staff;

public class MainActivity extends AppCompatActivity
{
    private int tableNum = -1; // Placeholder table number

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Res.loadStaff();
    }

    //Start the staff login activity
    public void enterStaff(View v)
    {
        Intent i = new Intent(this, atvStaffLogin.class);
        startActivity(i);
    }

    public void enterCustomer(final View v)
    {
        // Show dialog before entering to decide if it's a new or returning customer
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();

        final View dialogRef = inflater.inflate(R.layout.dialoglayout, null);

        builder.setView(dialogRef);

        final EditText numCustID = (EditText) dialogRef.findViewById(R.id.numEditText);
        numCustID.setHint("CustomerID");


        builder.setCancelable(true);
        builder.setTitle("Existing Customer? Enter your ID");
        builder.setMessage("If you are an existing customer, enter your ID number.\n" +
                "Or, press \"Okay\" to continue as a new customer.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                try
                {
                    Intent custIntent = new Intent(getApplicationContext(), atvCustomerMain.class);
                    Customer existingCust = Registry.getInstance()
                            .searchCustomerByID(Integer.parseInt(numCustID.getText().toString()));

                 if(existingCust != null)
                 {
                     custIntent.putExtra(atvCustomerMain.KEY_CUSTOMER_OBJECT, existingCust);
                     dialogInterface.dismiss();
                     startActivity(custIntent);
                 }

                 // Customer ID not found
                 else
                 {
                     throw new NullPointerException();
                 }
                }
                // Catch empty input error, start as a new customer
                catch(NumberFormatException e)
                {
                    dialogInterface.cancel();
                    Intent newCustomerIntent = new Intent(getApplicationContext(),
                            atvCustomerMain.class);
                    startActivity(newCustomerIntent);
                }
                catch(NullPointerException e)
                {
                    Snackbar.make(v, numCustID.getText().toString()
                                    + " not found."
                            , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        builder.show();

        /* Intent i = new Intent(this, atvCustomerMain.class);
        startActivity(i);*/
    }

    public void setTableNum(int num){this.tableNum = num;}
}
