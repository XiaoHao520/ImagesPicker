package com.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.tencent.qcloud.network.exception.QCloudException;
import com.test.ninegrid.Image;
import com.test.ninegrid.NineGridlayout;
import com.test.testCos.MyCos;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private static final int IMAGE_PICKER = 681;
    private WebView webView;
    private Button refrsh;
    private static ArrayList<Image> imageList = new ArrayList<>();

    private static ArrayList<String> imagesPath=new ArrayList<>();
    private static ArrayList<ImageItem> images;
    private NineGridlayout nineGridlayout;
    GridViewAdapter adapter;
    private GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nineGridlayout = (NineGridlayout) findViewById(R.id.ninegrid);
        webView = (WebView) findViewById(R.id.web);
        refrsh = (Button) findViewById(R.id.refresh);
        refrsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });
        gridView= (GridView) findViewById(R.id.gridview);
        adapter=new GridViewAdapter(MainActivity.this,imagesPath);
        gridView.setAdapter(adapter);
        class JsHook {

            @JavascriptInterface
            public void javaMethod(String p) {
                ImagePicker imagepicker = ImagePicker.getInstance();
                imagepicker.setImageLoader(new ImagePickers());
                imagepicker.setShowCamera(true);
                imagepicker.setSaveRectangle(true);

                imagepicker.setSelectLimit(9);
                imagepicker.setStyle(CropImageView.Style.RECTANGLE);
                imagepicker.setFocusWidth(800);
                imagepicker.setFocusHeight(800);
                imagepicker.setOutPutX(1000);

                imagepicker.setOutPutY(1000);


                System.out.println("JsHook.javaMethod:" + p);

                Intent intent = new Intent(MainActivity.this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
            }

            @JavascriptInterface
            public void showAndroid() {
                final String info = "来自手机内的内容";

                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl("javascript:show('" + info + "')");
                    }
                });


            }

            @JavascriptInterface
            public String getInfo() {
                return "获取手机内的信息";
            }

        }

        WebSettings settings = webView.getSettings();

        webView.loadUrl("http://192.168.1.205/public/index.php/news/index/test");
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JsHook(), "hello");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.d("mainActivity", "url:" + request.getUrl());
                webView.loadUrl(request.getUrl().getPath());
                return true;
            }
        });

        //webView.loadUrl("http://192.168.1.205/public/testjqm.html");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //      imageList=new ArrayList<>();
        if (IMAGE_PICKER == requestCode) {

            if (data != null && requestCode == IMAGE_PICKER) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (final ImageItem item : images) {
                  //  imageList.add(new Image("file://" + item.path, 250, 250));
                    imagesPath.add(item.path);
                   /* final MyCos myCos=new MyCos(getApplicationContext());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                myCos.fileUpload(item.path);
                            } catch (QCloudException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();*/

                }
              //  System.out.println(imageList.size());



                adapter.notifyDataSetChanged();

              // nineGridlayout.setImagesData(imageList);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
