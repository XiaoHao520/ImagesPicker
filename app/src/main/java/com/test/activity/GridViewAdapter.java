package com.test.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.test.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by xiaohao on 17-10-9.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> images;

    public GridViewAdapter(Context context, ArrayList<String> images) {
        System.out.println("适配器");
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        System.out.println("getview");
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.gridview_item, null);
        ImageView imageView = linearLayout.findViewById(R.id.gridviewImage);
        File file = new File(images.get(i));
        if (file.exists()) {
            System.out.println("文件存在");
            //    imageView.setImageResource(R.drawable.selector_grid_camera_bg);
          imageView.setImageBitmap(BitmapFactory.decodeFile(images.get(i)));


            return imageView;
        } else {
            System.out.println(images.get(i)+"文件不存在");
        }


        return null;
    }
}
