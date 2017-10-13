package com.test.activity;

import android.content.Intent;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.tencent.qcloud.network.exception.QCloudException;
import com.test.*;
import com.test.upload.MyCos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebActivity extends AppCompatActivity {

    private static final int IMAGE_PICKER = 681;
    private WebView notesWebView;
    private static String url = "http://39.108.178.40/study/public/index.php/mobile/index";
    private static Map<String,String> imagesMap = new ArrayMap<String, String>();
     private static String json=null;
    private static List<Map<String,String>> imagesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        initView();
        initWebViewSetting();
    }

    private void initWebViewSetting() {
        WebSettings ws = notesWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        notesWebView.loadUrl(url);
        notesWebView.addJavascriptInterface(new Js(), "imagePicker");
        notesWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });

    }


    private void initView() {
        notesWebView = (WebView) findViewById(R.id.note_webView);

    }


    class Js {
        @JavascriptInterface
        public void openAlbum(String code) {
            if (code.equals("刷新")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                     notesWebView.loadUrl("file:///android_asset/add.html");
                       // notesWebView.loadUrl("http://39.108.178.40/study/public/static/add.html");
                    }
                });

            } else if (code.equals("openAlbum")) {

                ImagePicker imagepicker = ImagePicker.getInstance();
                imagepicker.setImageLoader(new com.test.ImagePickers());
                imagepicker.setShowCamera(true);
                imagepicker.setSaveRectangle(true);
                imagepicker.setSelectLimit(9);
                imagepicker.setStyle(CropImageView.Style.RECTANGLE);
                imagepicker.setFocusWidth(800);
                imagepicker.setFocusHeight(800);
                imagepicker.setOutPutX(1000);
                imagepicker.setOutPutY(1000);

                Intent intent = new Intent(WebActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);


            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       notesWebView.loadUrl("file:///android_asset/add.html");
                      //notesWebView.loadUrl("http://39.108.178.40/study/public/static/add.html");
                    }
                });
            }
        }


        @JavascriptInterface
        public void send() throws QCloudException {
            MyCos cos=new MyCos(getApplicationContext());
            System.out.println("this is send");
            for (Map<String,String> map:imagesList) {
                System.out.println(map.get("img"));
                cos.fileUpload(map.get("img"));

            }


        }

        @JavascriptInterface
        public void delImage(String num){

            System.out.println("删除图片:"+num);
            imagesList.remove(Integer.parseInt(num));



        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (IMAGE_PICKER == requestCode) {

            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

             imagesList=new ArrayList<Map<String, String>>();
                for (int i = 0; i < images.size(); i++) {

                    Map<String,String> map=new HashMap<>();
                    map.put("img",images.get(i).path);
                    imagesList.add(map);

                }
               json= JSON.toJSONString(imagesList);

                System.out.println(json);


            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("暂停");

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("恢复");
        notesWebView.loadUrl("javascript:appendImage('" + json+ "')");
    }
}
