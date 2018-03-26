package com.se.pete.e_restaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.se.pete.e_restaurant.restaurant.Chef;
import com.se.pete.e_restaurant.restaurant.Manager;
import com.se.pete.e_restaurant.restaurant.Registry;
import com.se.pete.e_restaurant.restaurant.Staff;
import com.se.pete.e_restaurant.restaurant.Waiter;

public class atvStaffAddStaff extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    Spinner spnRoles;
    EditText etName;
    EditText etPassword;

    // New staff object to be added to system
    Staff newStaff;
    int newID = Staff.getIdCounter();

    Intent fromMain;

    ArrayAdapter<CharSequence> rolesAdapter;

    // User's role selection
    private String roleSelection;
    private String name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_staff_add_staff);

        fromMain = getIntent();

        // Set what the ID of this staff will be if added
        TextView tvID = findViewById(R.id.tvStaffID);
        tvID.setText(String.valueOf(newID));

        etName = findViewById(R.id.etStaffName);
        etPassword = findViewById(R.id.etStaffPassword);

        // Set the spinner for the list of roles to choose from
        spnRoles = findViewById(R.id.spnStaffRole);
        rolesAdapter = ArrayAdapter.createFromResource(getApplicationContext()
        , R.array.roles, R.layout.spinnerlayout);
        rolesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRoles.setAdapter(rolesAdapter);
        spnRoles.setOnItemSelectedListener(this);
    }

    // Spinner's onItemSelected logic
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        // Update user's location selection
        roleSelection = (String) adapterView.getItemAtPosition(i);
        rolesAdapter.notifyDataSetChanged();
        System.out.println(roleSelection + " is what you selected.");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        roleSelection = null;
    }

    public void confirmStaffAdd(View v)
    {
        name = etName.getText().toString();
        password = etPassword.getText().toString();

        if(name.equals("") || password.equals(""))
        {
            AlertDialog.Builder dialogEmptyFields = new AlertDialog.Builder(atvStaffAddStaff.this);

            dialogEmptyFields.setCancelable(true);
            dialogEmptyFields.setTitle("Error- No name or Password entered!");
            dialogEmptyFields.setMessage("Please enter a name or password.");

            dialogEmptyFields.setNegativeButton("Okay", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.cancel();
                }
            });
            dialogEmptyFields.show();
        }

        else
        {
            switch (roleSelection)
            {
                case "Waiter":
                    newStaff = new Waiter(newID, name, password);
                    break;

                case "Chef":
                    newStaff = new Chef(newID, name, password);
                    break;

                case "Manager":
                    newStaff = new Manager(newID, name, password);
                    break;

                default:
                    Snackbar.make(v, "Error with role- Nothing changed.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
            }
            Registry.getInstance().addStaff(newStaff);

            Intent finishAdd = new Intent(getApplicationContext(), atvStaffMain.class);

            // Add flag for returning to staff main and put calling staff member in
            finishAdd.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finishAdd.putExtra(atvStaffMain.KEY_STAFF_OBJECT,
                    fromMain.getSerializableExtra(atvStaffMain.KEY_STAFF_OBJECT));
            finishAdd.putExtra(atvStaffMain.KEY_STAFF_ADDED, true);
            startActivity(finishAdd);
        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(Registry.getInstance().searchStaffByID(newID) == null)
            Staff.decrementCounter();
    }
}
