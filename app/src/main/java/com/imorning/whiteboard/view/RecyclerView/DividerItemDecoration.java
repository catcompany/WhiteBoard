package com.imorning.whiteboard.view.RecyclerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {


    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    //我们通过获取系统属性中的listDivider来添加，在系统中的AppTheme中设置
    public static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    private final Drawable drawable;
    private int mOrientation;

    /**
     * 构造函数
     * @param context 上下文
     * @param orientation 屏幕方向
     */
    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        this.drawable = typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
    }


    /**
     * 设置屏幕的方向
     *
     * @param orientation HORIZONTAL为横屏，VERTICAL为竖屏 <br>
     *                    默认为竖屏
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            mOrientation = VERTICAL;
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(@NotNull Canvas c, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        if (mOrientation == HORIZONTAL) {
            drawVerticalLine(c, parent, state);
        } else {
            drawHorizontalLine(c, parent, state);
        }
    }

    //画横线, 这里的parent其实是显示在屏幕显示的这部分
    public void drawHorizontalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + drawable.getIntrinsicHeight();
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(c);
        }
    }

    //画竖线
    public void drawVerticalLine(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + drawable.getIntrinsicWidth();
            drawable.setBounds(left, top, right, bottom);
            drawable.draw(c);
        }
    }

    //由于Divider也有长宽高，每一个Item需要向下或者向右偏移
    @Override
    public void getItemOffsets(@NotNull Rect outRect, @NotNull View view, @NotNull RecyclerView parent, @NotNull RecyclerView.State state) {
        if (mOrientation == HORIZONTAL) {
            //画横线，就是往下偏移一个分割线的高度
            outRect.set(0, 0, 0, drawable.getIntrinsicHeight());
        } else {
            //画竖线，就是往右偏移一个分割线的宽度
            outRect.set(0, 0, drawable.getIntrinsicWidth(), 0);
        }
    }
}
