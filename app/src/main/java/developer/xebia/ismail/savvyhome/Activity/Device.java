package developer.xebia.ismail.savvyhome.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pixplicity.fontview.FontAppCompatTextView;

import java.util.ArrayList;

import developer.xebia.ismail.savvyhome.Controller.DeviceAdapter;
import developer.xebia.ismail.savvyhome.Controller.RoomAdapter;
import developer.xebia.ismail.savvyhome.Model.DeviceItem;
import developer.xebia.ismail.savvyhome.Model.RoomItem;
import developer.xebia.ismail.savvyhome.R;

/**
 * Created by Helmi Ismail on 5/16/2016.
 */
public class Device extends Fragment{

    RecyclerView list_rooms;
    private RecyclerView.Adapter mAdapter;
    ArrayList<DeviceItem> ListData;
    FontAppCompatTextView firstText;
    LinearLayout llAll;

    public Device() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_device, container, false);
        list_rooms = (RecyclerView) rootView.findViewById(R.id.list_rooms);
        firstText = (FontAppCompatTextView) rootView.findViewById(R.id.tvLoading);
        llAll = (LinearLayout) rootView.findViewById(R.id.llAll);

        ListData = new ArrayList<>();
        list_rooms.setHasFixedSize(true);

        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(),2);
        list_rooms.setLayoutManager(lm);

        FloatingActionButton aboutBtn = (FloatingActionButton) rootView.findViewById(R.id.fab);

        aboutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                //Toast.makeText(Device.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                Snackbar.make(getActivity().findViewById(R.id.llAll), "Coming Soon", Snackbar.LENGTH_SHORT).show();
            }
        });

        setMenu();

        return rootView;
    }

    public void setMenu() {
        ListData.add(new DeviceItem("Lighting", R.drawable.ic_lamp));
        ListData.add(new DeviceItem("Gas", R.drawable.ic_tank));
        ListData.add(new DeviceItem("Galloon", R.drawable.ic_gallon));
        ListData.add(new DeviceItem("Door", R.drawable.ic_key));

        firstText.setVisibility(View.GONE);
        mAdapter = new DeviceAdapter(ListData,getActivity(), R.layout.list_device);
        list_rooms.setAdapter(mAdapter);
    }
}
