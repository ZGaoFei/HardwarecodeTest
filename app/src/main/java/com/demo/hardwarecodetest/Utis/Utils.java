package com.demo.hardwarecodetest.Utis;

import android.os.Environment;
import android.text.TextUtils;

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
import java.util.Random;

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
            object.put("baseBand", entity.getBaseBand());
            object.put("simSerial", entity.getSimSerial());
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
            entity.setBaseBand(object.optString("baseBand"));
            entity.setSimSerial(object.optString("simSerial"));
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

    // 只返回数字
    public static String getRandomWithInt(int length) {
        String str = "0123456789";
        return getRandom(str, length);
    }

    // 带数字和小写字母
    public static String getRandomWithLittle(int length) {
        String str = "abcdefghijklmnopqrstuvwxyz0123456789";
        return getRandom(str, length);
    }

    // 带数字和大写字母和小写字母
    public static String getRandomWithString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return getRandom(str, length);
    }

    public static String getRandom(String parse, int length) {
        if (TextUtils.isEmpty(parse)) {
            return "";
        }
        int l = parse.length();
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(l);
            sb.append(parse.charAt(number));
        }
        return sb.toString();
    }

    public static void deleteFile() {
        File file = Environment.getExternalStorageDirectory();
        String android = file.getAbsolutePath() + "/Android";
        String netease = file.getAbsolutePath() + "/netease";
        String tencent = file.getAbsolutePath() + "/Tencent";

        // 删除Android目录下的文件夹
        deleteDirWithFile(new File(android), false);

        // 删除netease
        deleteDirWithFile(new File(netease), true);

        // 删除Tencent
        deleteDirWithFile(new File(tencent), true);
    }

    //删除文件夹和文件夹里面的文件
    public static void deleteDirWithFile(File dir, boolean isDeleteRootFile) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete(); // 删除所有文件
            } else if (file.isDirectory()) {
                deleteDirWithFile(file, true); // 递规的方式删除文件夹
            }
        }
        if (isDeleteRootFile) {
            dir.delete();// 删除目录本身
        }
    }

}
