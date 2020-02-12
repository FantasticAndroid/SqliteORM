package com.android.sqldemo.sqlimpl;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sqldb.SqlQueryCallback;
import com.android.sqldb.SqlQueryListener;
import com.android.sqldemo.MainApp;
import com.android.sqldemo.sqlimpl.dao.PointSyncTableDao;
import com.android.sqldemo.sqlimpl.tables.PointSyncTable;

import java.util.ArrayList;

public class OfferSqlManager {

    private MainApp mainApp;

    private final String TAG = OfferSqlManager.class.getSimpleName();

    private static OfferSqlManager offerSqlManager;

    /***
     *
     * @param context
     * @return
     */
    public static OfferSqlManager getInstance(@NonNull MainApp context) {
        if (offerSqlManager == null) {
            offerSqlManager = new OfferSqlManager(context);
        }
        return offerSqlManager;
    }

    /**
     * @param context
     */
    private OfferSqlManager(@NonNull MainApp context) {
        this.mainApp = context;
    }


    /***
     *
     * @param currentDate
     * @param maxPointsCount
     * @param point
     * @param sqlQueryListener
     */
    public synchronized void insertPointInSyncModel(@NonNull String currentDate, int maxPointsCount,
                                                    int point, @Nullable SqlQueryListener<Boolean> sqlQueryListener) {
        OfferSqlAdapter offerSqlAdapter = mainApp.getMegaOfferAdapter();
        if (offerSqlAdapter != null) {
            Handler handler = new Handler(mainApp.getMainLooper());
            new Thread(new SqlQueryCallback<Boolean>(handler, sqlQueryListener) {
                @Override
                public void run() {
                    try {
                        PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
                        long id = pointSyncTableDao.insertOrUpdatePoint(offerSqlAdapter.getSqliteDatabase(), currentDate, maxPointsCount, point);
                        if (id == -1) {
                            deliverFailedResult("Insertion/Updation Failed with id -1");
                        } else {
                            deliverSuccessResult(true);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "insertPointInSyncModel() " + e.getMessage());
                        deliverFailedResult(e.getMessage() + "");
                    }
                }
            }).start();
        }
    }

    /***
     *
     * @param sqlQueryListener
     */
    public synchronized void getPointsFromSyncTable(@NonNull SqlQueryListener<ArrayList<PointSyncTable>> sqlQueryListener) {
        OfferSqlAdapter offerSqlAdapter = mainApp.getMegaOfferAdapter();
        if (offerSqlAdapter != null) {
            Handler handler = new Handler(mainApp.getMainLooper());
            new Thread(new SqlQueryCallback<ArrayList<PointSyncTable>>(handler, sqlQueryListener) {
                @Override
                public void run() {
                    try {
                        PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
                        ArrayList<PointSyncTable> pointsSyncTableArrayList = pointSyncTableDao.getAllPointsFromTable
                                (offerSqlAdapter.getSqliteDatabase());
                        deliverSuccessResult(pointsSyncTableArrayList);
                    } catch (Exception e) {
                        Log.e("getAllCoinsOptedEntries", e.getMessage() + "");
                        deliverFailedResult(e.getMessage() + "");
                    }
                }
            }).start();
        }
    }

    /***
     *
     * @param currentDate
     * @param sqlQueryListener
     */
    public synchronized void insertAppVisitedStatus(@NonNull String currentDate,
                                                    @NonNull SqlQueryListener<Boolean> sqlQueryListener) {
        OfferSqlAdapter offerSqlAdapter = mainApp.getMegaOfferAdapter();
        if (offerSqlAdapter != null) {
            Handler handler = new Handler(mainApp.getMainLooper());
            new Thread(new SqlQueryCallback<Boolean>(handler, sqlQueryListener) {
                @Override
                public void run() {
                    try {
                        PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
                        long rowId = pointSyncTableDao.insertAppVisited(offerSqlAdapter.getSqliteDatabase(), currentDate);

                        if (rowId == -1) {
                            deliverFailedResult("Insertion/Updation Failed with id -1");
                        } else {
                            deliverSuccessResult(true);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "insertPointInSyncModel() " + e.getMessage());
                        deliverFailedResult(e.getMessage() + "");
                    }
                }
            }).start();
        }
    }

    /***
     *
     * @param currentDate
     */
    public void deleteRowsNotHavingCurrentDate(String currentDate) throws Exception {
        OfferSqlAdapter offerSqlAdapter = mainApp.getMegaOfferAdapter();
        PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
        pointSyncTableDao.deleteRowsNotHavingCurrentDate(offerSqlAdapter.getSqliteDatabase(), currentDate);
    }

    /***
     *
     * @param maxPointCount
     */
    public void deleteAllSyncedPointsRow(int maxPointCount) {
        OfferSqlAdapter offerSqlAdapter = mainApp.getMegaOfferAdapter();
        PointSyncTableDao pointSyncTableDao = new PointSyncTableDao();
        pointSyncTableDao.deleteAllSyncedPointsRow(offerSqlAdapter.getSqliteDatabase(), maxPointCount);
    }
}
