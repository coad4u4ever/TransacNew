package com.example.coad4u4ever.transacnew;

import android.content.AsyncTaskLoader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.coad4u4ever.transacnew.DbHelper;

public class TransacLoader extends AsyncTaskLoader<Cursor> {
    public static final String TransacLoader_RELOAD = "TransacLoader.RELOAD";
    Receiver receiver;
    Cursor loaderCur = null;
    private DbHelper dbHelper;
    private SQLiteDatabase database;

    public TransacLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        Log.v("2", "##onStartLoading");
        receiver = new Receiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(TransacLoader_RELOAD);
        getContext().registerReceiver(receiver, filter);
        if (loaderCur != null) {
            super.deliverResult(loaderCur);
        }
        forceLoad();
        super.onStartLoading();
    }

    @Override
    public Cursor loadInBackground() {
        Log.v("1", "##222");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dbHelper = new DbHelper(getContext());
        database = dbHelper.getWritableDatabase();

        String[] allColumns = new String[]{"id", "detail", "amount", "type", "balance"};
        Cursor c = database.query("transac", allColumns, null, null, null,
                null, null);
        if (c != null) {
            c.moveToFirst();
        }
        database.close();
        return c;
    }

    @Override
    public void deliverResult(Cursor data) {
        loaderCur = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onReset() {
        getContext().unregisterReceiver(receiver);
        this.stopLoading();
        super.onReset();
    }

    public class Receiver extends BroadcastReceiver {

        TransacLoader loader;

        public Receiver(TransacLoader loader) {
            this.loader = loader;
        }


        @Override
        public void onReceive(Context context, Intent intent) {

            loader.onContentChanged();
        }

    }


}
