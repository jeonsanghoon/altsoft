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

import com.altsoft.Framework.module.BaseFragment;
import com.altsoft.Framework.Global;
import com.altsoft.Service.BannerListViewAdapter;
import com.altsoft.loggalapp.R;
import com.altsoft.loggalapp.SignageControlActivity;
import com.altsoft.loggalapp.WebViewActivity;
import com.altsoft.model.AD_SEARCH_COND;
import com.altsoft.model.T_AD;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**

 */
public class TabFragment1 extends BaseFragment {
    boolean lastitemVisibleFlag = false;
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
    BannerListViewAdapter adapter;
    ListView listview ;
    boolean bLastPage = false;
    Integer nPageSize = 30;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adapter = new BannerListViewAdapter();
        GetBannerList();


        return inflater.inflate(R.layout.fragment_tab_fragment1, container, false);
    }

    private void GetBannerList() {
        this.GetBannerList(null);
    }

    /// 배너정보가져오기
    private void GetBannerList(Integer page) {

        AD_SEARCH_COND Cond = new AD_SEARCH_COND();

        try {
            if(bLastPage) {
                Toast.makeText(getActivity(),"데이터가 모두 검색되었습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            Cond.LATITUDE = Global.getMapInfo().latitude;
            Cond.LONGITUDE = Global.getMapInfo().longitude;
            Cond.PageCount = nPageSize;
            if(page != null) Cond.Page = page;
            String sAddr = Global.getMapInfo().currentLocationAddress;
            Global.getCommon().ProgressShow(getActivity());
            Call<List<T_AD>> call = Global.getAPIService().GetBannerList(Cond);
            call.enqueue(new Callback<List<T_AD>>() {
                @Override
                public void onResponse(Call<List<T_AD>> call, Response<List<T_AD>> response) {
                    List<T_AD> list = response.body();
                    Global.getCommon().ProgressHide(getActivity());
                    if(list.size() == 0) {
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
                                    GetBannerList(page);

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
                           T_AD adItem = adapter.getItem(position);
                            //Toast.makeText(getActivity(),adItem.TITLE  + "가 선택되었습니다.", Toast.LENGTH_LONG).show();
                            if(adItem.SIGN_CODE == null) {
                                Intent intent = new Intent(getContext(), WebViewActivity.class);
                                intent.putExtra("T_AD", adItem);
                                getContext().startActivity(intent);
                            }else {
                                /// 사이니지제어
                                Toast.makeText(getActivity(),adItem.TITLE  + "가 선택되었습니다.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getContext(), SignageControlActivity.class);
                                intent.putExtra("T_AD", adItem);
                                getContext().startActivity(intent);
                            }

                         }
                    });
                }

                @Override
                public void onFailure(Call<List<T_AD>> call, Throwable t) {

                }
            });

        }catch(Exception ex) {
            Log.d("로그", ex.getMessage());
        }
    }
}
