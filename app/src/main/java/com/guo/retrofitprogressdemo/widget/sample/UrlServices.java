package com.guo.retrofitprogressdemo.widget.sample;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by guoqiang on 17/2/8.
 */

public interface UrlServices {


    @Multipart
    @POST("api")
    Call<ResponseBody> UpImage(@PartMap Map<String, RequestBody> params);
}
