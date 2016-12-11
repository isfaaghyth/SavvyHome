package developer.xebia.ismail.savvyhome.Controller;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.pixplicity.fontview.FontAppCompatTextView;

import java.util.List;

import developer.xebia.ismail.savvyhome.Model.LightItem;
import developer.xebia.ismail.savvyhome.Model.RoomItem;
import developer.xebia.ismail.savvyhome.R;

public class LightAdapter extends RecyclerView.Adapter<LightAdapter.ViewHolder> {

    private List<LightItem> items;
    private int itemLayout;
    Context mContext;

    public LightAdapter(List<LightItem> items, Context mContext, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.mContext = mContext;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
        final LightItem item = items.get(position);
        holder.nama.setText(item.getLocation());
        if(item.getStatus().equals("1")) {
            holder.bg_check.setChecked(true);
            holder.on.setImageResource(R.drawable.ic_on);
        } else {
            holder.bg_check.setChecked(false);
            holder.on.setImageResource(R.drawable.ic_off);
        }
    }

    public void remove(RoomItem item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public FontAppCompatTextView nama;
        public SwitchCompat bg_check;
        public ImageView on;

        public ViewHolder(View v) {
            super(v);
            nama = (FontAppCompatTextView) v.findViewById(R.id.nama_lampu);
            bg_check = (SwitchCompat) v.findViewById(R.id.bg_check);
            on = (ImageView) v.findViewById(R.id.on);
        }
    }
}
