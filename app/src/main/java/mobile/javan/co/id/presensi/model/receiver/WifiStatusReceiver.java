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

        String currentWatch = ((Settings) new Gson().fromJson(new Statics().getConfigData(context), Settings.class)).getWatchNik();

        String ssid = new ConnectionFragment().getWifiStatus(context);
        Person person = new PresensiResultAdapter().getPersonByNik(currentWatch);
        LocalDateTime thisTime = LocalDateTime.fromDateFields(new Date());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 10);
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.SECOND, 00);
        cal.set(Calendar.AM_PM , 0);
        cal.set(Calendar.MILLISECOND, 0000);

        LocalDateTime endTime = LocalDateTime.fromDateFields(cal.getTime());
        if (thisTime.isAfter(endTime)) {
            if (person != null && person.getJamKeluar() == null && person.getDurasiKerja() >= 9) {
                new Statics().setNotification(context, "Jam Kerja Anda Telah Cukup", "Silahkan Absen Pulang", notifLogo);
            }
            return;
        }

        if (person != null && person.getJamMasuk() == null) {
            if (ssid.equalsIgnoreCase(JAVANSSID)) {
                new Statics().setNotification(context, "Selamat Datang", "Anda Telah Terkoneksi Ke Javan Labs, Silahkan Absen", notifLogo);
            }
        }
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
