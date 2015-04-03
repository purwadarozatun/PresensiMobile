package mobile.javan.co.id.presensi.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import java.net.URL;

/**
 * Created by Purwa on 01/04/2015.
 */
public class DownloadPresensiData extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public DownloadPresensiData() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
