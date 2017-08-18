package com.pranjaldesai.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pranjaldesai.popularmovies.utilities.NetworkUtils;

import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private final String popularMovie= "popular";
    private final String topRatedMovie= "top_rated";
    private String popMovie, topMovie;
    private boolean firstCall, secondCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeMovieURL(popularMovie);
        makeMovieURL(topRatedMovie);
    }

    /*
    *   Makes URL for topRated api or popular api
    */
    private void makeMovieURL(String urlString){
        URL searchURL= NetworkUtils.buildUrl(urlString,getString(R.string.moviedb_api_key));

        MovieDBTask movieDBTask= new MovieDBTask();
        movieDBTask.execute(searchURL);
    }

    /*
    *   Set JSON Strings and Starts intent for the MainActivity
    */
    private void setMovieJSONResult(String movieResult){

        if(firstCall && !secondCall){
            popMovie= movieResult;
        }else{
            topMovie= movieResult;
        }

        if(firstCall && secondCall){
            Intent intent= new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra(getString(R.string.popularMovieIntent), popMovie);
            intent.putExtra(getString(R.string.topRatedIntent), topMovie);
            startActivity(intent);
            finish();
        }

    }

    /*
    *   Making the api call on a different thread and then saving the strings.
    */
    private class MovieDBTask extends AsyncTask<URL,Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];

            if(searchUrl.toString().contains(popularMovie)){
                firstCall=true;
            }else if(searchUrl.toString().contains(topRatedMovie)){
                secondCall=true;
            }

            String movieResults= null;
            if(isOnline()) {
                try {
                    movieResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return movieResults;
        }

        @Override
        protected void onPostExecute(String movieResults){
            if(movieResults!=null && !movieResults.equals("")){
                setMovieJSONResult(movieResults);
            }

        }

        /*
        *   Is internet available.
        */
        public boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }

    }
}
