package com.altsoft.loggalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.loggalapp.databinding.ActivityMainBinding;

import br.com.liveo.searchliveo.SearchLiveo;

public class SearchActivity extends BaseActivity implements SearchLiveo.OnSearchListener {
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.onInitView();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    private void onInitView()
    {
        mBinding = (ActivityMainBinding) this.bindView(R.layout.activity_search);
        this.onInitToolbar(mBinding.toolbar, R.string.app_name);
        mBinding.searchLiveo.
                with(this).
                removeMinToSearch().
                removeSearchDelay().
                build();
    }
    @Override
    public void changedSearch(CharSequence text) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.action_search : {
                mBinding.searchLiveo.show();
                return true;
           }
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == SearchLiveo.REQUEST_CODE_SPEECH_INPUT) {
                mBinding.searchLiveo.resultVoice(requestCode, resultCode, data);
            }
        }
    }
}
