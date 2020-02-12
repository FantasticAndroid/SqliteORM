package com.android.sqldb;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;

public abstract class SqlQueryCallback<T> implements Runnable{

    private Context context;
    private SqlQueryListener<T> sqlQueryListener;

    /**
     *
     * @param context
     * @param sqlQueryListener
     */
    public SqlQueryCallback(@NonNull Context context,SqlQueryListener<T> sqlQueryListener) {
        this.context = context;
        this.sqlQueryListener = sqlQueryListener;
    }

    public void deliverSuccessResult(final @NonNull T response) {
        final Handler handler = new Handler(context.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                sqlQueryListener.onQuerySuccess(response);
            }
        });
    }

    public void deliverFailedResult(final @NonNull String errorMsg) {
        final Handler handler = new Handler(context.getMainLooper());
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
