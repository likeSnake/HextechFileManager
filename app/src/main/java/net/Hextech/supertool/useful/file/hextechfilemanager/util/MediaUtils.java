package net.Hextech.supertool.useful.file.hextechfilemanager.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import net.Hextech.supertool.useful.file.hextechfilemanager.bean.ImageItem;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.VideoItem;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.ZipItem;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MediaUtils {
    public interface MediaCountListener {
        void onMediaCountObtained(ArrayList<ImageItem> imagePathList, ArrayList<VideoItem> videoPathList, ArrayList<ZipItem> zipPathList);
    }

    public static void getMediaCountsAsync(Context context, MediaCountListener listener) {
        new MediaCountAsyncTask(context, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static class MediaCountAsyncTask extends AsyncTask<Void, Void, MediaCounts> {
        private Context context;
        private MediaCountListener listener;
        ArrayList<ImageItem> imagePathList = new ArrayList<>();
        ArrayList<VideoItem> videoPathList = new ArrayList<>();
        ArrayList<ZipItem> zipPathList = new ArrayList<>();

        private MediaCountAsyncTask(Context context, MediaCountListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected MediaCounts doInBackground(Void... voids) {
            MediaCounts mediaCounts = new MediaCounts();

            Thread imageThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ContentResolver imageResolver = context.getContentResolver();
                    String[] imageProjection = {MediaStore.Images.Media.DATA};
                    Cursor imageCursor = imageResolver.query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            imageProjection,
                            null,
                            null,
                            null
                    );

                    if (imageCursor != null) {
                        // 遍历查询结果
                        while (imageCursor.moveToNext()) {
                            // 获取图片路径
                            String imagePath = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            File file = new File(imagePath);
                            if (file.exists() && file.isFile()) {
                                // 添加到图片路径列表
                                imagePathList.add(new ImageItem(imagePath));
                            }


                        }

                    }
                    mediaCounts.imagePathList = imagePathList;

                    if (imageCursor != null) {
                        imageCursor.close();
                    }
                }
            });

            Thread videoThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ContentResolver videoResolver = context.getContentResolver();
                    String[] videoProjection = {MediaStore.Video.Media.DATA};
                    Cursor videoCursor = videoResolver.query(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            videoProjection,
                            null,
                            null,
                            null
                    );
                    if (videoCursor != null) {
                        // 遍历查询结果
                        while (videoCursor.moveToNext()) {
                            // 获取图片路径
                            String imagePath = videoCursor.getString(videoCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            // 添加到图片路径列表

                            File file = new File(imagePath);
                            if (file.exists() && file.isFile()) {
                                videoPathList.add(new VideoItem(imagePath));
                            }

                        }

                    }
                    mediaCounts.videoPathList = videoPathList;
                    if (videoCursor != null) {
                        videoCursor.close();
                    }
                }
            });

            Thread compressedFileThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ContentResolver fileResolver = context.getContentResolver();
                    String[] fileProjection = {MediaStore.Files.FileColumns.DATA};
                    String fileSelection = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
                    String[] fileSelectionArgs = new String[]{"application/zip"};
                    Cursor fileCursor = fileResolver.query(
                            MediaStore.Files.getContentUri("external"),
                            fileProjection,
                            fileSelection,
                            fileSelectionArgs,
                            null
                    );
                    if (fileCursor != null) {
                        // 遍历查询结果
                        while (fileCursor.moveToNext()) {
                            String filePath = fileCursor.getString(fileCursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                            File file = new File(filePath);
                            if (file.exists() && file.isFile()) {
                                String name = file.getName();
                                long size = file.length();  // 获取压缩包的大小，单位为字节
                                double sizeMB = (double) size / (1024 * 1024);
                                String formattedSize;

                                if (sizeMB < 1) {
                                    // 如果大小小于1MB，则显示为KB
                                    double sizeKB = (double) size / 1024;
                                    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                                    formattedSize = decimalFormat.format(sizeKB) + "KB";
                                } else {
                                    // 如果大小大于等于1MB，则显示为MB
                                    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                                    formattedSize = decimalFormat.format(sizeMB) + "MB";
                                }

                                long lastModified = file.lastModified();  // 获取压缩包的最后修改时间，单位为毫秒

                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                String formattedTime = dateFormat.format(new Date(lastModified));

                                zipPathList.add(new ZipItem(filePath,formattedSize,formattedTime,name));

                            }

                        }

                    }
                    mediaCounts.zipPathList = zipPathList;
                    if (fileCursor != null) {
                        fileCursor.close();
                    }
                }
            });

            // 启动线程并等待所有线程完成
            try {
                imageThread.start();
                videoThread.start();
                compressedFileThread.start();
                imageThread.join();
                videoThread.join();
                compressedFileThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return mediaCounts;
        }


        @Override
        protected void onPostExecute(MediaCounts mediaCounts) {
            if (listener != null) {
                listener.onMediaCountObtained(
                        mediaCounts.imagePathList,
                        mediaCounts.videoPathList,
                        mediaCounts.zipPathList
                );
            }
        }
    }

    private static class MediaCounts {
        ArrayList<ImageItem> imagePathList ;
        ArrayList<VideoItem> videoPathList ;
        ArrayList<ZipItem> zipPathList  ;
    }
}