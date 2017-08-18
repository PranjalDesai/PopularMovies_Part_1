package com.pranjaldesai.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private ImageView bannerImageView, posterImageView;
    private TextView userResultTV, releaseDateTV, plotTV;
    private static final String mThemeName= "pref_theme_selection";
    private Result mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   Sets the Theme
        final SharedPreferences mPreferences = getSharedPreferences(mThemeName, Context.MODE_PRIVATE);
        boolean currentThemeState= mPreferences.getBoolean(getString(R.string.themeSetting), false);
        if (currentThemeState) {
            setTheme(R.style.AppThemeDark);
        }else{
            setTheme(R.style.AppThemeLight);
        }

        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        setData();

        /* Saving for Project 2
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    /*
    *   initializes the views
    */
    private void init(){
        bannerImageView= (ImageView) findViewById(R.id.bannerImageView);
        posterImageView= (ImageView) findViewById(R.id.ivPoster);
        userResultTV= (TextView) findViewById(R.id.userRatingTV);
        releaseDateTV= (TextView) findViewById(R.id.releaseTV);
        plotTV= (TextView) findViewById(R.id.plot);
    }

    /*
    *   grabs data object from the intent and then sets it to the views
    */
    private void setData(){
        Intent intent= getIntent();
        if(intent.hasExtra(getString(R.string.detailIntent))
                 && intent.getSerializableExtra(getString(R.string.detailIntent))!=null){
            mData= (Result) intent.getSerializableExtra(getString(R.string.detailIntent));
            getSupportActionBar().setTitle(mData.getTitle());
            userResultTV.setText(Double.toString(mData.getVote_average()));
            releaseDateTV.setText(mData.getRelease_date());
            plotTV.setText(mData.getOverview());

        }

        if(mData!=null) {
            try {
                String bannerImage = "http://image.tmdb.org/t/p/original";
                Picasso.with(this)
                        .load(bannerImage +mData.getBackdrop_path())
                        .placeholder(R.drawable.ic_menu_slideshow)
                        .into(bannerImageView);
                String posterImage = "http://image.tmdb.org/t/p/w500/";
                Picasso.with(this)
                        .load(posterImage +mData.getPoster_path())
                        .placeholder(R.drawable.ic_menu_slideshow)
                        .into(posterImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
