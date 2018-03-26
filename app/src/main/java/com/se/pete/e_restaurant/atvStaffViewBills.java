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

import com.se.pete.e_restaurant.restaurant.Bill;
import com.se.pete.e_restaurant.restaurant.Registry;

import java.util.List;

public class atvStaffViewBills extends AppCompatActivity
{
    public static final String KEY_SPECIFIC_ORDER = "com.se.pete.e_restaurant.SPECIFIC_ORDER";
    ListView lvBills;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atv_staff_view_bills);

        lvBills = (ListView) findViewById(R.id.lvBills);

        // Check if this was started for a different purpose
        //e.g. refund or printing receipt
        Intent flagIn = getIntent();

        final StaffBillAdapter billAdapter = new StaffBillAdapter(getApplicationContext(),
                R.layout.menurow, Registry.getInstance().getBillList());
        lvBills.setAdapter(billAdapter);

        // If this has been opened for a refund
        if(flagIn.getBooleanExtra(atvStaffMain.KEY_REFUND, false))
        {
            lvBills.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {


                    final Bill selectedBill = (Bill) adapterView.getItemAtPosition(i);

                    // Check if bill was already refunded
                    if(!selectedBill.wasRefunded())
                    {

                        // Dialog to confirm refund
                        AlertDialog.Builder builder = new AlertDialog.Builder(atvStaffViewBills.this);

                        builder.setCancelable(true);
                        builder.setTitle("Refund bill?");
                        builder.setMessage("Are you sure you want to refund this bill?");

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.cancel();

                            }
                        });
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.dismiss();
                                selectedBill.refundBill();
                                selectedBill.setPaymentMethod("REFUNDED");

                                Snackbar.make(lvBills, "Bill Refunded.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                billAdapter.notifyDataSetChanged();
                            }
                        });
                        builder.show();

                    }

                    // Refund was already made for this bill.
                    else
                    {
                        Snackbar.make(lvBills, "Bill was already refunded. Nothing changed."
                                , Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            });
        }

        // The bill is to be printed when tapped. (STUB)
        else if(flagIn.getBooleanExtra(atvStaffMain.KEY_RECEIPT, false))
        {

            lvBills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    AlertDialog.Builder dialogPrinting = new AlertDialog.Builder(atvStaffViewBills.this);

                    dialogPrinting.setCancelable(true);
                    dialogPrinting.setTitle("Bill Printing");
                    dialogPrinting.setMessage("The selected bill is now printing.");

                    dialogPrinting.setNegativeButton("Okay", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            dialogInterface.cancel();
                        }
                    });
                    dialogPrinting.show();
                }
            });

        }


        // No flag- tapping on a bill shows that bill's orders
        else
        {

            lvBills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*
                * Pass the bill to the view orders activity.
                * It will display all of that specific bill's orders.
                */
                    Bill selectedBill = (Bill) adapterView.getItemAtPosition(i);

                    Intent intent = new Intent(getApplicationContext(), atvStaffViewOrders.class);
                    intent.putExtra(KEY_SPECIFIC_ORDER, selectedBill);
                    startActivity(intent);
                }
            });
        }

        // Error for no orders in system
        if(Registry.getInstance().getOrderList().isEmpty())
        {
            AlertDialog.Builder dialogNoOrders = new AlertDialog.Builder(atvStaffViewBills.this);

            dialogNoOrders.setCancelable(true);
            dialogNoOrders.setTitle("No Orders");
            dialogNoOrders.setMessage("No orders have been made in the system.");

            dialogNoOrders.setNegativeButton("Okay", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    dialogInterface.cancel();
                    finish();
                }
            });
            dialogNoOrders.show();
        }
    }

    private class StaffBillAdapter extends ArrayAdapter
    {
        private List<Bill> bills;
        private int resource;
        private LayoutInflater inflater;

        public StaffBillAdapter(@NonNull Context context, int resourceP, @NonNull List objects)
        {
            super(context, resourceP, objects);
            bills = objects;
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

            TextView tvBillID = (TextView) convertView.findViewById(R.id.tvFoodName);
            TextView tvCustID = (TextView) convertView.findViewById(R.id.tvStatus);
            TextView tvPrice = convertView.findViewById(R.id.tvStock);
            TextView tvHIDE = (TextView) convertView.findViewById(R.id.tvFoodPrice);

            Bill bill= bills.get(position);

            StringBuffer contents = new StringBuffer();

            tvBillID.setText(String.valueOf(bill.getBillID()));
            tvPrice.setText("£" + String.valueOf(bill.getPrice()));
            tvCustID.setText("Table Num: " + String.valueOf(bill.getCustomer().getTableNumber()));
            tvHIDE.setVisibility(View.INVISIBLE);

            return convertView;
        }

        public Bill getItemAtPosition(int position){return bills.get(position);}
    }
}
