package com.altsoft.loggalapp.Fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.altsoft.Adapter.BannerRecyclerViewAdapter;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.module.BaseFragment;
import com.altsoft.loggalapp.R;
import com.altsoft.loggalapp.SignageControlActivity;
import com.altsoft.loggalapp.WebViewActivity;
import com.altsoft.model.AD_SEARCH_COND;
import com.altsoft.model.T_AD;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.RecyclerView.*;

public class TabFragment1_Recylerview extends BaseFragment {
    BannerRecyclerViewAdapter adapter;
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
    RecyclerView listview;
    boolean bLastPage = false;
    Integer nPageSize = 30;
    Integer nPage = 1;
    int nCnt = 0;

    public static List<T_AD> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        GetBannerList();
        return inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    /*
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }*/

    private void setContentView(int activity_main) {
    }

    public void GetBannerList() {

        this.GetBannerList(1);
    }

    /// 배너정보가져오기
    public void GetBannerList(Integer page) {

        AD_SEARCH_COND Cond = new AD_SEARCH_COND();

        try {

            mLockListView = true;
            Cond.LATITUDE = Global.getMapInfo().latitude;
            Cond.LONGITUDE = Global.getMapInfo().longitude;
            Cond.PageCount = nPageSize;
            Cond.Page = page;// page == null ? 1 : page;
            if (Cond.Page != 1 && bLastPage) {
                Toast.makeText(getActivity(), "데이터가 모두 검색되었습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            nPage = Cond.Page;
            Cond.nCnt = nCnt++;


            String sAddr = Global.getMapInfo().currentLocationAddress;
            Global.getCommon().ProgressShow(getActivity());
            Call<List<T_AD>> call = Global.getAPIService().GetBannerList(Cond);
            call.enqueue(new Callback<List<T_AD>>() {
                @Override
                public void onResponse(Call<List<T_AD>> call, Response<List<T_AD>> response) {
                    List<T_AD> tmplist = response.body();
                    Global.getCommon().ProgressHide(getActivity());
                    if (tmplist.size() == 0) {
                        Toast.makeText(getActivity(), "데이터가 모두 검색되었습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (tmplist.size() < nPageSize) bLastPage = true;

                    if (nPage == 1) {
                        list = tmplist;
                    } else if (nPage > 1) {
                        list.addAll(tmplist);
                    }
                    if (list.size() % nPageSize > 0 && list.size() != 0) mLockListView = false;
                    else mLockListView = true;

                    listview = (RecyclerView) getView().findViewById(R.id.listview1);

                    listview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                    if(adapter == null) adapter = new BannerRecyclerViewAdapter();
                    if(nPage <= 1) {
                        adapter.clear();
                        adapter.notifyDataSetChanged();
                        listview.setAdapter(null);
                    }

                    adapter.SetBind((ArrayList)list, nPage);
                    listview.setAdapter(adapter);
                    listview.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    listview.setItemAnimator(new DefaultItemAnimator());

                    mLockListView = false;
                    listview.setOnScrollListener(new OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView view, int scrollState) {
                            if (!listview.canScrollVertically(1) && mLockListView == false) {
                                // 데이터 로드
                                //Integer page = (listview.getCount() / nPageSize) + 1;

                                nPage = nPage + 1;
                                GetBannerList(nPage);
                                mLockListView = false;
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<T_AD>> call, Throwable t) {

                }
            });

        } catch (Exception ex) {
            Log.d("로그", ex.getMessage());
        }
    }
}
