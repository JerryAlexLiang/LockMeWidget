package com.liangyang.lockmewidget;

import android.util.Log;

/**
 * 创建日期：2017/2/13 on 11:13
 * 作者:杨亮 liangyang
 * 描述:2.创建Point类，用于保存9个点
 */
public class Point {

    //定义三个整数常量来显示状态
    public static int STATE_NORMAL = 0;
    public static int STATE_PRESS = 1;
    public static int STATE_ERROR = 2;

    //保存当前点的坐标位置
    float x;
    float y;

    //当前状态
    int state = STATE_NORMAL;

    /**
     * 创建构造函数构造点
     *
     * @param x
     * @param y
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 计算距离的方法
     * @param a
     * @return
     */
    public float distance(Point a) {
        Log.d("Lock",a.x + "/" + a.y + "/" +x + "/" + y);
        float distance = (float) (Math.sqrt((x - a.x) * (x - a.x) + (y - a.y) * (y - a.y)));
        return distance;
    }

//    /**
//     *
//     * @param a 圆点
//     * @param b 移动点
//     * @return
//     */
//    public float distance(Point a ,Point b) {
//        float distance = (float) (Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y)));
//        return distance;
//    }

}
