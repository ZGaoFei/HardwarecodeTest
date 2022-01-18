package com.demo.hardwarecodetest.Utis;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.demo.hardwarecodetest.HookEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public final class Utils {
    private static final String CACHE_FILE_NAME = "/clean2.txt";

    private Utils() {
    }

    // 将entity转换为String
    public static String entityToString(HookEntity entity) {
        if (entity == null) {
            return "";
        }
        try {
            JSONObject object = new JSONObject();
            object.put("imei", entity.getImei());
            object.put("androidId", entity.getAndroidId());
            object.put("serial", entity.getSerial());
            object.put("mac", entity.getMac());
            object.put("bluetoothMac", entity.getBluetoothMac());
            object.put("ip", entity.getIp());
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    // 将String转换为HookEntity
    public static HookEntity stringToEntity(String data) {
        if (TextUtils.isEmpty(data)) {
            return null;
        }
        try {
            HookEntity entity = new HookEntity();
            JSONObject object = new JSONObject(data);
            entity.setImei(object.optString("imei"));
            entity.setAndroidId(object.optString("androidId"));
            entity.setSerial(object.optString("serial"));
            entity.setMac(object.optString("mac"));
            entity.setBluetoothMac(object.optString("bluetoothMac"));
            entity.setIp(object.optString("ip"));
            return entity;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 将HookEntity的字符串缓存到文件中
    public static void saveHookData(String data) {
        File file = Environment.getExternalStorageDirectory();
        try {
            FileWriter fw = new FileWriter(file.getAbsolutePath() + CACHE_FILE_NAME);
            fw.flush();
            fw.write(data);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 从文件中取出HookEntity的字符串
    public static String getHookData() {
        File file = Environment.getExternalStorageDirectory();
        String path = file.getAbsolutePath() + CACHE_FILE_NAME;
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = new FileInputStream(path);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    public static void saveDeviceIdToFile(String deviceId) {
        File file = Environment.getExternalStorageDirectory();
        try {
            FileWriter fw = new FileWriter(file.getAbsolutePath() + CACHE_FILE_NAME);
            fw.flush();
            fw.write(deviceId);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDeviceIdFromFile() {
        File file = Environment.getExternalStorageDirectory();
        String path = file.getAbsolutePath() + CACHE_FILE_NAME;
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = new FileInputStream(path);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    public static String getIMEI(Context context) {
        if (PackageManager.PERMISSION_GRANTED !=
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "";
        }
        String imei = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                imei = telephonyManager.getImei();
//            } else {
//                imei = telephonyManager.getDeviceId();
//            }
            String a = telephonyManager.getImei();
            Log.e("test", "=======getImei=====" + a);
            imei = telephonyManager.getDeviceId();
            Log.e("test", "=======getDeviceId=======" + imei);
        }
        return imei != null ? imei : "";
    }

    public static boolean getRoot(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); // 切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }
}
