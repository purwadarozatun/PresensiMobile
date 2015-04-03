package mobile.javan.co.id.presensi.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.PresensiResultAdapter;

/**
 * Created by Purwa on 21/03/2015.
 */
public class Statics {

    public String configfilename = "niksettingfile";

    public void setConfgData(String nik, Activity activity) {
        String FILENAME = configfilename;
        String string = nik;
        try {

            PresensiResultAdapter presensiResultAdapter = new PresensiResultAdapter();
            Person person = presensiResultAdapter.getPersonByNik(nik.trim());
            if (person == null) {
                Toast.makeText(activity, "Cant Save Config File , Nik Not Found In Database", Toast.LENGTH_SHORT).show();
            } else {
                FileOutputStream fos = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write(string.getBytes());
                fos.close();
                Toast.makeText(activity, "Config Saved!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(activity, "Cant Save Config File", Toast.LENGTH_SHORT).show();
        }

    }

    public String getConfigData(Activity activity) {
        String FILENAME = configfilename;
        String fileResponse = null;

        try {
            FileInputStream fos = activity.openFileInput(FILENAME);
            fileResponse = new BufferedReader(new InputStreamReader(fos)).readLine();

            PresensiResultAdapter presensiResultAdapter = new PresensiResultAdapter();
            Person person = presensiResultAdapter.getPersonByNik(fileResponse);
            if(person == null){
                return null;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return fileResponse;
    }
}
