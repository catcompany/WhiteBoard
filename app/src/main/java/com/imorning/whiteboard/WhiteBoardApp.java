package com.imorning.whiteboard;

import android.app.Application;

import com.imorning.whiteboard.utils.AppContextUtil;
import com.imorning.whiteboard.utils.OperationUtils;
import com.imorning.whiteboard.utils.SdCardStatus;
import com.imorning.whiteboard.utils.StoreUtil;

public class WhiteBoardApp extends Application {

    private static String filePath;
    private static String fileTitle;
    private static boolean isModified;

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        WhiteBoardApp.filePath = filePath;
    }

    public static String getFileTitle() {
        return fileTitle;
    }

    public static void setFileTitle(String fileTitle) {
        WhiteBoardApp.fileTitle = fileTitle;
    }

    public static boolean getIsModified() {
        return isModified;
    }

    public static void setIsModified(boolean isModified) {
        WhiteBoardApp.isModified = isModified;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppContextUtil.init(this);
        SdCardStatus.init(StoreUtil.ROOT_FOLDER);
        OperationUtils.getInstance().init();
    }
}
