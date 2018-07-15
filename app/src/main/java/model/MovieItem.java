package model;

public class MovieItem {

    private long movieID;
    private String movieTitle;
    private String movieDate;
    private String movieAdultRating;
    private String movieThumbnailURL;

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieDate() {
        return movieDate;
    }

    public void setMovieDate(String movieDate) {
        this.movieDate = movieDate;
    }

    public String getMovieAdultRating() {
        return movieAdultRating;
    }

    public void setMovieAdultRating(String movieAdultRating) {
        this.movieAdultRating = movieAdultRating;
    }

    public long getMovieID() {
        return movieID;
    }

    public void setMovieID(long movieID) {
        this.movieID = movieID;
    }

    public String getMovieThumbnailURL() {
        return movieThumbnailURL;
    }

    public void setMovieThumbnailURL(String movieThumbnailURL) {
        this.movieThumbnailURL = movieThumbnailURL;
    }
}
