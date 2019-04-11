package com.vaio.p2.appstreetsubmission.Network;



import com.vaio.p2.appstreetsubmission.Network.Response.SearchResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface Api
{
    @GET("/search/photos")
    Call<SearchResponse> getSearchResult(@Query("client_id") String client_id,
                                        @Query("query") String query,
                                        @Query("page") int page);


}
