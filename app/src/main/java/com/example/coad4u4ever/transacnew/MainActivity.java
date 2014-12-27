package com.example.coad4u4ever.transacnew;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
public class MainActivity extends ActionBarActivity {

    private TextView date_today;
    private Button income_btn;
    private Button outcome_btn;
    private Button table_btn;
    private EditText detail_use;
    private EditText detail_amount;
    //database part
    private DbHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeLayout();

        date_today.setText(getCurrentDate());
        // db
        dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();

        // Listener
        income_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addTransac("in");
            }
        });
        outcome_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addTransac("out");
            }
        });

        table_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getBaseContext(), TransacActivity.class);
                //intent.putExtra("keyDate", date_today.getText().toString());
                startActivityForResult(intent, 1);
            }
        });
    }

    private void addTransac(String type) {
        if (detail_use.length() > 0 && detail_amount.length() > 0) {
            if (isDouble(detail_amount.getText().toString())) {
                if (Double.parseDouble(detail_amount.getText().toString()) >= 0) {
                    ContentValues values = new ContentValues();
                    values.put("detail", detail_use.getText().toString());
                    values.put("amount", Double.parseDouble(detail_amount.getText().toString()));
                    values.put("type", type);
                    values.put("date", System.currentTimeMillis());
                    values.put("balance", getCurrentBalance(type, Double.parseDouble(detail_amount.getText().toString())));
                    database.insert("transac", null, values);
                    Toast.makeText(this, "Add Data Complete", Toast.LENGTH_SHORT).show();
                    detail_use.setText("");
                    detail_amount.setText("");
                } else {
                    Toast.makeText(this, "Please Input Positive Value", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please Input Number Value", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please Input Data", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isDouble(String test) {
        try {
            Double.parseDouble(test);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private double getCurrentBalance(String type, double amount) {
        try {
            Cursor mCursor = database.rawQuery("SELECT balance FROM transac ORDER BY id DESC LIMIT 1", null);
            double newBalance = 0.0;
            if (mCursor != null) {

                mCursor.moveToFirst();
                if (type.equalsIgnoreCase("in")) {
                    newBalance = mCursor.getDouble(mCursor.getColumnIndex("balance")) + amount;
                } else if (type.equalsIgnoreCase("out")) {
                    newBalance = mCursor.getDouble(mCursor.getColumnIndex("balance")) - amount;
                }
            }
            return newBalance;
        } catch (Exception e) {
            return amount;
        }
    }

    public void initializeLayout() {
        date_today = (TextView) findViewById(R.id.date_header);
        income_btn = (Button) findViewById(R.id.btnIncome);
        outcome_btn = (Button) findViewById(R.id.btnOutcome);
        table_btn = (Button) findViewById(R.id.btnTable);
        detail_use = (EditText) findViewById(R.id.detail_use);
        detail_amount = (EditText) findViewById(R.id.detail_amount);
        Log.w("1", "layout init");
    }

    public String getCurrentDate() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        return sdf.format(d);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
