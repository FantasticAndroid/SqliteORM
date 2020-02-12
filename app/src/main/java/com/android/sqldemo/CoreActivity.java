package com.android.sqldemo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CoreActivity extends AppCompatActivity {
    protected MainApp mainApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApp = (MainApp) getApplication();
    }
}
