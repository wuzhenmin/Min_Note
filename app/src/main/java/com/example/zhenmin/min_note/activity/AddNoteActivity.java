package com.example.zhenmin.min_note.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import com.example.zhenmin.min_note.db.NoteDB;
import com.example.zhenmin.min_note.model.Note;
import com.example.zhenmin.min_note.util.BitmapOption;
import com.example.zhenmin.min_note.util.CustomAnim;
import com.example.zhenmin.min_note.util.Tools;
import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by zhenmin on 2015/11/4.
 */
public class AddNoteActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText title;
    private EditText content;
    private TextView buildTime;
    private ImageView ivPic;
    private VideoView vedioView;
    private String currentTime = DateFormat.format("yyyy-MM-dd HH:mm", new Date(System.currentTimeMillis())).toString();
    private NoteDB noteDB;
    private Toolbar toolbar;
    private String flag;
    private File phoneFile, vedioFile;
    /* 请求码 */
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int RESULT_REQUEST_CODE = 2;
    private static final int VIDEO_REQUEST_CODE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        flag = getIntent().getStringExtra("flag");
        initView();
        noteDB = NoteDB.getInstance(this);
        RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.addnote_layout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getWarnDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        initView();
        getMenuInflater().inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CustomAnim customAnim = new CustomAnim();
        switch (item.getItemId()) {
            case R.id.add_note_save:
                saveNote(customAnim);
                break;
            case android.R.id.home:
                finish();
                break;

            case R.id.add_note_cancel:

                getWarnDialog();
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    private void getWarnDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
        builder.setMessage("取消后将不保存笔记，确认退出吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AddNoteActivity.this.finish();
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

    private void saveNote(CustomAnim customAnim) {
        String noteTitle = title.getText().toString();
        String noteContent = content.getText().toString();
        if (!TextUtils.isEmpty(noteContent) && !TextUtils.isEmpty(noteTitle)) {
            Note note = new Note();
            note.setTitle(noteTitle);
            note.setContent(noteContent);
            note.setBuildTime(currentTime);
            note.setImgPath(phoneFile + "");
            note.setVedioPath(vedioFile + "");
            noteDB.saveNote(note);
            Toast.makeText(AddNoteActivity.this, "save success", Toast.LENGTH_LONG).show();
            finish();
        } else if (TextUtils.isEmpty(noteTitle)) {
            title.setAnimation(customAnim);
            title.setError("笔记的标题不能为空");
        } else if (TextUtils.isEmpty(noteContent)) {
            content.setAnimation(customAnim);
            content.setError("笔记内容不能为空");
        }
    }


    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String str = format.format(date);
        return str;
    }

    private void showDialog() {
        String[] items = new String[]{"选择本地图片", "拍照"};
        new AlertDialog.Builder(this).setTitle("插入图片").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intentFromGallery = new Intent();
                        intentFromGallery.setType("image*//*"); // 设置文件类型
                        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
                        break;
                    case 1:
                        // 判断存储卡是否可以用，可用进行存储
                        if (Tools.hasSdcard()) {
                            Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            phoneFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + getTime() + ".jpg");
                            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phoneFile));
                            startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                        } else {
                            Toast.makeText(AddNoteActivity.this, "sd卡不可用", Toast.LENGTH_LONG).show();
                        }
                        break;
                }

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.add_note_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWarnDialog();
            }
        });
        ivPic = (ImageView) findViewById(R.id.iv_picNote_add);
        vedioView = (VideoView) findViewById(R.id.iv_vedio_add);
        title = (EditText) findViewById(R.id.add_note_title);
        content = (EditText) findViewById(R.id.add_note_content);
        buildTime = (TextView) findViewById(R.id.first_build_time);
        ivPic.setOnClickListener(this);
        switch (flag) {
            case "1":
                ivPic.setVisibility(View.GONE);
                vedioView.setVisibility(View.GONE);
                break;
            case "2":
                ivPic.setVisibility(View.VISIBLE);
                vedioView.setVisibility(View.GONE);
                break;
            case "3":
                ivPic.setVisibility(View.GONE);
                vedioView.setVisibility(View.VISIBLE);
                Intent vedio = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                vedioFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + getTime() + ".mp4");
                vedio.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(vedioFile));
                startActivityForResult(vedio, VIDEO_REQUEST_CODE);
                break;
        }
        buildTime.setText(currentTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_picNote_add:
                showDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != 0) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    break;
                case CAMERA_REQUEST_CODE:
                    Bitmap bitmap = BitmapOption.getImageThumbnail(phoneFile.getAbsolutePath(), 180, 180);
                    ivPic.setImageBitmap(bitmap);
                    break;
                case VIDEO_REQUEST_CODE:
                    vedioView.setVideoURI(Uri.fromFile(vedioFile));
                    vedioView.start();
                    break;
                default:break;
            }
        }
    }


}
