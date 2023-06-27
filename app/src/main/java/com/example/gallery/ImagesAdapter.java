package com.example.gallery;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ImagesAdapter extends ListAdapter<Uri, ImagesAdapter.ImageViewHolder> {

    private static final DiffUtil.ItemCallback<Uri> itemDiffCallback = new DiffUtil.ItemCallback<Uri>() {

        @Override
        public boolean areItemsTheSame(@NonNull Uri oldItem, @NonNull Uri newItem) {
            return oldItem.toString().equals(newItem.toString());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Uri oldItem, @NonNull Uri newItem) {
            return true;
        }
    };

    ImagesAdapter() {
        super(itemDiffCallback);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_list_item, parent, false);
        return new ImageViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }

        public void bind(Uri imageUri) {
            Glide.with(imageView).load(imageUri).into(imageView);
        }
    }
}
