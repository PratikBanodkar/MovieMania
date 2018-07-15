# MovieMania
A demo movies app using the TMDB API

This demo Android application will show upcoming movies using the The Movie Db API (Check it out at:- https://www.themoviedb.org/?language=en).

The app displays a list of upcoming movies in a RecyclerView and you can check out details about movie by clicking on any item.

**REST API Details**:-

1. Fetching Upcoming Movies

https://api.themoviedb.org/3/movie/upcoming

Params:- api_key

For example:- If your API Key is something like b7cd8765a6716bhjdu9097f then the complete url would look like :-

https://api.themoviedb.org/3/movie/upcoming?api_key=b7cd8765a6716bhjdu9097f

2. Movie Details

https://api.themoviedb.org/3/movie/<movie-id>

Params:- api_key

3. Movie Images

https://api.themoviedb.org/3/movie/<movie-id>/images

Params:- api_key


Apart from the TMDb API , the app also uses Volley API for JSON requests and also a slider library found at :- https://github.com/daimajia/AndroidImageSlider
