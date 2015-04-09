package mobile.javan.co.id.presensi.model.receiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.Date;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.Settings;
import mobile.javan.co.id.presensi.model.adapter.result.PresensiResultAdapter;
import mobile.javan.co.id.presensi.service.ConnectionFragment;
import mobile.javan.co.id.presensi.util.Statics;

/**
 * Created by Purwa on 08/04/2015.
 */
public class WifiStatusReceiver extends BroadcastReceiver {
    final public static String JAVANSSID = "60:02:b4:bb:29:5d";

    private int notifLogo;
    private Activity mActivity;

    @Override
    public void onReceive(Context context, Intent intent) {


        String ssid = new ConnectionFragment().getWifiStatus(context);
        Settings currentSettings = new Gson().fromJson(new Statics().getConfigData(context), Settings.class);


        String currentWatch = currentSettings.getWatchNik();

        Person person = new PresensiResultAdapter().getPersonByNik(currentWatch);
        LocalDateTime thisTime = LocalDateTime.fromDateFields(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 10);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.AM_PM, 0);
        cal.set(Calendar.MILLISECOND, 0000);


        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(new Date());
        cal1.set(Calendar.HOUR, 7);
        cal1.set(Calendar.MINUTE, 00);
        cal1.set(Calendar.SECOND, 00);
        cal1.set(Calendar.AM_PM, 0);
        cal1.set(Calendar.MILLISECOND, 0000);

        LocalDateTime endTime = LocalDateTime.fromDateFields(cal.getTime());
        LocalDateTime jamMasuk = LocalDateTime.fromDateFields(cal.getTime());
        Log.v("CurrentSetting", new Gson().toJson(currentSettings));
        if (person == null) {
            return;
        }
        if (thisTime.isAfter(jamMasuk)) {
            if (thisTime.isAfter(endTime) && currentSettings.getIsPulangNotifShow() == false) {
                currentSettings.setIsDatangNotifShow(false);
                if (person != null && person.getJamKeluar() == null && person.getDurasiKerja() >= 9) {
                    new Statics().setNotification(context, "Jam Kerja Anda Telah Cukup", "Silahkan Absen Pulang", notifLogo);
                    currentSettings.setIsPulangNotifShow(true);
                }
            }
            if (thisTime.isBefore(endTime) && currentSettings.getIsDatangNotifShow() == false) {
                currentSettings.setIsPulangNotifShow(false);
                if (person != null && person.getJamMasuk() == null) {
                    if (ssid.equalsIgnoreCase(JAVANSSID)) {
                        new Statics().setNotification(context, "Selamat Datang", "Anda Telah Terkoneksi Ke Javan Labs, Silahkan Absen", notifLogo);
                        currentSettings.setIsDatangNotifShow(true);
                    }
                }
            }
        } else {
            currentSettings.setIsPulangNotifShow(false);
            currentSettings.setIsDatangNotifShow(false);
        }
        new Statics().setConfgDataNoNotif(currentSettings, context);

    }

    public void SetAlarm(Context context, int notifLogo, Activity mActivity) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WifiStatusReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        this.notifLogo = notifLogo;
        this.mActivity = mActivity;
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 10, pi);
    }

    public void CancelAlarm(Context context) {
        Intent intent = new Intent(context, WifiStatusReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WifiStatusReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }

}
