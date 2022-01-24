package com.demo.hardwarecodetest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.demo.hardwarecodetest.Utis.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSION_CODE = 0x0001;

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private TextView tvCurrentData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();

        initView();
    }

    private void initView() {
        findViewById(R.id.bt_random_data).setOnClickListener(this);

        tvCurrentData = findViewById(R.id.tv_current_data);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                PERMISSIONS,
                REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_random_data:
                change();
                break;
        }
    }

    private void change() {
        HookEntity entity = randomData();
        String data = Utils.entityToString(entity);
        Log.e("test", "=====changer====222==data==" + data);
        Utils.saveHookData(data);
        tvCurrentData.setText(data);

        deleteFile();
    }

    /**
     * 这里随机生成以下数据
     * imei
     * android id
     * serial
     * sim serial
     */
    private HookEntity randomData() {
        // imei: 506066104722640 / 15
        // android_id: fc4ad25f66d554a8 / 16
        // 串口序列号: aee5060e / 8
        // mac: a8:a6:68:a3:d9:ef / 12
        // 蓝牙mac: BC:1A:EA:D9:8D:98 / 10
        // ip: -123456789
        // baseband: SCL23KDU1BNG3 / 13
        // simSerial: 89860179328595969501 / 20

        HookEntity entity = new HookEntity();
        // imei 15
        String imei = Utils.getRandomWithInt(15);
        entity.setImei(imei);

        // android id 16
        String androidId = Utils.getRandomWithLittle(16);
        entity.setAndroidId(androidId);

        // serial 8
        String serial = Utils.getRandomWithLittle(8);
        entity.setSerial(serial);

        // sim serial 20
        String simSerial = Utils.getRandomWithInt(20);
        entity.setSimSerial(simSerial);

        // ip -123456789 9
        String ip = Utils.getRandomWithInt(9);
        try {
            int i = Integer.parseInt("-" + ip);
            entity.setIp(i);
        } catch (Exception e) {
            Log.e("test", "======parseInt========" + e.getMessage());
        }

        return entity;
    }

    private void cleanFile() {

    }

    private void deleteFile() {
        Utils.deleteFile();
    }

}