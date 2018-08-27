package com.altsoft.loggalapp.Fragement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.altsoft.Framework.Global;
import com.altsoft.Framework.module.BaseFragment;
import com.altsoft.Service.LocalBoxListViewAdapter;
import com.altsoft.loggalapp.R;
import com.altsoft.loggalapp.detail.LocalboxbannerListActivity;
import com.altsoft.model.DEVICE_LOCATION;
import com.altsoft.model.DEVICE_LOCATION_COND;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TabFragment2 extends BaseFragment {
    LocalBoxListViewAdapter adapter;
    boolean lastitemVisibleFlag = false;
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
    ListView listview;
    boolean bLastPage = false;
    Integer nPageSize = 30;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adapter = new LocalBoxListViewAdapter();

        GetDeviceLocation();
        return inflater.inflate(R.layout.fragment_tab_fragment2, container, false);
    }
    private void GetDeviceLocation()
    {
        this.GetDeviceLocation(null);
    }
    private void GetDeviceLocation(Integer page) {

        DEVICE_LOCATION_COND Cond = new DEVICE_LOCATION_COND();
        try {

            if(bLastPage) {
                Toast.makeText(getActivity(),"데이터가 모두 검색되었습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            Cond.LATITUDE = Global.getMapInfo().latitude;
            Cond.LONGITUDE = Global.getMapInfo().longitude;
            Cond.PAGE_COUNT = nPageSize;
            if(page != null) Cond.PAGE = page;
            String sAddr = Global.getMapInfo().currentLocationAddress;
            Global.getCommon().ProgressShow(getActivity());
            Call<List<DEVICE_LOCATION>> call = Global.getAPIService().GetDeviceLocation(Cond);
            call.enqueue(new Callback<List<DEVICE_LOCATION>>() {
                @Override
                public void onResponse(Call<List<DEVICE_LOCATION>> call, Response<List<DEVICE_LOCATION>> response) {
                    Global.getCommon().ProgressHide(getActivity());
                    List<DEVICE_LOCATION> list = response.body();
                    if(list.size() == 0) {
                        bLastPage = true;
                        Toast.makeText(getActivity(),"데이터가 모두 검색되었습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(list.size() < nPageSize) bLastPage = true;
                    if(adapter.SetDataBind(list) == true) return;

                    listview = (ListView) getView().findViewById(R.id.listview1);
                    listview.setAdapter(adapter);
                    listview.setOnScrollListener(new ListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag && mLockListView == false) {
                                // 데이터 로드
                                if(lastitemVisibleFlag == true) {
                                    Integer page = (listview.getCount() / nPageSize) + 1;
                                    GetDeviceLocation(page);
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
                            DEVICE_LOCATION data = adapter.getItem(position);
                            //Toast.makeText(getActivity(),adItem.TITLE  + "가 선택되었습니다.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), LocalboxbannerListActivity.class);
                            intent.putExtra("DEVICE_CODE", Long.parseLong(data.DEVICE_CODE) );
                            getContext().startActivity(intent);

                        }
                    });


                }

                @Override
                public void onFailure(Call<List<DEVICE_LOCATION>> call, Throwable t) {
                    Global.getCommon().ProgressHide(getActivity());
                }
            });

        }catch(Exception ex) {
            Log.d("로그", ex.getMessage());
        }
    }
}