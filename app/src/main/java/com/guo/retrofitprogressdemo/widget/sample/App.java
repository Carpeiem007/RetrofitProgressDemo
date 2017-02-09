package com.guo.retrofitprogressdemo.widget.sample;

import android.app.Application;

import com.guo.retrofitprogressdemo.R;

import java.io.File;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * Created by guoqiang on 17/2/9.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initPickerTheme();

    }

    private void initPickerTheme() {
        ThemeConfig theme = new ThemeConfig.Builder().build();
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnableRotate(false)
                .setForceCropEdit(true)
                .setEnablePreview(true).build();
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), new GlideImageLoader(), theme).setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);
    }
}
