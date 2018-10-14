package com.keby_lcs.lcs.kebitour;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LCS on 2017-11-12.
 */

public class CommentItem {
    @SerializedName("pk")
    @Expose
    public Integer pk;
    @SerializedName("post")
    @Expose
    public Integer post;
    @SerializedName("author")
    @Expose
    public Integer author;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    public Integer getPk() {
        return pk;
    }

    public Integer getPost() {
        return post;
    }

    public Integer getAuthor() {
        return author;
    }

    public String getMsg() {
        return msg;
    }

    public String getImage() {
        return image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public void setPost(Integer post) {
        this.post = post;
    }

    public void setAuthor(Integer author) {
        this.author = author;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
