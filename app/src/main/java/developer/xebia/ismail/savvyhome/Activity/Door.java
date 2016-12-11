package developer.xebia.ismail.savvyhome.Activity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pixplicity.fontview.FontAppCompatTextView;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import developer.xebia.ismail.savvyhome.Controller.Config;
import developer.xebia.ismail.savvyhome.Controller.DoorAdapter;
import developer.xebia.ismail.savvyhome.Controller.LightAdapter;
import developer.xebia.ismail.savvyhome.Model.DoorItem;
import developer.xebia.ismail.savvyhome.Model.LightItem;
import developer.xebia.ismail.savvyhome.R;

/**
 * Created by Helmi on 5/15/2016.
 */
public class Door extends AppCompatActivity {

    RecyclerView list_door;
    String URL = "http://agnosthings.com/3bc178b6-1d4c-11e6-8001-005056805279";
    String location = "/channel/limit/feed/416/location/100";
    String statusdata = "/channel/limit/feed/416/status/100";
    String result = "";
    String uid;
    FontAppCompatTextView valuea, valueb;
    ArrayList<DoorItem> ListData;
    AVLoadingIndicatorView firstext;
    private RecyclerView.Adapter mAdapter;
    String status[];
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.kosong);
        ab.setDisplayHomeAsUpEnabled(true);

        list_door = (RecyclerView) findViewById(R.id.list_door);
        valuea = (FontAppCompatTextView) findViewById(R.id.valuea);
        valueb = (FontAppCompatTextView) findViewById(R.id.valueb);
        firstext = (AVLoadingIndicatorView) findViewById(R.id.tvLoading);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        ListData = new ArrayList<>();

        TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        uid = tManager.getDeviceId();

        list_door.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_door.setLayoutManager(linearLayoutManager);

        if(isInternetAvailable()) {
            new requestAPI3().execute();
        } else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new requestAPI3().execute();
                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.CYAN);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);

            snackbar.show();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.notif) {
            if(isInternetAvailable()) {
                ListData.clear();
                firstext.setVisibility(View.VISIBLE);
                list_door.setVisibility(View.GONE);
                new requestAPI3().execute();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                firstext.setVisibility(View.VISIBLE);
                                list_door.setVisibility(View.GONE);
                                new requestAPI3().execute();
                            }
                        });

                // Changing message text color
                snackbar.setActionTextColor(Color.CYAN);

                // Changing action button text color
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);

                snackbar.show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class requestAPI2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(URL + location);
            ResponseHandler<String> handler = new BasicResponseHandler();
            try {
                result = httpclient.execute(request, handler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpclient.getConnectionManager().shutdown();
            Log.i("door: ", result);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getDoor();
        }
    }

    public void getDoor() {
        try {
            JSONObject obj = new JSONObject(result);
            Iterator x = obj.keys();
            JSONArray jArray = new JSONArray();
            while (x.hasNext()) {
                String key = (String) x.next();
                if(key.equals("value")) {
                    String[] data = obj.get(key).toString().split(",");

                    for (int i=0; i<data.length; i++) {
                        jArray.put(data[i]);
                        ListData.add(new DoorItem(uid, jArray.get(i).toString(), status[i]));
                    }
                }
            }

            if(!ListData.isEmpty()) {
                firstext.setVisibility(View.GONE);
                mAdapter = new DoorAdapter(ListData,this, R.layout.list_door);
                list_door.setAdapter(mAdapter);
                list_door.setVisibility(View.VISIBLE);
            } else {
                firstext.setVisibility(View.VISIBLE);
                Snackbar.make(coordinatorLayout, "No data found",Snackbar.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class requestAPI3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(URL + statusdata);
            ResponseHandler<String> handler = new BasicResponseHandler();
            try {
                result = httpclient.execute(request, handler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpclient.getConnectionManager().shutdown();
            Log.i("status: ", result);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getStatusDoor();
        }
    }

    public void getStatusDoor() {
        try {
            int count = 0;
            JSONObject obj = new JSONObject(result);
            Iterator x = obj.keys();
            JSONArray jArray = new JSONArray();
            while (x.hasNext()) {
                String key = (String) x.next();
                if(key.equals("value")) {
                    String[] data = obj.get(key).toString().split(",");
                    status = new String[data.length];
                    for (int i = 0; i < data.length; i++) {

                        if(data[i].equals("1")) {
                            count++;
                        }

                        status[i] = data[i];
                        Log.i("status: ", data[i] + "");
                    }

                    valuea.setText(data.length+"");
                    valueb.setText(count + "");
                }
                //jArray.put("Basement");
            }

            for (int i=0; i<jArray.length(); i++) {

                //ListData2.add(new RoomItem(uid, jArray.get(i).toString(), R.drawable.r_bedroom));

                Log.i("dataa: ", jArray.get(i).toString());
            }

            new requestAPI2().execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
