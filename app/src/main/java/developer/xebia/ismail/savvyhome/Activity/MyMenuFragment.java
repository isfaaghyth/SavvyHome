package developer.xebia.ismail.savvyhome.Activity;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.mxn.soul.flowingdrawer_core.MenuFragment;
import com.pixplicity.fontview.FontAppCompatTextView;
import com.pixplicity.fontview.FontTextView;

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

import developer.xebia.ismail.savvyhome.Controller.RoomAdapter;
import developer.xebia.ismail.savvyhome.Model.RoomItem;
import developer.xebia.ismail.savvyhome.R;

public class MyMenuFragment extends MenuFragment {

    String URL2 = "http://agnosthings.com/111531d4-1d4c-11e6-8001-005056805279";
    String send2 = "/field/month/feed/415/kwh";
    String URL3 = "http://agnosthings.com/6326b678-1d4c-11e6-8001-005056805279";
    String send3 = "/field/last/feed/417/progress";
    String URL4 = "http://agnosthings.com/83df94c0-1d4c-11e6-8001-005056805279";
    String send4 = "/field/last/feed/418/progress";
    String result2 = "";
    String result3 = "";
    String result4 = "";
    String tag2 = "";
    String uid2;
    private RecyclerView.Adapter mAdapter2;
    ArrayList<RoomItem> ListData2;
    FontTextView value, device;
    FontTextView type, value_gas;
    ImageView warning;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        value = (FontTextView) view.findViewById(R.id.value_kwh);
        device = (FontTextView) view.findViewById(R.id.desc_kwh);
        type = (FontTextView) view.findViewById(R.id.value_gallon);
        value_gas = (FontTextView) view.findViewById(R.id.value_gas);
        warning = (ImageView) view.findViewById(R.id.warning1);
        //new requestAPI2().execute();
        return setupReveal(view);
    }

    class requestAPI2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(URL2 + send2);
            ResponseHandler<String> handler = new BasicResponseHandler();
            try {
                result2 = httpclient.execute(request, handler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpclient.getConnectionManager().shutdown();
            Log.i("kwh: ", result2);
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
            int totKwh = 0, count = 0;
            JSONObject obj = new JSONObject(result2);
            Iterator x = obj.keys();
            JSONArray jArray = new JSONArray();
            while (x.hasNext()) {
                String key = (String) x.next();
                if(key.equals("cValue")) {
                    String[] data = obj.get(key).toString().split("\",\"");

                    for (int i = 0; i < data.length; i++) {
                        String[] data2 = data[i].replaceAll("\\[","").replaceAll("\\]","").replaceAll("\"","").split(",");
                        if(data2[0].isEmpty() || data2[0].equals("") || data2[0].equals("[]")) {
                            totKwh += 0;
                        } else {
                            //Toast.makeText(getActivity(), data2[0], Toast.LENGTH_SHORT).show();
                            totKwh += Integer.parseInt(data2[0]);
                        }
                        if (!data[i].equals("0")) {
                            count++;
                        }
                        Log.i("datacek: ", data2[0] + "");
                    }

                    Log.i("totalkwh2: ", totKwh + "");
                    value.setText(totKwh + "");
                    device.setText("Available "+count + " devices active");
                }
                //jArray.put("Basement");
            }

            for (int i=0; i<jArray.length(); i++) {

                //ListData2.add(new RoomItem(uid, jArray.get(i).toString(), R.drawable.r_bedroom));

                Log.i("dataa: ", jArray.get(i).toString());
            }

            new requestAPI3().execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class requestAPI3 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(URL3 + send3);
            ResponseHandler<String> handler = new BasicResponseHandler();
            try {
                result3 = httpclient.execute(request, handler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpclient.getConnectionManager().shutdown();
            Log.i("halloooo", result3);
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
            JSONObject obj = new JSONObject(result3);
            Iterator x = obj.keys();
            while (x.hasNext()) {
                String key = (String) x.next();
                String[] data = obj.get(key).toString().split("\",\"");

                while (i<data.length) {
                    String[] data2 = data[i].split(":\"");
                    type.setText(data2[0].replaceAll("[^\\p{L}\\p{Nd}]+", ""));
                    Log.i("tess: ", data[i] + " idx " + i);
                    i++;
                }
            }
            new requestAPI4().execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class requestAPI4 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(URL4 + send4);
            ResponseHandler<String> handler = new BasicResponseHandler();
            try {
                result4 = httpclient.execute(request, handler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpclient.getConnectionManager().shutdown();
            Log.i("halloooo", result4);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getGasProgress();
        }
    }

    public void getGasProgress() {
        try {
            int i = 0;
            JSONObject obj = new JSONObject(result4);
            Iterator x = obj.keys();
            while (x.hasNext()) {
                String key = (String) x.next();
                String[] data = obj.get(key).toString().split("\",\"");

                while (i<data.length) {
                    String[] data2 = data[i].split(":\"");
                    value_gas.setText(data2[0].replaceAll("[^\\p{L}\\p{Nd}]+", ""));

                    if(Integer.parseInt(data2[0].replaceAll("[^\\p{L}\\p{Nd}]+", "")) <= 10) {
                        warning.setVisibility(View.VISIBLE);
                    } else {
                        warning.setVisibility(View.INVISIBLE);
                    }

                    Log.i("tess: ", data2[0] + " idx " + i);
                    i++;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}