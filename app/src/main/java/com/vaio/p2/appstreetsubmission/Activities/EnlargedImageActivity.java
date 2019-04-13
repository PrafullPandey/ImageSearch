package com.vaio.p2.appstreetsubmission.Activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.vaio.p2.appstreetsubmission.Adapter.ImagePagerAdapter;
import com.vaio.p2.appstreetsubmission.R;

import java.util.ArrayList;

public class EnlargedImageActivity extends AppCompatActivity {

    private ImageView imageView ;
    private ArrayList<String> urlList ;
    private ViewPager viewPager ;
    private ImagePagerAdapter imagePagerAdapter ;
    public static int currentItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarged_image);

        //retreive the list from the shared preferences
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("URLLIST", "");
        urlList= gson.fromJson(json,
                new TypeToken<ArrayList<String>>(){}.getType());

        initializeView();


        viewPager.setCurrentItem(getIntent().getIntExtra("POSITION",0));

        supportPostponeEnterTransition();
        viewPager.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        viewPager.getViewTreeObserver().removeOnPreDrawListener(this);
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                }
        );



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        currentItem=viewPager.getCurrentItem();

    }

    private void initializeView() {
        imageView = (ImageView)findViewById(R.id.enlargedImage);
        imagePagerAdapter = new ImagePagerAdapter(this,urlList);
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(imagePagerAdapter);
    }
}
