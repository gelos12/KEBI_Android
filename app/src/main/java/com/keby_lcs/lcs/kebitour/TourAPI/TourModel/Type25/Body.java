package com.keby_lcs.lcs.kebitour.TourAPI.TourModel.Type25;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LCS on 2017-11-11.
 */

public class Body {

    @SerializedName("items")
    @Expose
    public Items items;
    @SerializedName("numOfRows")
    @Expose
    public Integer numOfRows;
    @SerializedName("pageNo")
    @Expose
    public Integer pageNo;
    @SerializedName("totalCount")
    @Expose
    public Integer totalCount;

}