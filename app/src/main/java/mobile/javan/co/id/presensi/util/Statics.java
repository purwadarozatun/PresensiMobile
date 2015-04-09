package mobile.javan.co.id.presensi.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import mobile.javan.co.id.presensi.MainActivity;
import mobile.javan.co.id.presensi.R;
import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.Settings;
import mobile.javan.co.id.presensi.model.adapter.result.PresensiResultAdapter;

/**
 * Created by Purwa on 21/03/2015.
 */
public class Statics {

    public String configfilename = "presensiSettingFile";

    public void setConfgData(Settings settings, Context activity) {
        String FILENAME = configfilename;
        String string = new Gson().toJson(settings);
        try {

            PresensiResultAdapter presensiResultAdapter = new PresensiResultAdapter();
            Person person = presensiResultAdapter.getPersonByNik(settings.getWatchNik().trim());
            if (person == null) {
                Toast.makeText(activity, "Cant Save Config File , Nik Not Found In Database", Toast.LENGTH_SHORT).show();
            } else {
                FileOutputStream fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write(string.getBytes());
                fos.close();
                Log.v("CurrentSetting", "Config Saved");
                Toast.makeText(activity, "Config Saved!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(activity, "Cant Save Config File", Toast.LENGTH_SHORT).show();

        }

    }

    public void setConfgDataNoNotif(Settings settings, Context activity) {
        String FILENAME = configfilename;
        String string = new Gson().toJson(settings);
        try {

            PresensiResultAdapter presensiResultAdapter = new PresensiResultAdapter();
            Person person = presensiResultAdapter.getPersonByNik(settings.getWatchNik().trim());
            if (person == null) {
            } else {
                FileOutputStream fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write(string.getBytes());
                fos.close();
                Log.v("CurrentSetting", "Config Saved");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    public String getConfigData(Context activity) {
        String FILENAME = configfilename;
        String fileResponse = null;

        try {
            FileInputStream fos = activity.openFileInput(FILENAME);
            fileResponse = new BufferedReader(new InputStreamReader(fos)).readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return fileResponse;
    }


    public Date getDateFrom(String pattern, String input, Date devault) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.parse(input);

        } catch (Exception ex) {
            return devault;
        }

    }

    public String getStringFrom(String pattern, Date input, String devault) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(input);

        } catch (Exception ex) {
            return devault;
        }

    }

    public void setNotification(Context context, CharSequence contentTitle, CharSequence contentText, int notifIcon) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.abc_btn_check_material)
                        .setContentTitle(contentTitle)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS)
                        .setContentText(contentText);

        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());

    }


}
