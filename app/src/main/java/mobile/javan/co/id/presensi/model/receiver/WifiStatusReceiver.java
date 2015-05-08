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


    @Override
    public void onReceive(Context context, Intent intent) {
        String ssid = new ConnectionFragment().getWifiStatus(context);

        Settings currentSettings = null;
        try {
            currentSettings = new Gson().fromJson(new Statics().getConfigData(context), Settings.class);
        } catch (Exception ex) {
            return;
        }

        if ((currentSettings == null || ssid == null)) {
            return;
        }

        if(!ssid.equalsIgnoreCase(JAVANSSID)){
            return;
        }

        String currentWatch = currentSettings.getWatchNik();

        LocalDateTime thisTime = LocalDateTime.fromDateFields(new Date());
        Calendar endJamMasuk = Calendar.getInstance();

        Calendar jamMasuk = Calendar.getInstance();
        jamMasuk.setTime(new Date());
        jamMasuk.set(Calendar.HOUR, 7);
        jamMasuk.set(Calendar.MINUTE, 00);
        jamMasuk.set(Calendar.SECOND, 00);
        jamMasuk.set(Calendar.AM_PM, 0);
        jamMasuk.set(Calendar.MILLISECOND, 0000);

        endJamMasuk.setTime(new Date());
        endJamMasuk.set(Calendar.HOUR, 10);
        endJamMasuk.set(Calendar.MINUTE, 00);
        endJamMasuk.set(Calendar.SECOND, 00);
        endJamMasuk.set(Calendar.AM_PM, 0);
        endJamMasuk.set(Calendar.MILLISECOND, 0000);


        Calendar jamPulang = Calendar.getInstance();
        jamPulang.setTime(new Date());
        jamPulang.set(Calendar.HOUR, 4);
        jamPulang.set(Calendar.MINUTE, 00);
        jamPulang.set(Calendar.SECOND, 00);
        jamPulang.set(Calendar.AM_PM, 1);
        jamPulang.set(Calendar.MILLISECOND, 0000);

        Calendar endJamPulang = Calendar.getInstance();
        endJamPulang.setTime(new Date());
        endJamPulang.set(Calendar.HOUR, 8);
        endJamPulang.set(Calendar.MINUTE, 00);
        endJamPulang.set(Calendar.SECOND, 00);
        endJamPulang.set(Calendar.AM_PM, 1);
        endJamPulang.set(Calendar.MILLISECOND, 0000);

        LocalDateTime isEndJamMasuk = LocalDateTime.fromDateFields(endJamMasuk.getTime());
        LocalDateTime isStartJamMasuk = LocalDateTime.fromDateFields(jamMasuk.getTime());

        LocalDateTime isEndJamPulang = LocalDateTime.fromDateFields(endJamPulang.getTime());
        LocalDateTime isStartJamPulang = LocalDateTime.fromDateFields(jamPulang.getTime());


        Person person;
        PresensiResultAdapter  presensiResultAdapter= new PresensiResultAdapter();
        if (thisTime.isAfter(isStartJamMasuk) && thisTime.isBefore(isEndJamMasuk) && currentSettings.getIsDatangNotifShow() == false) {

            person = presensiResultAdapter.getPersonByNik(currentWatch , new Date());
            Log.v("SCHEDULEACT" , "MASUK");
            if (person == null) {
                return;
            }
            currentSettings.setIsPulangNotifShow(false);
            if (person != null && person.getJamMasuk() == null) {
                new Statics().setNotification(context, "Presensi Javan", "Anda Telah Terkoneksi Ke Javan Labs , Silahkan Absen");
                currentSettings.setIsDatangNotifShow(true);

            }

        } else if (thisTime.isAfter(isStartJamPulang) && thisTime.isBefore(isEndJamPulang) && currentSettings.getIsPulangNotifShow() == false) {

            person = presensiResultAdapter.getPersonByNik(currentWatch , new Date());

            if (person == null) {
                return;
            }
            Log.v("SCHEDULEACT" , "PULANG");
            currentSettings.setIsDatangNotifShow(false);
            if (person != null && person.getJamKeluar() == null && person.getDurasiKerja() >= 9) {
                new Statics().setNotification(context, "Presensi Javan", "Jam Kerja Anda Sudah Cukup Silahkan Absen Pulang");
                currentSettings.setIsPulangNotifShow(true);
            }

        } else {
            currentSettings.setIsPulangNotifShow(false);
            currentSettings.setIsDatangNotifShow(false);
        }
        new Statics().setConfgDataNoNotif(currentSettings, context);

    }

    public void SetAlarm(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WifiStatusReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 , pi);
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
