package mobile.javan.co.id.presensi.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.Settings;
import mobile.javan.co.id.presensi.model.adapter.result.PresensiResultAdapter;

/**
 * Created by Purwa on 21/03/2015.
 */
public class Statics {

    public String configfilename = "presensiSettingFile";

    public void setConfgData(Settings settings, Activity activity) {
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
                Toast.makeText(activity, "Config Saved!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(activity, "Cant Save Config File", Toast.LENGTH_SHORT).show();
        }

    }

    public String getConfigData(Activity activity) {
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


}
