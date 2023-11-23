package net.Hextech.supertool.useful.file.hextechfilemanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import net.Hextech.supertool.useful.file.hextechfilemanager.R;
import net.Hextech.supertool.useful.file.hextechfilemanager.adapter.ImageAdapter;
import net.Hextech.supertool.useful.file.hextechfilemanager.adapter.VideoAdapter;
import net.Hextech.supertool.useful.file.hextechfilemanager.adapter.ZipAdapter;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.ImageItem;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.VideoItem;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.ZipItem;
import net.Hextech.supertool.useful.file.hextechfilemanager.util.AppUtils;

import java.io.File;
import java.util.ArrayList;

public class ZipAct extends AppCompatActivity implements View.OnClickListener{

    private ImageAdapter imageAdapter;
    private VideoAdapter videoAdapter;
    private ZipAdapter zipAdapter;
    private RecyclerView recyclerView;
    private Button bt_delete1;
    private Button bt_delete2;
    private String myFlag = "";
    private ImageView ic_back;
    private TextView layout_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_zip);
        initUI();
        initData();
        initListener();
    }
    public void initUI(){
        recyclerView = findViewById(R.id.recyclerView);
        bt_delete1 = findViewById(R.id.delete_no);
        bt_delete2 = findViewById(R.id.delete_yes);
        ic_back = findViewById(R.id.ic_back);
        layout_title = findViewById(R.id.layout_title);
    }
    public void initListener(){
        bt_delete2.setOnClickListener(this);
        ic_back.setOnClickListener(this);
    }
    public void initData(){
        myFlag = "";
        myFlag = getIntent().getStringExtra("MyFlag");
        switch (myFlag){
            case "image_layout":
                layout_title.setText("Image");
                String gs_imagePathList = MMKV.defaultMMKV().decodeString("Gs_imagePathList");
                if (gs_imagePathList!=null){
                    ArrayList<ImageItem> imagePathList = new Gson().fromJson(gs_imagePathList,new TypeToken<ArrayList<ImageItem>>() {}.getType());

                   // ArrayList<ImageItem> imagePathList = new ArrayList<>();
                    startAdImageApter(imagePathList);
                }else {
                    AppUtils.appLog("ImageItem是空的");
                }

                break;
            case "video_layout":
                layout_title.setText("Video");
                String Gs_videoPathList = MMKV.defaultMMKV().decodeString("Gs_videoPathList");
                if (Gs_videoPathList!=null){
                    ArrayList<VideoItem> videoItemArrayList = new Gson().fromJson(Gs_videoPathList,new TypeToken<ArrayList<VideoItem>>() {}.getType());
                    startAdVideoApter(videoItemArrayList);
                }else {
                    AppUtils.appLog("ImageItem是空的");
                }

                break;
            case "zip_layout":
                layout_title.setText("Zip");
                String Gs_zipPathList = MMKV.defaultMMKV().decodeString("Gs_zipPathList");
                if (Gs_zipPathList!=null){
                    ArrayList<ZipItem> zipItems = new Gson().fromJson(Gs_zipPathList,new TypeToken<ArrayList<ZipItem>>() {}.getType());
                    startAdZipApter(zipItems);
                }else {
                    AppUtils.appLog("ImageItem是空的");
                }
                break;
        }


    }
    public void startAdVideoApter(ArrayList<VideoItem> videoItemArrayList){
       // AppUtils.appLog(videoItemArrayList.get(0).getPath());

        videoAdapter = new VideoAdapter(this, videoItemArrayList, new ImageAdapter.ItemClickListener() {
            @Override
            public void onClick() {
                bt_delete2.setVisibility(View.VISIBLE);
                bt_delete1.setVisibility(View.GONE);
            }

            @Override
            public void Deselect() {
                bt_delete2.setVisibility(View.GONE);
                bt_delete1.setVisibility(View.VISIBLE);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(videoAdapter);

    }

    public void startAdImageApter(ArrayList<ImageItem> imagePathList){
        imageAdapter = new ImageAdapter(this, imagePathList, new ImageAdapter.ItemClickListener() {
            @Override
            public void onClick() {
                bt_delete2.setVisibility(View.VISIBLE);
                bt_delete1.setVisibility(View.GONE);
            }

            @Override
            public void Deselect() {
                bt_delete2.setVisibility(View.GONE);
                bt_delete1.setVisibility(View.VISIBLE);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(imageAdapter);

    }

    public void startAdZipApter(ArrayList<ZipItem> imagePathList){
        zipAdapter = new ZipAdapter(this, imagePathList, new ImageAdapter.ItemClickListener() {
            @Override
            public void onClick() {
                bt_delete2.setVisibility(View.VISIBLE);
                bt_delete1.setVisibility(View.GONE);
            }

            @Override
            public void Deselect() {
                bt_delete2.setVisibility(View.GONE);
                bt_delete1.setVisibility(View.VISIBLE);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(zipAdapter);

    }

    public void VideoItemDelete(){
        ArrayList<VideoItem> selectImageBeans = videoAdapter.getSelectItems();
        ArrayList<Integer> integers = new ArrayList<>();

        for (int i = 0; i < selectImageBeans.size(); i++) {
            if (selectImageBeans.get(i).getSelect()){
                integers.add(i);
                boolean b = deleteSingleFile(this,selectImageBeans.get(i).getPath());
                if (b){

                }
            }
        }

        for (int i = 0; i < integers.size(); i++) {
            if (i!=0){
                videoAdapter.flushedListByPosition(integers.get(i)-i);
            }else {
                videoAdapter.flushedListByPosition(integers.get(i));
            }

        }

        bt_delete2.setVisibility(View.GONE);
        bt_delete1.setVisibility(View.VISIBLE);
    }

    public void ImageItemDelete(){
        ArrayList<ImageItem> selectImageBeans = imageAdapter.getSelectItems();
        ArrayList<Integer> integers = new ArrayList<>();

        for (int i = 0; i < selectImageBeans.size(); i++) {
            if (selectImageBeans.get(i).getSelect()){
                integers.add(i);
                boolean b = deleteSingleFile(this,selectImageBeans.get(i).getPath());
                if (b){

                }
            }
        }

        for (int i = 0; i < integers.size(); i++) {
            AppUtils.appLog("现在删除的序号:"+i);
            if (i!=0){
                imageAdapter.flushedListByPosition(integers.get(i)-i);
            }else {
                imageAdapter.flushedListByPosition(integers.get(i));
            }

        }

        bt_delete2.setVisibility(View.GONE);
        bt_delete1.setVisibility(View.VISIBLE);
    }

    public void ZipItemDelete(){
        ArrayList<ZipItem> selectImageBeans = zipAdapter.getSelectItems();
        ArrayList<Integer> integers = new ArrayList<>();

        for (int i = 0; i < selectImageBeans.size(); i++) {
            if (selectImageBeans.get(i).getSelect()){
                integers.add(i);
                boolean b = deleteSingleFile(this,selectImageBeans.get(i).getPath());

            }
        }

        for (int i = 0; i < integers.size(); i++) {
            if (i!=0){


                zipAdapter.flushedListByPosition(integers.get(i)-i);
            }else {
                zipAdapter.flushedListByPosition(integers.get(i));
            }

        }

        bt_delete2.setVisibility(View.GONE);
        bt_delete1.setVisibility(View.VISIBLE);
    }

    public static boolean deleteSingleFile(Context context,String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                AppUtils.appLog(" 删除单个文件" + filePath$Name + "成功！");
                notifyMediaStoreUpdate(context, filePath$Name);
                return true;
            } else {
                AppUtils.appLog("删除单个文件" + filePath$Name + "失败！");
                return false;
            }

        } else {
            AppUtils.appLog( "删除单个文件失败：" + filePath$Name + "不存在！");
            return false;
        }
    }
    private static void notifyMediaStoreUpdate(Context context, String filePath) {
        android.media.MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                (path, uri) -> {
                    // 扫描完成后的回调
                });
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete_yes){
            switch (myFlag){
                case "image_layout":
                    ImageItemDelete();
                    break;
                case "video_layout":
                    VideoItemDelete();
                    break;
                case "zip_layout":
                    ZipItemDelete();
                    break;
            }
        }
        if (v.getId() == R.id.ic_back){
            finish();
        }
    }
}