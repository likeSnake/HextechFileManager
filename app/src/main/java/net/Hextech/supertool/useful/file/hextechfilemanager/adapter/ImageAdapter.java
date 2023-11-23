package net.Hextech.supertool.useful.file.hextechfilemanager.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;


import net.Hextech.supertool.useful.file.hextechfilemanager.R;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.ImageItem;
import net.Hextech.supertool.useful.file.hextechfilemanager.util.AppUtils;

import java.util.ArrayList;
import java.util.Collections;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.PhotoViewHolder> {

    private Context context;
    private ArrayList<ImageItem> photoPaths;
    private int selectedPosition;
    private ItemClickListener listener;
    private int selects=0;
    private ArrayList<String> select_list = new ArrayList<>();
    private boolean b = false;

    public ImageAdapter(Context context, ArrayList<ImageItem> photoPaths, ItemClickListener listener) {
        this.context = context;
        this.photoPaths = photoPaths;
        Collections.reverse(this.photoPaths);
        this.listener = listener;
    }
    @Override
    public PhotoViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder( PhotoViewHolder holder, int position) {
        ImageItem photoBean = photoPaths.get(position);


        Glide.with(context)
                .load(photoBean.getPath())
                .into(holder.ivPhoto);
        //
        if (b){
            if (!photoBean.getSelect()){
                holder.select.setImageResource(R.mipmap.select_no);
            }else {
                holder.select.setImageResource(R.mipmap.select_yes);
            }
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b = true;
                selectedPosition = holder.getAdapterPosition();
                if (photoBean.getSelect()){
                    selects--;
                    select_list.remove(photoBean.getPath());
                    photoBean.setSelect(false);
                    holder.select.setImageResource(R.mipmap.select_no);
                }else {
                    selects++;
                    photoBean.setSelect(true);
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
        return photoPaths.size();
    }


    public ArrayList<ImageItem> getSelectItems(){

        return photoPaths;
    }

    public void flushedListByPosition (int position){
        selects=0;
        AppUtils.appLog("移除的Item:"+position);
        try {
            photoPaths.remove(position);
            notifyItemRemoved(position);
        }catch (Exception e){
            Log.e("移除item时候出错",e.getMessage());
        }
    }

    public void setNoe(){

    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        ImageView select;
        RelativeLayout layout;

        public PhotoViewHolder( View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            select = itemView.findViewById(R.id.select);
            layout = itemView.findViewById(R.id.layout);

        }
    }

    public interface ItemClickListener{
        void onClick();
        void Deselect();
    }
}