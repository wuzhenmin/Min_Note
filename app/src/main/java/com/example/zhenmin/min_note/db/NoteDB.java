package com.example.zhenmin.min_note.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.zhenmin.min_note.model.Note;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenmin on 2015/11/4.
 */
public class NoteDB {

    public static final String DB_NAME = "note";
    public static final int VERSION = 4;
    private static NoteDB noteDB ;
    private SQLiteDatabase db;

    public NoteDB(Context context){
        OpenHelper openHelper = new OpenHelper(context,DB_NAME,null,VERSION);
        db = openHelper.getWritableDatabase();
    }

    public synchronized static NoteDB getInstance(Context context){
        if (noteDB == null){
            noteDB = new NoteDB(context);
        }
        return noteDB;
    }

    public void saveNote(Note note){
        if (note!=null){
            ContentValues values = new ContentValues();
            values.put("title",note.getTitle());
            values.put("content",note.getContent());
            values.put("buildTime",note.getBuildTime());
            values.put("img_path",note.getImgPath());
            values.put("vedio",note.getVedioPath());
            db.insert("Note",null,values);
        }
    }

    public void deleteNoteById(int id){
        String args[] = {String.valueOf(id)};
        db.delete("Note","id=?",args);
    }

    public void deleteAll(){
        db.delete("Note",null,null);
    }

    public void editNoteById(Note note){
        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("content",note.getContent());
        values.put("buildTime",note.getBuildTime());
        values.put("img_path",note.getImgPath());
        values.put("vedio",note.getVedioPath());
        String args[] = {String.valueOf(note.getId())};
        db.update("Note", values, "id = ?", args);
    }

    public List<Note> loadNotes(){
        List<Note> notes = new ArrayList<Note>();
        Cursor cursor = db.query("Note",null,null,null,null,null,"buildTime DESC");
        if(cursor.moveToFirst()){
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex("id")));
                note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                note.setContent(cursor.getString(cursor.getColumnIndex("content")));
                note.setImgPath(cursor.getString(cursor.getColumnIndex("img_path")));
                note.setVedioPath(cursor.getString(cursor.getColumnIndex("vedio")));
                note.setBuildTime(cursor.getString(cursor.getColumnIndex("buildTime")));
                notes.add(note);
            }while(cursor.moveToNext());
        }
        return notes;
    }

}
