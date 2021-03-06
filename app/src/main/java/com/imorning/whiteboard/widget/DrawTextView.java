package com.imorning.whiteboard.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.guanpy.library.EventBus;
import com.imorning.whiteboard.R;
import com.imorning.whiteboard.bean.DrawPoint;
import com.imorning.whiteboard.utils.Events;
import com.imorning.whiteboard.utils.OperationUtils;


public class DrawTextView extends RelativeLayout implements
        View.OnClickListener {

    /**
     * In editing status
     */
    public static final int TEXT_VIEW = 1;
    /**
     * 编辑（文字编辑）状态
     */
    public static final int TEXT_EDIT = 2;
    /**
     * 详情（显示删除、编辑按钮）状态
     */
    public static final int TEXT_DETAIL = 3;
    /**
     * In the deleted state
     */
    public static final int TEXT_DELETE = 4;

    /**
     *
     */
    private View mVOutside;
    /**
     *
     */
    private RelativeLayout mRlContent;
    /**
     *
     */
    private RelativeLayout mRlText;
    /**
     *
     */
    private EditText mEtTextEdit;
    /**
     *
     */
    private TextView mTvTextEdit;
    /**
     *
     */
    private Button mBtTextDelete;
    /**
     *
     */
    private Button mBtTextEdit;

    private Context mContext;

    private CallBackListener mCallBackListener;

    private DrawPoint mDrawPoint;

    private int mWidth;
    /**
     * Special characters required
     */
    private Spannable mSpannable;

    public DrawTextView(Context context
            , DrawPoint drawPoint,
                        CallBackListener callBackListener) {
        super(context);
        init(context, drawPoint, callBackListener);
    }

    private void init(Context context, DrawPoint drawPoint, CallBackListener callBackListener) {
        mContext = context;
        mDrawPoint = DrawPoint.copyDrawPoint(drawPoint);
        mCallBackListener = callBackListener;
        //Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
        //mWidth = display.getWidth();
        DisplayMetrics dm = new DisplayMetrics();
         ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWidth  = dm.widthPixels;
        initUI();
        initEvent();
        switchView(mDrawPoint.getDrawText().getStatus());

    }


    /**
     * init ui and controls.
     */
    private void initUI() {
        LayoutInflater.from(mContext).inflate(R.layout.draw_text, this, true);
        mVOutside = findViewById(R.id.v_outside);
        mRlContent = findViewById(R.id.rl_content);
        mRlText = findViewById(R.id.rl_text);
        mEtTextEdit = findViewById(R.id.et_text_edit);
        mTvTextEdit = findViewById(R.id.tv_text_edit);
        mBtTextDelete = findViewById(R.id.bt_text_delete);
        mBtTextEdit = findViewById(R.id.bt_text_edit);
        if (null != mDrawPoint) {
            setText(mDrawPoint.getDrawText().getStr());
        }
        setLayoutParams();

    }


    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        mVOutside.setOnClickListener(this);
        mRlText.setOnClickListener(this);
        mEtTextEdit.setOnClickListener(this);
        mBtTextDelete.setOnClickListener(this);
        mBtTextEdit.setOnClickListener(this);
        mTvTextEdit.setOnClickListener(this);
        mTvTextEdit.setOnTouchListener(new OnTouchListener() {
            int lastX, lastY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (mDrawPoint.getDrawText().getStatus() == TEXT_DETAIL && OperationUtils.getInstance().DISABLE) {
                    int ea = event.getAction();
                    switch (ea) {
                        case MotionEvent.ACTION_DOWN:
                            lastX = (int) event.getRawX();
                            lastY = (int) event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int dx = (int) event.getRawX() - lastX;
                            int dy = (int) event.getRawY() - lastY;

                            int left = mRlContent.getLeft() + dx;
                            int top = mRlContent.getTop() + dy;
                            int right = mRlContent.getRight() + dx;
                            int bottom = mRlContent.getBottom() + dy;
                            if (left < 0) {
                                left = 0;
                                right = left + mRlContent.getWidth();
                            }
                            if (right > getWidth()) {
                                right = getWidth();
                                left = right - mRlContent.getWidth();
                            }
                            if (top < 0) {
                                top = 0;
                                bottom = top + mRlContent.getHeight();
                            }
                            if (bottom > getHeight()) {
                                bottom = getHeight();
                                top = bottom - mRlContent.getHeight();
                            }
                            mDrawPoint.getDrawText().setX(left);
                            mDrawPoint.getDrawText().setY(top);
                            mRlContent.layout(left, top, right, bottom);
                            lastX = (int) event.getRawX();
                            lastY = (int) event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            if (null != mCallBackListener) {
                                mCallBackListener.onUpdate(mDrawPoint);
                            }
                            break;
                        default:
                            break;
                    }

                }
                return false;
            }
        });
    }


    private void setText(String strText) {
        if (!TextUtils.isEmpty(strText)) {
            mEtTextEdit.setText(strText);
            mTvTextEdit.setText(strText);
        }
        mEtTextEdit.setTextColor(mDrawPoint.getDrawText().getColor());
        mTvTextEdit.setTextColor(mDrawPoint.getDrawText().getColor());
        if (mDrawPoint.getDrawText().getIsUnderline()) {
            mTvTextEdit.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            mEtTextEdit.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
        if (mDrawPoint.getDrawText().getIsBold()) {
            mTvTextEdit.getPaint().setFakeBoldText(true);
            mEtTextEdit.getPaint().setFakeBoldText(true);
        }

    }

    private void setLayoutParams() {
        LayoutParams layParamsTxt = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layParamsTxt.leftMargin = (int) mDrawPoint.getDrawText().getX();
        layParamsTxt.topMargin = (int) mDrawPoint.getDrawText().getY();
        mRlContent.setLayoutParams(layParamsTxt);
    }

    public void switchView(int currentStatus) {
        switch (currentStatus) {
            case TEXT_VIEW:
                mVOutside.setVisibility(View.GONE);
                mEtTextEdit.setVisibility(View.GONE);
                mTvTextEdit.setVisibility(View.VISIBLE);
                mRlText.setBackgroundResource(R.color.transparent);
                mBtTextEdit.setVisibility(View.GONE);
                mBtTextDelete.setVisibility(View.GONE);
                break;
            case TEXT_EDIT:
                mVOutside.setBackgroundResource(R.color.colorText);
                mVOutside.setVisibility(View.VISIBLE);
                mEtTextEdit.setVisibility(View.VISIBLE);
                mTvTextEdit.setVisibility(View.GONE);
                mRlText.setBackgroundResource(R.drawable.draw_text_border);
                mBtTextEdit.setVisibility(View.GONE);
                mEtTextEdit.setSelection(mEtTextEdit.getText().length());
                mBtTextDelete.setVisibility(View.GONE);
                EventBus.postEvent(Events.WHITE_BOARD_TEXT_EDIT);
                showSoftKeyBoard(mEtTextEdit);
                break;
            case TEXT_DETAIL:
                mVOutside.setBackgroundResource(R.color.transparent);
                mVOutside.setVisibility(View.VISIBLE);
                mEtTextEdit.setVisibility(View.GONE);
                mTvTextEdit.setVisibility(View.VISIBLE);
                mRlText.setBackgroundResource(R.drawable.draw_text_border);
                mBtTextEdit.setVisibility(View.VISIBLE);
                mBtTextDelete.setVisibility(View.VISIBLE);
                break;
            case TEXT_DELETE:

                break;
            default:
                break;
        }
        if (mDrawPoint.getDrawText().getStatus() != currentStatus) {
            mDrawPoint.getDrawText().setStatus(currentStatus);
            if (null != mCallBackListener && currentStatus != TEXT_EDIT) {
                mCallBackListener.onUpdate(mDrawPoint);
            }
        }

    }

    /**
     * finish text edit
     *
     * @param isSave Save or don't save
     */
    public void afterEdit(boolean isSave) {
        if (isSave) {
            mDrawPoint.getDrawText().setStr(mEtTextEdit.getText().toString());
        }
        switchView(TEXT_VIEW);
        hideSoftInput();
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.v_outside) {
            if (mDrawPoint.getDrawText().getStatus() == TEXT_DETAIL && OperationUtils.getInstance().DISABLE) {
                switchView(TEXT_VIEW);
            }
            hideSoftInput();
        } else if (vId == R.id.tv_text_edit) {
            if (OperationUtils.getInstance().DISABLE) {
                switchView(TEXT_DETAIL);
            }
        } else if (vId == R.id.bt_text_delete) {
            if (OperationUtils.getInstance().DISABLE) {
                switchView(TEXT_DELETE);
            }
        } else if (vId == R.id.bt_text_edit) {
            if (OperationUtils.getInstance().DISABLE) {
                switchView(TEXT_EDIT);
            }
        }
    }

    private void showSoftKeyBoard(final EditText et) {
        et.requestFocus();
        et.post(() -> {
            // Pop-up input method
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        });
    }

    private void hideSoftInput() {
        if (mContext == null || mEtTextEdit == null) {
            return;
        }
        // Hide input method
        ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(mEtTextEdit.getWindowToken(), 0);
    }

    public interface CallBackListener {
        /**
         * update text prop
         */
        void onUpdate(DrawPoint drawPoint);
    }
}
