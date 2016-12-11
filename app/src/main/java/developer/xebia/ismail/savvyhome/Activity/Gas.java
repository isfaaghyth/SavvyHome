package developer.xebia.ismail.savvyhome.Activity;

import android.app.ProgressDialog;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.pixplicity.fontview.FontAppCompatTextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import developer.xebia.ismail.savvyhome.Controller.Config;
import developer.xebia.ismail.savvyhome.R;

/**
 * Created by Helmi on 5/15/2016.
 */
public class Gas extends AppCompatActivity {

    String URL = "http://agnosthings.com/83df94c0-1d4c-11e6-8001-005056805279";
    String send = "/field/last/feed/418/progress";
    String send2 = "/field/month/feed/418/usage";
    String result = "";
    FontAppCompatTextView btnPesan, value_gas;
    private ProgressDialog pDialog;
    private CoordinatorLayout coordinatorLayout;
    ArrayList<Entry> entries;
    ArrayList<String> labels;
    LineChart lineChart;
    LineDataSet dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnPesan = (FontAppCompatTextView) findViewById(R.id.btnPesan);
        value_gas = (FontAppCompatTextView) findViewById(R.id.value_gas);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.kosong);
        ab.setDisplayHomeAsUpEnabled(true);

        lineChart = (LineChart) findViewById(R.id.chart1);
        entries = new ArrayList<>();
        labels = new ArrayList<String>();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(R.id.ssNested3), "Permintaan telah dikirim", Snackbar.LENGTH_SHORT).show();
            }
        });

        pDialog = new ProgressDialog(Gas.this);
        pDialog.setMessage("Please wait, Loading Data...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        if(isInternetAvailable()) {
            new requestAPI2().execute();
        } else {
            pDialog.dismiss();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            pDialog = new ProgressDialog(Gas.this);
                            pDialog.setMessage("Please wait, Loading Data...");
                            pDialog.setIndeterminate(false);
                            pDialog.setCancelable(false);
                            pDialog.show();
                            new requestAPI2().execute();
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
                entries.clear();
                labels.clear();
                pDialog = new ProgressDialog(Gas.this);
                pDialog.setMessage("Please wait, Loading Data...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
                new requestAPI2().execute();
            } else {
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pDialog = new ProgressDialog(Gas.this);
                                pDialog.setMessage("Please wait, Loading Data...");
                                pDialog.setIndeterminate(false);
                                pDialog.setCancelable(false);
                                pDialog.show();
                                new requestAPI2().execute();
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
            HttpGet request = new HttpGet(URL + send2);
            ResponseHandler<String> handler = new BasicResponseHandler();
            try {
                result = httpclient.execute(request, handler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpclient.getConnectionManager().shutdown();
            Log.i("halloooo", result);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setData();
        }
    }

    public void setData() {
        try {
            JSONObject obj = new JSONObject(result);
            Iterator x = obj.keys();
            while (x.hasNext()) {
                String key = (String) x.next();
                if(key.equals("cValue")) {
                    String[] data = obj.get(key).toString().split("\",\"");

                    for (int i=0; i<data.length; i++) {
                        String[] data2 = data[i].split(",");
                        entries.add(new Entry(Integer.parseInt(data2[0].replaceAll("[^\\p{L}\\p{Nd}]+", "")), i));
                        labels.add(data2[1].replace("]","").replace("\"",""));
                        Log.i("statuse: ", data2[0].replaceAll("[^\\p{L}\\p{Nd}]+", "") + " - " + data2[1].replace("]","").replace("\"",""));
                    }

                    dataset = new LineDataSet(entries, "# of PSI");
                    LineData Lindata = new LineData(labels, dataset);
                    lineChart.setData(Lindata);
                    lineChart.notifyDataSetChanged();
                    lineChart.invalidate();
                }
            }
            pDialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class requestAPI2 extends AsyncTask<Void, Void, Void> {

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
            Log.i("halloooo", result);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getProgress();
        }
    }

    public void getProgress() {
        try {
            int i = 0;
            JSONObject obj = new JSONObject(result);
            Iterator x = obj.keys();
            while (x.hasNext()) {
                String key = (String) x.next();
                String[] data = obj.get(key).toString().split("\",\"");

                while (i<data.length) {
                    String[] data2 = data[i].split(":\"");
                    value_gas.setText(data2[0].replaceAll("[^\\p{L}\\p{Nd}]+", ""));
                    Log.i("tess: ", data[i] + " idx " + i);
                    i++;
                }

            }
            new requestAPI().execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
