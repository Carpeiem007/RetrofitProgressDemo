package com.guo.retrofitprogressdemo.widget.sample;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.guo.retrofitprogressdemo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import kr.co.namee.permissiongen.PermissionGen;


/**
 * Created by yesudooDevK on 2016/6/21.
 */
public class MultipleImageView implements GalleryFinal.OnHanlderResultCallback {
    //设置Activity的requestCode 只有在同一个Activity使用多次时设置不同来区分 ，只有一个可以不设置
    private int requestCode = 101;

    //设置默认的图片个数
    private int defaultSize = 4;

    private Activity mActivity;

    private ArrayList<PhotoInfo> inofs = new ArrayList<>();

    private RecyclerView recyclerView = null;

    private MyAdapter adapter = null;

    private MyDialog pic = null;

    private PhotoInfo p;

    public boolean isForce = false;

    private HashMap<String, File> files = new HashMap<>();


    public MultipleImageView(Activity mActivity, RecyclerView recyclerView) {
        this.mActivity = mActivity;
        this.recyclerView = recyclerView;
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        this.recyclerView.setLayoutManager(manager);
        p = new PhotoInfo();
        p.setPhotoId(-1);
        inofs.add(p);
        adapter = new MyAdapter();
        this.recyclerView.setAdapter(adapter);

    }

    public boolean isEmpty() {
        if (inofs.contains(p))
            return inofs.size() == 1;
        return inofs.isEmpty();
    }


    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;

    }

    public void setPhotos(ArrayList<String> images) {
        if (images == null)
            throw new RuntimeException("images can't be null");
        PhotoInfo info = null;
        inofs.clear();
        for (int i = 0; i < images.size(); i++) {
            info = new PhotoInfo();
            info.setPhotoId(i + 1);
            info.setPhotoPath(images.get(i));
            inofs.add(info);
        }
        if (inofs.size() < defaultSize) {
            inofs.add(p);
        }
        adapter.notifyDataSetChanged();
    }


    public void onPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(mActivity, requestCode, permissions, grantResults);
    }

    @Override
    public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        inofs.remove(p);
        if (reqeustCode == requestCode) {
//            inofs.clear();
            for (PhotoInfo r : resultList) {
                inofs.add(0, r);
            }
//            inofs.addAll(resultList);
            adapter.notifyDataSetChanged();
            if (inofs.size() < defaultSize) {
                addImage();
            }
        }

    }

    @Override
    public void onHanlderFailure(int requestCode, String errorMsg) {
        Toast.makeText(mActivity, "选择图片失败", Toast.LENGTH_SHORT).show();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        int width = (int) (60 * mActivity.getResources().getDisplayMetrics().density);

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mActivity).inflate(R.layout.image_item_4_recycleview, null, false);
            MyViewHolder viewHolder = new MyViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final int id = inofs.get(position).getPhotoId();
            if (id == -1) {
                holder.sdv.setImageResource(R.drawable.ic_add);
            } else {
                if (inofs.get(position).getPhotoPath().startsWith("http"))
                    Glide.with(mActivity).load(inofs.get(position).getPhotoPath()).placeholder(R.drawable.loading).into(holder.sdv);
                else
                    Glide.with(mActivity).load(new File(inofs.get(position).getPhotoPath())).placeholder(R.drawable.loading).into(holder.sdv);

            }

            holder.sdv.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  if (id == -1) {
                                                      if (pic == null) {
                                                          pic = new MyDialog(mActivity, R.style.dialog);
                                                      }
                                                      pic.show();
                                                  } else {
                                                      inofs.remove(position);
                                                      addImage();
                                                      adapter.notifyDataSetChanged();
                                                  }
                                              }
                                          }

            );


        }

        @Override
        public int getItemCount() {
            return inofs.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView sdv = null;

        public MyViewHolder(View itemView) {
            super(itemView);
            sdv = (ImageView) itemView.findViewById(R.id.sdv_image);

        }
    }

    private class MyDialog extends Dialog implements View.OnClickListener {


        public MyDialog(Context context, int themeResId) {
            super(context, themeResId);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            setContentView(R.layout.dialog_head);
            findViewById(R.id.tv_cancel).setOnClickListener(this);
            findViewById(R.id.tv_photo).setOnClickListener(this);
            findViewById(R.id.tv_camera).setOnClickListener(this);
            Window window = getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(-1, -2);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setWindowAnimations(R.style.mystyle);
        }

        public void setPermission(int requestCode, String[] args, int[] ar) {
            PermissionGen.onRequestPermissionsResult(mActivity, requestCode, args, ar);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_photo:
                    FunctionConfig config = new FunctionConfig.Builder().setForceCrop(isForce).setForceCropEdit(isForce).setMutiSelectMaxSize(defaultSize - inofs.size() + 1).build();
                    GalleryFinal.openGalleryMuti(requestCode, config, MultipleImageView.this);
                    this.dismiss();
                    break;
                case R.id.tv_camera:
                    PermissionGen.needPermission(mActivity, 101, new String[]{android.Manifest.permission.CAMERA});
                    this.dismiss();
                    break;
                case R.id.tv_cancel:
                    this.dismiss();
                    break;
            }
        }

    }


    private void addImage() {
        if (inofs.isEmpty()) {
            inofs.add(p);
        }
        for (PhotoInfo f : inofs) {
            if (f.getPhotoId() == -1) {
                return;
            }
        }
        inofs.add(p);


    }

    public ArrayList<File> getImages() {
        ArrayList<File> files = new ArrayList<>();
        for (PhotoInfo info : inofs) {
            if (info.getPhotoId() == -1) {
            } else if (info.getPhotoPath().startsWith("http://")||info.getPhotoPath().startsWith("https://")) {

            } else {
                File file =new File(info.getPhotoPath());
                files.add(file);
            }
        }
        return files;
    }

}