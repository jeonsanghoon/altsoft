package com.altsoft.Framework.Editor;

import android.graphics.Bitmap;

import com.altsoft.Framework.Global;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class EditInfo {
    public void SetCirImage(android.widget.ImageView view, String src)
    {
        RequestOptions requestOptions  =  new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(100, 100)
                .circleCropTransform();
        Glide.with(Global.getCurrentActivity())
                .load(src)
                .apply(requestOptions)
                // .apply(RequestOptions.circleCropTransform())
                .into(view);
    }

    public void SetCirImageBmp(android.widget.ImageView view, Bitmap src)
    {
        RequestOptions requestOptions  =  new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(100, 100)
                .circleCropTransform();
        Glide.with(Global.getCurrentActivity())
                .load(src)
                .apply(requestOptions)
                // .apply(RequestOptions.circleCropTransform())
                .into(view);
    }
}
