package com.vaio.p2.appstreetsubmission.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vaio.p2.appstreetsubmission.R;

import java.util.ArrayList;

/**
 * Created by p2 on 12/4/19.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    ArrayList<String> urls ;
    Context context ;

    public ImageAdapter(ArrayList<String> urls, Context context) {
        this.urls = urls;
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String url = urls.get(position);
        holder.setImage(context ,url);
    }

    @Override
    public int getItemCount() {
        return urls!=null?urls.size():0;
    }

    public void addItem(ArrayList<String> urls){
        this.urls.addAll(urls);
        notifyDataSetChanged();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ImageViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

        public void setImage(Context context, String url) {
            Picasso.with(context).load(url).into(image);
        }
    }
}
