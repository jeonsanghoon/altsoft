package com.altsoft.loggalapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.altsoft.Adapter.SearchAdapter;
import com.altsoft.Adapter.SearchBannerAdapter;
import com.altsoft.Framework.Global;
import com.altsoft.Framework.GsonInfo;
import com.altsoft.Framework.control.altAutoCmpleateTextView;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.model.T_AD;
import com.altsoft.model.category.CATEGORY_COND;
import com.altsoft.model.category.CATEGORY_LIST;
import com.altsoft.model.keyword.CODE_DATA;
import com.altsoft.model.keyword.KEYWORD_COND;
import com.altsoft.model.search.MOBILE_AD_SEARCH_COND;
import com.altsoft.model.search.MOBILE_AD_SEARCH_DATA;
import com.altsoft.togglegroupbutton.MultiSelectToggleGroup;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.altsoft.loggalapp.Search2Activity.SearchCategory.multiCustomCompoundButton;

public class Search2Activity extends BaseActivity {
    private String TAG = Search2Activity.class.getSimpleName();
    private android.support.v7.widget.Toolbar tbMainSearch;
    Activity activity;
    /// 자동완성
    public static class SearchAutoCompleate
    {
        public static SearchAdapter searchadapter;
        public static List<String> list;          // 데이터를 넣은 리스트변수
        public static altAutoCmpleateTextView autoCompleteTextView ;
        public static Boolean bAutoDrop = false;
        public static String beforeData = "";
    }

    /// 카테고리 검색
    public static class SearchCategory {
        public static MultiSelectToggleGroup multiCustomCompoundButton;
    }
    /// 배너검색
    public static class ListPageParam {

        public static SearchBannerAdapter searchBannerAdapter;
        public static ListView listview ;
        public static boolean bLastPage = false;
        public static Integer nFirstPageSize = 4;
        public static Integer nPageSize = 20;
        public static Integer nPage = 1;
        public static boolean lastitemVisibleFlag = false;
        public static boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        activity = this;

        this.setUpViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Global.getCommon().hideSoftInputWindow(activity, SearchAutoCompleate.autoCompleteTextView, true);
    }


    @SuppressLint("ResourceAsColor")
    private void setUpViews() {
        tbMainSearch = (android.support.v7.widget.Toolbar) findViewById(R.id.tb_toolbarsearch);
        ListPageParam.listview = (ListView) activity.findViewById(R.id.listview1);


        setSupportActionBar(tbMainSearch);
        SearchAutoCompleate.list = new ArrayList<String>();
        ListPageParam.searchBannerAdapter = new SearchBannerAdapter();

        multiCustomCompoundButton = (MultiSelectToggleGroup) findViewById(R.id.group_multi_custom_compoundbutton);
        SetCategoryList();
        multiCustomCompoundButton.setOnCheckedChangeListener(new MultiSelectToggleGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedStateChanged(MultiSelectToggleGroup group, int checkedId, boolean isChecked) {

                Set<Integer> chklist = multiCustomCompoundButton.getCheckedIds();
                for(Integer  data: multiCustomCompoundButton.getCheckedIds())
                Log.v("dd", "onCheckedStateChanged(): " + checkedId + ", isChecked = " + isChecked);

                doQueryMobileBanner(1);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        SearchAutoCompleate.autoCompleteTextView = (altAutoCmpleateTextView) findViewById(R.id.autoCompleteTextView);



        SearchAutoCompleate.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchAutoCompleate.autoCompleteTextView.dismissDropDown();

                SearchAutoCompleate.searchadapter.setSelectedItem(SearchAutoCompleate.searchadapter.getObject(position));;

                SearchAutoCompleate.bAutoDrop = false;
            }
        });



