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

    public interface GrowUpOnClick{
        public void growUpOnClicking(String url , int position ,ImageView imageView);
    }

    ArrayList<String> urls ;
    Context context ;
    int column ;
    GrowUpOnClick growUpOnClick ;


    public ImageAdapter(ArrayList<String> urls, Context context, int column , GrowUpOnClick growUpOnClick) {
        this.urls = urls;
        this.context = context;
        this.column = column;
        this.growUpOnClick = growUpOnClick;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =null ;
        if(column==2) {
            view = inflater.inflate(R.layout.image_item, parent, false);
        }
        else if(column==3){
            view = inflater.inflate(R.layout.image_item3, parent, false);
        }else if(column==4){
            view = inflater.inflate(R.layout.image_item4, parent, false);
        }
        return new ImageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String url = urls.get(position);
        holder.image.setImageResource(0);
        holder.setImage(context ,url);
    }

    @Override
    public int getItemCount() {
        return urls!=null?urls.size()-urls.size()%column:0;
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

        public void setImage(Context context, final String url) {
            Picasso.with(context).load(url).into(image);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    growUpOnClick.growUpOnClicking(url,getAdapterPosition(),image);
                }
            });
        }
    }
}
