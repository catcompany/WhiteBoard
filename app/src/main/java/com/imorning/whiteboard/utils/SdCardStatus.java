package com.imorning.whiteboard.utils;

import android.os.Environment;

import java.io.File;


public class SdCardStatus {
    private static String CACHE_FOLDER_NAME;

    public static void init(String cacheFolderName) {
        CACHE_FOLDER_NAME = cacheFolderName;
    }

    /**
     * get default cache dir in sdcard,if the sdcard doesn't exist,return null
     *
     * @return cache dir in sdcard or null
     */
    public static String getDefaultCacheDirInSdcard() {
        String rootDir;
        rootDir = getRootDir();
        if (null == rootDir) {
            return null;
        }
        return rootDir + File.separator + CACHE_FOLDER_NAME;
    }

    /**
     * when not exist sd card,return null.
     *
     * @return sdcard path or null
     */
    public static String getRootDir() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return null;
        }
    }
}
