package com.se.pete.e_restaurant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.se.pete.e_restaurant.restaurant.Manager;
import com.se.pete.e_restaurant.restaurant.Registry;
import com.se.pete.e_restaurant.restaurant.Staff;

import java.util.List;

public class atvStaffRemoveStaff extends AppCompatActivity
{
    ListView lvStaff;
    Manager manager;
    Intent callingStaffMem;

    StaffRemoveAdapter removeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_staff_remove_staff);

        lvStaff = findViewById(R.id.lvStaff);

        callingStaffMem = getIntent();
        manager = (Manager) callingStaffMem.getSerializableExtra(atvStaffMain.KEY_STAFF_OBJECT);

        removeAdapter = new StaffRemoveAdapter(getApplicationContext()
        , R.layout.menurow
        , Registry.getInstance().getStaffList());

        lvStaff.setAdapter(removeAdapter);

        lvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Staff selectedStaff = (Staff) adapterView.getItemAtPosition(i);

                // Ensure the user cannot remove the root account or currently logged in account.
                if(selectedStaff.getID() == 0 || selectedStaff.getID() == manager.getID())
                {
                    Snackbar.make(lvStaff, "Error- Can't remove root/current staff member!"
                            , Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }

                // Confirm the removal of the selected staff member
                confirmRemoveStaff(selectedStaff);
            }
        });
    }

    private class StaffRemoveAdapter extends ArrayAdapter
    {
        private List<Staff> staffList;
        private int resource;
        private LayoutInflater inflater;

        public StaffRemoveAdapter(@NonNull Context context, int resourceP, @NonNull List objects)
        {
            super(context, resourceP, objects);
            staffList = objects;
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
            TextView tvStaffRole = (TextView) convertView.findViewById(R.id.tvStatus);
            TextView tvStaffID = convertView.findViewById(R.id.tvStock);
            TextView tvHide = (TextView) convertView.findViewById(R.id.tvFoodPrice);

            Staff staffMem = staffList.get(position);

            tvStaffName.setText(staffMem.getName().toString());

            //Get the staff member's object type (waiter, chef, manager)
            tvStaffRole.setText(staffMem.getClass().getSimpleName().toString());

            tvStaffID.setText("ID:   " + String.valueOf(staffMem.getID()));
            tvHide.setVisibility(View.INVISIBLE);

            return convertView;
        }

        public Staff getItemAtPosition(int position){return staffList.get(position);}
    }

    // Creates dialog to confirm removal of selectedStaff from system
    public void confirmRemoveStaff(final Staff selectedStaff)
    {
        AlertDialog.Builder dialogRemoveStaff = new AlertDialog.Builder(atvStaffRemoveStaff.this);

        dialogRemoveStaff.setCancelable(true);
        dialogRemoveStaff.setTitle("Remove " + selectedStaff.getName() + "?");
        dialogRemoveStaff.setMessage("Are you sure you want to remove " + selectedStaff.getName()
                + " from the system?");

        dialogRemoveStaff.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.cancel();
            }
        });
        dialogRemoveStaff.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                manager.removeStaff(selectedStaff);

                Snackbar.make(lvStaff, selectedStaff.getName() + " removed.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                removeAdapter.notifyDataSetChanged();


            }
        });
        dialogRemoveStaff.show();
    }
}
