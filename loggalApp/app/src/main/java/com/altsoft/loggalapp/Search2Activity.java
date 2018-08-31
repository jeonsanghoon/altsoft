package com.altsoft.loggalapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.altsoft.Framework.Global;
import com.altsoft.Framework.module.BaseActivity;
import com.altsoft.model.category.CATEGORY_COND;
import com.altsoft.model.category.CATEGORY_LIST;
import com.altsoft.model.keyword.CODE_DATA;
import com.altsoft.model.keyword.KEYWORD_COND;
import com.altsoft.togglegroupbutton.MultiSelectToggleGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search2Activity extends BaseActivity implements SearchView.OnQueryTextListener {
    private String TAG = Search2Activity.class.getSimpleName();
    private android.support.v7.widget.Toolbar tbMainSearch;
    private ListView lvToolbarSerch;
    MultiSelectToggleGroup multiCustomCompoundButton;
    String[] arrays = new String[]{"98411", "98422", "98433", "98444", "98455"};
    private List<String> list;          // 데이터를 넣은 리스트변수
    AutoCompleteTextView autoCompleteTextView ;
    ArrayAdapter<String> adapter;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        activity = this;
        this.setUpViews();
    }


    @SuppressLint("ResourceAsColor")
    private void setUpViews() {
        tbMainSearch = (android.support.v7.widget.Toolbar) findViewById(R.id.tb_toolbarsearch);

        lvToolbarSerch = (ListView) findViewById(R.id.lv_toolbarsearch);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrays);
        lvToolbarSerch.setAdapter(adapter);
        setSupportActionBar(tbMainSearch);
        list = new ArrayList<String>();

        multiCustomCompoundButton = (MultiSelectToggleGroup) findViewById(R.id.group_multi_custom_compoundbutton);
        SetCategoryList();
        multiCustomCompoundButton.setOnCheckedChangeListener(new MultiSelectToggleGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedStateChanged(MultiSelectToggleGroup group, int checkedId, boolean isChecked) {

                Set<Integer> chklist = multiCustomCompoundButton.getCheckedIds();
                for(Integer  data: multiCustomCompoundButton.getCheckedIds())


                Log.v("dd", "onCheckedStateChanged(): " + checkedId + ", isChecked = " + isChecked);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        // AutoCompleteTextView 에 아답터를 연결한다.
        autoCompleteTextView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,  list ));

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = autoCompleteTextView.getText().toString();
                settingList(str);

            }
        });
    }
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
                    autoCompleteTextView.setAdapter(new ArrayAdapter<String>(activity,
                            android.R.layout.simple_dropdown_item_1line,  list ));
                    autoCompleteTextView.showDropDown();
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
        MenuInflater menuInflater = getMenuInflater();
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
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "onQueryTextChange: newText->" + newText);
        //adapter.getFilter().filter(newText);
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