        SearchAutoCompleate.autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // SearchAutoCompleate.beforeData = s.toString();
                SearchAutoCompleate.bAutoDrop = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                String data = s.toString();
                if(!SearchAutoCompleate.beforeData.equals(data)) {
                    if (data.length() > 0) {
                        settingList(data);
                    }
                    if(SearchAutoCompleate.searchadapter != null) {
                        if(!(SearchAutoCompleate.searchadapter.getSelectedItem().NAME.equals(s.toString())))
                        {
                            SearchAutoCompleate.searchadapter.setSelectedItem(new CODE_DATA());
                        }
                    }
                }
                SearchAutoCompleate.beforeData = data;
            }
        });

        ImageButton search = (ImageButton)findViewById(R.id.btnSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doQuery();
            }
        });
        Global.getCommon().ProgressHide(this);
    }

    /// 전체조회
    private void doQuery() {
        this.doQueryMobileBanner(1);
        this.doQueryLocalBox();
        this.doQueryLocalStation();
        this.doQuerySignage();
    }
    private void doQueryMobileBanner()
    {
        this.doQueryMobileBanner(1);
    }
    private void doQueryMobileBanner(Integer page)
    {

        try {
            ListPageParam.bLastPage = false;
            doQueryMobileBanner(page, ListPageParam.nFirstPageSize);
        }catch(Exception ex){ Log.d(TAG, ex.getMessage());}
    }
    /// 모바일 조회
    private void doQueryMobileBanner(Integer page, Integer pagesize)
    {
        if( ListPageParam.bLastPage ) {
            Toast.makeText(activity,"데이터가 모두 검색되었습니다.", Toast.LENGTH_LONG).show();
            return;
        }
        ListPageParam.nPage = page;
        MOBILE_AD_SEARCH_COND Cond = new MOBILE_AD_SEARCH_COND();

        Cond.PAGE_COUNT = pagesize;
        Cond.PAGE = page;
        Cond.LONGITUDE = Global.getMapInfo().longitude;
        Cond.LATITUDE  = Global.getMapInfo().latitude;
        Cond.USER_ID = Global.getLoginInfo().USER_ID;
        Object[] arrCategory = multiCustomCompoundButton.getCheckedIds().toArray();
        for(int i=0;  i < arrCategory.length; i++){
            if(i==0) Cond.CATEGORY_CODES =   arrCategory[i].toString();
            else Cond.CATEGORY_CODES = Cond.CATEGORY_CODES + "," + arrCategory[i].toString();
        }

        if(SearchAutoCompleate.searchadapter != null
                && SearchAutoCompleate.searchadapter.getSelectedItem() != null
                && SearchAutoCompleate.searchadapter.getSelectedItem().CODE != null)
        {
            Cond.KEYWORD_CODE = SearchAutoCompleate.searchadapter.getSelectedItem().CODE;
            Cond.KEYWORD_CODE = (Cond.KEYWORD_CODE == null || Cond.KEYWORD_CODE < 0) ? null : Cond.KEYWORD_CODE;
        }
        else
        {
            Cond.KEYWORD_NAME = SearchAutoCompleate.autoCompleteTextView.getText().toString();
        }


        String sJson = new GsonInfo<MOBILE_AD_SEARCH_COND, String>(MOBILE_AD_SEARCH_COND.class).ToString(Cond);
        Global.getCommon().ProgressShow(activity);
        Call<List<MOBILE_AD_SEARCH_DATA>> call = Global.getAPIService().GetMobileAdSearchList(Cond);

        call.enqueue(new Callback<List<MOBILE_AD_SEARCH_DATA>>() {

            @Override
            public void onResponse(Call<List<MOBILE_AD_SEARCH_DATA>> call, Response<List<MOBILE_AD_SEARCH_DATA>> response) {
                List<MOBILE_AD_SEARCH_DATA> list = response.body();
                Global.getCommon().ProgressHide(activity);

                if((ListPageParam.nPage ==  1 &&  list.size() < ListPageParam.nFirstPageSize)
                        || ( list.size() > ListPageParam.nFirstPageSize && list.size() < ListPageParam.nPageSize)) {
                    ListPageParam.bLastPage = true;
                }

                //if(searchBannerAdapter.SetDataBind(list, (list.size() <= 4) ? true : false  ) == true) return;
                if(ListPageParam.searchBannerAdapter.SetDataBind(list, (ListPageParam.nPage == 1) ? true : false  )) return;
                ListPageParam.listview.setAdapter(ListPageParam.searchBannerAdapter);

                ListPageParam.listview.setOnScrollListener(new ListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && ListPageParam.lastitemVisibleFlag && ListPageParam.mLockListView == false) {

                            // 데이터 로드
                            if(ListPageParam.lastitemVisibleFlag == true) {
                                Integer page = ((ListPageParam.listview.getCount() - ListPageParam.nFirstPageSize)  / ListPageParam.nPageSize) + 2;
                                if( ListPageParam.nPage == 1 && ListPageParam.listview.getCount() == ListPageParam.nFirstPageSize) {
                                    page = 1;
                                }


                                doQueryMobileBanner(page, ListPageParam.nPageSize);
                            }
                            ListPageParam.mLockListView = false;
                            ListPageParam.lastitemVisibleFlag = false;
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        ListPageParam.lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
                    }
                });

                ListPageParam.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MOBILE_AD_SEARCH_DATA data = ListPageParam.searchBannerAdapter.getItem(position);
                        T_AD adItem = new T_AD();
                        try {
                            adItem = new GsonInfo<MOBILE_AD_SEARCH_DATA, T_AD>(MOBILE_AD_SEARCH_DATA.class, T_AD.class).ToCopy(data);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(getActivity(),adItem.TITLE  + "가 선택되었습니다.", Toast.LENGTH_LONG).show();
                        Toast.makeText(activity,adItem.TITLE  + "가 선택되었습니다.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra("T_AD", adItem);
                        activity.startActivity(intent);


                    }
                });
            }
            @Override
            public void onFailure(Call<List<MOBILE_AD_SEARCH_DATA>> call, Throwable t) {
                Global.getCommon().ProgressHide(activity);
            }
        });
    }

    /// 로컬박스조회
    private void doQueryLocalBox()
    {
    }
    /// 로컬스테이션조회
    private void doQueryLocalStation()
    {

    }
    /// 로컬사이니지 조회
    private void doQuerySignage() {
    }



    /// 자동완성 값 셋팅
    private void settingList(String query){
        SearchAutoCompleate.list = new ArrayList<String>();

        KEYWORD_COND Cond = new KEYWORD_COND();
        Cond.KEYWORD_NAME = query;
        try {
            Call<List<CODE_DATA>> call = Global.getAPIService().GetKeywordAutoCompleateList(Cond);

            call.enqueue(new Callback<List<CODE_DATA>>() {
                @Override
                public void onResponse(Call<List<CODE_DATA>> call, Response<List<CODE_DATA>> response) {
                    SearchAutoCompleate.list = new ArrayList<String>();
                    List<CODE_DATA> rtn = response.body();
                    for(CODE_DATA data : rtn) {
                        SearchAutoCompleate.list.add(data.NAME);
                    }
                    SearchAutoCompleate.searchadapter = new SearchAdapter(activity, R.layout.autocomplate_list_item, rtn);
                    SearchAutoCompleate.autoCompleteTextView.setAdapter(SearchAutoCompleate.searchadapter);
                    if(SearchAutoCompleate.bAutoDrop) {
                        SearchAutoCompleate.autoCompleteTextView.showDropDown();
                    }
                }
                @Override
                public void onFailure(Call<List<CODE_DATA>> call, Throwable t) {

                }
            });

        }catch(Exception ex) {
            Log.d("로그", ex.getMessage());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    /// 배너정보가져오기
    private void SetCategoryList() {

        CATEGORY_COND Cond = new CATEGORY_COND();

        try {
            Cond.HIDE = false;
            Cond.CATEGORY_TYPE = 1;

            Call<List<CATEGORY_LIST>> call = Global.getAPIService().GetCategoryList(Cond);

            call.enqueue(new Callback<List<CATEGORY_LIST>>() {
                @Override
                public void onResponse(Call<List<CATEGORY_LIST>> call, Response<List<CATEGORY_LIST>> response) {
                    List<CATEGORY_LIST> list = response.body();
                    for(CATEGORY_LIST data  : list)
                    {
                        multiCustomCompoundButton.addButton(data.CATEGORY_CODE, data.CATEGORY_NAME);
                    }
                }
                @Override
                public void onFailure(Call<List<CATEGORY_LIST>> call, Throwable t) {
                }
            });

        }catch(Exception ex) {
            Log.d("로그", ex.getMessage());
        }
    }

}
