package com.vaio.p2.appstreetsubmission.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vaio.p2.appstreetsubmission.R;

import java.util.ArrayList;

import static android.view.View.inflate;

public class ImagePagerAdapter  extends PagerAdapter {
    Context context;
    ArrayList<String> urls ;

    public ImagePagerAdapter(Context context , ArrayList<String> urls){
        this.context=context;
        this.urls =urls ;
    }
    @Override
    public int getCount() {
        return urls!=null?urls.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = (View) inflate(context,R.layout.enlarge_image_item, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.enlargedImage);

        /*ImageView imageView = new ImageView(context);
        int padding = context.getResources().getDimensionPixelSize(R.dimen.medium_padding);
        imageView.setPadding(padding, padding, padding, padding);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);*/

        Picasso.with(context).load(urls.get(position)).placeholder(context.getDrawable(R.drawable.thumbnail)).into(imageView);
        if(imageView.getParent()!=null){
            ((ViewGroup)imageView.getParent()).removeView(imageView);
        }
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}