package developer.xebia.ismail.savvyhome.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.pixplicity.fontview.FontAppCompatTextView;
import com.pixplicity.fontview.FontTextView;

import java.util.Calendar;
import java.util.List;

import developer.xebia.ismail.savvyhome.Model.RoomItem;
import developer.xebia.ismail.savvyhome.R;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    private List<RoomItem> items;
    private int itemLayout;
    Context mContext;

    public RoomAdapter(List<RoomItem> items, Context mContext, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.mContext = mContext;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
        final RoomItem item = items.get(position);
        holder.nama.setText(item.getRoom_name());
        holder.gambar.setImageResource(item.getImage());
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
        public ImageView gambar;

        public ViewHolder(View v) {
            super(v);
            nama = (FontAppCompatTextView) v.findViewById(R.id.nama);
            gambar = (ImageView) v.findViewById(R.id.gambar);
        }
    }
}
