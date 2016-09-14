/*
 * Copyright (C) 2014-2016 Qiujuer <qiujuer@live.cn>
 * WebSite http://www.qiujuer.net
 * Author qiujuer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.qiujuer.tips.common.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

/**
 * Created by JuQiu
 * on 16/6/30.
 */

public final class SysTool {
    /**
     * Equipment is started for the first time the generated number
     * Are potential "9774d56d682e549c"
     *
     * @param context Context
     * @return Number
     */
    public static String getAndroidId(Context context) {
        return android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    /**
     * This device's SN
     *
     * @return SerialNumber
     */
    public static String getSerialNumber() {
        String serialNumber = android.os.Build.SERIAL;
        if ((serialNumber == null || serialNumber.length() == 0 || serialNumber.contains("unknown"))) {
            String[] keys = new String[]{"ro.boot.serialno", "ro.serialno"};
            for (String key : keys) {
                try {
                    Method systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
                    serialNumber = (String) systemProperties_get.invoke(null, key);
                    if (serialNumber != null && serialNumber.length() > 0 && !serialNumber.contains("unknown"))
                        break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return serialNumber;
    }

    /**
     * Get TelephonyManager DeviceId
     * This Need READ_PHONE_STATE permission
     *
     * @param context Context
     * @return DeviceId
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }
}
