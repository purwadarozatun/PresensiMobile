package mobile.javan.co.id.presensi.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import mobile.javan.co.id.presensi.model.adapter.result.PresensiResultAdapter;
import mobile.javan.co.id.presensi.util.Statics;

/**
 * Created by Purwa on 01/04/2015.
 */
public class DownloadPresensiData extends IntentService {
    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.vogella.android.service.receiver";
    public PresensiResultAdapter PRESENSI_RESULT_ADAPTER = new PresensiResultAdapter();


    public DownloadPresensiData() {
        super("DownloadPresensiData");
    }

    public DownloadPresensiData(String name, int result, PresensiResultAdapter PRESENSI_RESULT_ADAPTER) {
        super(name);
        this.result = result;
        this.PRESENSI_RESULT_ADAPTER = PRESENSI_RESULT_ADAPTER;
    }

    // will be called asynchronously by Android
    @Override
    protected void onHandleIntent(Intent intent) {
        ConnectionFragment connectionFragment = new ConnectionFragment();
        Date tanggal;
        if(intent.getExtras().get("tanggal") != null){
            tanggal = new Date(intent.getExtras().getLong("tanggal", -1));
        }else{
            tanggal = new Date();
        }
        PRESENSI_RESULT_ADAPTER = connectionFragment.getPresensis(tanggal);
        // successfully finished
        if (PRESENSI_RESULT_ADAPTER != null && PRESENSI_RESULT_ADAPTER.getResult().size() > 0) {
            result = Activity.RESULT_OK;
        }
        publishResults(new Gson().toJson(PRESENSI_RESULT_ADAPTER.getResult()), result);
    }

    private void publishResults(String outputPath, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

}
