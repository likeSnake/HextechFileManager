package net.Hextech.supertool.useful.file.hextechfilemanager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import net.Hextech.supertool.useful.file.hextechfilemanager.R;
import net.Hextech.supertool.useful.file.hextechfilemanager.adapter.ImageAdapter;
import net.Hextech.supertool.useful.file.hextechfilemanager.bean.ImageItem;
import net.Hextech.supertool.useful.file.hextechfilemanager.util.AppUtils;
import net.Hextech.supertool.useful.file.hextechfilemanager.util.ImageQueryTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageAct extends AppCompatActivity implements View.OnClickListener{

    private ImageAdapter imageAdapter;
    private RecyclerView recyclerView;
    private Button bt_delete1;
    private Button bt_delete2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_photo);
        initUI();
        initData();
        initListener();
    }
    public void initUI(){
        recyclerView = findViewById(R.id.recyclerView);
        bt_delete1 = findViewById(R.id.bt_delete_no);
        bt_delete2 = findViewById(R.id.bt_delete_yes);
    }
    public void initListener(){
        bt_delete2.setOnClickListener(this);
    }
    public void initData(){
        String gs_imagePathList = MMKV.defaultMMKV().decodeString("Gs_imagePathList");
        if (gs_imagePathList!=null){
            ArrayList<ImageItem> imagePathList = new Gson().fromJson(gs_imagePathList,new TypeToken<ArrayList<ImageItem>>() {}.getType());
            startAdapter(imagePathList);
        }else {
            AppUtils.appLog("ImageItem是空的");
        }
    }
    public void startAdapter(ArrayList<ImageItem> imagePathList){
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

    public void startDelete(){
        ArrayList<ImageItem> selectImageBeans = imageAdapter.getSelectItems();
        ArrayList<Integer> integers = new ArrayList<>();

        for (int i = 0; i < selectImageBeans.size(); i++) {
            if (selectImageBeans.get(i).getSelect()){

                boolean b = deleteSingleFile(selectImageBeans.get(i).getPath());
                if (b){
                    integers.add(i);
                }
            }
        }

        for (int i = 0; i < integers.size(); i++) {
            if (i!=0){
                imageAdapter.flushedListByPosition(integers.get(i)-i);
            }
            imageAdapter.flushedListByPosition(integers.get(i));
        }

        bt_delete2.setVisibility(View.GONE);
        bt_delete1.setVisibility(View.VISIBLE);
    }

    public static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                AppUtils.appLog(" 删除单个文件" + filePath$Name + "成功！");
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_delete_yes){
            startDelete();
        }
    }
}