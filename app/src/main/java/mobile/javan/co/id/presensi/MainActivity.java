package mobile.javan.co.id.presensi;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import mobile.javan.co.id.presensi.model.Person;
import mobile.javan.co.id.presensi.R;
import mobile.javan.co.id.presensi.model.PresensiArrayAdapter;
import mobile.javan.co.id.presensi.service.ConnectionFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getList();

    }

    private void getList() {

        ConnectionFragment connectionFragment = new ConnectionFragment();
        //Todo : Buat Proses Parse Json Di Connection Fragment
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        connectionFragment.getPresensiData();
        ArrayAdapter<Person> adapter =
                new PresensiArrayAdapter(this, connectionFragment.getPresensiResultAdapter().getResult());

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new PresensiListActivity());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            this.getList();
        }

        return super.onOptionsItemSelected(item);
    }
}
