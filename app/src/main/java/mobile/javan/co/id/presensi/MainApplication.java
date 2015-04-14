package mobile.javan.co.id.presensi;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.Settings;
import mobile.javan.co.id.presensi.model.adapter.result.PresensiResultAdapter;
import mobile.javan.co.id.presensi.service.ConnectionFragment;
import mobile.javan.co.id.presensi.util.Statics;

/**
 * Created by Purwa on 07/04/2015.
 */
public class MainApplication extends Application {

    private Activity mActivity;

    private Settings settings; //Data Setting di set global

    private Person person; //Ceritanya Active Person , Jadi Data Active Person Di simpan di global

    public MainApplication() {
    }

    public MainApplication(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public Settings getSettings(Activity mActivity) {
        String jSetting = new Statics().getConfigData(mActivity);

        settings = new Gson().fromJson(jSetting, Settings.class);

        return settings;
    }

    public Person getPerson(Activity mActivity) {
        Settings settingsinstance = this.getSettings(mActivity);
        if(settingsinstance == null){
            return null;
        }
        PresensiResultAdapter presensiResultAdapter = new PresensiResultAdapter();
        return presensiResultAdapter.getPersonByNik(settingsinstance.getWatchNik());
    }

}
