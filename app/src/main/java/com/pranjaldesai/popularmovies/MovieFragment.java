package com.pranjaldesai.popularmovies;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements MovieAdaptor.GridItemClickListener{

    private ArrayList<Result> popularMovie, topRatedMovie;
    private ArrayList<String> popularImageURLString, topRatedImageURLString, popularTitleString, topRatedTitleString;
    private final String beginImage= "http://image.tmdb.org/t/p/w500/";
    private static final String mThemeName= "pref_theme_selection";
    private String mData="pop";
    private MovieAdaptor movieAdaptor;
    private RecyclerView mRecylerView;
    private View mView;
    private Activity activity;
    private SharedPreferences mPreferences;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView popularTextView, topRatedTextView, apiFailTextView;
    private ImageView checkPopImg, checkTopImg;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView= inflater.inflate(R.layout.fragment_movie, container, false);
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        buildImagesAndTitle();
        init(mView);
        return mView;
    }

    /*
    *   Builds Title and Images ArrayList
    */
    private void buildImagesAndTitle() {
        popularImageURLString = new ArrayList<>();
        topRatedImageURLString = new ArrayList<>();
        popularTitleString = new ArrayList<>();
        topRatedTitleString = new ArrayList<>();
        if (popularMovie != null && topRatedMovie != null){
            for (Result result : popularMovie) {
                popularImageURLString.add(beginImage + result.getPoster_path());
                popularTitleString.add(result.getTitle());
            }
            for (Result result : topRatedMovie) {
                topRatedImageURLString.add(beginImage + result.getPoster_path());
                topRatedTitleString.add(result.getTitle());
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        buildImagesAndTitle();
        updateRecyclerView(mData);

    }

    /*
    *   Initializes the views
    */
    private void init(View view){
        checkPopImg= (ImageView) view.findViewById(R.id.popimg);
        checkTopImg= (ImageView) view.findViewById(R.id.topimg);
        popularTextView= (TextView) view.findViewById(R.id.popradio);
        topRatedTextView= (TextView) view.findViewById(R.id.topradio);
        apiFailTextView= (TextView) view.findViewById(R.id.apiFailTV);

        mRecylerView = (RecyclerView) view.findViewById(R.id.rv_movie_view);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getContext(), 3);
        mRecylerView.setLayoutManager(gridLayoutManager);
        mRecylerView.setHasFixedSize(true);

        if(getActivity()!=null) {
            mPreferences = getActivity().getSharedPreferences(mThemeName, Context.MODE_PRIVATE);
            mData= mPreferences.getString(getString(R.string.sort), "");
        }

        if(mData.equalsIgnoreCase(getString(R.string.sort_pop))) {
            checkTopImg.setVisibility(View.GONE);
            checkPopImg.setVisibility(View.VISIBLE);
            movieAdaptor = new MovieAdaptor(this, popularTitleString, popularImageURLString);
        }else if (mData.equalsIgnoreCase(getString(R.string.sort_top))){
            checkTopImg.setVisibility(View.VISIBLE);
            checkPopImg.setVisibility(View.GONE);
            movieAdaptor = new MovieAdaptor(this, topRatedTitleString, topRatedImageURLString);
        }

        mRecylerView.setAdapter(movieAdaptor);

        if(popularImageURLString.isEmpty() && popularTitleString.isEmpty()
                && topRatedTitleString.isEmpty() && topRatedImageURLString.isEmpty()){
            apiFailTextView.setVisibility(View.VISIBLE);
        }else{
            apiFailTextView.setVisibility(View.GONE);
        }

        View bottomSheet= view.findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior= BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(0);

        listener();

    }

    /*
    *   Listener to listen for button clicks for sort
    */
    private void listener(){
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setPeekHeight(0);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        popularTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor= mPreferences.edit();
                editor.putString(getString(R.string.sort),getString(R.string.sort_pop));
                editor.apply();
                mData=getString(R.string.sort_pop);
                checkTopImg.setVisibility(View.GONE);
                checkPopImg.setVisibility(View.VISIBLE);
                updateRecyclerView(mData);
            }
        });
        topRatedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor= mPreferences.edit();
                editor.putString(getString(R.string.sort),getString(R.string.sort_top));
                editor.apply();
                mData=getString(R.string.sort_top);
                checkTopImg.setVisibility(View.VISIBLE);
                checkPopImg.setVisibility(View.GONE);
                updateRecyclerView(mData);
            }
        });


    }

    /*
    *   Updates the Recycler View during orientation change or sort change.
    */
    private void updateRecyclerView(String isPopular){
        if(isPopular.equalsIgnoreCase(getString(R.string.sort_pop))){
            movieAdaptor.removeData();
            movieAdaptor.updateList(popularTitleString,popularImageURLString);
            mBottomSheetBehavior.setPeekHeight(0);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else{
            movieAdaptor.removeData();
            movieAdaptor.updateList(topRatedTitleString,topRatedImageURLString);
            mBottomSheetBehavior.setPeekHeight(0);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity()!=null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_movies_fragment);
            activity = getActivity();
        }

    }

    /*
    *   Sets the arguments from MainActivity
    */
    public void setArguments(ArrayList<Result> result, ArrayList<Result> result2){
        this.popularMovie = result;
        this.topRatedMovie = result2;
    }

    /*
    *   OnItemClick pass the object to new activity
    */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent= new Intent(getActivity(), DetailsActivity.class);
        if(mData.equalsIgnoreCase(getString(R.string.sort_pop))) {
            intent.putExtra(getString(R.string.detailIntent),popularMovie.get(clickedItemIndex));
        }else{
            intent.putExtra(getString(R.string.detailIntent),topRatedMovie.get(clickedItemIndex));
        }
        startActivity(intent);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(activity!=null) {
            activity.getMenuInflater().inflate(R.menu.main, menu);
        }
        super.onCreateOptionsMenu(menu,inflater);
    }

    /*
    *   onClick of the settings icon bottom sheet appear or it collapse
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if(mBottomSheetBehavior.getPeekHeight()==0){
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                mBottomSheetBehavior.setPeekHeight(2);
            }else{
                mBottomSheetBehavior.setPeekHeight(0);
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
