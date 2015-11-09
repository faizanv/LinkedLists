package xyz.faizanv.linkedlists;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by faizanv on 11/8/15.
 */
public class ListDetailAdapter extends RecyclerView.Adapter {

    List<ParseObject> list;

    public ListDetailAdapter(List<ParseObject> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card, parent, false);
        RecyclerView.ViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).detail.setText(list.get(position).getString("Title"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView detail;

        public MyViewHolder(View itemView) {
            super(itemView);
            detail = (TextView) itemView.findViewById(R.id.list_item);
        }


    }
}
