package com.android.sqldemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.sqldb.SqlQueryCallback;
import com.android.sqldb.SqlQueryListener;
import com.android.sqldemo.provider.OfferPointsUtils;
import com.android.sqldemo.sqlimpl.tables.PointSyncTable;

import java.util.ArrayList;

public class MainActivity extends CoreActivity implements View.OnClickListener {

    private TextView outputTv;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_get).setOnClickListener(this);
        findViewById(R.id.btn_insert).setOnClickListener(this);
        outputTv = findViewById(R.id.tv_output);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get:
                readOfferPoints();
                break;
            case R.id.btn_insert:
                insertOfferPoints();
                break;
        }
    }

    private void insertOfferPoints() {
        int point = 1;
        OfferPointsUtils.insertOrUpdatePoints(mainApp,
                point, new SqlQueryListener<Boolean>() {
                    @Override
                    public void onQuerySuccess(@NonNull Boolean response) {
                        Log.d(TAG, "insertOrUpdatePoints onQuerySuccess(): " + response);
                        if (response.equals(true)) {
                            outputTv.setText(point + " is Inserted");
                        }
                    }

                    @Override
                    public void onQueryFailed(@NonNull String message) {
                        Log.e(TAG, "insertOrUpdatePoints onQueryFailed(): " + message);
                        outputTv.setText("ErrorMessage: " + message);
                    }
                });

    }

    private void readOfferPoints() {

        OfferPointsUtils.getAllPoints(mainApp,
                new SqlQueryListener<ArrayList<PointSyncTable>>() {
                    @Override
                    public void onQuerySuccess(@NonNull ArrayList<PointSyncTable> response) {
                        if (!response.isEmpty()) {
                            outputTv.setText(response.toString());
                        } else {
                            outputTv.setText("No Points found");
                        }
                    }

                    @Override
                    public void onQueryFailed(@NonNull String message) {
                        outputTv.setText("ErrorMessage: " + message);
                    }
                });
    }
}
