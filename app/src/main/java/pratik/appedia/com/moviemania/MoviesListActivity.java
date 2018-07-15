package pratik.appedia.com.moviemania;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import adapter.EndlessRecyclerViewScrollListener;
import model.MovieItem;

public class MoviesListActivity extends AppCompatActivity {

    private TextView upcomingMoviesTextview;
    private RecyclerView moviesRecyclerView;
    private LinearLayoutManager layoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ProgressDialog progressDialog;
    private List<MovieItem> moviesList = new ArrayList<MovieItem>();
    private Parcelable recyclerViewState;
    private int page;
    private CustomAdapter adapter;
    private String imagesBaseUrl, posterSize92,size780;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        preferences = this.getSharedPreferences("pratik.appedia.com.moviemania.pref", Context.MODE_PRIVATE);
        editor = preferences.edit();
        adapter = new CustomAdapter(this,moviesList);
        findAndInitializeViews();
        initializeConfiguration();
        getUpcomingMovies(1);
    }

    public void findAndInitializeViews(){
        final Typeface galada_regular = Typeface.createFromAsset(getAssets(),"fonts/Galada-Regular.ttf");
        upcomingMoviesTextview = findViewById(R.id.upcomingMoviesTextview);
        upcomingMoviesTextview.setTypeface(galada_regular);
        upcomingMoviesTextview.setSelected(true);

        moviesRecyclerView = findViewById(R.id.moviesRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        moviesRecyclerView.setLayoutManager(layoutManager);
        moviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        moviesRecyclerView.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        moviesRecyclerView.addOnScrollListener(scrollListener);

    }

    public void loadNextDataFromApi(int offset) {
        recyclerViewState = moviesRecyclerView.getLayoutManager().onSaveInstanceState();
        getUpcomingMovies(++page);

    }

    public void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.fetching_upcoming_movies));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void goToInformationScreen(View v){
        Intent intent = new Intent(MoviesListActivity.this,InformationActivity.class);
        startActivity(intent);
    }

    public void getUpcomingMovies(int pageNo) {
        showProgressDialog();
        String url = String.format(getString(R.string.upcoming_movies_url),pageNo);
        final JsonObjectRequest movieReq = new JsonObjectRequest(
                Request.Method.GET
                ,url
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialog();
                try {
                    page = response.getInt("page");
                    JSONArray moviesArray = response.getJSONArray("results");
                    for(int i=0;i<moviesArray.length();i++){
                        JSONObject movieObject = moviesArray.getJSONObject(i);
                        MovieItem movieItem = new MovieItem();
                        movieItem.setMovieTitle(movieObject.getString("title"));
                        movieItem.setMovieID(movieObject.getLong("id"));
                        String releaseDate = movieObject.getString("release_date");
                        movieItem.setMovieDate(getFormattedDate(releaseDate));
                        movieItem.setMovieAdultRating(movieObject.getBoolean("adult") ? "(A)" : "(U/A)");
                        String filePath = movieObject.getString("poster_path");
                        filePath = filePath.replace("\\","");
                        String thumbnailUrl = imagesBaseUrl+posterSize92+filePath;
                        movieItem.setMovieThumbnailURL(thumbnailUrl);
                        moviesList.add(movieItem);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                moviesRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                adapter.notifyDataSetChanged();
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("MOVIES LIST ACTIVITY", "Error: " + error.getMessage());
                hideProgressDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(movieReq);

    }

    public void initializeConfiguration(){
        String configUrl = getString(R.string.configuration_url);
        final JsonObjectRequest configRequest = new JsonObjectRequest(
                Request.Method.GET
                , configUrl
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject imagesObject = response.getJSONObject("images");
                    imagesBaseUrl = imagesObject.getString("base_url");
                    JSONArray posterSizes = imagesObject.getJSONArray("poster_sizes");
                    posterSize92 = posterSizes.getString(0);
                    size780 = posterSizes.getString(5);
                    editor.putString("images_base_url",imagesBaseUrl);
                    editor.putString("size_780",size780);
                    editor.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("MOVIES LIST ACTIVITY", "Error: " + error.getMessage());
            }
        }
        );

        AppController.getInstance().addToRequestQueue(configRequest);
    }

    public String getFormattedDate(String date){
        String formatedDate = "";
        String[] arr = date.split("-");
        formatedDate = arr[2]+"/"+arr[1]+"/"+arr[0];
        return formatedDate;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

}
