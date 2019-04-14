package com.vaio.p2.appstreetsubmission.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vaio.p2.appstreetsubmission.Adapter.ImageAdapter;
import com.vaio.p2.appstreetsubmission.Database.DatabaseHelper;
import com.vaio.p2.appstreetsubmission.Network.Response.SearchResponse;
import com.vaio.p2.appstreetsubmission.Network.RestAPI;
import com.vaio.p2.appstreetsubmission.R;
import com.vaio.p2.appstreetsubmission.Utilities.Constants;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ImageAdapter.GrowUpOnClick {

    private static final String TAG = "MainActivity";
    int columnCount = 2;
    String query = "apple";
    private int page = 1;
    private static int current = -1;
    private int per_page = 40 ;
    private ArrayList<String> imageURL;

    private SearchView searchView;
    private ProgressBar progressBar ;
    private TextView noResulttextView ;
    private RecyclerView recyclerViewImage;
    private GridLayoutManager gridLayoutManager;
    private ImageAdapter imageAdapter;
    private DatabaseHelper db ;



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
                s=s.toLowerCase();
                page = 1;
                query = s;
                if(isNetworkConnected()) {
                    progressBar.setVisibility(View.VISIBLE);
                    getData(s);
                }
                else {
                    if (db.getAllImages(query) != null && db.getAllImages(query).size() > 0 && page == 1) {
                        noResulttextView.setVisibility(View.GONE);
                        imageURL = db.getAllImages(query);
                        imageAdapter.addItem(imageURL);
                        page = imageURL.size() / 10;
                    }else{
                        noResulttextView.setVisibility(View.VISIBLE);
                        imageAdapter.addItem(null);
                    }
                }
                return false;
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
                if (!recyclerView.canScrollVertically(1)) {
                    if(isNetworkConnected()){
                        progressBar.setVisibility(View.VISIBLE);
                        page++;
                        getData(query);
                    }else{
                        Toast.makeText(getApplicationContext() , "Not Connected to Network" , Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        if (savedInstanceState != null) {
            current = savedInstanceState.getInt("VIEW_POSITION");
        }else{
            current =0 ;
        }


        setExitSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                if (sharedElements.isEmpty()) {
                    View view = recyclerViewImage.getLayoutManager().findViewByPosition(current);
                    if (view != null) {
                        sharedElements.put(names.get(0), view);
                    }
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("VIEW_POSITION", current);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        
    }

    private void initializeRecyclerView() {
        recyclerViewImage = (RecyclerView) findViewById(R.id.recyclerViewImage);
        gridLayoutManager = new GridLayoutManager(this, columnCount);
        recyclerViewImage.setHasFixedSize(true);
        recyclerViewImage.setLayoutManager(gridLayoutManager);
        imageAdapter = new ImageAdapter(imageURL, this, columnCount, this);
        recyclerViewImage.setAdapter(imageAdapter);

        if(EnlargedImageActivity.currentItem!=-1){
            recyclerViewImage.getLayoutManager().scrollToPosition(EnlargedImageActivity.currentItem);
        }
    }

    private void initialize() {
        searchView = (SearchView) findViewById(R.id.searchImage);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        recyclerViewImage = (RecyclerView) findViewById(R.id.recyclerViewImage);
        noResulttextView = (TextView)findViewById(R.id.noResults);
        db = new DatabaseHelper(getApplicationContext());

        imageURL = new ArrayList<>();
    }

    private void getData(final String query) {
        if(db.getAllImages(query)!=null&&db.getAllImages(query).size()>0&&page==1){
            noResulttextView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            imageURL.addAll(db.getAllImages(query));
            imageAdapter.addItem(imageURL);
            page = imageURL.size()/10 ;
        }else{
            if(page!=1)
                per_page=10;
            else{
                per_page=40;
            }
            RestAPI.getAppService().getSearchResult(Constants.ACCESS_KEY, query, page ,per_page).enqueue(new Callback<SearchResponse>() {
                @Override
                public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                    if (response.body() != null) {
                        progressBar.setVisibility(View.GONE);
                        noResulttextView.setVisibility(View.GONE);
                        if (page == 1)
                            imageURL.clear();
                        for (int i = 0; i < response.body().getResults().size(); i++) {
                            imageURL.add(response.body().getResults().get(i).getUrls().getThumb());
                            db.createImage(query,response.body().getResults().get(i).getUrls().getThumb());
                        }
                        imageAdapter.addItem(imageURL);
                    }
                }

                @Override
                public void onFailure(Call<SearchResponse> call, Throwable t) {
                    if(page==1){
                        noResulttextView.setVisibility(View.VISIBLE);
                    }else{
                        page--;
                    }
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

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
            columnCount = 2;
            initializeRecyclerView();
        } else if (id == R.id.column3) {
            columnCount = 3;
            initializeRecyclerView();
        } else if (id == R.id.column4) {
            columnCount = 4;
            initializeRecyclerView();
        } else if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void growUpOnClicking(String url, int position, ImageView imageView) {
        current = position ;

        //to store the list retrieved from response
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(imageURL);
        prefsEditor.putString("URLLIST", json);
        prefsEditor.commit();

        Intent intent = new Intent(this, EnlargedImageActivity.class);
        // Pass data object in the bundle and populate details activity.
        intent.putExtra("POSITION", position);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, (View) imageView, "profile");
        startActivity(intent, options.toBundle());

    }
}
