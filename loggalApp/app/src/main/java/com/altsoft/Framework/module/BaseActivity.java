package com.altsoft.Framework.module;


import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.altsoft.Framework.Global;
import com.altsoft.loggalapp.R;

import java.util.ArrayList;


@SuppressLint("Registered")
public abstract class BaseActivity extends AppCompatActivity {

    public static ArrayList<Activity> actList = new ArrayList<Activity>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Global.setCurrentActivity(this);
    }
    @Override
    protected  void onStart()
    {
        super.onStart();
        Global.setCurrentActivity(this);
        if(Global.getCommon().getComponentName(this).toLowerCase() != "mainactivity") {
            actList.add(this);
        }
    }

    //region Methods Toolbar
    public void onInitToolbar(Toolbar toolBar) {
        onInitToolbar(toolBar, getString(R.string.clear), -1, false);
    }

    public void onInitToolbar(Toolbar toolBar, String title) {
        onInitToolbar(toolBar, title, -1, false);
    }

    public void onInitToolbar(Toolbar toolBar, int title) {
        onInitToolbar(toolBar, title, -1, false);
    }

    public void onInitToolbar(Toolbar toolBar, int title, int icon) {
        onInitToolbar(toolBar, getString(title), icon, true);
    }

    public void onInitToolbar(Toolbar toolBar, String title, boolean displayHome) {
        onInitToolbar(toolBar, title, -1, displayHome);
    }

    public void onInitToolbar(Toolbar toolBar, int title, boolean displayHome) {
        onInitToolbar(toolBar, title, -1, displayHome);
    }

    public void onInitToolbar(Toolbar toolBar, int title, int icon, boolean displayHome) {
        onInitToolbar(toolBar, getString(title), icon, displayHome);
    }

    public void onInitToolbar(Toolbar toolBar, String title, int icon, boolean displayHome) {

        if (toolBar != null) {
            setSupportActionBar(toolBar);
            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setTitle(title);
                actionBar.setDisplayShowHomeEnabled(displayHome);
                actionBar.setDisplayHomeAsUpEnabled(displayHome);
                if (icon != -1 && displayHome) {
                    toolBar.setNavigationIcon(ContextCompat.getDrawable(this, icon));
                }
            }
        }
    }
    //endregion
}
