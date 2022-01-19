package com.demo.hardwarecodetest.Hook;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.demo.hardwarecodetest.HookEntity;
import com.demo.hardwarecodetest.Utis.SharedPref;
import com.demo.hardwarecodetest.Utis.Utils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class Phone {
    public Phone(XC_LoadPackage.LoadPackageParam sharePkgParam) {
        String hookData = Utils.getHookData();
        HookEntity hookEntity = Utils.stringToEntity(hookData);

        // getType(sharePkgParam);
        // Bluetooth(sharePkgParam, hookEntity);
        // Wifi(sharePkgParam, hookEntity);
        Telephony(sharePkgParam, hookEntity);
    }

    // 联网方式
    public void getType(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod("android.net.NetworkInfo", loadPackageParam.classLoader, "getType",
                new XC_MethodHook() {
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        param.setResult(SharedPref.getintXValue("getType"));
                    }

                    ;


                });
    }

    // ------- MAC 蓝牙-----------------------------------------------------------
    public void Bluetooth(XC_LoadPackage.LoadPackageParam loadPkgParam, HookEntity hookEntity) {
        try {
            // 双层 MAC
            XposedHelpers.findAndHookMethod(
                    "android.bluetooth.BluetoothAdapter",
                    loadPkgParam.classLoader, "getAddress",
                    new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            // TODO Auto-generated method stub
                            super.afterHookedMethod(param);
                            param.setResult(hookEntity.getBluetoothMac());
                        }

                    });
            // 双层MAC
            XposedHelpers.findAndHookMethod(
                    "android.bluetooth.BluetoothDevice",
                    loadPkgParam.classLoader, "getAddress",
                    new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            // TODO Auto-generated method stub
                            super.afterHookedMethod(param);
                            param.setResult(hookEntity.getBluetoothMac());
                        }

                    });
        } catch (Exception e) {
            XposedBridge.log("phone MAC HOOK 失败 " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------------

    // WIF MAC
    public void Wifi(XC_LoadPackage.LoadPackageParam loadPkgParam, HookEntity hookEntity) {
        try {
            XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo",
                    loadPkgParam.classLoader, "getMacAddress",
                    new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            // TODO Auto-generated method stub
                            super.afterHookedMethod(param);
                            param.setResult(hookEntity.getMac());
                        }

                    });

            // 内网IP
            XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo",
                    loadPkgParam.classLoader, "getIpAddress",
                    new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            // TODO Auto-generated method stub
                            super.afterHookedMethod(param);
                            param.setResult(hookEntity.getIp());
                            // param.setResult(tryParseInt(SharedPref.getXValue("getIP")));
                        }
                    });

            /*
            XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo",
                    loadPkgParam.classLoader, "getSSID", new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            // TODO Auto-generated method stub
                            super.afterHookedMethod(param);
                            param.setResult(SharedPref.getXValue("WifiName"));
                        }

                    });
             */
        } catch (Exception e) {

        }

        // ------------------------基站信息
        // 基站的信号强度
        /*
        XposedHelpers.findAndHookMethod("android.net.wifi.WifiInfo",
                loadPkgParam.classLoader, "getBSSID", new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                        // TODO Auto-generated method stub
                        super.afterHookedMethod(param);
                        param.setResult(SharedPref.getXValue("BSSID"));
                    }
                });
         */
    }

    public void Telephony(XC_LoadPackage.LoadPackageParam loadPkgParam, HookEntity hookEntity) {
        String TelePhone = "android.telephony.TelephonyManager";
        try {
            // String imei = SharedPref.getXValue("IMEI");
            String imei = hookEntity.getImei();
            if (Build.VERSION.SDK_INT < 22) {
                XposedHelpers.findAndHookMethod("com.android.internal.telephony.gsm.GSMPhone", loadPkgParam.classLoader, "getDeviceId", XC_MethodReplacement.returnConstant(imei));
                XposedHelpers.findAndHookMethod("com.android.internal.telephony.PhoneProxy", loadPkgParam.classLoader, "getDeviceId", XC_MethodReplacement.returnConstant(imei));
            } else if (Build.VERSION.SDK_INT == 22) {
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", loadPkgParam.classLoader, "getDeviceId", XC_MethodReplacement.returnConstant(imei));
                XposedHelpers.findAndHookMethod("com.android.internal.telephony.PhoneSubInfo", loadPkgParam.classLoader, "getDeviceId", XC_MethodReplacement.returnConstant(imei));
            } else if (Build.VERSION.SDK_INT == 23) {
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", loadPkgParam.classLoader, "getDeviceId", XC_MethodReplacement.returnConstant(imei));
                XposedHelpers.findAndHookMethod("com.android.internal.telephony.PhoneSubInfo", loadPkgParam.classLoader, "getDeviceId", String.class, XC_MethodReplacement.returnConstant(imei));
            } else if (Build.VERSION.SDK_INT >= 24) {
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", loadPkgParam.classLoader, "getDeviceId", XC_MethodReplacement.returnConstant(imei));
                XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", loadPkgParam.classLoader, "getImei", XC_MethodReplacement.returnConstant(imei));
                XposedHelpers.findAndHookMethod("com.android.internal.telephony.PhoneSubInfoController", loadPkgParam.classLoader, "getDeviceIdForPhone", int.class, String.class, XC_MethodReplacement.returnConstant(imei));
                XposedHelpers.findAndHookMethod("com.android.internal.telephony.PhoneSubInfoController", loadPkgParam.classLoader, "getImeiForSubscriber", int.class, String.class, XC_MethodReplacement.returnConstant(imei));
            }
        } catch (Exception ex) {
            XposedBridge.log(" IMEI 错误: " + ex.getMessage());
        }

        HookTelephony(TelePhone, loadPkgParam, "getSimSerialNumber",
                hookEntity.getSimSerial());

        /*
        HookTelephony(TelePhone, loadPkgParam, "getDeviceSoftwareVersion",
                SharedPref.getXValue("deviceversion"));// 返系统版本
        HookTelephony(TelePhone, loadPkgParam, "getSubscriberId",
                SharedPref.getXValue("IMSI"));
        HookTelephony(TelePhone, loadPkgParam, "getLine1Number",
                SharedPref.getXValue("PhoneNumber"));
        HookTelephony(TelePhone, loadPkgParam, "getSimSerialNumber",
                SharedPref.getXValue("SimSerial"));
        HookTelephony(TelePhone, loadPkgParam, "getNetworkOperator",
                SharedPref.getXValue("networktor")); // 网络运营商类型
        HookTelephony(TelePhone, loadPkgParam, "getNetworkOperatorName",
                SharedPref.getXValue("Carrier")); // 网络类型名
        HookTelephony(TelePhone, loadPkgParam, "getSimOperator",
                SharedPref.getXValue("CarrierCode")); // 运营商
        HookTelephony(TelePhone, loadPkgParam, "getSimOperatorName",
                SharedPref.getXValue("simopename")); // 运营商名字
        HookTelephony(TelePhone, loadPkgParam, "getNetworkCountryIso",
                SharedPref.getXValue("gjISO")); // 国家iso代码
        HookTelephony(TelePhone, loadPkgParam, "getSimCountryIso",
                SharedPref.getXValue("CountryCode")); // 手机卡国家

        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", loadPkgParam.classLoader, "getNetworkType", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param)
                    throws Throwable {
                // TODO Auto-generated method stub
                super.afterHookedMethod(param);
                //      网络类型
                param.setResult(SharedPref.getintXValue("networkType"));

            }

        });

        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager",
                loadPkgParam.classLoader, "getPhoneType", new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                        // TODO Auto-generated method stub
                        super.afterHookedMethod(param);

                        param.setResult(SharedPref.getintXValue("phonetype"));
                    }
                });

        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager",
                loadPkgParam.classLoader, "getSimState", new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                        // TODO Auto-generated method stub
                        super.afterHookedMethod(param);

                        param.setResult(SharedPref.getintXValue("SimState"));
                    }
                });
         */
    }

    private void HookTelephony(String hookClass, XC_LoadPackage.LoadPackageParam loadPkgParam,
                               String funcName, final String value) {
        try {
            XposedHelpers.findAndHookMethod(hookClass,
                    loadPkgParam.classLoader, funcName, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            // TODO Auto-generated method stub
                            super.afterHookedMethod(param);
                            param.setResult(value);
                        }
                    });
        } catch (Exception e) {
        }
    }
}