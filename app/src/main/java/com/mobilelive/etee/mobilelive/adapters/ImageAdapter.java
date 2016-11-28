package com.mobilelive.etee.mobilelive.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilelive.etee.mobilelive.ui.ImageListActivity;
import com.mobilelive.etee.mobilelive.R;
import com.mobilelive.etee.mobilelive.model.ImageObject;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private ArrayList<ImageObject> images;

    public void setListener(ImageListActivity.OnItemClickListener listener) {
        this.listener = listener;
    }

    ImageListActivity.OnItemClickListener listener;
    public ImageAdapter(ArrayList<ImageObject> imageObjects) {
        this.images = imageObjects;

    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);

        ImageViewHolder vh = new ImageViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.updateView(images.get(position));

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setData(ArrayList<ImageObject> imageObjects) {
        this.images = imageObjects;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewImageName;

        public ImageViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            initComponents(v);
        }

        private void initComponents(View v) {
            textViewImageName = (TextView) v.findViewById(R.id.text_view);
        }

        public void updateView(ImageObject image) {
            textViewImageName.setText(image.getImageName());
        }


        @Override
        public void onClick(View view) {
            listener.onItemClick(view,getAdapterPosition(),view.getId());
        }
    }


}
