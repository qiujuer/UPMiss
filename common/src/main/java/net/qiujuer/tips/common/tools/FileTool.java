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

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by JuQiu
 * on 16/6/25.
 */

public class FileTool {
    public static String getExternalStoragePath() {
        if (Environment.getExternalStorageState() == null || !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "UPMiss";
    }

    public static String getExternalStorageImagePath() {
        if (Environment.getExternalStorageState() == null || !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath() + File.separator + "UPMiss";
    }


    public static String saveBitmap(Bitmap bmp, String dirName, String fileName) {
        String extPath = getExternalStorageImagePath();
        if (extPath == null)
            return null;

        String dirPath = extPath;
        if (!TextUtils.isEmpty(dirName))
            dirPath = extPath + File.separator + dirName;

        File dir = new File(dirPath);
        if (!dir.isDirectory() && !dir.mkdirs()) {
            return null;
        }

        FileOutputStream fos = null;
        try {
            File file = new File(dirPath, fileName);
            if (!file.exists() && !file.createNewFile()) {
                return null;
            }
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;

    }
}
