package net.Hextech.supertool.useful.file.hextechfilemanager.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

public class ImageQueryTask extends AsyncTask<Void, Void, ArrayList<String>> {
    private Context mContext;
    private OnImageQueryCompleteListener mCompleteListener;


    public ImageQueryTask(Context context, OnImageQueryCompleteListener completeListener) {
        mContext = context;
        mCompleteListener = completeListener;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> imagePathList = new ArrayList<>();

        // 定义查询的URI和要返回的列
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.Media.DATA };

        // 执行查询
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            // 遍历查询结果
            while (cursor.moveToNext()) {
                // 获取图片路径
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                // 添加到图片路径列表
                imagePathList.add(imagePath);
            }

            // 关闭游标
            cursor.close();
        }

        return imagePathList;
    }

    @Override
    protected void onPostExecute(ArrayList<String> imagePathList) {
        if (mCompleteListener != null) {
            // 通知查询完成，将结果传递给监听器
            mCompleteListener.onImageQueryComplete(imagePathList);
        }
    }

    public interface OnImageQueryCompleteListener {
        void onImageQueryComplete(ArrayList<String> imagePathList);
    }
}