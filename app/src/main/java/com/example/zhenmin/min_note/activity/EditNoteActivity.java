package com.example.zhenmin.min_note.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhenmin.min_note.db.NoteDB;
import com.example.zhenmin.min_note.model.Note;

import java.sql.Date;
import java.util.List;

/**
 * Created by zhenmin on 2015/11/4.
 */
public class EditNoteActivity extends ActionBarActivity {

    private EditText editTitle;
    private EditText editContent;
    private Toolbar toolbar;
    private TextView editTime;
    private NoteDB noteDB;
    String currentTime = DateFormat.format("yyyy-MM-dd HH:mm", new Date(System.currentTimeMillis())).toString();
    Note noteFromIntent;
    private List<Note> notes;
    private boolean isSaved = false;
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_note);

        initView();
        initData();
        noteDB = NoteDB.getInstance(this);
        RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.edit_layout);
       /* ScaleAnimation scaleAnimation = new ScaleAnimation(0,1,0,1);
        scaleAnimation.setDuration(1000);
        LayoutAnimationController lac = new LayoutAnimationController(scaleAnimation,0.5f);
        linearLayout.setLayoutAnimation(lac);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        MenuItem shareItem = menu.findItem(R.id.edit_note_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        shareActionProvider.setShareIntent(getDefaultIntent());
        return true;
    }
    private Intent getDefaultIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra("title","测试");
        intent.putExtra("content","Helloween");
        return intent;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_note:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
                builder.setMessage("确定删除吗？");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove(noteFromIntent);
                        noteDB.deleteNoteById(noteFromIntent.getId());
                        Toast.makeText(EditNoteActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                        EditNoteActivity.this.finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            case R.id.edit_save:
                String title = editTitle.getText().toString();
                String content = editContent.getText().toString();
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                    Note note = new Note();
                    note.setId(noteFromIntent.getId());
                    note.setTitle(title);
                    note.setContent(content);
                    note.setBuildTime(currentTime);
                    noteDB.editNoteById(note);
                    Toast.makeText(EditNoteActivity.this, "edit success", Toast.LENGTH_LONG).show();
                    isSaved = true;
                    finish();
                } else {
                    Toast.makeText(EditNoteActivity.this, "edit fail", Toast.LENGTH_LONG).show();
                    isSaved = false;
                }
            //case R.id.edit_note_share:



        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!isSaved){
                getWarnDialog();
            }else{
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void getWarnDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditNoteActivity.this);
        builder.setMessage("取消后将不保存笔记，确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                EditNoteActivity.this.finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.create().show();
    }

    private void initData() {
        noteDB = NoteDB.getInstance(EditNoteActivity.this);
        notes = noteDB.loadNotes();
        noteFromIntent = (Note) getIntent().getSerializableExtra("note");
        editTitle.setText(noteFromIntent.getTitle());
        editContent.setText(noteFromIntent.getContent());
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.edit_note_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSaved)
                    getWarnDialog();
                else
                    finish();
            }
        });
        editTitle = (EditText) findViewById(R.id.edit_note_title);
        editContent = (EditText) findViewById(R.id.edit_note_content);
        editTime = (TextView) findViewById(R.id.edit_build_time);
        editTime.setText(currentTime);
    }
}
