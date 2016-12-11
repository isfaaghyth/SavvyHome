package developer.xebia.ismail.savvyhome.Activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import developer.xebia.ismail.savvyhome.Controller.Config;
import developer.xebia.ismail.savvyhome.R;

/**
 * Created by Admin on 5/16/2016.
 */
public class AddRoom extends AppCompatActivity {
    String[] SPINNERLIST = {"Basement",
            "Bathroom",
            "Bedroom",
            "Kitchen",
            "Living Room"};

    MaterialEditText txtRoomName;
    CardView btnCreate;
    String uid;
    String URL = "http://agnosthings.com/ed31f9d4-1ea2-11e6-8001-005056805279";
    String send = "";
    String result = "";
    String tag = "";
    Config config = new Config();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rooms);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtRoomName = (MaterialEditText) findViewById(R.id.txtRoomName);
        btnCreate = (CardView) findViewById(R.id.c);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.kosong);
        ab.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        uid = tManager.getDeviceId();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        final MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner)
                findViewById(R.id.android_material_design_spinner);
        materialDesignSpinner.setAdapter(arrayAdapter);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!txtRoomName.getText().toString().isEmpty()) {
                    try {
                        send = "/feed?push=device_id="+uid+",location="+materialDesignSpinner.getText().toString()+",room_name="+txtRoomName.getText().toString();
                        //new requestAPI().execute();
                    } catch(Exception e) {

                    }
                } else {
                    Snackbar.make(findViewById(R.id.ssNested), "Please complete the form!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    class requestAPI extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(URL + send);
            ResponseHandler<String> handler = new BasicResponseHandler();
            try {
                result = httpclient.execute(request, handler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpclient.getConnectionManager().shutdown();
            Log.i(tag, result);

            Snackbar.make(findViewById(R.id.ssNested), "Room Added!", Snackbar.LENGTH_SHORT).show();
            return null;
        }
    }

}
