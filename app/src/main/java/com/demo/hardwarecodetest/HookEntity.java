package com.demo.hardwarecodetest;

public class HookEntity {
    private String imei;
    private String androidId;
    private String serial;
    private String mac;
    private String bluetoothMac;
    private String ip;
    private String baseBand;
    private String simSerial;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getBluetoothMac() {
        return bluetoothMac;
    }

    public void setBluetoothMac(String bluetoothMac) {
        this.bluetoothMac = bluetoothMac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBaseBand() {
        return baseBand;
    }

    public void setBaseBand(String baseBand) {
        this.baseBand = baseBand;
    }

    public String getSimSerial() {
        return simSerial;
    }

    public void setSimSerial(String simSerial) {
        this.simSerial = simSerial;
    }

    @Override
    public String toString() {
        return "HookEntity{" +
                "imei='" + imei + '\'' +
                ", androidId='" + androidId + '\'' +
                ", serial='" + serial + '\'' +
                ", mac='" + mac + '\'' +
                ", bluetoothMac='" + bluetoothMac + '\'' +
                ", ip='" + ip + '\'' +
                ", baseBand='" + baseBand + '\'' +
                ", simSerial='" + simSerial + '\'' +
                '}';
    }
}
