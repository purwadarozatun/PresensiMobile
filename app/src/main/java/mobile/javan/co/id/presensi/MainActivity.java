package mobile.javan.co.id.presensi;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.model.PresensiResultAdapter;
import mobile.javan.co.id.presensi.util.Statics;


public class MainActivity extends ActionBarActivity implements PresensiListFragment.OnFragmentInteractionListener {
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mPlanetTitles = new String[]{"Home", "List", "Watch", "Setting"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mPlanetTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        selectItem(1);
        selectItem(0);

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

        return super.onOptionsItemSelected(item);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        if (position == 0) {

            String fileResponse = new Statics().getConfigData(this);
            if (fileResponse != null) {
                startWatchActivity(fileResponse.trim());
            } else {
                Toast.makeText(this, "Watch Feature Need Configuration First", Toast.LENGTH_SHORT);
                selectItem(1);
            }
        } else if (position == 1) {
            Fragment fragment = null;
            fragment = new PresensiListFragment();

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        } else if (position == 2) {
            Toast.makeText(this, "Watch Feature Not Avaiable", Toast.LENGTH_SHORT);
            selectItem(1);
        } else if (position == 3) {
            startSettinngActivity();
        }
        mDrawerList.setItemChecked(position, true);

        setTitle("Presensi Javan");
        mDrawerLayout.closeDrawer(mDrawerList);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    @Override
    public void onFragmentInteraction(Person p) {
        startWatchActivity(p.getNik());
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
