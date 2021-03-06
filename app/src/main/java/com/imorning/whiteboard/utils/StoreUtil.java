package com.imorning.whiteboard.utils;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.imorning.whiteboard.R;
import com.imorning.whiteboard.WhiteBoardApp;
import com.imorning.whiteboard.bean.DrawPenPoint;
import com.imorning.whiteboard.bean.DrawPenStr;
import com.imorning.whiteboard.bean.DrawPoint;
import com.imorning.whiteboard.bean.Point;
import com.imorning.whiteboard.bean.WhiteBoardPoint;
import com.imorning.whiteboard.bean.WhiteBoardPoints;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * The util for store project. </br>
 * Currently,the project can export to ".wb" file or save as a png photo.
 */
public class StoreUtil {
    /**
     * the folder name for root dir  <br>
     * like: "/sdcard/{#ROOT_FOLDER}/"
     */
    public static final String ROOT_FOLDER = "WhiteBoard";
    /**
     * project ext.
     */
    public static final String PROJECT_FORMAT = ".wbp";
    private static final String TAG = "StoreUtil";
    private static final String CHARSET = "UTF-8";
    /**
     * The dir to save photo.
     */
    private static final String PHOTO_DIR = "photos";
    /**
     * photo format.
     */
    private static final String PHOTO_FORMAT_PNG = ".png";
    /**
     * default dir for saving wb projects.
     */
    private static final String PROJECT_DIR = "projects";

    /**
     * 获取保存路径
     */
    public static String getPhotoSavePath() {
        return getPhotoPath() + File.separator + getProjectName() + PHOTO_FORMAT_PNG;
    }

    public static String getPhotoPath() {
        return SdCardStatus.getDefaultCacheDirInSdcard() + File.separator + PHOTO_DIR;
    }

    /**
     * 获取保存路径
     */
    public static String getWbSavePath() {
        if (WhiteBoardApp.getFilePath() == null) {
            return getWbPath() + File.separator + getProjectName() + PROJECT_FORMAT;
        } else {
            return WhiteBoardApp.getFilePath();
        }
    }

    public static String getWbPath() {
        return SdCardStatus.getDefaultCacheDirInSdcard() + File.separator + PROJECT_DIR;
    }

    /**
     * 存储白板集
     */
    public static void saveWhiteBoardPoints() {
        WhiteBoardPoints whiteBoardPoints = OperationUtils.getInstance().getWhiteBoardPoints();
        if (whiteBoardPoints == null || whiteBoardPoints.getWhiteBoardPoints() == null || whiteBoardPoints.getWhiteBoardPoints().isEmpty()) {
            return;
        }
        for (WhiteBoardPoint whiteBoardPoint : whiteBoardPoints.getWhiteBoardPoints()) {
            //清除绘画路径，保留字符串形式就行
            for (DrawPoint drawPoint : whiteBoardPoint.getSavePoints()) {
                if (drawPoint.getType() == OperationUtils.DRAW_PEN) {
                    drawPoint.setDrawPen(null);

                }
            }
        }
        String strJson = new Gson().toJson(whiteBoardPoints);
        StoreUtil.write(strJson, getWbSavePath());
        convertWhiteBoardPoints(whiteBoardPoints);
        OperationUtils.getInstance().setWhiteBoardPoints(whiteBoardPoints);
        ToastUtils.showToast(AppContextUtil.getContext(), AppContextUtil.getContext().getString(R.string.white_board_save_success));
    }

    /**
     * 读取白板集
     */
    public static void readWhiteBoardPoints(String filePath) {
        String jsonData = StoreUtil.read(filePath);
        if (!TextUtils.isEmpty(jsonData)) {
            WhiteBoardPoints whiteBoardPoints = new Gson().fromJson(jsonData, WhiteBoardPoints.class);
            convertWhiteBoardPoints(whiteBoardPoints);
            OperationUtils.getInstance().setWhiteBoardPoints(whiteBoardPoints);
        }

    }

