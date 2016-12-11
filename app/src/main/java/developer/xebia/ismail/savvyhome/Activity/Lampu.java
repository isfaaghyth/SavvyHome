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
import developer.xebia.ismail.savvyhome.Controller.LightAdapter;
import developer.xebia.ismail.savvyhome.Controller.RoomAdapter;
import developer.xebia.ismail.savvyhome.Model.LightItem;
import developer.xebia.ismail.savvyhome.Model.RoomItem;
import developer.xebia.ismail.savvyhome.R;

/**
 * Created by Helmi on 5/15/2016.
 */
public class Lampu extends AppCompatActivity {

    RecyclerView list_rooms;
    String URL = "http://agnosthings.com/111531d4-1d4c-11e6-8001-005056805279";
    String send = "/field/month/feed/415/kwh";
    String location = "/field/month/feed/415/location";
    String statusdata = "/field/month/feed/415/status";
    String result = "";
    FontAppCompatTextView value_kwh, sum_lamp, lamp_on;
    ArrayList<LightItem> ListData;
    AVLoadingIndicatorView firstext;
    private RecyclerView.Adapter mAdapter;
    String uid;
    String kwh[];
    String status[];
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        list_rooms = (RecyclerView) findViewById(R.id.list_rooms);
        value_kwh = (FontAppCompatTextView) findViewById(R.id.value_kwh);
        sum_lamp = (FontAppCompatTextView) findViewById(R.id.valuea);
        lamp_on = (FontAppCompatTextView) findViewById(R.id.valueb);
        firstext = (AVLoadingIndicatorView) findViewById(R.id.tvLoading);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        ListData = new ArrayList<>();

        TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        uid = tManager.getDeviceId();

        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.kosong);
        ab.setDisplayHomeAsUpEnabled(true);

        list_rooms.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list_rooms.setLayoutManager(linearLayoutManager);

        if(isInternetAvailable()) {
            new requestAPI().execute();
        } else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new requestAPI().execute();
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
                list_rooms.setVisibility(View.GONE);
                new requestAPI().execute();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                firstext.setVisibility(View.VISIBLE);
                                list_rooms.setVisibility(View.GONE);
                                new requestAPI().execute();
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
            Log.i("kwh: ", result);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getKwh();
        }
    }

    public void getKwh() {
        try {
            int totKwh = 0, count = 0;
            JSONObject obj = new JSONObject(result);
            Iterator x = obj.keys();
            while (x.hasNext()) {
                String key = (String) x.next();
                if(key.equals("cValue")) {
                    String[] data = obj.get(key).toString().split("\",\"");
                    kwh = new String[data.length];
                    for (int i = 0; i < data.length; i++) {
                        String[] data2 = data[i].replaceAll("\\[","").replaceAll("\\]","").replaceAll("\"","").split(",");
                        totKwh += Integer.parseInt(data2[0]);
                        kwh[i] = data2[0];
                        if (!data2[0].equals("0")) {
                            count++;
                        }
                        Log.i("datacek: ", data2[0] + "");
                    }

                    Log.i("totalkwh2: ", totKwh + "");
                    value_kwh.setText(totKwh + "");
                    sum_lamp.setText(count + "");
                }
            }

            new requestAPI3().execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            Log.i("lamp: ", result);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getLamp();
        }
    }

    public void getLamp() {
        try {
            JSONObject obj = new JSONObject(result);
            Iterator x = obj.keys();
            JSONArray jArray = new JSONArray();
            while (x.hasNext()) {
                String key = (String) x.next();
                if(key.equals("cValue")) {
                    String[] data = obj.get(key).toString().split("\",\"");

                    for (int i=0; i<data.length; i++) {
                        String[] data2 = data[i].replaceAll("\\[","").replaceAll("\\]","").replaceAll("\"","").split(",");
                        jArray.put(data2[0]);
                    }
                }
            }

            for (int i=0; i<jArray.length(); i++) {
                ListData.add(new LightItem(uid, jArray.get(i).toString(), kwh[i], status[i]));
                Log.i("datalamp: ", jArray.get(i).toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(!ListData.isEmpty()) {
            firstext.setVisibility(View.GONE);
            mAdapter = new LightAdapter(ListData,this, R.layout.list_lamp);
            list_rooms.setAdapter(mAdapter);
            list_rooms.setVisibility(View.VISIBLE);
        } else {
            firstext.setVisibility(View.VISIBLE);
            Snackbar.make(coordinatorLayout, "No data found",Snackbar.LENGTH_SHORT).show();
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
            getStatusLampu();
        }
    }

    public void getStatusLampu() {
        try {
            int count = 0;
            JSONObject obj = new JSONObject(result);
            Iterator x = obj.keys();
            while (x.hasNext()) {
                String key = (String) x.next();
                if(key.equals("cValue")) {
                    String[] data = obj.get(key).toString().split("\",\"");
                    status = new String[data.length];
                    for (int i = 0; i < data.length; i++) {

                        String[] data2 = data[i].replaceAll("\\[","").replaceAll("\\]","").replaceAll("\"","").split(",");
                        if(data2[0].equals("1")) {
                            count++;
                        }

                        status[i] = data2[0];
                        Log.i("status: ", data2[0] + "");
                    }

                    lamp_on.setText(count + "");
                }
            }

            new requestAPI2().execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
