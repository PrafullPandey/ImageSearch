package com.vaio.p2.appstreetsubmission.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vaio.p2.appstreetsubmission.R;

public class EnlargedImageActivity extends AppCompatActivity {

    private ImageView imageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlarged_image);

        initializeView();
        Picasso.with(getApplicationContext()).load(getIntent().getStringExtra("URL")).into(imageView);

        supportPostponeEnterTransition();
        imageView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                }
        );

    }

    private void initializeView() {
        imageView = (ImageView)findViewById(R.id.enlargedImage);
    }
}
