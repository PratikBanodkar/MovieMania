package pratik.appedia.com.moviemania;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailsActivity extends AppCompatActivity {

    private TextView movieTitleTextview1, movieTitleTextview2, overviewTextview,overviewValueTextview, popularityTextview;
    String imagesUrl;
    private ProgressDialog progressDialog;
    private SliderLayout sliderLayout;
    private String imagesBaseUrl,size780,detailsUrl;
    private RatingBar popularityRatingBar;
    private String movieName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        SharedPreferences preferences = this.getSharedPreferences("pratik.appedia.com.moviemania.pref", Context.MODE_PRIVATE);
        imagesBaseUrl = preferences.getString("images_base_url","");
        size780 = preferences.getString("size_780","");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        long movieID = bundle.getLong("movieid",0);
        movieName = bundle.getString("movie_name","");

        imagesUrl = String.format(getString(R.string.images_url),movieID);

        findAndInitializeViews();
        showProgressDialog();
        getMovieDetails(movieID);

    }

    public void findAndInitializeViews(){
        final Typeface galada_regular = Typeface.createFromAsset(getAssets(),"fonts/Galada-Regular.ttf");
        final Typeface roboto_bold = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Bold.ttf");
        final Typeface roboto_regular = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        final Typeface roboto_light = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");

        movieTitleTextview1 = findViewById(R.id.movieTitleTextview1);
        movieTitleTextview1.setTypeface(galada_regular);
        movieTitleTextview1.setSelected(true);
        movieTitleTextview1.setText(movieName);

        movieTitleTextview2 = findViewById(R.id.movieTitleTextview2);
        movieTitleTextview2.setTypeface(roboto_bold);
        movieTitleTextview2.setText(movieName);

        sliderLayout = findViewById(R.id.imageSliderLayout);
        sliderLayout.stopAutoCycle();

        overviewTextview = findViewById(R.id.overviewTextview);
        overviewTextview.setTypeface(roboto_light);
        overviewValueTextview = findViewById(R.id.overviewValueTextview);
        overviewValueTextview.setTypeface(roboto_regular);
        popularityTextview = findViewById(R.id.popularityTextview);
        popularityTextview.setTypeface(roboto_light);

        popularityRatingBar = findViewById(R.id.popularityRatingbar);
    }

    public void getMovieDetails(long movieID){
        detailsUrl = String.format(getString(R.string.movie_details_url),movieID);
        final JsonObjectRequest detailsReq = new JsonObjectRequest(
                Request.Method.GET
                , detailsUrl
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String overview = response.getString("overview");
                    overviewValueTextview.setText(overview);
                    double popularity = response.getDouble("popularity");
                    popularity = Math.round(popularity/20);
                    popularityRatingBar.setRating((float) popularity);
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
        AppController.getInstance().addToRequestQueue(detailsReq);
        getMovieImages();
    }

    public void getMovieImages(){
        final JsonObjectRequest imagesReq = new JsonObjectRequest(
                Request.Method.GET
                , imagesUrl
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgressDialog();
                try {
                    JSONArray backdropsArray = response.getJSONArray("backdrops");
                    int noOfImages=0;
                    if(backdropsArray.length() >= 5)
                        noOfImages=5;
                    else
                        noOfImages = backdropsArray.length();
                    for(int i=0; i < noOfImages ;i++){
                        JSONObject backdropObject = backdropsArray.getJSONObject(i);
                        String filePath = backdropObject.getString("file_path");
                        filePath = filePath.replace("\\","");
                        String completePath = imagesBaseUrl+size780+filePath;
                        DefaultSliderView defaultSliderView = new DefaultSliderView(MovieDetailsActivity.this);
                        defaultSliderView.image(completePath);
                        sliderLayout.addSlider(defaultSliderView);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("MOVIES LIST ACTIVITY", "Error: " + error.getMessage());
                hideProgressDialog();
            }
        }
        );
        AppController.getInstance().addToRequestQueue(imagesReq);
    }

    public void showProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.fetching_movie_details));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

}
