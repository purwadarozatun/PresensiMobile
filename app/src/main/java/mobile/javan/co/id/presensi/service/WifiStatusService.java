package mobile.javan.co.id.presensi.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by Purwa on 07/04/2015.
 */
public class WifiStatusService extends IntentService {
    public WifiStatusService() {
        super("WifiStatusService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("NetworkState", WifiManager.NETWORK_STATE_CHANGED_ACTION);
    }
}
