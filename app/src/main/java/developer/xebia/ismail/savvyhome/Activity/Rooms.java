package developer.xebia.ismail.savvyhome.Activity;


/**
 * Created by Ismail Xebia on 5/9/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.yalantis.phoenix.PullToRefreshView;

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
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

import developer.xebia.ismail.savvyhome.Controller.Config;
import developer.xebia.ismail.savvyhome.Controller.RoomAdapter;
import developer.xebia.ismail.savvyhome.Model.RoomItem;
import developer.xebia.ismail.savvyhome.R;

public class Rooms extends Fragment{

    RecyclerView list_rooms;
    String URL = "http://agnosthings.com/ed31f9d4-1ea2-11e6-8001-005056805279";
    String send = "";
    String getRoom = "/field/month/feed/426/room_name";
    String result = "";
    String uid;
    private RecyclerView.Adapter mAdapter;
    ArrayList<RoomItem> ListData;
    AVLoadingIndicatorView firstText;
    String room[];
    private CoordinatorLayout coordinatorLayout;
    PullToRefreshView mPullToRefreshView;

    public Rooms() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);

        FloatingActionButton aboutBtn = (FloatingActionButton) rootView.findViewById(R.id.fab);
        list_rooms = (RecyclerView) rootView.findViewById(R.id.list_rooms);
        firstText = (AVLoadingIndicatorView) rootView.findViewById(R.id.tvLoading);
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinator);
        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
        ListData = new ArrayList<>();

        TelephonyManager tManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        uid = tManager.getDeviceId();

        list_rooms.setHasFixedSize(true);
        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(),2);
        list_rooms.setLayoutManager(lm);

        send = "/field/month/feed/426/location";
        if(isInternetAvailable()) {
            //new requestAPI2().execute();
        } else {
            /*Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new requestAPI2().execute();
                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.CYAN);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);

            snackbar.show();*/
        }

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), AddRoom.class);
                startActivity(i);
            }
        });

        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isInternetAvailable()) {
                            ListData.clear();
                            list_rooms.removeAllViews();
                            firstText.setVisibility(View.VISIBLE);
                            list_rooms.setVisibility(View.INVISIBLE);
                            new requestAPI2().execute();
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
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
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        return rootView;
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
            JSONArray jArray = new JSONArray();
            while (x.hasNext()) {
                String key = (String) x.next();
                if(key.equals("cValue")) {
                    String[] data = obj.get(key).toString().split("\",\"");

                    for (int i=0; i<data.length; i++) {
                        if (data[i].contains("Basement")) {
                            jArray.put("Basement,"+room[i]);
                        } else if (data[i].contains("Bathroom")) {
                            jArray.put("Bathroom,"+room[i]);
                        } else if (data[i].contains("Bedroom")) {
                            jArray.put("Bedroom,"+room[i]);
                        } else if (data[i].contains("Kitchen")) {
                            jArray.put("Kitchen,"+room[i]);
                        } else if (data[i].contains("Living Room")) {
                            jArray.put("Living Room,"+room[i]);
                        }

                        String[] split = jArray.get(i).toString().split(",");
                        if(jArray.get(i).toString().contains("Basement")) {
                            ListData.add(new RoomItem(uid, split[1], R.drawable.r_basement));
                        } else if(jArray.get(i).toString().contains("Bathroom")) {
                            ListData.add(new RoomItem(uid, split[1], R.drawable.r_bath));
                        } else if(jArray.get(i).toString().contains("Bedroom")) {
                            ListData.add(new RoomItem(uid, split[1], R.drawable.r_bedroom));
                        } else if(jArray.get(i).toString().contains("Kitchen")) {
                            ListData.add(new RoomItem(uid, split[1], R.drawable.r_kitchen));
                        } else if(jArray.get(i).toString().contains("Living Room")) {
                            ListData.add(new RoomItem(uid, split[1], R.drawable.r_living));
                        }
                    }
                }
            }

            if(!ListData.isEmpty()) {
                firstText.setVisibility(View.GONE);
                mAdapter = new RoomAdapter(ListData,getActivity(), R.layout.item_rooms);
                list_rooms.setAdapter(mAdapter);
                list_rooms.setVisibility(View.VISIBLE);
            } else {
                firstText.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class requestAPI2 extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet(URL + getRoom);
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
            getRoomData();
        }
    }

    public void getRoomData() {
        try {
            JSONObject obj = new JSONObject(result);
            Iterator x = obj.keys();
            while (x.hasNext()) {
                String key = (String) x.next();
                if(key.equals("cValue")) {
                    String[] data = obj.get(key).toString().split("\",\"");
                    room = new String[data.length];

                    for (int i=0; i<data.length; i++) {
                        String[] data2 = data[i].split(",");
                        room[i] = data2[0].replaceAll("[^\\p{L}\\p{Nd}]+", "");
                    }
                }
            }

            new requestAPI().execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
