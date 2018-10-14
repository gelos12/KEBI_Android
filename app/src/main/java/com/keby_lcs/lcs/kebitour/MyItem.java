package com.keby_lcs.lcs.kebitour;

import java.io.Serializable;

/**
 * Created by LCS on 2017-11-09.
 */

public class MyItem implements Serializable {
    private String title;
    private String image;
    private int contentid;

    public int getContentid() {
        return contentid;
    }



    public void setContentid(int contentid) {
        this.contentid = contentid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}