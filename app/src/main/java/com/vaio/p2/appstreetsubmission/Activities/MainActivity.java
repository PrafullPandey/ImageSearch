package com.vaio.p2.appstreetsubmission.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vaio.p2.appstreetsubmission.Adapter.ImageAdapter;
import com.vaio.p2.appstreetsubmission.Network.Response.SearchResponse;
import com.vaio.p2.appstreetsubmission.Network.RestAPI;
import com.vaio.p2.appstreetsubmission.R;
import com.vaio.p2.appstreetsubmission.Utilities.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ImageAdapter.GrowUpOnClick{

    private static final String TAG = "MainActivity";

    private int page =1;
    int columnCount = 2 ;
    String query = "apple";
    private ArrayList<String> imageURL ;

    private SearchView searchView ;
    private RecyclerView recyclerViewImage ;
    private GridLayoutManager gridLayoutManager ;
    private ImageAdapter imageAdapter ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize the view and the objects
        initialize();
        initializeRecyclerView();

        //testing
        getData(query);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                page=1;
                query=s;
                getData(s);
                return false ;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        recyclerViewImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)){
                    page++;
                    getData(query);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });



    }

    private void initializeRecyclerView() {
        recyclerViewImage = (RecyclerView)findViewById(R.id.recyclerViewImage);
        gridLayoutManager = new GridLayoutManager(this ,columnCount);
        recyclerViewImage.setHasFixedSize(true);
        recyclerViewImage.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(imageURL ,this,columnCount ,this);
        recyclerViewImage.setAdapter(imageAdapter);
    }

    private void initialize() {
        searchView = (SearchView)findViewById(R.id.searchImage);
        recyclerViewImage = (RecyclerView)findViewById(R.id.recyclerViewImage);

        imageURL = new ArrayList<>();
    }

    private void getData(String query) {
        RestAPI.getAppService().getSearchResult(Constants.ACCESS_KEY ,query ,page).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if(response.body()!=null) {
                    if(page==1)
                        imageURL.clear();
                    for(int i=0 ;i<response.body().getResults().size();i++){
                        imageURL.add(response.body().getResults().get(i).getUrls().getThumb());
                    }
                    imageAdapter.addItem(imageURL);
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.column2) {
            columnCount =2 ;
            initializeRecyclerView();
        }else if(id ==R.id.column3){
            columnCount = 3;
            initializeRecyclerView();
        }else if (id == R.id.column4){
            columnCount =4;
            initializeRecyclerView();
        }else if(id==android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void growUpOnClicking(String url, int position , ImageView imageView) {
//        Toast.makeText(this , position+" "+url ,Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, EnlargedImageActivity.class);
        // Pass data object in the bundle and populate details activity.
        intent.putExtra("URL", url);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View)imageView, "profile");
        startActivity(intent, options.toBundle());
    }
}
