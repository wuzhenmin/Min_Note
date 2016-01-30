package com.example.zhenmin.min_note.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.example.zhenmin.min_note.db.NoteDB;
import com.example.zhenmin.min_note.model.Note;
import com.example.zhenmin.min_note.util.DataUtil;
import com.example.zhenmin.min_note.util.MyAdapter;
import com.example.zhenmin.min_note.util.PullListView;
import java.util.List;

public class AutoGetMoreFragment extends Fragment {

    private PullListView plvData;
    private MyAdapter adapter;
    private NoteDB noteDB;
    private List<Note> notes;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_auto_get_more, container, false);
        initData();
        initView(v);
        return v;
    }

    private void initData() {
        noteDB = NoteDB.getInstance(getActivity());
        notes = noteDB.loadNotes();
    }


    private void initView(View v) {
        plvData = (PullListView) v.findViewById(R.id.plv_data);
        adapter = new MyAdapter(getContext(),R.layout.item,notes,plvData);
        plvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), EditNoteActivity.class);
                intent.putExtra("note",notes.get(position-1));
                startActivity(intent);
            }
        });
        plvData.setOnRefreshListener(new PullListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                DataUtil.getData(true, adapter, plvData);
            }
        });

        plvData.setOnGetMoreListener(new PullListView.OnGetMoreListener() {

            @Override
            public void onGetMore() {
                DataUtil.getData(false, adapter, plvData);
            }
        });

        adapter = new MyAdapter(getActivity(), R.layout.item,notes,plvData);
        plvData.setAdapter(adapter);

        plvData.performRefresh();

    }


}
