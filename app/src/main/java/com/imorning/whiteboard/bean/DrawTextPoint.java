package com.imorning.whiteboard.bean;

/**
 * 绘画存储-文字
 */
public class DrawTextPoint {

    /**
     * 唯一性标识
     */
    private long mId;
    /**
     * 文字x坐标
     */
    private float mX;
    /**
     * 文字y坐标
     */
    private float mY;
    /**
     * 文字
     */
    private String mStr;
    /**
     * 是否有下划线
     */
    private boolean mIsUnderline;
    /**
     * 是否斜体
     */
    private boolean mIsItalics;
    /**
     * 是否粗体
     */
    private boolean mIsBold;

    /**
     * 文字颜色
     */
    private int mColor;
    /**
     * 当前文字状态
     */
    private int mStatus;

    /**
     * 是否显示
     */
    private boolean mIsVisible;

    /**
     * 获得唯一性标识
     * @return 唯一性标识

     */
    public long getId() {
        return mId;
    }

    /**
     * 设置唯一性标识
     * @param id 唯一性标识

     */
    public void setId(long id) {
        this.mId = id;
    }

    /**
     * 获得文字x坐标

     * @return 文字x坐标

     */
    public float getX() {
        return mX;
    }

    /**
     * 设置文字x坐标

     * @param x 文字x坐标

     */
    public void setX(float x) {
        this.mX = x;
    }

    /**
     * 获得文字y坐标

     * @return 文字y坐标

     */
    public float getY() {
        return mY;
    }

    /**
     * 设置文字y坐标

     * @param y 文字y坐标

     */
    public void setY(float y) {
        this.mY = y;
    }

    /**
     * 获得文字

     * @return 文字

     */
    public String getStr() {
        return mStr;
    }

    /**
     * 设置文字

     * @param str 文字

     */
    public void setStr(String str) {
        this.mStr = str;
    }

    /**
     * 获得是否有下划线
     * Created 2015-7-17 14:57:13
     *
     * @return 是否有下划线

     */
    public boolean getIsUnderline() {
        return mIsUnderline;
    }

    /**
     * 设置是否有下划线
     * Created 2015-7-17 14:57:13
     *
     * @param isUnderline 是否有下划线

     */
    public void setIsUnderline(boolean isUnderline) {
        this.mIsUnderline = isUnderline;
    }

    /**
     * 获得是否斜体
     * Created 2015-7-17 14:57:13
     *
     * @return 是否斜体

     */
    public boolean getIsItalics() {
        return mIsItalics;
    }

    /**
     * 设置是否斜体
     * Created 2015-7-17 14:57:13
     *
     * @param isItalics 是否斜体

     */
    public void setIsItalics(boolean isItalics) {
        this.mIsItalics = isItalics;
    }

    /**
     * 获得是否粗体
     * Created 2015-7-17 14:57:13
     *
     * @return 是否粗体

     */
    public boolean getIsBold() {
        return mIsBold;
    }

    /**
     * 设置是否粗体
     * Created 2015-7-17 14:57:13
     *
     * @param isBold 是否粗体

     */
    public void setIsBold(boolean isBold) {
        this.mIsBold = isBold;
    }

    /**
     * 获得文字颜色
     * Created 2015-7-17 15:40:0
     *
     * @return 文字颜色

     */
    public int getColor() {
        return mColor;
    }

    /**
     * 设置文字颜色
     * Created 2015-7-17 15:40:0
     *
     * @param color 文字颜色

     */
    public void setColor(int color) {
        this.mColor = color;
    }

    /**
     * 获得当前文字状态

     * @return 当前文字状态

     */
    public int getStatus() {
        return mStatus;
    }

    /**
     * 设置当前文字状态

     * @param status 当前文字状态

     */
    public void setStatus(int status) {
        this.mStatus = status;
    }

    /**
     * 获得是否显示
     * Created 2015-7-13 11:31:52
     *
     * @return 是否显示

     */
    public boolean getIsVisible() {
        return mIsVisible;
    }

    /**
     * 设置是否显示
     * Created 2015-7-13 11:31:52
     *
     * @param isVisible 是否显示

     */
    public void setIsVisible(boolean isVisible) {
        this.mIsVisible = isVisible;
    }
}