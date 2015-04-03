package mobile.javan.co.id.presensi;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;

import mobile.javan.co.id.presensi.util.Statics;


public class SettingActivity extends ActionBarActivity {
    Activity mActivity;
    Button mButton;

    EditText mNikEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mActivity = this;
        mButton = (Button) findViewById(R.id.buttonSettingSave);
        mNikEdit = (EditText) mActivity.findViewById(R.id.nik);
        mNikEdit.setText(new Statics().getConfigData(this));
        mButton.setOnClickListener(new SaveButtonClickListener());

    }


    private class SaveButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            new Statics().setConfgData(mNikEdit.getText().toString(), mActivity);
        }
    }

}
