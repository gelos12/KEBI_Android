package com.keby_lcs.lcs.kebitour.TourAPI;


import com.keby_lcs.lcs.kebitour.TourAPI.TourModel.ResponseTour;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by LCS on 2017-11-10.
 */

public interface ApiService {
    @GET("{operation}?ServiceKey=Im%2BetJbsR1gv2RSIDO5y8ehBZPNMV0ZxZiws3nTVFNwQkez04gHgOI83LDadJoJZnj%2FTHzEAeOv3cnxzGn5Bfw%3D%3D")
    Call<ResponseTour> getList(
            @Path("operation") String operation,
            @Query("contentTypeid") int contentTypeid,
            @Query("numOfRows") int numOfRows,
            @Query("pageNo") int pageNo,
            @Query("MobileOS") String MobileOS,
            @Query("MobileApp") String MobileApp,
            @Query("_type") String _type
    );

    //type 25인 경우 공토정보로
    @GET("{operation}?ServiceKey=Im%2BetJbsR1gv2RSIDO5y8ehBZPNMV0ZxZiws3nTVFNwQkez04gHgOI83LDadJoJZnj%2FTHzEAeOv3cnxzGn5Bfw%3D%3D")
    Call<ResponseBody> getDetail(
            @Path("operation") String operation,
            @Query("contentId") int contentId,
            @Query("defaultYN") String defaultYN,
            @Query("overviewYN") String overviewYN,
            @Query("addrinfoYN") String addrinfoYN,
            @Query("MobileOS") String MobileOS,
            @Query("MobileApp") String MobileApp,
            @Query("_type") String _type
    );

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.visitkorea.or.kr/openapi/service/rest/KorService/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
