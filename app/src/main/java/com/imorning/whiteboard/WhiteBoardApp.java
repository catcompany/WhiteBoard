package com.imorning.whiteboard;

import android.app.Application;

import com.imorning.whiteboard.utils.AppContextUtil;
import com.imorning.whiteboard.utils.OperationUtils;
import com.imorning.whiteboard.utils.SdCardStatus;
import com.imorning.whiteboard.utils.StoreUtil;

public class WhiteBoardApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppContextUtil.init(this);
        SdCardStatus.init(StoreUtil.CACHE_DIR);
        OperationUtils.getInstance().init();
    }
}
