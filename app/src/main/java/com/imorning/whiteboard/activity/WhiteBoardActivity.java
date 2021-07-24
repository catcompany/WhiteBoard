package com.imorning.whiteboard.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.github.guanpy.library.ann.ReceiveEvents;
import com.imorning.whiteboard.R;
import com.imorning.whiteboard.bean.DrawPoint;
import com.imorning.whiteboard.databinding.ActivityWhiteBoardBinding;
import com.imorning.whiteboard.utils.Events;
import com.imorning.whiteboard.utils.OperationUtils;
import com.imorning.whiteboard.utils.StoreUtil;
import com.imorning.whiteboard.utils.WhiteBoardVariable;
import com.imorning.whiteboard.widget.DrawTextView;

import java.io.File;
import java.io.FileOutputStream;

public class WhiteBoardActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "WhiteBoardActivity";
    private ActivityWhiteBoardBinding binding;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
    }

    private void initView() {
        //Hide status bar and make app full screen
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityWhiteBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        changePenBack();
        changeColorBack();
        changeEraserBack();
        ToolsOperation(WhiteBoardVariable.Operation.PEN_NORMAL);
        binding.dbView.post(this::showPoints);
    }

    private void initEvent() {
        //头部
        binding.ivWhiteBoardBack.setOnClickListener(this);
        binding.ivWhiteBoardExport.setOnClickListener(this);
        binding.ivWhiteBoardSave.setOnClickListener(this);
        binding.ivWhiteBoardQuit.setOnClickListener(this);
        binding.ivWhiteBoardConfirm.setOnClickListener(this);
        binding.vBottomBack.setOnClickListener(this);
        //画笔尺寸大小
        binding.fabMenuSize.setOnFloatingActionsMenuClickListener(() -> ToolsOperation(WhiteBoardVariable.Operation.PEN_CLICK));
        binding.btSizeLarge.setOnClickListener(this);
        binding.btSizeMiddle.setOnClickListener(this);
        binding.btSizeMini.setOnClickListener(this);
        //画笔或者文字颜色
        binding.fabMenuColor.setOnFloatingActionsMenuClickListener(() -> ToolsOperation(WhiteBoardVariable.Operation.COLOR_CLICK));
        binding.btColorGreen.setOnClickListener(this);
        binding.btColorPurple.setOnClickListener(this);
        binding.btColorPink.setOnClickListener(this);
        binding.btColorOrange.setOnClickListener(this);
        binding.btColorBlack.setOnClickListener(this);
        //文字样式
        binding.fabMenuText.setOnFloatingActionsMenuClickListener(() -> ToolsOperation(WhiteBoardVariable.Operation.TEXT_CLICK));
        binding.btTextUnderline.setOnClickListener(this);
        binding.btTextItalics.setOnClickListener(this);
        binding.btTextBold.setOnClickListener(this);
        //橡皮擦尺寸大小
        binding.fabMenuEraser.setOnFloatingActionsMenuClickListener(() -> ToolsOperation(WhiteBoardVariable.Operation.ERASER_CLICK));
        binding.btEraserLarge.setOnClickListener(this);
        binding.btEraserMiddle.setOnClickListener(this);
        binding.btEraserMini.setOnClickListener(this);

        binding.ivWhiteBoardUndo.setOnClickListener(this);
        binding.ivWhiteBoardRedo.setOnClickListener(this);


        binding.llWhiteBoardPre.setOnClickListener(this);
        binding.ivWhiteBoardPre.setOnClickListener(this);
        binding.llWhiteBoardNext.setOnClickListener(this);
        binding.ivWhiteBoardNext.setOnClickListener(this);
        binding.ivWhiteBoardAdd.setOnClickListener(this);
        binding.ivWhiteBoardDisable.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_white_board_back://返回键
                onBackPressed();
                break;
            case R.id.iv_white_board_quit://退出文字编辑
                afterEdit(false);
                break;
            case R.id.iv_white_board_confirm://保存文字编辑
                afterEdit(true);
                break;
            case R.id.iv_white_board_export://保存白板操作集到本地
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                StoreUtil.saveWhiteBoardPoints();
                break;
            case R.id.iv_white_board_save://保存白板为图片
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                saveImage();
                break;
            case R.id.v_bottom_back://点击挡板
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                break;
            case R.id.bt_size_large://设置画笔尺寸-大号
                setPenSize(WhiteBoardVariable.PenSize.LARRGE);
                break;
            case R.id.bt_size_middle://设置画笔尺寸-中号
                setPenSize(WhiteBoardVariable.PenSize.MIDDLE);
                break;
            case R.id.bt_size_mini://设置画笔尺寸-小号
                setPenSize(WhiteBoardVariable.PenSize.MINI);
                break;

            case R.id.bt_color_green://设置颜色-绿色
                setColor(WhiteBoardVariable.Color.GREEN);
                break;
            case R.id.bt_color_purple://设置颜色-紫色
                setColor(WhiteBoardVariable.Color.PURPLE);
                break;
            case R.id.bt_color_pink://设置颜色-粉色
                setColor(WhiteBoardVariable.Color.PINK);
                break;
            case R.id.bt_color_orange://设置颜色-橙色
                setColor(WhiteBoardVariable.Color.ORANGE);
                break;
            case R.id.bt_color_black://设置颜色-黑色
                setColor(WhiteBoardVariable.Color.BLACK);
                break;

            case R.id.bt_text_underline://设置文字样式-下划线
                setTextStyle(WhiteBoardVariable.TextStyle.CHANGE_UNDERLINE);
                break;
            case R.id.bt_text_italics://设置文字样式-斜体
                setTextStyle(WhiteBoardVariable.TextStyle.CHANGE_ITALICS);
                break;
            case R.id.bt_text_bold://设置文字样式-粗体
                setTextStyle(WhiteBoardVariable.TextStyle.CHANGE_BOLD);
                break;

            case R.id.bt_eraser_large://设置橡皮擦尺寸-大号
                setEraserSize(WhiteBoardVariable.EraserSize.LARRGE);
                break;
            case R.id.bt_eraser_middle://设置橡皮擦尺寸-中号
                setEraserSize(WhiteBoardVariable.EraserSize.MIDDLE);
                break;
            case R.id.bt_eraser_mini://设置橡皮擦尺寸-小号
                setEraserSize(WhiteBoardVariable.EraserSize.MINI);
                break;

            case R.id.iv_white_board_undo://撤销
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (OperationUtils.getInstance().DISABLE) {
                    undo();
                }
                break;
            case R.id.iv_white_board_redo://重做
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (OperationUtils.getInstance().DISABLE) {
                    redo();
                }
                break;
            case R.id.ll_white_board_pre:
            case R.id.iv_white_board_pre://上一页
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                prePage();
                break;
            case R.id.ll_white_board_next:
            case R.id.iv_white_board_next://下一页
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                nextPage();
                break;
            case R.id.iv_white_board_add://新建白板
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                newPage();
                break;
            case R.id.iv_white_board_disable://禁止/解禁按钮
                ToolsOperation(WhiteBoardVariable.Operation.OUTSIDE_CLICK);
                if (OperationUtils.getInstance().DISABLE) {
                    OperationUtils.getInstance().DISABLE = false;
                    binding.ivWhiteBoardDisable.setImageResource(R.drawable.white_board_undisable_selector);
                    binding.rlBottom.setVisibility(View.GONE);
                } else {
                    OperationUtils.getInstance().DISABLE = true;
                    binding.ivWhiteBoardDisable.setImageResource(R.drawable.white_board_disable_selector);
                    binding.rlBottom.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    /**
     * 设置画笔尺寸
     */
    private void setPenSize(int size) {
        OperationUtils.getInstance().mCurrentPenSize = size;
        changePenBack();
        binding.dbView.setPenSize();
    }

    /**
     * 切换画笔尺寸按按钮背景
     */
    private void changePenBack() {
        if (OperationUtils.getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.LARRGE) {
            binding.btSizeLarge.drawCircleAndRing(WhiteBoardVariable.PenSize.LARRGE, OperationUtils.getInstance().mCurrentColor);
            binding.btSizeMiddle.drawCircle(WhiteBoardVariable.PenSize.MIDDLE);
            binding.btSizeMini.drawCircle(WhiteBoardVariable.PenSize.MINI);
        } else if (OperationUtils.getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.MIDDLE) {
            binding.btSizeLarge.drawCircle(WhiteBoardVariable.PenSize.LARRGE);
            binding.btSizeMiddle.drawCircleAndRing(WhiteBoardVariable.PenSize.MIDDLE, OperationUtils.getInstance().mCurrentColor);
            binding.btSizeMini.drawCircle(WhiteBoardVariable.PenSize.MINI);
        } else if (OperationUtils.getInstance().mCurrentPenSize == WhiteBoardVariable.PenSize.MINI) {
            binding.btSizeLarge.drawCircle(WhiteBoardVariable.PenSize.LARRGE);
            binding.btSizeMiddle.drawCircle(WhiteBoardVariable.PenSize.MIDDLE);
            binding.btSizeMini.drawCircleAndRing(WhiteBoardVariable.PenSize.MINI, OperationUtils.getInstance().mCurrentColor);

        }
    }

    /**
     * 设置颜色
     */
    private void setColor(int color) {
        OperationUtils.getInstance().mCurrentColor = color;
        changeColorBack();
        setPenSize(OperationUtils.getInstance().mCurrentPenSize);
        binding.dbView.setPenColor();
        binding.dtView.setTextColor();
    }

    /**
     * 切换颜色控制按钮背景
     */
    private void changeColorBack() {
        if (OperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.BLACK) {
            binding.fabMenuColor.setAddButtonBackground(R.drawable.white_board_color_black_selector);
        } else if (OperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.ORANGE) {
            binding.fabMenuColor.setAddButtonBackground(R.drawable.white_board_color_orange_selector);
        } else if (OperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.PINK) {
            binding.fabMenuColor.setAddButtonBackground(R.drawable.white_board_color_pink_selector);
        } else if (OperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.PURPLE) {
            binding.fabMenuColor.setAddButtonBackground(R.drawable.white_board_color_purple_selector);
        } else if (OperationUtils.getInstance().mCurrentColor == WhiteBoardVariable.Color.GREEN) {
            binding.fabMenuColor.setAddButtonBackground(R.drawable.white_board_color_green_selector);
        }
    }

    /**
     * 设置文字风格
     */
    private void setTextStyle(int textStyle) {
        binding.dtView.setTextStyle(textStyle);
        changeTextBack();
    }

    /**
     * 切换文字相关按钮背景
     */
    private void changeTextBack() {
        int size = OperationUtils.getInstance().getSavePoints().size();
        if (size < 1) {
            return;
        }
        DrawPoint dp = OperationUtils.getInstance().getSavePoints().get(size - 1);
        if (dp.getType() != OperationUtils.DRAW_TEXT) {
            return;
        }
        if (dp.getDrawText().getIsUnderline()) {
            binding.btTextUnderline.setBackgroundResource(R.drawable.white_board_text_underline_selected_selector);
        } else {
            binding.btTextUnderline.setBackgroundResource(R.drawable.white_board_text_underline_selector);
        }

        if (dp.getDrawText().getIsItalics()) {
            binding.btTextItalics.setBackgroundResource(R.drawable.white_board_text_italics_selected_selector);
        } else {
            binding.btTextItalics.setBackgroundResource(R.drawable.white_board_text_italics_selector);
        }

        if (dp.getDrawText().getIsBold()) {
            binding.btTextBold.setBackgroundResource(R.drawable.white_board_text_bold_selected_selector);
        } else {
            binding.btTextBold.setBackgroundResource(R.drawable.white_board_text_bold_selector);
        }
    }

    /**
     * 设置橡皮擦尺寸
     */
    private void setEraserSize(int size) {
        OperationUtils.getInstance().mCurrentEraserSize = size;
        changeEraserBack();
        binding.dbView.setEraserSize();

    }

    /**
     * 切换橡皮擦尺寸按钮背景
     */
    private void changeEraserBack() {
        if (OperationUtils.getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.LARRGE) {
            binding.btEraserLarge.drawCircleAndRing(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            binding.btEraserMiddle.drawCircle(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            binding.btEraserMini.drawCircle(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);
        } else if (OperationUtils.getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.MIDDLE) {
            binding.btEraserLarge.drawCircle(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            binding.btEraserMiddle.drawCircleAndRing(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            binding.btEraserMini.drawCircle(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);
        } else if (OperationUtils.getInstance().mCurrentEraserSize == WhiteBoardVariable.EraserSize.MINI) {
            binding.btEraserLarge.drawCircle(WhiteBoardVariable.EraserSize.LARRGE, WhiteBoardVariable.Color.BLACK);
            binding.btEraserMiddle.drawCircle(WhiteBoardVariable.EraserSize.MIDDLE, WhiteBoardVariable.Color.BLACK);
            binding.btEraserMini.drawCircleAndRing(WhiteBoardVariable.EraserSize.MINI, WhiteBoardVariable.Color.BLACK);

        }
    }

    /**
     * 新建白板
     */
    private void newPage() {
        OperationUtils.getInstance().newPage();
        showPoints();
    }

    /**
     * 上一页
     */
    private void prePage() {
        if (OperationUtils.getInstance().mCurrentIndex > 0) {
            OperationUtils.getInstance().mCurrentIndex--;
            showPoints();
        }
    }

    /**
     * 下一页
     */
    private void nextPage() {
        if (OperationUtils.getInstance().mCurrentIndex + 1 < OperationUtils.getInstance().getDrawPointSize()) {
            OperationUtils.getInstance().mCurrentIndex++;
            showPoints();
        }
    }

    /**
     * 重新显示白板
     */
    private void showPoints() {
        binding.dbView.showPoints();
        binding.dtView.showPoints();
        binding.tvWhiteBoardPage.setText(getString(R.string.project_index_all,
                OperationUtils.getInstance().mCurrentIndex + 1, OperationUtils.getInstance().getDrawPointSize()));
        showPage();
        showUndoRedo();
    }

    /**
     * 显示上下页是否可点击
     */
    private void showPage() {
        if (OperationUtils.getInstance().mCurrentIndex + 1 == OperationUtils.getInstance().getDrawPointSize()) {
            binding.ivWhiteBoardNext.setImageResource(R.drawable.white_board_next_page_click);
        } else {
            binding.ivWhiteBoardNext.setImageResource(R.drawable.white_board_next_page_selector);
        }
        if (OperationUtils.getInstance().mCurrentIndex == 0) {
            binding.ivWhiteBoardPre.setImageResource(R.drawable.white_board_pre_page_click);
        } else {
            binding.ivWhiteBoardPre.setImageResource(R.drawable.white_board_pre_page_selector);
        }

    }

    /**
     * 撤销
     */
    private void undo() {
        int size = OperationUtils.getInstance().getSavePoints().size();
        if (size != 0) {
            OperationUtils.getInstance().getDeletePoints().add(OperationUtils.getInstance().getSavePoints().get(size - 1));
            OperationUtils.getInstance().getSavePoints().remove(size - 1);
            size = OperationUtils.getInstance().getDeletePoints().size();
            if (OperationUtils.getInstance().getDeletePoints().get(size - 1).getType() == OperationUtils.DRAW_PEN) {
                binding.dbView.undo();
            } else if (OperationUtils.getInstance().getDeletePoints().get(size - 1).getType() == OperationUtils.DRAW_TEXT) {
                binding.dtView.undo();
            }
            showUndoRedo();
        }

    }

    /**
     * 重做
     */
    private void redo() {
        int size = OperationUtils.getInstance().getDeletePoints().size();
        if (size != 0) {
            OperationUtils.getInstance().getSavePoints().add(OperationUtils.getInstance().getDeletePoints().get(size - 1));
            OperationUtils.getInstance().getDeletePoints().remove(size - 1);
            size = OperationUtils.getInstance().getSavePoints().size();
            if (OperationUtils.getInstance().getSavePoints().get(size - 1).getType() == OperationUtils.DRAW_PEN) {
                binding.dbView.redo();
            } else if (OperationUtils.getInstance().getSavePoints().get(size - 1).getType() == OperationUtils.DRAW_TEXT) {
                binding.dtView.redo();
            }
            showUndoRedo();
        }

    }

    /**
     * 文字编辑之后
     */
    private void afterEdit(boolean isSave) {
        binding.ivWhiteBoardBack.setVisibility(View.VISIBLE);
        binding.ivWhiteBoardExport.setVisibility(View.VISIBLE);
        binding.ivWhiteBoardSave.setVisibility(View.VISIBLE);
        binding.rlBottom.setVisibility(View.VISIBLE);
        binding.ivWhiteBoardDisable.setVisibility(View.VISIBLE);

        binding.ivWhiteBoardQuit.setVisibility(View.GONE);
        binding.ivWhiteBoardConfirm.setVisibility(View.GONE);
        binding.dbView.showPoints();
        binding.dtView.afterEdit(isSave);
    }

    /**
     * 白板工具栏点击切换操作
     */
    private void ToolsOperation(int currentOperation) {
        setPenOperation(currentOperation);
        setColorOperation(currentOperation);
        setTextOperation(currentOperation);
        setEraserOperation(currentOperation);
        showOutSideView();
    }

    /**
     * 显示挡板
     */
    private void showOutSideView() {
        new Handler().postDelayed(() -> {
            if (OperationUtils.getInstance().mCurrentOperationPen == WhiteBoardVariable.Operation.PEN_EXPAND
                    || OperationUtils.getInstance().mCurrentOperationColor == WhiteBoardVariable.Operation.COLOR_EXPAND
                    || OperationUtils.getInstance().mCurrentOperationText == WhiteBoardVariable.Operation.TEXT_EXPAND
                    || OperationUtils.getInstance().mCurrentOperationEraser == WhiteBoardVariable.Operation.ERASER_EXPAND) {
                binding.vBottomBack.setVisibility(View.VISIBLE);
            } else {
                binding.vBottomBack.setVisibility(View.GONE);
            }
        }, 100);

    }

    /**
     * 白板工具栏点击切换操作-画笔
     */
    private void setPenOperation(int currentOperation) {
        switch (currentOperation) {
            case WhiteBoardVariable.Operation.PEN_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        OperationUtils.getInstance().mCurrentDrawType = OperationUtils.DRAW_PEN;
                        binding.dbView.setPaint(null);
                        binding.fabMenuSize.setAddButtonBackground(R.drawable.white_board_pen_selected_selector);
                        OperationUtils.getInstance().mCurrentOperationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        binding.fabMenuSize.expand();
                        changePenBack();
                        OperationUtils.getInstance().mCurrentOperationPen = WhiteBoardVariable.Operation.PEN_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        binding.fabMenuSize.collapse();
                        OperationUtils.getInstance().mCurrentOperationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.TEXT_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        binding.fabMenuSize.clearDraw();
                        binding.fabMenuSize.setAddButtonBackground(R.drawable.white_board_pen_selector);
                        OperationUtils.getInstance().mCurrentOperationPen = WhiteBoardVariable.Operation.PEN_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        binding.fabMenuSize.collapse();
                        binding.fabMenuSize.clearDraw();
                        binding.fabMenuSize.setAddButtonBackground(R.drawable.white_board_pen_selector);
                        OperationUtils.getInstance().mCurrentOperationPen = WhiteBoardVariable.Operation.PEN_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationPen) {
                    case WhiteBoardVariable.Operation.PEN_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.PEN_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.PEN_EXPAND:
                        binding.fabMenuSize.collapse();
                        OperationUtils.getInstance().mCurrentOperationPen = WhiteBoardVariable.Operation.PEN_CLICK;
                        break;
                }
                break;

        }

    }

    /**
     * 白板工具栏点击切换操作-颜色
     */
    private void setColorOperation(int currentOperation) {
        switch (currentOperation) {
            case WhiteBoardVariable.Operation.PEN_CLICK:
            case WhiteBoardVariable.Operation.TEXT_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationColor) {
                    case WhiteBoardVariable.Operation.COLOR_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.COLOR_EXPAND:
                        binding.fabMenuColor.collapse();
                        OperationUtils.getInstance().mCurrentOperationColor = WhiteBoardVariable.Operation.COLOR_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationColor) {
                    case WhiteBoardVariable.Operation.COLOR_NORMAL:
                        binding.fabMenuColor.expand();
                        OperationUtils.getInstance().mCurrentOperationColor = WhiteBoardVariable.Operation.COLOR_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.COLOR_EXPAND:
                        binding.fabMenuColor.collapse();
                        OperationUtils.getInstance().mCurrentOperationColor = WhiteBoardVariable.Operation.COLOR_NORMAL;
                        break;
                }
                break;

        }

    }

    /**
     * 白板工具栏点击切换操作-文字
     */
    private void setTextOperation(int currentOperation) {
        switch (currentOperation) {
            case WhiteBoardVariable.Operation.TEXT_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        OperationUtils.getInstance().mCurrentDrawType = OperationUtils.DRAW_TEXT;
                        binding.fabMenuText.setAddButtonBackground(R.drawable.white_board_text_selected_selector);
                        OperationUtils.getInstance().mCurrentOperationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        int size = OperationUtils.getInstance().getSavePoints().size();
                        if (size > 0) {
                            DrawPoint dp = OperationUtils.getInstance().getSavePoints().get(size - 1);
                            if (dp.getType() == OperationUtils.DRAW_TEXT && dp.getDrawText().getStatus()
                                    == DrawTextView.TEXT_DETAIL) {
                                changeTextBack();
                                binding.fabMenuText.expand();
                                OperationUtils.getInstance().mCurrentOperationText
                                        = WhiteBoardVariable.Operation.TEXT_EXPAND;
                            }
                        }
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        binding.fabMenuText.collapse();
                        OperationUtils.getInstance().mCurrentOperationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.PEN_CLICK:
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        binding.fabMenuText.clearDraw();
                        binding.fabMenuText.setAddButtonBackground(R.drawable.white_board_text_selector);
                        OperationUtils.getInstance().mCurrentOperationText = WhiteBoardVariable.Operation.TEXT_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        binding.fabMenuText.collapse();
                        binding.fabMenuText.clearDraw();
                        binding.fabMenuText.setAddButtonBackground(R.drawable.white_board_text_selector);
                        OperationUtils.getInstance().mCurrentOperationText = WhiteBoardVariable.Operation.TEXT_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationText) {
                    case WhiteBoardVariable.Operation.TEXT_NORMAL:
                    case WhiteBoardVariable.Operation.TEXT_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.TEXT_EXPAND:
                        binding.fabMenuText.collapse();
                        OperationUtils.getInstance().mCurrentOperationText = WhiteBoardVariable.Operation.TEXT_CLICK;
                        break;
                }
                break;

        }

    }

    /**
     * 白板工具栏点击切换操作-橡皮擦
     */
    private void setEraserOperation(int currentOperation) {
        switch (currentOperation) {
            case WhiteBoardVariable.Operation.ERASER_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        OperationUtils.getInstance().mCurrentDrawType = OperationUtils.DRAW_ERASER;
                        binding.dbView.changeEraser();
                        binding.fabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selected_selector);
                        OperationUtils.getInstance().mCurrentOperationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        binding.fabMenuEraser.expand();
                        changeEraserBack();
                        OperationUtils.getInstance().mCurrentOperationEraser = WhiteBoardVariable.Operation.ERASER_EXPAND;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        binding.fabMenuEraser.collapse();
                        OperationUtils.getInstance().mCurrentOperationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.TEXT_CLICK:
            case WhiteBoardVariable.Operation.PEN_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        binding.fabMenuEraser.clearDraw();
                        binding.fabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selector);
                        OperationUtils.getInstance().mCurrentOperationEraser = WhiteBoardVariable.Operation.ERASER_NORMAL;
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        binding.fabMenuEraser.collapse();
                        binding.fabMenuEraser.clearDraw();
                        binding.fabMenuEraser.setAddButtonBackground(R.drawable.white_board_eraser_selector);
                        OperationUtils.getInstance().mCurrentOperationEraser = WhiteBoardVariable.Operation.ERASER_NORMAL;
                        break;
                }
                break;
            case WhiteBoardVariable.Operation.COLOR_CLICK:
            case WhiteBoardVariable.Operation.OUTSIDE_CLICK:
                switch (OperationUtils.getInstance().mCurrentOperationEraser) {
                    case WhiteBoardVariable.Operation.ERASER_NORMAL:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_CLICK:
                        break;
                    case WhiteBoardVariable.Operation.ERASER_EXPAND:
                        binding.fabMenuEraser.collapse();
                        OperationUtils.getInstance().mCurrentOperationEraser = WhiteBoardVariable.Operation.ERASER_CLICK;
                        break;
                }
                break;

        }

    }

    /**
     * 保存当前白板为图片
     */
    public void saveImage() {
        String fileName = StoreUtil.getPhotoSavePath();
        Log.e(TAG, fileName);
        File file = new File(fileName);
        try {
            File directory = file.getParentFile();
            assert directory != null;
            if (!directory.exists() && !directory.mkdirs()) {
                showMessage(getString(R.string.white_board_export_fail));
                return;
            }
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            binding.flView.setDrawingCacheEnabled(true);
            binding.flView.buildDrawingCache();
            Bitmap bitmap = binding.flView.getDrawingCache();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            binding.flView.destroyDrawingCache();
            //update thumbnail
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            sendBroadcast(intent);

            showMessage(getString(R.string.white_board_export_tip, fileName));
        } catch (Exception e) {
            showMessage(getString(R.string.white_board_export_fail));
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @ReceiveEvents(name = Events.WHITE_BOARD_TEXT_EDIT)
    private void textEdit() {
        //文字编辑
        binding.ivWhiteBoardBack.setVisibility(View.GONE);
        binding.ivWhiteBoardExport.setVisibility(View.GONE);
        binding.ivWhiteBoardSave.setVisibility(View.GONE);
        binding.rlBottom.setVisibility(View.GONE);
        binding.ivWhiteBoardDisable.setVisibility(View.GONE);
        binding.ivWhiteBoardQuit.setVisibility(View.VISIBLE);
        binding.ivWhiteBoardConfirm.setVisibility(View.VISIBLE);
    }

    @ReceiveEvents(name = Events.WHITE_BOARD_UNDO_REDO)
    private void showUndoRedo() {
        //是否显示撤销、重装按钮
        if (OperationUtils.getInstance().getSavePoints().isEmpty()) {
            binding.ivWhiteBoardUndo.setVisibility(View.INVISIBLE);
            binding.ivWhiteBoardExport.setVisibility(View.INVISIBLE);
            binding.ivWhiteBoardSave.setVisibility(View.INVISIBLE);
        } else {
            binding.ivWhiteBoardUndo.setVisibility(View.VISIBLE);
            binding.ivWhiteBoardExport.setVisibility(View.VISIBLE);
            binding.ivWhiteBoardSave.setVisibility(View.VISIBLE);
        }
        if (OperationUtils.getInstance().getDeletePoints().isEmpty()) {
            binding.ivWhiteBoardRedo.setVisibility(View.INVISIBLE);
        } else {
            binding.ivWhiteBoardRedo.setVisibility(View.VISIBLE);
        }
    }

}
