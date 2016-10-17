package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.utils.ConstantValue;
import com.itheima.mobilesafe74.utils.SpUtil;
import com.itheima.mobilesafe74.utils.ToastUtil;

/**
 * Created by Aleck_ on 2016/10/16.
 */
public abstract class BaseSetupActivity extends Activity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //2，创建手势管理的对象，用作管理在onTouchEvent(event)传递过来的手势动作
        //velocityX：X轴的移动速率
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            //velocityX：X轴的移动速率
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //监听手势的移动
                if (e1.getX() - e2.getX() > 0) {
                    //调用子类的下一页方法，抽象的方法

                    //在第一界面的时候，无响应
                    //在第二界面的时候，跳转到第一个界面
                    //在第三界面的时候，跳转到第二个界面
                    //...
                    showNextPage();
                }
                if (e1.getX() - e2.getX() < 0) {
                    //调用子类的上一页的方法
                    //在第一界面的时候，跳转到第二个界面
                    //在第二界面的时候，跳转到第三个界面
                    //在第三界面的时候，跳转到第四个界面
                    //...
                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

//    public void nextPage(View view){
//        //调用一个抽象方法
//    }

    //1，监听屏幕上响应的事件类型（按下（1次），移动（多次），抬起（1次））
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //3，通过手势类，接收多种类型的事件用作对比的方法
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    //下一页的抽象方法，由子类决定具体的跳转到哪个界面
    protected abstract void showNextPage();
    //上一页的抽象方法，由子类决定具体的跳转到哪个界面
    protected abstract void showPrePage();


    //点击下一页按钮的时候，根据子类的shownextPage方法做相应跳转
    public void nextPage(View view) {
        showNextPage();
    }

    //点击上一页按钮的时候，根据子类的showPrePage方法做相应跳转
    public void prePage(View view) {
        showPrePage();
    }
}
