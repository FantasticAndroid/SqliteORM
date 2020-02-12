package com.android.sqldb;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;

public abstract class SqlQueryCallback<T> implements Runnable {

    private SqlQueryListener<T> sqlQueryListener;
    private final Handler handler;

    /**
     * @param application
     * @param sqlQueryListener
     */
    public SqlQueryCallback(@NonNull Application application, SqlQueryListener<T> sqlQueryListener) {
        handler = new Handler(application.getMainLooper());
        this.sqlQueryListener = sqlQueryListener;
    }

    public void deliverSuccessResult(final @NonNull T response) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                sqlQueryListener.onQuerySuccess(response);
            }
        });
    }

    public void deliverFailedResult(final @NonNull String errorMsg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                sqlQueryListener.onQueryFailed(errorMsg);
            }
        });
    }

    /*public abstract void onQuerySuccess(@NonNull T response);

    public abstract void onQueryFailed(@NonNull String message);*/
}
