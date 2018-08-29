package com.altsoft.loggalapp;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;

public class Search2Activity extends AppCompatActivity  implements SearchView.OnQueryTextListener{
    private String TAG = Search2Activity.class.getSimpleName();
    private android.support.v7.widget.Toolbar tbMainSearch;
    private ListView lvToolbarSerch;
    String[] arrays = new String[]{"98411", "98422", "98433", "98444", "98455"};
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        this.setUpViews();
    }



    private void setUpViews() {
        tbMainSearch = (android.support.v7.widget.Toolbar) findViewById(R.id.tb_toolbarsearch);

        lvToolbarSerch =(ListView) findViewById(R.id.lv_toolbarsearch);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrays);
        lvToolbarSerch.setAdapter(adapter);
        setSupportActionBar(tbMainSearch);

        MultiSelectToggleGroup multiCustomCompoundButton =
                (MultiSelectToggleGroup) findViewById(R.id.group_multi_custom_compoundbutton);
        multiCustomCompoundButton.setOnCheckedChangeListener(new MultiSelectToggleGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedStateChanged(MultiSelectToggleGroup group, int checkedId, boolean isChecked) {
                Log.v("dd", "onCheckedStateChanged(): " + checkedId + ", isChecked = " + isChecked);
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
                Log.d(TAG, "onQueryTextSubmit: query->"+query);
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
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
