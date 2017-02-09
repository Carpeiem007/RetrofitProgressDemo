package com.guo.library.retrofit;

import retrofit2.Callback;

/**
 * Created by guoqiang on 16/11/24.
 */

public abstract class RetrofitCallBack<T> implements Callback<T> {

    private long total;
    private long cureent;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCureent() {
        return cureent;
    }

    public void setCureent(long cureent) {
        this.cureent = cureent;
    }





    public void onLoading(long total, long progress) {
    }

}
