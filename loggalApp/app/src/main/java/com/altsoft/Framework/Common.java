package com.altsoft.Framework;

import android.app.Activity;
import android.widget.ProgressBar;

import com.altsoft.loggalapp.R;

public class Common {
    public void ProgressHide(Activity activity) {
       ProgressBar progress=  activity.findViewById(R.id.progress);
        progress.setVisibility((int)4);


    }
    public void ProgressShow(Activity activity) {
        ProgressBar progress=  activity.findViewById(R.id.progress);
        progress.setVisibility((int)0);
    }
}
