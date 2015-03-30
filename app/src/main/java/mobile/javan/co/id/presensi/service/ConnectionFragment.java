package mobile.javan.co.id.presensi.service;

import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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
import mobile.javan.co.id.presensi.model.PresensiResultAdapter;
import mobile.javan.co.id.presensi.model.StaticResponse;

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

    public void getPresensiData() {

        List<Person> persons = new ArrayList<Person>();
        JSONObject json = null;
        String str = "";
        HttpResponse response;
        HttpClient myClient = new DefaultHttpClient();
        //2015-03-23T00:17:13.933Z
        String url = "http://presensi.javan.co.id/list.php?orderby=masuk&tanggal=" + new StaticResponse().getStringFrom("yyyy-MM-dd", new Date(), null) + "T00:17:13.933Z";
//        String url = "http://presensi.javan.co.id/list.php?orderby=masuk&tanggal=2015-03-20T00:17:13.933Z";
        Log.v("Url", url);
        HttpPost myConnection = new HttpPost(url);

        try {
            response = myClient.execute(myConnection);
            str = EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jsonArray = new JSONArray(str);
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jObject = jsonArray.getJSONObject(i);
                Person p = new Person();
                p.setNama(jObject.getString("absensi_nama_lengkap"));
                p.setNik(jObject.getString("absensi_pin"));
                p.setIzin(jObject.getString("absensi_izin"));
                p.setJamMasuk(new StaticResponse().getDateFrom("hh:MM:ss", jObject.getString("absensi_masuk"), null));
                p.setJamKeluar(new StaticResponse().getDateFrom("hh:MM:ss", jObject.getString("absensi_keluar"), null));
                p.setJamKerja(new StaticResponse().getDateFrom("hh:MM:ss", jObject.getString("duration"), null));

                persons.add(p);


            } // End Loop

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.presensiResultAdapter = new PresensiResultAdapter(persons);
        Log.v("Hasil", "Hasil" + str);
    }

    public PresensiResultAdapter getPresensis() {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)

        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
        }
        this.getPresensiData();
        return this.getPresensiResultAdapter();
    }
}
