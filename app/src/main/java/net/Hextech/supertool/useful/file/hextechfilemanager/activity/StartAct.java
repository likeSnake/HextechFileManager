package net.Hextech.supertool.useful.file.hextechfilemanager.activity;



import static net.Hextech.supertool.useful.file.hextechfilemanager.util.AppUtils.checkAllPermission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;

import com.tencent.mmkv.MMKV;

import net.Hextech.supertool.useful.file.hextechfilemanager.R;
import net.Hextech.supertool.useful.file.hextechfilemanager.util.AppUtils;


public class StartAct extends AppCompatActivity {
    private int REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMKV.initialize(this);
        setContentView(R.layout.act_start);
        initUI();
        initData();

    }

    public void initUI(){

    }



    public void initData(){
        CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }


            @Override
            public void onFinish() {
                if(checkAllPermission(StartAct.this,REQUEST_CODE)){
                    startMain();
                }
            }
        }.start();
    }

    public void startMain(){
        startActivity(new Intent(StartAct.this,MainAct.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            AppUtils.appLog("onRequestPermissionsResult");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startMain();
            } else {
                //  Toast.makeText(this, "请给存储权限，更新应用", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            AppUtils.appLog("onActivityResult");
            if (Environment.isExternalStorageManager()) {
                startMain();
            } else {
                //    Toast.makeText(this, "请给存储权限，更新应用", Toast.LENGTH_SHORT).show();
            }
        }

    }
}