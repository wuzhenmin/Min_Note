package com.example.zhenmin.min_note.util;

import android.os.Handler;
import com.example.zhenmin.min_note.activity.MyApplication;
import com.example.zhenmin.min_note.db.NoteDB;
import com.example.zhenmin.min_note.model.Note;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by grumoon on 15/1/10.
 */
public class DataUtil {

    public static void getData(final boolean isRefresh, final MyAdapter myAdapter, final PullListView plv) {

        NoteDB noteDB = NoteDB.getInstance(MyApplication.getContext());
        List<Note> notes = new ArrayList<Note>();
        //延迟加载数据，模拟耗时操作
        notes = noteDB.loadNotes();
        final List<Note> finalNotes = notes;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (isRefresh) {
                    myAdapter.clear();
                }
                myAdapter.addAll(finalNotes);
                myAdapter.notifyDataSetChanged();
                plv.refreshComplete();
               // plv.getMoreComplete();

            }
        }, 300);
    }

    public static void getSearchResult( MyAdapter myAdapter, final PullListView plv, List<Note> notes){
        myAdapter.clear();
        myAdapter.addAll(notes);
        plv.setAdapter(myAdapter);

    }
}
