package com.example.zhenmin.min_note.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

/**
 * Created by zhenmin on 2016/1/21.
 */
public class BitmapOption {

    public static Bitmap getImageThumbnail(String uri,int width,int height){
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth/width;
        int beHeight = options.outHeight/height;
        int be = 1;
        if (beWidth<beHeight){
            be = beWidth;
        }else{
            be = beHeight;
        }
        if (be<0){
            be = 1 ;
        }
        options.inSampleSize = be;
        bitmap  = BitmapFactory.decodeFile(uri, options);
        bitmap  = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public static Bitmap getVideoThumnail(String uri,int width,int height,int kind){
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri,kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

}
