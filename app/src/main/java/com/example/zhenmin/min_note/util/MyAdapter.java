package com.example.zhenmin.min_note.util;

import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.zhenmin.min_note.activity.R;
import com.example.zhenmin.min_note.model.Note;
import java.util.List;


/**
 * Created by zhenmin on 2015/11/4.
 */
public class MyAdapter extends ArrayAdapter<Note> {

    public List<Note> mynotes;
    private Context myContext;
    private int resourceId;
    private PullListView mListView;

    //private SortList sortList = new SortList();

    public MyAdapter(final Context context, int textViewResourceId, List<Note> objects,PullListView listView) {
        super(context, textViewResourceId, objects);
        this.mynotes = objects;
        this.myContext = context;
        this.resourceId = textViewResourceId;
        this.mListView = listView;

    }


    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }



    public long getItemId(int position) {
        return position-1;
    }


    @Override
    public int getCount() {
        return mynotes.size();
    }

    @Override
    public Note getItem(int position) {
        return mynotes.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        Note note = getItem(position);
        View view ;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) view.findViewById(R.id.item_note_title);
            viewHolder.content = (TextView) view.findViewById(R.id.item_note_content);
            viewHolder.buildTime = (TextView) view.findViewById(R.id.item_build_time);
            viewHolder.ivImg = (ImageView) view.findViewById(R.id.iv_pic);
            viewHolder.ivVedio = (ImageView) view.findViewById(R.id.iv_vedio);
            view.setTag(viewHolder);
            viewHolder.title.setText(note.getTitle());
            viewHolder.content.setText(note.getContent());
            viewHolder.buildTime.setText(note.getBuildTime());
            Log.e("HELLOWORLD","noteimg:"+note.getImgPath()+"\nvediopath:"+note.getVedioPath());
            if (note.getImgPath() == null){
                viewHolder.ivImg.setVisibility(View.GONE);
            }else{
                viewHolder.ivImg.setVisibility(View.VISIBLE);
                viewHolder.ivImg.setImageBitmap(BitmapOption.getImageThumbnail(note.getImgPath(), 100, 100));
            }
            if (note.getVedioPath() == null){
                viewHolder.ivVedio.setVisibility(View.GONE);
            }else{
                viewHolder.ivVedio.setVisibility(View.VISIBLE);
                viewHolder.ivVedio.setImageBitmap(BitmapOption.getVideoThumnail(note.getVedioPath(),100,100, MediaStore.Images.Thumbnails.MICRO_KIND));
            }
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    class ViewHolder{
        TextView title ;
        TextView content ;
        TextView buildTime ;
        ImageView ivImg;
        ImageView ivVedio;
    }
}
