package com.pranjaldesai.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Pranjal on 8/11/17.
 */

public class Movies implements Serializable{

    @SerializedName("page")
    private int page;

    @SerializedName("total_results")
    private int total_results;

    @SerializedName("total_pages")
    private int total_pages;

    @SerializedName("results")
    private ArrayList<Result> result= new ArrayList<>();

    public ArrayList getResult(){
        return result;
    }

    public int getPage(){
        return page;
    }

    public int getTotal_results(){
        return total_results;
    }

    public int getTotal_pages(){
        return total_pages;
    }


}
