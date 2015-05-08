package mobile.javan.co.id.presensi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import mobile.javan.co.id.presensi.model.MenuModel;
import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.Settings;
import mobile.javan.co.id.presensi.model.adapter.array.MenuArrayAdapter;
import mobile.javan.co.id.presensi.model.adapter.array.PresensiArrayAdapter;
import mobile.javan.co.id.presensi.model.adapter.result.PresensiResultAdapter;
import mobile.javan.co.id.presensi.model.receiver.WifiStatusReceiver;
import mobile.javan.co.id.presensi.service.ConnectionFragment;
import mobile.javan.co.id.presensi.service.DownloadPresensiData;
import mobile.javan.co.id.presensi.util.Statics;


public class MainActivity extends ActionBarActivity {
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBar mActionBar;

    private List<Person> mPersons;
    private ArrayAdapter mAdapter;
    private ListView mPresensiListView;

    private RelativeLayout mReloadStatus;

    private MainApplication mMainApplication;
    private WifiStatusReceiver wifiStatusReceiver;

    private Date selectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getSupportActionBar();
        mPlanetTitles = new String[]{"Home", "Watch", "Setting"};
        mPresensiListView = (ListView) findViewById(R.id.list_presensi);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        List<MenuModel> menuModels = new ArrayList<MenuModel>();
        menuModels.add(new MenuModel(R.drawable.home, "Home"));
        menuModels.add(new MenuModel(R.drawable.clock, "Watch"));
        menuModels.add(new MenuModel(R.drawable.setting, "Setting"));

        MenuArrayAdapter menuArrayAdapter = new MenuArrayAdapter(this, menuModels);

        mDrawerList.setAdapter(menuArrayAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        ViewGroup container = mDrawerList;
        LayoutInflater inflater = getLayoutInflater();
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.menu_header, container, false);
        setMenuHeaderData(relativeLayout);
        mDrawerList.addHeaderView(relativeLayout);
        //Display App Logo
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        mActionBar.setIcon(R.mipmap.ic_launcher);
        mReloadStatus = (RelativeLayout) findViewById(R.id.reload_status);

        mMainApplication = (MainApplication) getApplication();

        wifiStatusReceiver = new WifiStatusReceiver();


        startRepeatingTimer();

        selectItem(1);
    }


    public void startRepeatingTimer() {
        Context context = this.getApplicationContext();
        if (wifiStatusReceiver != null) {
            wifiStatusReceiver.SetAlarm(context);
        } else {
            Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
        }
    }


    private void getPresensiData() {

        getList();

    }

    private void setAdapter() {
        mAdapter = new PresensiArrayAdapter(this, mPersons);
    }

    private void getList() {

        Log.v("Thread Result", "Start");
        mReloadStatus.setVisibility(View.VISIBLE);
        Intent i = new Intent(this, DownloadPresensiData.class);
        i.putExtra("tanggal", mMainApplication.getCurrentDate().getTime());
        this.startService(i);


    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String jPerson = bundle.getString(DownloadPresensiData.FILEPATH);
                int resultCode = bundle.getInt(DownloadPresensiData.RESULT);
                if (resultCode == RESULT_OK) {
                    try {
                        Type listType = new TypeToken<List<Person>>() {
                        }.getType();
                        mPersons = new Gson().fromJson(jPerson, listType);
                        if (mPersons != null) {
                            setAdapter();
                            mPresensiListView.setAdapter(mAdapter);
                            mPresensiListView.setOnItemClickListener(new PresensiListOnClickListener());
                            Log.v("Thread Result", jPerson);
                        } else {
                            Toast.makeText(context, "Cant Load Data, Please Check Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {

                    }
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
        registerReceiver(receiver, new IntentFilter(DownloadPresensiData.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            selectItem(1);
        }
        if (id == R.id.pickDate) {
            showDatePickerDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Swaps fragments in the main content view
     */

    private void selectItem(int position) {
        if (position == 1) {
            getPresensiData();
        } else if (position == 2) {
            Settings settings = ((MainApplication) getApplication()).getSettings(this);
            if (settings != null) {
                startWatchActivity(settings.getWatchNik() , new Date());
            } else {
                Toast.makeText(this, "Watch Feature Need Configuration First", Toast.LENGTH_SHORT).show();
                selectItem(3);
            }
        } else if (position == 3) {
            startSettinngActivity();
        }
        mDrawerList.setItemChecked(position, true);

        setTitle("Presensi");
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    private class PresensiListOnClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startWatchActivity(mPersons.get(position).getNik() , mMainApplication.getCurrentDate());
        }
    }

    private void startWatchActivity(String nik , Date tanggal) {
        Intent intent = new Intent(this, WatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("personNik", nik);
        intent.putExtra("tanggal", tanggal.getTime());
        startActivity(intent);
    }

    private void startSettinngActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setMenuHeaderData(RelativeLayout relativeLayout) {
        TextView logedName = (TextView) relativeLayout.findViewById(R.id.logedName);
        TextView logedNik = (TextView) relativeLayout.findViewById(R.id.logedNik);
        ImageView imgLoader = (ImageView) relativeLayout.findViewById(R.id.imageView);
        Person person = ((MainApplication) getApplication()).getPerson(this);
        logedName.setVisibility(View.GONE);
        imgLoader.setVisibility(View.GONE);
        logedNik.setVisibility(View.GONE);
//        if (person != null) {
//            logedName.setVisibility(View.VISIBLE);
//            imgLoader.setVisibility(View.VISIBLE);
//            logedNik.setVisibility(View.VISIBLE);
//            logedName.setText(person.getNama());
//            logedNik.setText(person.getNik());
//        }
    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            MainApplication myApplication = (MainApplication) getActivity().getApplication();

            c.setTime(myApplication.getCurrentDate());
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            MainApplication myApplication = (MainApplication) getActivity().getApplication();
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, day);
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            myApplication.setCurrentDate(cal.getTime());
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.getList();
            // Do something with the date chosen by the user
        }
    }

}
