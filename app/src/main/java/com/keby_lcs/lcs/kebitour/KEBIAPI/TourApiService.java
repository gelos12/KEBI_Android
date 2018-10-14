package com.keby_lcs.lcs.kebitour.KEBIAPI;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by LCS on 2017-11-11.
 */

public interface TourApiService {
    @FormUrlEncoded
    @POST("/o/token/")//로그인하기
    Call<Token> login(
            @Field("username") String username,
            @Field("password") String password,
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("grant_type") String grant_type
    );

    //회원가입하기
    @FormUrlEncoded
    @POST("user/sign_up/")//회원가입하기
    Call<ResponseResult> signupGo(
            @Field("username") String username,
            @Field("password") String password
    );


    //유저 pk가져오기
    @GET("user/users/")
    Call<ResponseBody> getUserPktest(
            @Query("username") String username
    );

    //제보
    @FormUrlEncoded
    @POST("tour/post/")
    Call<ResponsePost> postCreate(
            @Field("author") int author,
            @Field("title") String title,
            @Field("contentid") String contentid
    );

    //제보 리스트
    @GET("tour/post/")
    Call<ResponseBody> postGet(
            @Query("contentid") String contentid
    );

    //댓글 가져오기
    @GET("tour/comment/")
    Call<ResponseBody> commentGet(
            @Query("post") int post
    );

    @FormUrlEncoded
    @POST("tour/comment/")
    Call<ResponseBody> commentPost(
            @Field("author") int author,
            @Field("post") int post,
            @Field("msg") String msg
    );

    //댓글달기
    @Multipart
    @POST("tour/comment/")
    Call<ResponseBody> commentPostImage(
            @Part("author") RequestBody author,
            @Part("post") RequestBody post,
            @Part("msg") RequestBody msg,
            @Part MultipartBody.Part image
    );

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://52.78.16.95:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
