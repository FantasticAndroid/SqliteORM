package com.android.sqldemo.provider;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.sqldb.SqlQueryListener;
import com.android.sqldemo.MainApp;
import com.android.sqldemo.sqlimpl.OfferSqlManager;
import com.android.sqldemo.sqlimpl.tables.PointSyncTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class OfferPointsUtils {

    private static final String TAG = OfferPointsUtils.class.getSimpleName();

    /***
     *
     * @param mainApp
     * @param point
     */
    public static void insertOrUpdatePoints(@NonNull MainApp mainApp,
                                            int point,@NonNull SqlQueryListener<Boolean> sqlQueryListener) {
        Log.d(TAG, "insertOrUpdatePoints()");

        String currentDate = getDateWithPattern(Calendar.getInstance().getTime(), "dd-MMM-yyyy", null);

        OfferSqlManager offerSqlManager = OfferSqlManager.getInstance(mainApp);
        offerSqlManager.insertPointInSyncModel(currentDate,
                50000, point, sqlQueryListener);
    }

    /**
     *
     * @param mainApp
     * @param sqlQueryListener
     */
    public static void getAllPoints(@NonNull MainApp mainApp, @NonNull SqlQueryListener<ArrayList<PointSyncTable>> sqlQueryListener) {
        Log.d(TAG, "getAllPointsAndSyncToServer()");
        //MegaOfferSharedPref megaOfferSharedPref = MegaOfferSharedPref.getInstance(mainApp);
        OfferSqlManager offerSqlManager = OfferSqlManager.getInstance(mainApp);

        offerSqlManager.getPointsFromSyncTable(sqlQueryListener);
    }


    /***
     *
     * @param mainApp
     * @param megaOfferTodayDate
     */
    static void deleteAllSyncedPoints(@NonNull MainApp mainApp, @NonNull String megaOfferTodayDate) {
        try {
            OfferSqlManager offerSqlManager = OfferSqlManager.getInstance(mainApp);
            offerSqlManager.deleteRowsNotHavingCurrentDate(megaOfferTodayDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param date
     * @param patternRequired
     * @param timeZone
     * @return String
     */
    public static String getDateWithPattern(@NonNull Date date, @NonNull String patternRequired,
                                            TimeZone timeZone) {
        try {
            SimpleDateFormat requiredSdf = new SimpleDateFormat(patternRequired);
            if (timeZone == null) {
                requiredSdf.setTimeZone(TimeZone.getDefault());
            } else {
                requiredSdf.setTimeZone(timeZone);
            }
            return requiredSdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return date.toString();
        }
    }
}
