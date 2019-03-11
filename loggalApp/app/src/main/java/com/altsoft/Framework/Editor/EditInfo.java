package com.altsoft.Framework.Editor;

import android.graphics.Bitmap;

import com.altsoft.Framework.Global;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.io.File;

public class EditInfo {
    public void SetCircleImage(android.widget.ImageView view, String src)
    {
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

        if(Global.getValidityCheck().isEmpty(src))
        {
            src = null;
            Picasso.with(Global.getCurrentActivity()).load(src).rotate(0).transform(new CircleTransform()).into(view);
        }
        else if(src.indexOf("/storage/emulated/0") >=0)
        {Picasso.with(Global.getCurrentActivity()).load(new File(src)).rotate(0).transform(new CircleTransform()).into(view);}
        else
            Picasso.with(Global.getCurrentActivity()).load(src).rotate(0).transform(new CircleTransform()).into(view);
}

    public void SetCircleImageBmp(android.widget.ImageView view, Bitmap src)
    {
        Picasso.with(Global.getCurrentActivity()).load(Global.getCommon().getImageUriString(Global.getCurrentActivity(),src)).rotate(0).transform(new CircleTransform()).into(view);
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
