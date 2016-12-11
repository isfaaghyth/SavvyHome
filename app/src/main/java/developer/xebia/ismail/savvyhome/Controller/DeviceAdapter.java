package developer.xebia.ismail.savvyhome.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pixplicity.fontview.FontAppCompatTextView;

import java.util.List;

import developer.xebia.ismail.savvyhome.Activity.Door;
import developer.xebia.ismail.savvyhome.Activity.Gallon;
import developer.xebia.ismail.savvyhome.Activity.Gas;
import developer.xebia.ismail.savvyhome.Activity.Lampu;
import developer.xebia.ismail.savvyhome.Model.DeviceItem;
import developer.xebia.ismail.savvyhome.R;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    private List<DeviceItem> items;
    private int itemLayout;
    Context mContext;

    public DeviceAdapter(List<DeviceItem> items, Context mContext, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.mContext = mContext;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
        final DeviceItem item = items.get(position);
        holder.nama.setText(item.getDevice_name());
        holder.gambar.setImageResource(item.getImage());
        holder.llCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getDevice_name().equals("Lighting")) {
                    Intent i = new Intent(mContext, Lampu.class);
                    mContext.startActivity(i);
                } else if(item.getDevice_name().equals("Gas")) {
                    Intent i = new Intent(mContext, Gas.class);
                    mContext.startActivity(i);
                } else if(item.getDevice_name().equals("Galloon")) {
                    Intent i = new Intent(mContext, Gallon.class);
                    mContext.startActivity(i);
                } else if(item.getDevice_name().equals("Door")) {
                    Intent i = new Intent(mContext, Door.class);
                    mContext.startActivity(i);
                }
            }
        });
    }

    public void remove(DeviceItem item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public FontAppCompatTextView nama;
        public ImageView gambar;
        public LinearLayout llCard;

        public ViewHolder(View v) {
            super(v);
            nama = (FontAppCompatTextView) v.findViewById(R.id.nama);
            gambar = (ImageView) v.findViewById(R.id.gambar);
            llCard = (LinearLayout) v.findViewById(R.id.llCard);
        }
    }
}
