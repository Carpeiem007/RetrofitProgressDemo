package com.guo.retrofitprogressdemo.widget.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.guo.library.dialog.LoadingDialog;
import com.guo.library.retrofit.FileRequestBody;
import com.guo.library.retrofit.RetrofitCallBack;
import com.guo.library.retrofit.RetrofitUtils;
import com.guo.retrofitprogressdemo.R;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * Created by guoqiang on 17/2/9.
 */

public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private LoadingDialog dialog = null;
    private MultipleImageView miv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        miv = new MultipleImageView(this, recyclerView);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (miv.getImages().isEmpty()) {
                    Snackbar.make(getWindow().getDecorView(), "请选择图片", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                dialog = new LoadingDialog(MainActivity.this);
                LinkedHashMap<String, RequestBody> params = new LinkedHashMap<String, RequestBody>();
                RetrofitCallBack<ResponseBody> call = new RetrofitCallBack<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (dialog.isShowing())
                            dialog.dismiss();
                        try {
                            String message = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.i("info", t.getLocalizedMessage());
                        if (dialog.isShowing())
                            dialog.dismiss();
                    }

                    @Override
                    public void onLoading(long total, long progress) {
                        super.onLoading(total, progress);
                        if (dialog != null && dialog.isShowing()) {
                            float m = (progress / (float) total);
                            int pro = (int) (m * 100);
                            dialog.setProgress(pro);
                        }
                    }
                };
                for (File f : miv.getImages()) {
                    //bar_image[]为后台接收时的定义的字段
                    params.put("bar_image[]\"; filename=\"" + f.getName(), new FileRequestBody<>(RequestBody.create(MediaType.parse("image/*"), f), call));
                }
                //取得发送体的总大小
                //一定要在放完所有参数之后再获得大小
                call.setTotal(RetrofitUtils.getContentLength(params));
                //创建临时的retrofit对象
                Retrofit ft = new Retrofit.Builder().baseUrl("http://www.baidu.com/").build();
                UrlServices services = ft.create(UrlServices.class);
                //发送请求
                if (dialog != null && !dialog.isShowing())
                    dialog.show();
                services.UpImage(params).enqueue(call);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null){
            dialog.dismiss();
        }
    }
}
