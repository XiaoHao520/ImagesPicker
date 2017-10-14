package com.test.imagepicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.test.R;
import com.tools.ImagePicker;

import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class ImagePickerActivity extends AppCompatActivity {

    private Button openAlbum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_picker);

        initView();


    }

    private void initAlbum() {
        ThemeConfig theme = new ThemeConfig.Builder().build();

        FunctionConfig functionConfig = new FunctionConfig.Builder().setEnableCamera(true)
                .setEnableEdit(true).setEnableCrop(true)
                .setEnableRotate(true).build();
        ImageLoader imageLoader = new Loader();
        CoreConfig coreConfig = new CoreConfig.Builder(getApplicationContext(), imageLoader, theme).setDebug(true).setFunctionConfig(functionConfig).build();
        GalleryFinal.init(coreConfig);

        //多张图片选择
        GalleryFinal.openGalleryMuti(1, 9, new ResultHandler());
        //
        GalleryFinal.openGallerySingle(1, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }

    private void initView() {
        openAlbum = (Button) findViewById(R.id.openAlbum);
        openAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemeConfig theme = new ThemeConfig.Builder().build();

                FunctionConfig functionConfig = new FunctionConfig.Builder().setEnableCamera(true)
                        .setEnableEdit(true).setEnableCrop(true)
                        .setEnableRotate(true).build();

                ImagePicker imagePicker = new ImagePicker(getApplicationContext(), theme, functionConfig);
                imagePicker.getPhotoMuti();

                //initAlbum();
            }
        });

    }

    public class ResultHandler implements GalleryFinal.OnHanlderResultCallback {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {

        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    }

}
