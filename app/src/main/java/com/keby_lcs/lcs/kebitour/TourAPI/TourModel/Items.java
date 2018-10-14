package com.keby_lcs.lcs.kebitour.TourAPI.TourModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by LCS on 2017-11-10.
 */

public class Items {

    @SerializedName("item")
    @Expose
    private ArrayList<Item> item = null;

    public ArrayList<Item> getItem() {
        return item;
    }

    public void setItem(ArrayList<Item> item) {
        this.item = item;
    }

}