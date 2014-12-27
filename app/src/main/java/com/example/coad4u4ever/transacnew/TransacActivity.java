package com.example.coad4u4ever.transacnew;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class TransacActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //Calendar myCalendar = Calendar.getInstance();
    //private Button btnSelectDate;
    private Button btnClear;
    //private Button btn_backhome;
    private DbHelper dbHelper;
    private SQLiteDatabase database;
    //ProgressDialog PD;
    private TableLayout tableShow;
    private Cursor loaderCur;
    // this idOnPage will store every id show in transac table in activity_show_transac.xml
    // which will be used for reference in deleteTransac(id) method.
    private ArrayList<Integer> idOnPage;

    /* private void updateLabel() {
         String myFormat = "dd-MM-yyyy";
         SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
         btnSelectDate.setText(sdf.format(myCalendar.getTime()));
     }
     */
    /*
    public static Long persistDate(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return null;
    }

*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transac);
        //btnSelectDate = (Button)findViewById(R.id.btnSelectDate);
        btnClear = (Button) findViewById(R.id.btnClear);
        tableShow = (TableLayout) findViewById(R.id.tableShow);
        //btn_backhome = (Button)findViewById(R.id.btn_backhome);
        //btnSelectDate.setText(getIntent().getExtras().getString("keyDate"));
        this.getLoaderManager().initLoader(1, null, this);
        /*
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    new DatePickerDialog(TransacActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                    Toast.makeText(getBaseContext(), "date: " + (new Date().getTime()/86400000), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getBaseContext(), "date: " + myCalendar.getTimeInMillis()/86400000, Toast.LENGTH_SHORT).show();

                    Date date = new Date();
                    //86 400 000 mills = 1 day
                    long millis = getDateTest();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Toast.makeText(getBaseContext(), "date: " + sdf.format(new Date(millis)), Toast.LENGTH_SHORT).show();


                }catch (Exception e){
                    Toast.makeText(getBaseContext(), "No data", Toast.LENGTH_SHORT).show();
                }



            }

        });
         */
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                for(int i : idOnPage){
                    deleteTransac(i);
                }
                */
                deleteTransac();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivityForResult(intent, 1);
                Toast.makeText(getBaseContext(), "Delete transac complete.", Toast.LENGTH_SHORT).show();
            }
        });
        /*
        btn_backhome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        */
    }

    // in this method, transacs  will be deleted by its ID.
    /*public void deleteTransac(int id) {
        dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
        database.delete("transac", "id = " + id, null);
        // Toast.makeText(this, "Delete Data Id " + id + " Complete", Toast.LENGTH_SHORT).show();
        dbHelper.close();
    }
    */

    // this will delete all transac
    public void deleteTransac() {
        dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
        database.delete("transac", null, null);
        // Toast.makeText(this, "Delete Data Id " + id + " Complete", Toast.LENGTH_SHORT).show();
        dbHelper.close();
    }

    /*
    private long getDateTest(){
        dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
        Cursor mCursor = database.rawQuery("SELECT date FROM transac ORDER BY id DESC LIMIT 1", null);
        long date=0;
        if(mCursor!= null) {
            mCursor.moveToFirst();
            date = mCursor.getLong(mCursor.getColumnIndex("date"));
        }
        dbHelper.close();
        return date;
    }

    */
    private void clearTable() {
        TableLayout table = (TableLayout) findViewById(R.id.tableShow);
        table.removeAllViews();
    }

    private void BuildTable() {
        clearTable();
        dbHelper = new DbHelper(this);
        database = dbHelper.getWritableDatabase();
        idOnPage = new ArrayList<>();
        Cursor c = loaderCur;

        int rows = c.getCount();
        int cols = c.getColumnCount();
        c.moveToFirst();
        int count = 0;
        // outer for loop
        for (int i = 0; i < rows; i++) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            if (i % 2 == 0) {
                row.setBackgroundColor(Color.YELLOW);
            }
            // inner for loop
            for (int j = 0; j < cols; j++) {

                TextView tv = new TextView(this);
                tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
                tv.setGravity(Gravity.CENTER);
                tv.setTextSize(18);
                tv.setPadding(0, 5, 0, 5);
                tv.setText(c.getString(j));
                if (j == 0) {
                    idOnPage.add(Integer.parseInt(c.getString(j)));
                    tv.setText("" + (++count));

                }
                row.addView(tv);
            }
            c.moveToNext();
            tableShow.addView(row);
        }
        dbHelper.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_transac, menu);
        return true;
    }

    /*
        public void reloadStrings(View v) {
            Intent intent = new Intent();
            intent.setAction(TransacLoader.TransacLoader_RELOAD);
            sendBroadcast(intent);
        }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v("1", "##create loader");
        return new TransacLoader(this);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        this.loaderCur = data;
        BuildTable();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
