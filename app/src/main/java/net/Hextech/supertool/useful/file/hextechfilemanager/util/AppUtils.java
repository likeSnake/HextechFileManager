package net.Hextech.supertool.useful.file.hextechfilemanager.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class AppUtils {
    public static void appLog(Object o){
        System.out.println("--**--**--"+o);
    }


    public static boolean checkAllPermission(Activity activity,int REQUEST_CODE){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                return true;
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, REQUEST_CODE);
                return false;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 先判断有没有权限
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
                return false;
            }
        } else {
            return true;
        }
    }

    public static double getTotalStorageSpaces() {
        String path = Environment.getExternalStorageDirectory().getPath();
        StatFs stat = new StatFs(path);
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long totalBytes = totalBlocks * blockSize;
        double totalGB = (double) totalBytes / (1024 * 1024 * 1024); // 转换为GB
        return Math.round(totalGB * 100) / 100.0; // 保留两位小数
    }

    public static double getTotalStorageSpace() {
        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path);
        long totalBytes = file.getTotalSpace();
        double totalGB = (double) totalBytes / (1024 * 1024 * 1024); // 转换为GB
        return Math.round(totalGB * 100) / 100.0; // 保留两位小数
    }

    public static double getAvailableStorageSpace() {
        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path);
        long freeBytes = file.getFreeSpace();
        double freeGB = (double) freeBytes / (1024 * 1024 * 1024); // 转换为GB
        return Math.round(freeGB * 100) / 100.0; // 保留两位小数
    }
}
