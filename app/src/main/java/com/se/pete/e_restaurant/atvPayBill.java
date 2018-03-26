package com.se.pete.e_restaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.se.pete.e_restaurant.restaurant.Customer;

import java.lang.reflect.InvocationTargetException;

public class atvPayBill extends AppCompatActivity
{
    private float billPrice;

    TextView tvTotal;

    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_pay_bill);

        tvTotal = (TextView) findViewById(R.id.tvPayTotal);
        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra(atvCustomerMain.KEY_CUSTOMER_OBJECT);

        tvTotal.setText("£" + String.valueOf(customer.getBill().getPrice()));
    }

    public void payStub(View v)
    {
        Button b = (Button) v;

        customer.getBill().setPaymentMethod((String) b.getText());
        billPrice = customer.getBill().getPrice();

        AlertDialog.Builder dialogWaiter = new AlertDialog.Builder(atvPayBill.this);

        dialogWaiter.setCancelable(true);
        dialogWaiter.setTitle("Waiter coming");
        dialogWaiter.setMessage("Thank you! A waiter is coming to take payment via " + b.getText()
         + "\nThank you for dining with us!");

        dialogWaiter.setNegativeButton("Okay", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
                Intent restart = new Intent(getApplicationContext(), MainActivity.class);
                restart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(restart);
            }
        });
        dialogWaiter.show();
    }

    public void setTip(View v)
    {
        try
        {
            EditText etTip = (EditText) findViewById(R.id.numTip);

            // Get the value of the tip the user inputted
            float tipVal = Float.valueOf(etTip.getText().toString());

            customer.getBill().addTip(tipVal);
            billPrice += customer.getBill().getPrice();

            tvTotal.setText("£" + String.valueOf(customer.getBill().getPrice()));
            tvTotal.invalidate();

            etTip.setText("");
        } catch(NumberFormatException e) {}
    }


}
