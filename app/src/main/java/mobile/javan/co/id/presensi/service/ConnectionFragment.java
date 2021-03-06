package mobile.javan.co.id.presensi.service;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.adapter.result.PresensiResultAdapter;
import mobile.javan.co.id.presensi.model.adapter.result.StaticResultAdapter;
import mobile.javan.co.id.presensi.util.Statics;

/**
 * Created by Purwa on 20/03/2015.
 */
public class ConnectionFragment {
    private PresensiResultAdapter presensiResultAdapter = null;


    public PresensiResultAdapter getPresensiResultAdapter() {
        return presensiResultAdapter;
    }

    public void setPresensiResultAdapter(PresensiResultAdapter presensiResultAdapter) {
        this.presensiResultAdapter = presensiResultAdapter;
    }

    public void getPresensiData(String nik, Date tanggal) {
        List<Person> persons = new ArrayList<Person>();
        JSONObject json = null;
        String str = "";
        HttpResponse response;
        HttpClient myClient = new DefaultHttpClient();

        //2015-03-23T00:17:13.933Z
        String url = "http://presensi.javan.co.id/service.php/";
        if (!nik.isEmpty()) {
            url = url + nik;
        }
        url += "?";
        if (tanggal != null) {
            url = url + "&tanggal=" + new Statics().getStringFrom("yyyy-MM-dd", tanggal, null);
        }
        url = url + "&orderby=masuk";
//        String url = "http://presensi.javan.co.id/list.php?orderby=masuk&tanggal=2015-03-20T00:17:13.933Z";
        HttpGet myConnection = new HttpGet(url);

        try {
            response = myClient.execute(myConnection);
            str = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(str);
        } catch (JSONException e) {

        }
        if (jsonArray == null) {

        } else {

            for (int i = 0; i < jsonArray.length(); i++) {
                try {

                    JSONObject jObject = jsonArray.getJSONObject(i);
                    Person p = new Person(jObject.getString("absensi_nama_lengkap"));
                    if (jObject.has("absensi_pin"))
                        p.setNik(jObject.getString("absensi_pin"));
                    if (jObject.has("absensi_izin"))
                        p.setIzin(jObject.getString("absensi_izin"));
                    if (jObject.has("absensi_masuk"))
                        p.setJamMasuk(new Statics().getDateFrom("hh:mm:ss", jObject.getString("absensi_masuk"), null));
                    if (jObject.has("absensi_keluar"))
                        p.setJamKeluar(new Statics().getDateFrom("hh:mm:ss", jObject.getString("absensi_keluar"), null));
                    if (jObject.has("duration"))
                        p.setJamKerja(new Statics().getDateFrom("hh:mm:ss", jObject.getString("duration"), null));
                    if (jObject.has("duration_hour"))
                        p.setDurasiKerja(jObject.getInt("duration_hour"));

                    persons.add(p);


                } catch (JSONException e) {

                }
            } // End Loop

        }

        this.presensiResultAdapter = new PresensiResultAdapter(persons);
    }

    public PresensiResultAdapter getPresensis(Date tanggal) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)

        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }
        this.getPresensiData("", tanggal);
        return this.getPresensiResultAdapter();
    }

    public PresensiResultAdapter getPresensis(String nik, Date tanggal) {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)

        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }
        this.getPresensiData(nik, tanggal);
        return this.getPresensiResultAdapter();
    }


    public String getWifiStatus(Context contex) {
    /*
        this Prototype to get BSSID ,
        WHY Bissd ?
        Because BSSID Is Mac Adress Router / Server Computer
    */

        ConnectivityManager connManager = (ConnectivityManager) contex.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        String SSID = null;
        if (mWifi.isConnected()) {
            // Do whatever
            WifiManager wifiManager = (WifiManager) contex.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();

            SSID = info.getBSSID();
        }

        return SSID;
    }
}
