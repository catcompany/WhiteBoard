package com.imorning.whiteboard.bean;

import android.graphics.Paint;
import android.graphics.Path;

/**
 * 绘画存储-画笔路径
 */
public class DrawPenPoint {

    /**
     * 绘画路径
     */
    private Path mPath;
    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 获得绘画路径
     *
     * @return 绘画路径
     */
    public Path getPath() {
        return mPath;
    }

    /**
     * 设置绘画路径
     *
     * @param path 绘画路径
     */
    public void setPath(Path path) {
        this.mPath = path;
    }

    /**
     * 获得画笔
     *
     * @return 画笔
     */
    public Paint getPaint() {
        return mPaint;
    }

    /**
     * 设置画笔
     *
     * @param paint 画笔
     */
    public void setPaint(Paint paint) {
        this.mPaint = paint;
    }

}
