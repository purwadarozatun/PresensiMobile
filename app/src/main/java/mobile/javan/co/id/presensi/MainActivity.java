package mobile.javan.co.id.presensi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.PresensiArrayAdapter;
import mobile.javan.co.id.presensi.model.PresensiResultAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActionBar = getSupportActionBar();
        mPlanetTitles = new String[]{"Home", "Watch", "Setting"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //Display App Logo
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        mActionBar.setIcon(R.mipmap.ic_launcher);
        mReloadStatus = (RelativeLayout) findViewById(R.id.reload_status);
        selectItem(0);


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
                    Type listType = new TypeToken<List<Person>>() {
                    }.getType();
                    mPersons = new Gson().fromJson(jPerson, listType);

                    setAdapter();
                    mPresensiListView = (ListView) findViewById(R.id.list_presensi);
                    mPresensiListView.setAdapter(mAdapter);
                    mPresensiListView.setOnItemClickListener(new PresensiListOnClickListener());

                    Log.v("Thread Result", jPerson);
                } else {
                    mPersons = new ArrayList<Person>();
                    Log.v("Thread Result", "GAGAl");

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
            selectItem(0);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        if (position == 0) {
            getPresensiData();
        } else if (position == 1) {

            String fileResponse = new Statics().getConfigData(this);
            if (fileResponse != null) {
                startWatchActivity(fileResponse.trim());
            } else {
                Toast.makeText(this, "Watch Feature Need Configuration First", Toast.LENGTH_SHORT);
                selectItem(0);
            }
        } else if (position == 2) {
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
            startWatchActivity(mPersons.get(position).getNik());
        }
    }

    private void startWatchActivity(String nik) {
        PresensiResultAdapter presensiResultAdapter = new PresensiResultAdapter();
        Person person = presensiResultAdapter.getPersonByNik(nik.trim());
        if (person == null) {
            selectItem(1);
        }
//        Toast.makeText(this, "Watch Activate for user " + nik, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, WatchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("personNik", nik);
        startActivity(intent);
    }

    private void startSettinngActivity() {
        Intent intent = new Intent(this, SettingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
