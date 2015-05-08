package mobile.javan.co.id.presensi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.adapter.result.PresensiResultAdapter;
import mobile.javan.co.id.presensi.service.DownloadPresensiData;
import mobile.javan.co.id.presensi.service.WatchService;
import mobile.javan.co.id.presensi.util.Statics;


public class WatchActivity extends ActionBarActivity {
    private Person mPerson;
    private RelativeLayout mReloadStatus;
    private String currentUserNik;
    private Intent mServiceIntent;
    private Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);
        Intent intent = this.getIntent();
        currentUserNik = intent.getStringExtra("personNik");


        mServiceIntent = new Intent(this, WatchService.class);
        mServiceIntent.putExtra("nik", currentUserNik);
        mServiceIntent.putExtra("tanggal", (Long) intent.getExtras().get("tanggal"));

        mReloadStatus = (RelativeLayout) findViewById(R.id.reload_status);

        mReloadStatus.setVisibility(View.VISIBLE);

        getData();
    }

    private void getData() {

        mReloadStatus.setVisibility(View.VISIBLE);
        startService(mServiceIntent);
    }


    private void setDataToActivity() {

        if (mPerson == null) {
            Toast.makeText(this, "User Not Found !", Toast.LENGTH_SHORT).show();
        }
        TextView personName = (TextView) findViewById(R.id.personName);
        personName.setPaintFlags(personName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        ((TextView) findViewById(R.id.personNik)).setText("" + mPerson.getNik());
        (personName).setText("" + mPerson.getNama());
        ((TextView) findViewById(R.id.jam_masuk)).setText("" + new Statics().getStringFrom("hh:mm:ss", mPerson.getJamMasuk(), "-"));
        ((TextView) findViewById(R.id.jam_pulang)).setText("" + new Statics().getStringFrom("hh:mm:ss", mPerson.getJamKeluar(), "-"));

        Double persentasiJamkerja = 0D;
        Double jumlahJamKerja = 540D;
        Double jamKerjaMenit = 0D;
        Date jamKerja = mPerson.getJamKerja();
        Integer hours = 0;
        Integer minutes = 0;

        if (jamKerja != null) {
            LocalDateTime localDateTime = LocalDateTime.fromDateFields(jamKerja);
            hours = mPerson.getDurasiKerja();
            minutes = localDateTime.getMinuteOfHour();
        }
        jamKerjaMenit = Double.parseDouble("" + ((hours * 60) + minutes));
        persentasiJamkerja = (jamKerjaMenit / jumlahJamKerja) * 100;


        ((TextView) findViewById(R.id.tv_hour)).setText("" + hours);
        ((TextView) findViewById(R.id.tv_minute)).setText("" + minutes);

        ((ProgressBar) findViewById(R.id.circle_progress_bar)).setProgress(persentasiJamkerja.intValue());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_watch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            getData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String jPerson = bundle.getString(DownloadPresensiData.FILEPATH);
                int resultCode = bundle.getInt(DownloadPresensiData.RESULT);
                if (resultCode == RESULT_OK) {
                    mPerson = new Gson().fromJson(jPerson, Person.class);
                    setDataToActivity();
                } else {
                    Toast.makeText(context, "Cant Load Data, Please Check Network Connection", Toast.LENGTH_SHORT).show();

                }
                mReloadStatus.setVisibility(View.GONE);
            }
        }

    };


    @Override
    protected void onResume() {
        super.onResume();
        getData();
        registerReceiver(receiver, new IntentFilter(WatchService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

}
