package com.keby_lcs.lcs.kebitour.KEBIAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LCS on 2017-11-11.
 */

public class ResponseUserPk {
    @SerializedName("pk")
    @Expose
    public Integer pk;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("groups")
    @Expose
    public List<Object> groups = null;
}
