/*
* The main screen (activity) for the customer. A customer can either be a returning customer
* (through the intent) or a new customer. Customers can make and cancels orders, pay for their
*  bill and give feedback from this activity.
 */

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
import android.widget.TextView;

import com.se.pete.e_restaurant.restaurant.Customer;
import com.se.pete.e_restaurant.restaurant.Registry;

public class atvCustomerMain extends AppCompatActivity
{
    // Intent Extra keys
    public static final String KEY_CUSTOMER_OBJECT = "com.se.pete.e_restaurant.CUSTOMER";
    public static final String KEY_ORDER_OBJECT = "com.se.pete.e_restaurant.ORDER";
    public static final String KEY_CONFIRM_FLAG = "com.se.pete.e_restaurant.CONFIRM";

    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_customer_main);

        Intent intent = getIntent();

       // Check if the intent has a customer object (i.e. is from OrderConfirm)
        customer = (Customer) intent.getSerializableExtra(KEY_CUSTOMER_OBJECT);

        // If it's null, make a new customer object
        if(customer == null)
            customer = new Customer();

        TextView tvCustID = findViewById(R.id.tvCustID);
        tvCustID.setText("Your ID is: " + customer.getCustomerID());

        // Order confirm message via Snackbar
        if(intent.getBooleanExtra(KEY_CONFIRM_FLAG, false))
        {
            TextView tvWelcome = (TextView) findViewById(R.id.tvWelcome);
            Snackbar.make(tvWelcome, "Order confirmed", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    // Button Function- Open OrderMake activity
    public void startOrder(View v)
    {
        // Pass the Customer object to the Order activity
        Intent intent = new Intent(this, atvOrderMake.class);
        intent.putExtra(KEY_CUSTOMER_OBJECT, customer);
        startActivity(intent);
    }

    public void viewBill(View v)
    {
        Intent intent = new Intent(this, atvCustomerViewBill.class);
        intent.putExtra(KEY_CUSTOMER_OBJECT, customer);
        startActivity(intent);
    }

    // Button Function - Stub for calling waiter
    public void callWaiter(View v)
    {
        customer.requestWaiter();
        AlertDialog.Builder builder = new AlertDialog.Builder(atvCustomerMain.this);

        builder.setCancelable(true);
        builder.setTitle("Calling Waiter");
        builder.setMessage("A waiter is on their way to see you.");

        builder.setNegativeButton("Okay", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    public void orderStatus(View v)
    {
        Intent intent = new Intent(this, atvCustomerViewOrder.class);
        intent.putExtra(KEY_CUSTOMER_OBJECT, customer);
        startActivity(intent);
    }

    // Open dialog for customer to give feedback (Stub)
    public void giveFeedback(View v)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(atvCustomerMain.this);

        LayoutInflater inflater = atvCustomerMain.this.getLayoutInflater();

        final View dialogRef = inflater.inflate(R.layout.feedbackdialog, null);
        builder.setView(dialogRef);

        final EditText numNewStock = (EditText) dialogRef.findViewById(R.id.numEditText);

        builder.setCancelable(true);
        builder.setTitle("Enter Feedback");
        builder.setMessage("Please enter your feedback below.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("Set", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    // If the customer presses back without confirming an order, decrement the counter
    // This allows that number to be reused for a "real" customer.
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        // Check if that customer is in the Registry (i.e. have made an order/bill)
        if(Registry.getInstance().searchCustomerByID(customer.getCustomerID()) == null)
            Customer.decrementCounter();
    }
}
