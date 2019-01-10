package com.altsoft.loggalapp.Fragement;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.altsoft.Framework.enResult;
import com.altsoft.Framework.module.BaseFragment;
import com.altsoft.loggalapp.Login2Activity;
import com.altsoft.loggalapp.R;
import com.altsoft.loggalapp.kakaoMapActivity;

public class TabFragment_Myinfo extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_myinfo,container,false);



        return view;
    }
}
