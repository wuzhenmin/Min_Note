package com.example.zhenmin.min_note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhenmin.min_note.db.NoteDB;
import com.example.zhenmin.min_note.model.Note;
import com.example.zhenmin.min_note.util.DataUtil;
import com.example.zhenmin.min_note.util.MyAdapter;
import com.example.zhenmin.min_note.util.PullListView;

import java.util.ArrayList;
import java.util.List;


public class AppMain extends ActionBarActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout dlMain;
    private ActionBarDrawerToggle drawerToggle;
    private Button btnAddNote;
    private Button btnAddPicNote;
    private Button btnAddVedioNote;
    private Fragment autoGetMoreFragment;
    private FragmentManager fm;
    private MyAdapter myAdapter;
    private PullListView listView;
    private String mtitle;
    private List<Note> notes = new ArrayList<Note>();
    private NoteDB noteDB;
    private String searchText;
    private TextView searchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_main);
        mtitle = (String) getTitle();
        initView();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.search);//在菜单中找到对应控件的item
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchTextView = (TextView) searchView.findViewById(R.id.search_src_text);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                doSearch();
                return true;
            }

        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    DataUtil.getData(true,myAdapter,listView);
                }
            }
        });
        return true;
    }

    private void doSearch() {
        List<Note> resultNotes = new ArrayList<Note>();
        for (Note note : notes) {
            if (note.getTitle().contains(searchText)
                    || note.getContent().contains(searchText)
                    ||note.getBuildTime().contains(searchText)) {
                resultNotes.add(note);
            }
        }
        if (resultNotes.size()<1){
            searchTextView.setError("没有相关结果");
            return;
        }
        DataUtil.getSearchResult(myAdapter, listView, resultNotes);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();

    }

    private void initData() {
        noteDB = NoteDB.getInstance(this);
        notes = noteDB.loadNotes();
        listView = (PullListView) findViewById(R.id.plv_data);
        myAdapter = new MyAdapter(this, R.layout.item,notes,listView);
        myAdapter.notifyDataSetChanged();
        DataUtil.getData(true, myAdapter, listView);
        listView.setAdapter(myAdapter);
        listView.performRefresh();
    }


    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dlMain = (DrawerLayout) findViewById(R.id.dl_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, dlMain, toolbar, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toolbar.setTitle(mtitle);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setTitle("请选择");
            }
        };
        drawerToggle.syncState();
        dlMain.setDrawerListener(drawerToggle);
        btnAddNote = (Button) findViewById(R.id.btn_addNote);
        btnAddPicNote = (Button) findViewById(R.id.btn_addPicNote);
        btnAddVedioNote = (Button) findViewById(R.id.btn_addVedioNote);
        autoGetMoreFragment = new AutoGetMoreFragment();
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.dl_container, autoGetMoreFragment).commit();
        btnAddNote.setOnClickListener(this);
        btnAddPicNote.setOnClickListener(this);
        btnAddVedioNote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btn_addNote:
                dlMain.closeDrawers();
                intent = new Intent(MyApplication.getContext(), AddNoteActivity.class);
                intent.putExtra("flag", "1");
                startActivity(intent);
                break;
            case R.id.btn_addPicNote:
                dlMain.closeDrawers();
                intent = new Intent(MyApplication.getContext(), AddNoteActivity.class);
                intent.putExtra("flag", "2");
                startActivity(intent);
                break;
            case R.id.btn_addVedioNote:
                dlMain.closeDrawers();
                intent = new Intent(MyApplication.getContext(), AddNoteActivity.class);
                intent.putExtra("flag", "3");
                startActivity(intent);
                break;
        }
    }
}
