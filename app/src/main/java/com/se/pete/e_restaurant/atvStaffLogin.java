package com.se.pete.e_restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.se.pete.e_restaurant.restaurant.Chef;
import com.se.pete.e_restaurant.restaurant.Manager;
import com.se.pete.e_restaurant.restaurant.Order;
import com.se.pete.e_restaurant.restaurant.Registry;
import com.se.pete.e_restaurant.restaurant.Staff;
import com.se.pete.e_restaurant.restaurant.Waiter;

import java.util.List;

public class atvStaffLogin extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_staff_login);
    }

    public void login(View v)
    {
        //Get error text
        TextView tv = (TextView) findViewById(R.id.txtError);

        //Get input fields
        EditText idField = (EditText) findViewById(R.id.etxtUname);
        EditText pwField = (EditText) findViewById(R.id.etxtPassword);

        if(Res.isEmpty(idField.getText(), pwField.getText())) //Check if either are empty
        {
            tv.setText("Error- Fill in fields before pressing Login");
            tv.setVisibility(View.VISIBLE);
            return;
        }

        //Parse fields as primitives
        int sID = Integer.parseInt(idField.getText().toString());
        String password =  pwField.getText().toString();

        // Placeholder object to check for correct login
        Staff checkLogin = Registry.getInstance().correctLogin(sID, password);

        // Check if login is correct
        if(checkLogin != null)
        {
            Intent loginIntent = new Intent(getApplicationContext(), atvStaffMain.class);
            startActivity(genMainStaffIntent(checkLogin));
        }

        // Incorrect login details
        else
        {
            tv.setText("Error- Incorrect details");
            tv.setVisibility(View.VISIBLE);
        }
    }

    // Get the correct type of Staff object and put in an Intent
    private Intent genMainStaffIntent(Staff s)
    {
        Intent loginIntent = new Intent(getApplicationContext(), atvStaffMain.class);
        if(s instanceof Chef)
        {
            Chef c = (Chef) s;
            loginIntent.putExtra(atvStaffMain.KEY_STAFF_OBJECT, c);
        }
        else if(s instanceof Manager)
        {
            Manager m = (Manager) s;
            loginIntent.putExtra(atvStaffMain.KEY_STAFF_OBJECT, m);
        }

        else if(s instanceof Waiter)
        {
            Waiter w = (Waiter) s;
            loginIntent.putExtra(atvStaffMain.KEY_STAFF_OBJECT, w);
        }
        return loginIntent;
    }
}
