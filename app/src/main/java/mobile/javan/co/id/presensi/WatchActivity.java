package mobile.javan.co.id.presensi;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Calendar;
import java.util.Date;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.PresensiResultAdapter;
import mobile.javan.co.id.presensi.model.StaticResponse;
import mobile.javan.co.id.presensi.service.ConnectionFragment;


public class WatchActivity extends ActionBarActivity {
    private Person mPerson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);


        this.setDataToActivity();
    }


    private void setDataToActivity() {

        Intent intent = this.getIntent();
        String nik = intent.getStringExtra("personNik");
        PresensiResultAdapter presensiResultAdapter = new PresensiResultAdapter();
        mPerson = presensiResultAdapter.getPersonByNik(nik);

        ((TextView) findViewById(R.id.personNik)).setText("" + mPerson.getNik());
        ((TextView) findViewById(R.id.personName)).setText("" + mPerson.getNama());

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
            setDataToActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
