package com.example.ivyli.security.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ivyli.security.R;

import java.util.ArrayList;
import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {
    private List<String> mStatus = new ArrayList<>();
    private Context mContext;

    public StatusAdapter(List<String> myDataset, Context context) {
        if (myDataset != null) {
            mStatus = myDataset;
        }
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_status_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mStatus == null || mStatus.isEmpty()) {
            return;
        }
        holder.setText(mStatus.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mStatus.size();
    }

    public void addStatus(String value){
        mStatus.add(value);
        notifyDataSetChanged();
    }

    public void clear(){
        mStatus.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mText;

        public ViewHolder(View v) {
            super(v);
            mText = (TextView) v.findViewById(R.id.status);
        }

        public void setText(String text){
            mText.setText(text);
        }
    }
}

