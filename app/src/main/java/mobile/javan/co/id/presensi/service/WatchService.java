package mobile.javan.co.id.presensi.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;

import com.google.gson.Gson;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.adapter.result.PresensiResultAdapter;

/**
 * Created by Purwa on 07/04/2015.
 */
public class WatchService extends IntentService {
    public Person mPerson = new Person();
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.vogella.android.service.receiver";
    private int result = Activity.RESULT_CANCELED;

    public WatchService() {
        super("WatchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String currentNik = intent.getExtras().getString("nik");
        PresensiResultAdapter presensiResultAdapter = new PresensiResultAdapter();
        mPerson = presensiResultAdapter.getPersonByNik(currentNik);
        if (mPerson != null) {
            result = Activity.RESULT_OK;
            publishResults(new Gson().toJson(mPerson), result);
        }
    }

    private void publishResults(String outputPath, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
