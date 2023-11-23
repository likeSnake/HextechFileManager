package net.Hextech.supertool.useful.file.hextechfilemanager.activity;

import static net.Hextech.supertool.useful.file.hextechfilemanager.util.AppUtils.checkAllPermission;
import static net.Hextech.supertool.useful.file.hextechfilemanager.util.AppUtils.getAvailableStorageSpace;
import static net.Hextech.supertool.useful.file.hextechfilemanager.util.AppUtils.getTotalStorageSpace;
import static net.Hextech.supertool.useful.file.hextechfilemanager.util.AppUtils.getTotalStorageSpaces;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import net.Hextech.supertool.useful.file.hextechfilemanager.R;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.ImageItem;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.VideoItem;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.ZipItem;
import net.Hextech.supertool.useful.file.hextechfilemanager.util.AppUtils;
import net.Hextech.supertool.useful.file.hextechfilemanager.util.MediaUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainAct extends AppCompatActivity implements View.OnClickListener{
    private ProgressBar scanProgressBar;
    private TextView textMemory,image_text,video_text,zip_text,bt_text;
    private RelativeLayout image_layout,video_layout,zip_layout;
    private int progressStatus;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initUI();
        initData();
        initListener();

    }

    public void initUI(){
        textMemory = findViewById(R.id.textMemory);
        scanProgressBar = findViewById(R.id.scanProgressBar);
        image_text = findViewById(R.id.image_text);
        video_text = findViewById(R.id.video_text);
        zip_text = findViewById(R.id.zip_text);
        bt_text = findViewById(R.id.bt_text);
        image_layout = findViewById(R.id.image_layout);
        video_layout = findViewById(R.id.video_layout);
        zip_layout = findViewById(R.id.zip_layout);

    }
    public void initData(){

    }

    public void queryProcessing(){
        bt_text.setText("Scanning");
        startScan();
        // 创建DecimalFormat对象，并设置保留两位小数的格式
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

// 使用格式化器格式化数字
        String formattedNumber = decimalFormat.format((getTotalStorageSpace()-getAvailableStorageSpace()));
        String info = formattedNumber +" / "+ getTotalStorageSpaces();
        textMemory.setText(info+" Memory Used");
        MediaUtils.getMediaCountsAsync(this, new MediaUtils.MediaCountListener() {
            @Override
            public void onMediaCountObtained(ArrayList<ImageItem> imagePathList, ArrayList<VideoItem> videoPathList, ArrayList<ZipItem> zipPathList) {
                int imageCount = imagePathList.size();
                int videoCount = videoPathList.size();
                int compressedFileCount = zipPathList.size();

                String Gs_imagePathList = new Gson().toJson(imagePathList);
                String Gs_videoPathList = new Gson().toJson(videoPathList);
                String Gs_zipPathList = new Gson().toJson(zipPathList);

                MMKV.defaultMMKV().encode("Gs_imagePathList",Gs_imagePathList);
                MMKV.defaultMMKV().encode("Gs_videoPathList",Gs_videoPathList);
                MMKV.defaultMMKV().encode("Gs_zipPathList",Gs_zipPathList);

                AppUtils.appLog("imageCount:"+ imageCount +"videoCount:"+ videoCount +"compressedFileCount:"+ compressedFileCount);
                image_text.setText(String.valueOf(imageCount));
                video_text.setText(String.valueOf(videoCount));
                zip_text.setText(String.valueOf(compressedFileCount));

                countDownTimer.cancel();
                scanQuickFill();
                bt_text.setText("Start Scan");
                AppUtils.appLog(scanProgressBar.getProgress());
            }


        });
    }
    public void initListener(){
        scanProgressBar.setOnClickListener(this);
        image_layout.setOnClickListener(this);
        video_layout.setOnClickListener(this);
        zip_layout.setOnClickListener(this);
    }
    public void startScan(){
        progressStatus=0;
        countDownTimer = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressStatus++;
                scanProgressBar.setProgress(progressStatus);
            }


            @Override
            public void onFinish() {

            }
        }.start();
    }
    public void scanQuickFill(){
        new CountDownTimer(20000, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressStatus++;
                scanProgressBar.setProgress(progressStatus);
            }


            @Override
            public void onFinish() {

            }
        }.start();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.scanProgressBar){
            queryProcessing();
        }
        if (v.getId() == R.id.image_layout){
            Intent intent = new Intent(MainAct.this, ZipAct.class);
            intent.putExtra("MyFlag","image_layout");
            startActivity(intent);
        }
        if (v.getId() == R.id.video_layout){
            Intent intent = new Intent(MainAct.this, ZipAct.class);
            intent.putExtra("MyFlag","video_layout");
            startActivity(intent);
        }
        if (v.getId() == R.id.zip_layout){
            Intent intent = new Intent(MainAct.this, ZipAct.class);
            intent.putExtra("MyFlag","zip_layout");
            startActivity(intent);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryProcessing();
    }
}