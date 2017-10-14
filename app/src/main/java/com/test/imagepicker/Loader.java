package com.test.imagepicker;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.test.R;

import java.io.File;

import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by xiaohao on 17-10-14.
 */

public class Loader implements ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        Picasso.with(activity).load(Uri.fromFile(new File(path)))
                .placeholder(R.mipmap.default_image).error(R.mipmap.default_image)
                .resize(width,height).centerInside().memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
