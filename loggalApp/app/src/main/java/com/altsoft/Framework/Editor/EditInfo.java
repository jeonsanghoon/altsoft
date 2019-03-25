package com.altsoft.Framework.Editor;

import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.util.Log;

import com.altsoft.Framework.Global;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class EditInfo {
    public void SetCircleImage(android.widget.ImageView view, String src) {
        /*RequestOptions requestOptions  =  new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(100, 100)
                .circleCropTransform();
        Glide.with(Global.getCurrentActivity())
                .load(src)
                .apply(requestOptions)
                // .apply(RequestOptions.circleCropTransform())
                .into(view);
                */

        if (Global.getValidityCheck().isEmpty(src)) {
            src = null;
            Picasso.with(Global.getCurrentActivity()).load(src).resize(100,100).transform(new CircleTransform()).rotate(0).into(view);
        } else {
            int rotate =  0;//360-getCameraPhotoOrientation(src);
            if (src.indexOf("/storage/emulated/0") >= 0) {
                Picasso.with(Global.getCurrentActivity()).load(new File(src)).resize(100,100).transform(new CircleTransform()).rotate(rotate).into(view);
            } else
                Picasso.with(Global.getCurrentActivity()).load(src).resize(100,100).transform(new CircleTransform()).rotate(90).into(view);
        }
    }

    public void SetCircleImageBmp(android.widget.ImageView view, Bitmap src)
    {
        Picasso.with(Global.getCurrentActivity()).load(Global.getCommon().getImageUriString(Global.getCurrentActivity(),src)).transform(new CircleTransform()).rotate(0).into(view);
    /*    RequestOptions requestOptions  =  new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(100, 100)
                .circleCropTransform();
        Glide.with(Global.getCurrentActivity())
                .load(src)
                .apply(requestOptions)
                // .apply(RequestOptions.circleCropTransform())
                .into(view);*/
    }


}
