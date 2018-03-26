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

import com.se.pete.e_restaurant.restaurant.Chef;
import com.se.pete.e_restaurant.restaurant.Customer;
import com.se.pete.e_restaurant.restaurant.Manager;
import com.se.pete.e_restaurant.restaurant.Staff;

public class atvStaffMain extends AppCompatActivity
{
    public static final String KEY_STAFF_OBJECT = "com.se.pete.e_restaurant.STAFF";
    public static final String KEY_CHEF_OBJECT = "com.se.pete.e_restaurant.CHEF";
    public static final String KEY_RECEIPT = "com.se.pete.e_restaurant.RECEIPT";
    public static final String KEY_REFUND = "com.se.pete.e_restaurant.REFUND";
    public static final String KEY_ORDER_READY = "com.se.pete.e_restaurant.ORDER_READY";
    public static final String KEY_STAFF_ADDED = "com.se.pete.e_restaurant.STAFF_ADDED";

    Staff staffMem;

    private TextView tvStaffWelcome;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_staff_main);

        Intent intent = getIntent();
        staffMem = (Staff) intent.getSerializableExtra(KEY_STAFF_OBJECT);
        setupLayout();

        // Set welcome text
        tvStaffWelcome = (TextView) findViewById(R.id.tvStaffWelcome);
        tvStaffWelcome.setText("Welcome, " + staffMem.getName());

        if(intent.getBooleanExtra(KEY_STAFF_ADDED, false))
        {
            Snackbar.make(tvStaffWelcome, "New staff member added."
                    , Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void checkOrderStatus(View v)
    {
        Intent orderStats = new Intent(this, atvStaffViewOrders.class);
        startActivity(orderStats);
    }

    public void getNextOrder(View v)
    {
        Intent nextOrder = new Intent(getApplicationContext(), atvStaffGetNextOrder.class);
        nextOrder.putExtra(KEY_STAFF_OBJECT, staffMem);
        startActivity(nextOrder);
    }

    public void setOrderReady(View v)
    {
        Intent setOrder = new Intent(getApplicationContext(), atvStaffViewOrders.class);
        setOrder.putExtra(KEY_ORDER_READY, true);
        setOrder.putExtra(KEY_CHEF_OBJECT, staffMem);
        startActivity(setOrder);
    }




    // Allow the waiter/manager to change the table number of this program's instance
    public void resetTable(View v)
    {
        AlertDialog.Builder dialogResetTable = new AlertDialog.Builder(atvStaffMain.this);

        LayoutInflater inflater = atvStaffMain.this.getLayoutInflater();

        // View to refer to the dialog's box
        final View dialogRef = inflater.inflate(R.layout.dialoglayout, null);

        dialogResetTable.setView(dialogRef);

        // Get the EditText View from the dialog's box
        final EditText numTableNum = (EditText) dialogRef.findViewById(R.id.numEditText);


        dialogResetTable.setCancelable(true);
        dialogResetTable.setTitle("Enter new Table Number");

        dialogResetTable.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        dialogResetTable.setPositiveButton("Set", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                try
                {
                    // Set the customer's default table number
                    Customer.setTableNum(Integer.parseInt(numTableNum.getText().toString()));
                    dialogInterface.dismiss();

                    // Show confirmation message
                    Snackbar.make(tvStaffWelcome, "New table number is " + numTableNum.getText().toString(),
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();

                    // Catch empty input error
                } catch(NumberFormatException e) {dialogInterface.cancel();}
            }
        });

        dialogResetTable.show();
    }

    public void offerRefund(View v)
    {
        Intent refundIntent = new Intent(getApplication(), atvStaffViewBills.class);
        refundIntent.putExtra(KEY_REFUND, true);
        startActivity(refundIntent);
    }

    public void printReceipt(View v)
    {
        Intent receiptIntent = new Intent(getApplication(), atvStaffViewBills.class);
        receiptIntent.putExtra(KEY_RECEIPT, true);
        startActivity(receiptIntent);
    }

    public void viewBills(View v)
    {
        Intent bills = new Intent(this, atvStaffViewBills.class);
        startActivity(bills);
    }

    public void addStaff(View v)
    {
        Intent newStaff = new Intent(this, atvStaffAddStaff.class);
        newStaff.putExtra(KEY_STAFF_OBJECT, staffMem);
        startActivity(newStaff);
    }

    public void removeStaff(View v)
    {
        Intent removeStaff = new Intent(this, atvStaffRemoveStaff.class);
        removeStaff.putExtra(KEY_STAFF_OBJECT, staffMem);
        startActivity(removeStaff);
    }

    public void editMenu(View v)
    {
        Intent menuIntent = new Intent(this, atvStaffEditMenu.class);
        startActivity(menuIntent);
    }

    // Show/hide controls depending on staff type
    public void setupLayout()
    {
        if(staffMem instanceof Chef)
        {
            // Set Chef controls to visible
            findViewById(R.id.btnGetNextOrder).setVisibility(View.VISIBLE);
            findViewById(R.id.btnSetOrderReady).setVisibility(View.VISIBLE);

            // Set non-chef controls to invisible
            findViewById(R.id.btnViewBill).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnRefund).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnReceipt).setVisibility(View.INVISIBLE);
            findViewById(R.id.btnResetTable).setVisibility(View.INVISIBLE);
        }

        // Layout for Manager
        else if(staffMem instanceof Manager)
        {
            // Set Manager controls to visible
            findViewById(R.id.btnAddStaff).setVisibility(View.VISIBLE);
            findViewById(R.id.btnRemoveStaff).setVisibility(View.VISIBLE);
            findViewById(R.id.btnEditMenu).setVisibility(View.VISIBLE);
        }
        // staffMem is instance of Waiter. No changes needed, return.
    }
}

