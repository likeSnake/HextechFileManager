package net.Hextech.supertool.useful.file.hextechfilemanager.adapter;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import net.Hextech.supertool.useful.file.hextechfilemanager.R;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.VideoItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.PhotoViewHolder> {

    private ImageAdapter.ItemClickListener listener;
    private int selects=0;
    private int selectedPosition;
    private Context context;
    private ArrayList<VideoItem> videoBeans;
    private ArrayList<String> select_list = new ArrayList<>();
    private boolean b = false;

    public VideoAdapter(Context context, ArrayList<VideoItem> videoBeans, ImageAdapter.ItemClickListener listener) {
        this.context = context;
        this.videoBeans = videoBeans;
        this.listener = listener;

        Collections.reverse(this.videoBeans);
    }


    @Override
    public PhotoViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder( PhotoViewHolder holder, int position) {
        VideoItem videoBean = videoBeans.get(position);



        Glide.with(context)
                .asBitmap()
                .load(videoBean.getPath())
                .frame(TimeUnit.SECONDS.toMicros(1))
                .into(holder.ivPhoto);

        if (b){
            if (!videoBean.getSelect()){
                holder.select.setImageResource(R.mipmap.select_no);
            }else {
                holder.select.setImageResource(R.mipmap.select_yes);
            }
        }

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b = true;
                selectedPosition = holder.getAdapterPosition();
                if (videoBean.getSelect()){
                    selects--;
                    select_list.remove(videoBean.getPath());
                    videoBean.setSelect(false);
                    holder.select.setImageResource(R.mipmap.select_no);
                }else {
                    selects++;
                    videoBean.setSelect(true);
                    holder.select.setImageResource(R.mipmap.select_yes);

                }
                if (selects!=0){
                    listener.onClick();
                }else {
                    listener.Deselect();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return videoBeans.size();
    }


    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        ImageView select;

        public PhotoViewHolder( View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            select = itemView.findViewById(R.id.select);

        }
    }
    public ArrayList<VideoItem> getSelectItems(){

        return videoBeans;
    }

    public void flushedListByPosition (int position){
        selects=0;


        try {
            videoBeans.remove(position);
            notifyItemRemoved(position);
        }catch (Exception e){
            Log.e("移除item时候出错",e.getMessage());
        }

    }
}