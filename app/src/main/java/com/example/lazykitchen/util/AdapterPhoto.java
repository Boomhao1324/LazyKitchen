package com.example.lazykitchen.util;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.lazykitchen.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.PhotoViewHolder>{

    List<PhotoItem> photos;
    View inflater;

    public AdapterPhoto() {
        photos=new ArrayList<>();
    }
    private OnRecyclerPhotoClickListener monPhotoClickListener;

    public void setRecyclerItemClickListener(OnRecyclerPhotoClickListener listener) {
        monPhotoClickListener = listener;
    }

    public AdapterPhoto(List<PhotoItem> photos) {
        this.photos = photos;
    }

    public List<PhotoItem> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoItem> photos) {
        this.photos = photos;
    }

    @NonNull
    @NotNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item,parent,false);
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(inflater);
        return photoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PhotoViewHolder holder, int position) {
        PhotoItem photoItem = photos.get(position);
        if(photoItem.getPhotoUrl()!=null) {
            if(photoItem.getPhotoUrl().toString().startsWith("http")){
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.baseline_edit_off_red_300_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .override(720, 720);
                Glide.with(holder.imageView.getContext())
                        .load(photoItem.getPhotoUrl())
                        .apply(options)
                        .fitCenter()
                        .into(holder.imageView);
            }else if(photoItem.getPhotoUrl().toString().startsWith("content")){
                holder.imageView.setImageURI(photoItem.getPhotoUrl());
            }else {
                holder.imageView.setImageResource(R.drawable.ic_baseline_face_24);
            }
        }else if(photoItem.getBitmap()!=null){
            holder.imageView.setImageBitmap(photoItem.getBitmap());
        }else holder.imageView.setImageResource(R.drawable.ic_baseline_face_24);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        public PhotoViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (monPhotoClickListener != null) {
                        monPhotoClickListener.onItemClick(getAdapterPosition(), photos);
                    }
                }
            });
        }
    }
}
