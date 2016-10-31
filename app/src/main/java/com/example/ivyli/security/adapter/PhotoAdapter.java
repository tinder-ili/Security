package com.example.ivyli.security.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ivyli.security.R;
import com.example.ivyli.security.db.ImagesTable;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private List<ImagesTable> mPhotos = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView mImage;

        public ViewHolder(View v) {
            super(v);
            mImage = (ImageView) v.findViewById(R.id.photo);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PhotoAdapter(List<ImagesTable> myDataset) {
        if (myDataset != null) {
            mPhotos = myDataset;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_row, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mPhotos == null || mPhotos.isEmpty()) {
            return;
        }
        holder.mImage.setImageBitmap(mPhotos.get(position).getImageBitmap());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void setPhotos(List<ImagesTable> list) {
        mPhotos.clear();
        mPhotos.addAll(list);
        notifyDataSetChanged();
    }
}

