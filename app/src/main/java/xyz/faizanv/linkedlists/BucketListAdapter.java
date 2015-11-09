package xyz.faizanv.linkedlists;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by faizanv on 11/7/15.
 */
public class BucketListAdapter extends RecyclerView.Adapter {

    List<ParseObject> list;

    public BucketListAdapter(List<ParseObject> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bucket_list_card, parent, false);
        RecyclerView.ViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).title.setText(list.get(position).getString("Title"));
        ((MyViewHolder) holder).detail.setText(list.get(position).getString("Details"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView detail;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.bucket_card_title);
            detail = (TextView) itemView.findViewById(R.id.bucket_card_detail);
        }


    }
}
