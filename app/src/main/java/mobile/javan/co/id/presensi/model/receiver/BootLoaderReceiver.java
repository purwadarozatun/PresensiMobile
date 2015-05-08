package mobile.javan.co.id.presensi.model.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Purwa on 15/04/2015.
 */
public class BootLoaderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WifiStatusReceiver wifiStatusReceiver = new WifiStatusReceiver();
        if (wifiStatusReceiver != null) {
            wifiStatusReceiver.SetAlarm(context);
        }
    }

}
