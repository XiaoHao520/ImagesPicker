package com.test.testCos;

import android.content.Context;
import android.util.Log;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.CosXmlResultListener;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectResult;
import com.tencent.cos.xml.sign.CosXmlCredentialProvider;
import com.tencent.cos.xml.sign.CosXmlLocalCredentialProvider;
import com.tencent.qcloud.network.QCloudProgressListener;
import com.tencent.qcloud.network.exception.QCloudException;

import java.io.File;

/**
 * Created by xiaohao on 17-10-9.
 */

public class MyCos {
    String appid = "1252119503";
    String region = "ap-guangzhou";
    String secretId = "AKIDiScbQ4eoxuhIiwmrY6gVBg097uWwsF8p";
    String secretKey = "f9TcnncGj085qQp3792mP7mGZs8FHBtQ";
    long keyDuration = 600; //secretKey的有效时间,单位秒
    private Context context;

    CosXmlService cosXmlService;


    public MyCos(Context context) {
        this.context = context;
        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig(appid, region);

//创建获取签名类
        CosXmlCredentialProvider cosXmlCredentialProvider = new CosXmlLocalCredentialProvider(secretId, secretKey, keyDuration);

//创建 CosXmlService 对象，实现对象存储服务各项操作.
        cosXmlService = new CosXmlService(context, cosXmlServiceConfig, cosXmlCredentialProvider);
    }

    public void fileUpload(String filepath) throws QCloudException {
        File file = new File(filepath);
        String bucket = "test";
        String cosPath = "/"+file.getName(); //格式如 cosPath = "/test.txt";
        // 如 srcPath = Environment.getExternalStorageDirectory().getPath() + "/test.txt";
        long signDuration = 600; //签名的有效期，单位为秒

        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(bucket);
        putObjectRequest.setCosPath(cosPath);
        putObjectRequest.setSrcPath(filepath);
        putObjectRequest.setSign(signDuration, null, null);

//设置进度显示
        putObjectRequest.setProgressListener(new QCloudProgressListener() {
            @Override
            public void onProgress(long progress, long max) {
                float result = (float) (progress * 100.0 / max);
                Log.w("TEST", "progress =" + (long) result + "%");
            }
        });
        final PutObjectResult putObjectResult = cosXmlService.putObject(putObjectRequest);


//使用同步方法上传


        //上传失败， 返回httpCode 不在【200，300）之内；
        if (putObjectResult.getHttpCode() >= 300 || putObjectResult.getHttpCode() < 200) {
            StringBuilder stringBuilder = new StringBuilder("Error\n");
            stringBuilder.append(putObjectResult.error.code)
                    .append(putObjectResult.error.message)
                    .append(putObjectResult.error.resource)
                    .append(putObjectResult.error.requestId)
                    .append(putObjectResult.error.traceId);
            Log.w("TEST", stringBuilder.toString());
        }

        //上传成功
        if (putObjectResult.getHttpCode() >= 200 && putObjectResult.getHttpCode() < 300) {
            //上传成功后，则可以拼接访问此文件的地址，格式为：bucket-appid.region.myqcloud.com.cosPath;

            Log.w("TEST", "accessUrl =" + putObjectResult.accessUrl);
        }


        /*  cosXmlService.putObjectAsync(putObjectRequest, new CosXmlResultListener() {
           @Override
           public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
               System.out.println("成功");
           }

           @Override
           public void onFail(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {

               System.out.println("失败");
           }
       });*/
    }
}


