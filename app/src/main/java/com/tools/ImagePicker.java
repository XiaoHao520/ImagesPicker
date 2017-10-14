package com.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.test.R;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by xiaohao on 17-10-14.
 */

public class ImagePicker {
    private Context context;
    private static ThemeConfig theme;
    private static FunctionConfig functionConfig;
    private static CoreConfig coreConfig;
    private static String singlePath = null;
    private static List<PhotoInfo> photoInfoList;

    public ImagePicker(Context context, ThemeConfig theme, FunctionConfig functionConfig) {
        this.context = context;
        this.theme = theme;
        this.functionConfig = functionConfig;
        coreConfig = new CoreConfig.Builder(context, new MyLoader(), theme).setDebug(true).setFunctionConfig(functionConfig).build();
        getPhotoMuti();
    }

    public String getPhotoSingle() {

        GalleryFinal.init(coreConfig);
        GalleryFinal.openGallerySingle(1, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

                ImagePicker.singlePath = resultList.get(0).getPhotoPath();

            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
        return null;
    }

    public List<PhotoInfo> getPhotoMuti() {
        GalleryFinal.init(coreConfig);


        ResultData rs=new ResultData();
        GalleryFinal.openGalleryMuti(1, 9, new ImageResultHandler(rs));
        if(rs.getList()==null){
            System.out.println("------------------------空的");
        }else {
            System.out.println("取到图片"+rs.getList().get(0).getPhotoPath());
        }

        return null;
    }











    private class MyLoader implements ImageLoader {

        @Override
        public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
            Picasso.with(activity).load(Uri.fromFile(new File(path)))
                    .placeholder(R.mipmap.default_image).error(R.mipmap.default_image)
                    .resize(width, height).centerInside().memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(imageView);
        }

        @Override
        public void clearMemoryCache() {

        }
    }





    class ResultData{
        private List<PhotoInfo>list;

        public List<PhotoInfo> getList() {
            return list;
        }

        public void setList(List<PhotoInfo> list) {
            this.list = list;
        }
    }



    private class ImageResultHandler implements GalleryFinal.OnHanlderResultCallback {
      final private ResultData rs;

        public ImageResultHandler(ResultData rs) {
            this.rs = rs;
        }

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

            rs.setList(resultList);

        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    }


}
