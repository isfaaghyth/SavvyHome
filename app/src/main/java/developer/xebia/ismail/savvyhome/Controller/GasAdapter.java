package developer.xebia.ismail.savvyhome.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixplicity.fontview.FontAppCompatTextView;

import java.util.List;

import developer.xebia.ismail.savvyhome.Model.DeviceItem;
import developer.xebia.ismail.savvyhome.Model.GalloonItem;
import developer.xebia.ismail.savvyhome.Model.GasItem;
import developer.xebia.ismail.savvyhome.R;

public class GasAdapter extends RecyclerView.Adapter<GasAdapter.ViewHolder> {

    private List<GasItem> items;
    private int itemLayout;
    Context mContext;

    public GasAdapter(List<GasItem> items, Context mContext, int itemLayout) {
        this.items = items;
        this.itemLayout = itemLayout;
        this.mContext = mContext;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(final ViewHolder holder, int position) {
        final GasItem item = items.get(position);
        holder.nama.setText(item.getProgress());
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

        public ViewHolder(View v) {
            super(v);
            nama = (FontAppCompatTextView) v.findViewById(R.id.value_gas);
        }
    }
}
