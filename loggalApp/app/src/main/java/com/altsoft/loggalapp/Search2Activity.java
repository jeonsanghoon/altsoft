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
import com.altsoft.model.category.CATEGORY_COND;
import com.altsoft.model.category.CATEGORY_LIST;
import com.altsoft.model.keyword.CODE_DATA;
import com.altsoft.model.keyword.KEYWORD_COND;
import com.altsoft.model.search.MOBILE_AD_SEARCH_COND;
import com.altsoft.model.search.MOBILE_AD_SEARCH_DATA;
import com.altsoft.togglegroupbutton.MultiSelectToggleGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search2Activity extends BaseActivity {
    private String TAG = Search2Activity.class.getSimpleName();
    private android.support.v7.widget.Toolbar tbMainSearch;
    private ListView lvToolbarSerch;
    MultiSelectToggleGroup multiCustomCompoundButton;
    //String[] arrays = new String[]{"98411", "98422", "98433", "98444", "98455"};
    private List<String> list;          // 데이터를 넣은 리스트변수
    altAutoCmpleateTextView autoCompleteTextView ;
   // ArrayAdapter<String> adapter;
    Activity activity;
    String beforeData = "";
    Boolean bAutoDrop = false;

    SearchAdapter searchadapter;
    SearchBannerAdapter searchBannerAdapter;
    ListView listview ;
    boolean bLastPage = false;
    Integer nPageSize = 20;
    boolean lastitemVisibleFlag = false;
    private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수
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
        Global.getCommon().hideSoftInputWindow(activity, autoCompleteTextView, true);
    }


    @SuppressLint("ResourceAsColor")
    private void setUpViews() {
        tbMainSearch = (android.support.v7.widget.Toolbar) findViewById(R.id.tb_toolbarsearch);
        listview = (ListView) activity.findViewById(R.id.listview1);


        setSupportActionBar(tbMainSearch);
        list = new ArrayList<String>();
        searchBannerAdapter = new SearchBannerAdapter();

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


        autoCompleteTextView = (altAutoCmpleateTextView) findViewById(R.id.autoCompleteTextView);



        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoCompleteTextView.dismissDropDown();

                searchadapter.setSelectedItem(searchadapter.getObject(position));;

                bAutoDrop = false;
            }
        });



        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // beforeData = s.toString();

                bAutoDrop = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                String data = s.toString();
                if(!beforeData.equals(data)) {
                    if (data.length() > 0) {
                        settingList(data);
                    }
                    if(searchadapter != null) {
                        if(!(searchadapter.getSelectedItem().NAME.equals(s.toString())))
                        {
                            searchadapter.setSelectedItem(new CODE_DATA());
                        }
                    }
                }
                beforeData = data;
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
        this.doQueryMobileBanner(1,4);
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
        doQueryMobileBanner(page,4);
    }
    /// 모바일 조회
    private void doQueryMobileBanner(Integer page, Integer pagesize)
    {
        if(page != 1 && bLastPage ) {
            Toast.makeText(activity,"데이터가 모두 검색되었습니다.", Toast.LENGTH_LONG).show();
            return;
        }
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


        if(searchadapter != null && searchadapter.getSelectedItem() != null)
        {
            Cond.KEYWORD_CODE = searchadapter.getSelectedItem().CODE;
        }
        else
        {
            Cond.KEYWORD_NAME = autoCompleteTextView.getText().toString();
        }

        if(page == 1) {
            searchBannerAdapter.SetDataBind(new ArrayList<MOBILE_AD_SEARCH_DATA>(),true);
            listview.setAdapter(searchBannerAdapter);
        }
        String sJson = new GsonInfo<MOBILE_AD_SEARCH_COND, String>(MOBILE_AD_SEARCH_COND.class).ToString(Cond);
        Global.getCommon().ProgressShow(activity);
        Call<List<MOBILE_AD_SEARCH_DATA>> call = Global.getAPIService().GetMobileAdSearchList(Cond);

        call.enqueue(new Callback<List<MOBILE_AD_SEARCH_DATA>>() {

            @Override
            public void onResponse(Call<List<MOBILE_AD_SEARCH_DATA>> call, Response<List<MOBILE_AD_SEARCH_DATA>> response) {
                List<MOBILE_AD_SEARCH_DATA> list = response.body();
                Global.getCommon().ProgressHide(activity);
                if(list.size() == 0) {
                    Toast.makeText(activity,"데이터가 모두 검색되었습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
                if(list.size() < nPageSize) {
                    bLastPage = true;

                }

                if(searchBannerAdapter.SetDataBind(list) == true) return;

                listview.setAdapter(searchBannerAdapter);

                listview.setOnScrollListener(new ListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag && mLockListView == false) {

                            // 데이터 로드
                            if(lastitemVisibleFlag == true) {
                                Integer page = ((listview.getCount() -4)  / nPageSize) + 2;
                                if(listview.getCount() <= 4) page = 1;

                                doQueryMobileBanner(page, nPageSize);


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
                        MOBILE_AD_SEARCH_DATA adItem = searchBannerAdapter.getItem(position);
                        //Toast.makeText(getActivity(),adItem.TITLE  + "가 선택되었습니다.", Toast.LENGTH_LONG).show();

                            /// 사이니지제어
                            Toast.makeText(activity,adItem.TITLE  + "가 선택되었습니다.", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(activity, WebViewActivity.class);
                            intent.putExtra("T_AD", (Serializable) adItem);
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
        list = new ArrayList<String>();

        KEYWORD_COND Cond = new KEYWORD_COND();
        Cond.KEYWORD_NAME = query;
        try {
            Call<List<CODE_DATA>> call = Global.getAPIService().GetKeywordAutoCompleateList(Cond);

            call.enqueue(new Callback<List<CODE_DATA>>() {
                @Override
                public void onResponse(Call<List<CODE_DATA>> call, Response<List<CODE_DATA>> response) {
                    list = new ArrayList<String>();
                    List<CODE_DATA> rtn = response.body();
                    for(CODE_DATA data : rtn) {
                        list.add(data.NAME);
                    }

                    searchadapter = new SearchAdapter(activity, R.layout.autocomplate_list_item, rtn);
                    autoCompleteTextView.setAdapter(searchadapter);
                    if(bAutoDrop) {
                        autoCompleteTextView.showDropDown();
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
      /*  MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem mSearchmenuItem = menu.findItem(R.id.menu_toolbarsearch);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) mSearchmenuItem.getActionView();
        mSearchmenuItem.expandActionView();
        MenuItemCompat.collapseActionView(mSearchmenuItem);
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);
        searchView.setQueryHint("검색할 내용을 입력하세요");
        ImageView closeBtn = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeBtn.setEnabled(false);
        closeBtn.setImageDrawable(null);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: query->" + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        mSearchmenuItem.expandActionView();
        searchView.performClick();
        searchView.requestFocus();
        Log.d(TAG, "onCreateOptionsMenu: mSearchmenuItem->" + mSearchmenuItem.getActionView());
        */
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
