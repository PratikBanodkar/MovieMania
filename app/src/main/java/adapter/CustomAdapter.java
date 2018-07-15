package adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import model.MovieItem;
import pratik.appedia.com.moviemania.AppController;
import pratik.appedia.com.moviemania.MovieDetailsActivity;
import pratik.appedia.com.moviemania.R;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private Context context;
    private List<MovieItem> movieItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomAdapter(Context context, List<MovieItem> movieItems) {
        this.context = context;
        this.movieItems = movieItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.upcoming_movie_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.ViewHolder holder, final int position) {
        final Typeface roboto_regular = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Regular.ttf");
        final Typeface roboto_light = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Light.ttf");
        holder.movieTitleTextview.setText(movieItems.get(holder.getAdapterPosition()).getMovieTitle());
        holder.movieTitleTextview.setTypeface(roboto_regular);
        holder.movieAdultRatingTextview.setText(movieItems.get(holder.getAdapterPosition()).getMovieAdultRating());
        holder.movieAdultRatingTextview.setTypeface(roboto_light);
        holder.movieReleaseDateTextview.setText(movieItems.get(holder.getAdapterPosition()).getMovieDate());
        holder.movieReleaseDateTextview.setTypeface(roboto_light);
        holder.movieThumbnailImageview.setImageUrl(movieItems.get(holder.getAdapterPosition()).getMovieThumbnailURL(),imageLoader);
        holder.itemCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailsIntent = new Intent(context, MovieDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("movieid",movieItems.get(holder.getAdapterPosition()).getMovieID());
                bundle.putString("movie_name",movieItems.get(holder.getAdapterPosition()).getMovieTitle());
                detailsIntent.putExtras(bundle);
                context.startActivity(detailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CardView itemCardview;
        private TextView movieTitleTextview;
        private TextView movieReleaseDateTextview;
        private TextView movieAdultRatingTextview;
        private NetworkImageView movieThumbnailImageview;

        public ViewHolder(View itemView) {
            super(itemView);
            itemCardview = itemView.findViewById(R.id.itemCardview);
            movieTitleTextview = itemView.findViewById(R.id.movieTitleTextview);
            movieReleaseDateTextview = itemView.findViewById(R.id.movieReleaseDateTextview);
            movieAdultRatingTextview = itemView.findViewById(R.id.movieAdultRatingtextview);
            movieThumbnailImageview = itemView.findViewById(R.id.movieThumbnailImageview);
        }

        @Override
        public void onClick(View view) {
        }
    }
}