    /**
     * 从json字符中将Path、Paint系统类转换出来
     */
    public static void convertWhiteBoardPoints(WhiteBoardPoints whiteBoardPoints) {
        for (WhiteBoardPoint whiteBoardPoint : whiteBoardPoints.getWhiteBoardPoints()) {
            whiteBoardPoint.getDeletePoints().clear();
            for (DrawPoint drawPoint : whiteBoardPoint.getSavePoints()) {
                if (drawPoint.getType() == OperationUtils.DRAW_PEN) {
                    DrawPenStr drawPenStr = drawPoint.getDrawPenStr();
                    Paint paint = new Paint();
                    paint.setAntiAlias(true);//是否使用抗锯齿功能,会消耗较大资源，绘制图形速度会变慢
                    paint.setDither(true);// 设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
                    paint.setColor(drawPenStr.getColor());//设置绘制的颜色
                    paint.setStyle(Paint.Style.STROKE);//设置画笔的样式
                    paint.setStrokeJoin(Paint.Join.ROUND);//设置绘制时各图形的结合方式，如平滑效果等
                    paint.setStrokeCap(Paint.Cap.ROUND);//当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式    Cap.ROUND,或方形样式Cap.SQUARE
                    paint.setStrokeWidth(drawPenStr.getStrokeWidth());//当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的粗细度
                    if (drawPenStr.getIsEraser()) {
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//擦除模式
                    }
                    Path path = new Path();
                    path.moveTo(drawPenStr.getMoveTo().getX(), drawPenStr.getMoveTo().getY());
                    for (int i = 0; i < drawPenStr.getQuadToA().size(); i++) {
                        Point pointA = drawPenStr.getQuadToA().get(i);
                        Point pointB = drawPenStr.getQuadToB().get(i);
                        path.quadTo(pointA.getX(), pointA.getY(), pointB.getX(), pointB.getY());
                    }
                    path.lineTo(drawPenStr.getLineTo().getX(), drawPenStr.getLineTo().getY());
                    path.offset(drawPenStr.getOffset().getX(), drawPenStr.getOffset().getY());

                    DrawPenPoint drawPenPoint = new DrawPenPoint();
                    drawPenPoint.setPaint(paint);
                    drawPenPoint.setPath(path);
                    drawPoint.setDrawPen(drawPenPoint);
                }

            }
        }
    }

    /**
     * 保存内容到文件中
     */
    public static void write(String strWb, String path) {
        if (TextUtils.isEmpty(strWb) || TextUtils.isEmpty(path)) {
            Log.e(TAG, "Trying to save null or 0 length strWb or path");
            return;
        }
        File toFile = new File(path);
        if (!Objects.requireNonNull(toFile.getParentFile()).exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists()) {
            toFile.delete();
        }
        try {
            toFile.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "IOException：" + e.getMessage());
            toFile = null;
        } finally {
            if (null != toFile && null != strWb) {
                OutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(toFile);
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "FileNotFoundException：" + e.getMessage());
                    outStream = null;
                } finally {
                    if (null != outStream) {
                        try {
                            outStream.write(strWb.getBytes(StandardCharsets.UTF_8));
                            outStream.flush();
                        } catch (IOException e) {
                            Log.e(TAG, "IOException：" + e.getMessage());
                        } finally {
                            try {
                                outStream.close();
                            } catch (IOException e) {
                                Log.e(TAG, "IOException" + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 加载文件内容
     */
    public static String read(String wbPath) {
        File file = new File(wbPath);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                int len = fis.available();
                if (len > 0) {
                    byte[] buf = new byte[len];
                    fis.read(buf);
                    return new String(buf, CHARSET);
                }
            } catch (Exception | OutOfMemoryError e) {
                e.printStackTrace();
            } finally {
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        return null;
    }


    public static String getProjectName() {
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(new Date());
    }
}
