package com.altsoft.loggalapp.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.altsoft.Framework.Global;
import com.altsoft.Framework.GsonInfo;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.Adapter.LocalBoxListItemAdapter;
import com.altsoft.loggalapp.R;
import com.altsoft.loggalapp.WebViewActivity;
import com.altsoft.model.T_AD;
import com.altsoft.model.device.AD_DEVICE_MOBILE_COND;
import com.altsoft.model.device.AD_DEVICE_MOBILE_LIST;
import com.altsoft.model.device.AD_DEVICE_MOBILE_M;

import java.io.FileNotFoundException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LocalboxbannerListActivity extends BaseActivity {

    Activity activity;
    ListView listview ;
    boolean bLastPage = false;
    boolean lastitemVisibleFlag = false;
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
    Integer nPageSize = 30;
    LocalBoxListItemAdapter adapter;
    private Long deviceCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localboxbanner_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adapter = new LocalBoxListItemAdapter();
        activity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        deviceCode =  (Long)intent.getLongExtra("DEVICE_CODE",0);
        this.GetLocalBoxBannerList();

    }

    private void GetLocalBoxBannerList() {

        AD_DEVICE_MOBILE_COND Cond = new AD_DEVICE_MOBILE_COND();
        Cond.PAGE_COUNT = 10000;
        Cond.PAGE = 1;
        Cond.USER_ID = Global.getLoginInfo().USER_ID;
        Cond.DEVICE_CODE = deviceCode;
        Global.getCommon().ProgressShow(this);

        Call<AD_DEVICE_MOBILE_M> call = Global.getAPIService().GetMobileAdDeviceList(Cond);

        call.enqueue(new Callback<AD_DEVICE_MOBILE_M>() {
            @Override
            public void onResponse(Call<AD_DEVICE_MOBILE_M> call, Response<AD_DEVICE_MOBILE_M> response) {

                Global.getCommon().ProgressHide(activity);
                AD_DEVICE_MOBILE_M rtn = response.body();
                if(rtn == null) return;

                activity.setTitle(rtn.DEVICE_NAME);
                List<AD_DEVICE_MOBILE_LIST> list = rtn.AD_LIST;
                if(bLastPage) {
                    Toast.makeText(activity,"데이터가 모두 검색되었습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(list.size() < nPageSize) bLastPage = true;

                if(adapter.SetDataBind(list) == true) return;
                listview = (ListView)  activity.findViewById(R.id.listview1);
                listview.setAdapter(adapter);

                listview.setOnScrollListener(new ListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag && mLockListView == false) {

                            // 데이터 로드
                            if(lastitemVisibleFlag == true) {
                                Integer page = (listview.getCount() / nPageSize) + 1;
                                GetLocalBoxBannerList();
                            }
                            mLockListView = false;
                            lastitemVisibleFlag = false;
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
                    }
                });

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AD_DEVICE_MOBILE_LIST adItem = adapter.getItem(position);
                        T_AD data = new T_AD();
                        try {
                            data = (T_AD) new GsonInfo(AD_DEVICE_MOBILE_LIST.class, T_AD.class).ToCopy(adItem);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra("T_AD", data);
                        activity.startActivity(intent);

                    }
                });
            }

            @Override
            public void onFailure(Call<AD_DEVICE_MOBILE_M> call, Throwable t) {
                Global.getCommon().ProgressHide(activity);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
