package com.yohannes.app.dev.newsapp.net;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Yohannes on 30-Mar-20.
 */

public interface RequestInterface {
    @GET("news/index.php")
    Call<JSONResponse> getJSON();
}
