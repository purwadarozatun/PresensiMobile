package mobile.javan.co.id.presensi.service;

import android.os.AsyncTask;
import android.util.Log;

import java.net.URL;

/**
 * Created by Purwa on 01/04/2015.
 */
public class DownloadTask extends AsyncTask<URL, Integer, Long> {
    @Override
    protected Long doInBackground(URL... params) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        Log.v("Tasks", "Downloaded");
    }
}
