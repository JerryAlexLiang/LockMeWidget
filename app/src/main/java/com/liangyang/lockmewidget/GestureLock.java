package com.liangyang.lockmewidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期：2017/2/13 on 11:01
 * 作者:杨亮 liangyang
 * 描述:自定义view，图案解锁-九宫格点的绘制和创建
 */
public class GestureLock extends View {

    //3.创建3*3数组保存点
    private Point[][] points = new Point[3][3];
    private boolean inited = false;
    private boolean isDraw = false;

    private ArrayList<Point> pointList = new ArrayList<>();
    private ArrayList<Integer> passList = new ArrayList<>();//存序号(0-8)

    //6.创建3个变量(bitmap),在init()方法中对这三个变量进行初始化
    private Bitmap bitmapPointError;
    private Bitmap bitmapPointPress;
    private Bitmap bitmapPointNormal;

    private float bitmapR;//点的半径

    float mouseX, mouseY;

    private OnDrawFinishedListener listener;//在绘制完成的时候使用

    //7.创建画笔
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿

    //绘制带颜色的画笔
    Paint pressPaint = new Paint();//按下时的画笔
    Paint errorPaint = new Paint();//错误时的画笔

    public GestureLock(Context context) {
        super(context);
    }

    public GestureLock(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureLock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 8.处理onTouch触摸事件
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();

        int[] ij;
        int i, j;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                resetPoints();
                ij = getSelectedPoint();
                if (ij != null) {
                    isDraw = true;
                    i = ij[0];
                    j = ij[1];
                    points[i][j].state = Point.STATE_PRESS;
                    pointList.add(points[i][j]);//首次按压，起点
                    passList.add(i * 3 + j);//二维数组转为一维数组的方法
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDraw) {
                    ij = getSelectedPoint();
                    if (ij != null) {
                        i = ij[0];
                        j = ij[1];

                        //判断当前这个点是否已添加，因为每个点只能添加一次
                        if (!pointList.contains(points[i][j])) {
                            points[i][j].state = Point.STATE_PRESS;
                            pointList.add(points[i][j]);
                            passList.add(i * 3 + j);
                        }

                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                boolean valid = false;//
                if (listener != null && isDraw){
                    valid = listener.OnDrawFinished(passList);
                }
                if (!valid) {
                    for (Point p : pointList) {
                        p.state = Point.STATE_ERROR;
                    }
                }
                //停止绘制
                isDraw = false;
                break;
        }

        this.postInvalidate();//界面重新绘制
        return true;
    }


    /**
     * 9.触摸点
     *
     * @return
     */
    private int[] getSelectedPoint() {
        Point pointMouse = new Point(mouseX, mouseY);
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].distance(pointMouse) < bitmapR) {
                    //当前的手指点在点上了
                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        //当没有点击到9个点的时候，用来判断
        return null;
    }

    /**
     * 11.重置
     */
    public void resetPoints() {
        passList.clear();
        pointList.clear();
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                points[i][j].state = Point.STATE_NORMAL;
            }
        }
        this.postInvalidate();
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!inited) {
            init();
        }
        //绘制点
        drawPoints(canvas);

        //绘制连线
        if (pointList.size() > 0) {  //画线 2->3,3->5 ...
            Point a = pointList.get(0); // 第一个点
            for (int i = 1; i < pointList.size(); i++) { //从第一个点开始
                Point b = pointList.get(i);
                drawLine(canvas, a, b);
                a = b; //更新起点a的值，将上次的终点设置为新的起点
            }
            if (isDraw) {  //画最后一个点
                drawLine(canvas, a, new Point(mouseX, mouseY)); //此时a是循环后最后一个点
            }
        }
    }

    /**
     * 10.绘制连线
     *
     * @param canvas
     * @param a
     * @param b
     */
    private void drawLine(Canvas canvas, Point a, Point b) {

        if (a.state == Point.STATE_ERROR) {
            canvas.drawLine(a.x, a.y, b.x, b.y, errorPaint);
        } else if (a.state == Point.STATE_PRESS) {
            canvas.drawLine(a.x, a.y, b.x, b.y, pressPaint);
        }
    }

    /**
     * 5.绘制点
     *
     * @param canvas
     */
    private void drawPoints(Canvas canvas) {
        //使用两层循环嵌套对这9个点进行绘制
        for (int i = 0; i < points.length; i++) {

            for (int j = 0; j < points[i].length; j++) {

                if (points[i][j].state == Point.STATE_NORMAL) {
                    //normal
                    canvas.drawBitmap(bitmapPointNormal, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);

                } else if (points[i][j].state == Point.STATE_PRESS) {
                    //press
                    canvas.drawBitmap(bitmapPointPress, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);

                } else if (points[i][j].state == Point.STATE_ERROR) {
                    //error
                    canvas.drawBitmap(bitmapPointError, points[i][j].x - bitmapR, points[i][j].y - bitmapR, paint);
                }

            }
        }

    }


    /**
     * 1.初始化
     */
    private void init() {

        //10.绘制连线---画笔初始化
        pressPaint.setColor(Color.YELLOW);
        pressPaint.setStrokeWidth(4);
        errorPaint.setColor(Color.RED);
        errorPaint.setStrokeWidth(4);

        //6.初始化3个变量(bitmap)
        bitmapPointNormal = BitmapFactory.decodeResource(getResources(), R.drawable.normal_state);
        bitmapPointPress = BitmapFactory.decodeResource(getResources(), R.drawable.press_state);
        bitmapPointError = BitmapFactory.decodeResource(getResources(), R.drawable.error_state);


        //初始化圆点的半径
        bitmapR = bitmapPointNormal.getWidth() / 2;

        int width = getWidth();
        int height = getHeight();
        int offset = Math.abs(width - height) / 2;//偏移量
        int offsetX, offsetY;
        int space;//小空格边长

        //横屏
        if (width > height) {
            space = height / 4;
            offsetX = offset;
            offsetY = 0;
        } else {
            //竖屏
            space = width / 4;
            offsetX = 0;
            offsetY = offset;
        }

        //4.点的坐标位置
        points[0][0] = new Point(offsetX + space, offsetY + space);
        points[0][1] = new Point(offsetX + space * 2, offsetY + space);
        points[0][2] = new Point(offsetX + space * 3, offsetY + space);
        points[1][0] = new Point(offsetX + space, offsetY + space * 2);
        points[1][1] = new Point(offsetX + space * 2, offsetY + space * 2);
        points[1][2] = new Point(offsetX + space * 3, offsetY + space * 2);
        points[2][0] = new Point(offsetX + space, offsetY + space * 3);
        points[2][1] = new Point(offsetX + space * 2, offsetY + space * 3);
        points[2][2] = new Point(offsetX + space * 3, offsetY + space * 3);

        //表明已经初始化过
        inited = true;
    }

    /**
     * 对外实现的接口
     */
    public interface OnDrawFinishedListener {
        boolean OnDrawFinished(List<Integer> passList);
    }

    /**
     * 暴露接口，在外赋值
     * @param listener
     */
    public void setOnDrawFinishedListener(OnDrawFinishedListener listener) {
        this.listener = listener;
    }
}
