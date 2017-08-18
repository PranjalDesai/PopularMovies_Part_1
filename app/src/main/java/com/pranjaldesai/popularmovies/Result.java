package com.pranjaldesai.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Pranjal on 8/11/17.
 */

public class Result implements Serializable {

    @SerializedName("vote_count")
    private int vote_count;

    @SerializedName("id")
    private int id;

    @SerializedName("video")
    private boolean video;

    @SerializedName("vote_average")
    private double vote_average;

    @SerializedName("title")
    private String title;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("poster_path")
    private String poster_path;

    @SerializedName("original_language")
    private String original_language;

    @SerializedName("original_title")
    private String original_title;

    @SerializedName("backdrop_path")
    private String backdrop_path;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    private String release_date;

    public String getTitle(){
        return title;
    }

    public int getVote_count(){
        return vote_count;
    }

    public int getId(){
        return id;
    }

    public boolean isVideo(){
        return video;
    }

    public double getVote_average(){
        return vote_average;
    }

    public double getPopularity(){
        return popularity;
    }

    public String getPoster_path(){
        return poster_path;
    }

    public String getOriginal_language(){
        return original_language;
    }

    public String getOriginal_title(){
        return original_title;
    }

    public String getBackdrop_path(){
        return backdrop_path;
    }

    public boolean isAdult(){
        return adult;
    }

    public String getOverview(){
        return overview;
    }

    public String getRelease_date(){
        return release_date;
    }
}
