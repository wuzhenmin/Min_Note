package com.example.zhenmin.min_note.util;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by zhenmin on 2015/11/19.
 */
public class CustomAnim extends Animation {

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        t.getMatrix().setTranslate((float)(Math.sin(interpolatedTime*2000)*10),0);
        super.applyTransformation(interpolatedTime, t);
    }
}
