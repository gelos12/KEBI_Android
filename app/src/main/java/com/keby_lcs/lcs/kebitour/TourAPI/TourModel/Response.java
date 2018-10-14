package com.keby_lcs.lcs.kebitour.TourAPI.TourModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LCS on 2017-11-10.
 */

public class Response {
    @SerializedName("body")
    @Expose
    private Body body;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

}