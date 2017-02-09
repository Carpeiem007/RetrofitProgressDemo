package com.guo.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;


import com.guo.library.R;
import com.guo.library.circularProgress.CircularProgressView;


/**
 * Created by yesudooDevK on 2016/5/19.
 */
public class LoadingDialog extends Dialog {

    public final static String LOADING = "正在拼命加载中";
    public final static String UPDATE = "正在拼命上传中";


    private CircularProgressView ivLoading;
    private TextView tvContent = null;

    private String message = LOADING;

    private float max = 1.0f;

    private boolean flag = true;

    public LoadingDialog(Context context) {
        this(context, R.style.dialog);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_loading_layout);
        ivLoading = (CircularProgressView) findViewById(R.id.cpv_loading);
        tvContent = (TextView) findViewById(R.id.tv_content);
        ivLoading.setColor(Color.parseColor("#03b58c"));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(-1, -1);
        getWindow().setGravity(Gravity.CENTER);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
        tvContent.setText(!TextUtils.isEmpty(message) ? message : LOADING);
        if (flag) {
            ivLoading.setIndeterminate(true);
            ivLoading.startAnimation();

        } else {
            ivLoading.setIndeterminate(false);
        }

    }


    @Override
    public void show() {
        super.show();

    }

    @Override
    public void dismiss() {
        super.dismiss();
        ivLoading.stopAnimation();
    }


    public void setProgress(int progress) {

        tvContent.setText(UPDATE);
        ivLoading.setProgress(progress);
    }


    public void setMessage(String message) {
        this.message = message;
    }


}
