package com.itheima.mobilesafe74.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Aleck_ on 2016/10/12.
 * 能够获取焦点的自定义TextView
 */
public class FocusTextView extends TextView {
    //使用在通过java代码创建控件
    public FocusTextView(Context context) {
        super(context);
    }

    //AttributeSet：属性集合
    //由系统调用（带属性+上下文环境的构造方法）
    public FocusTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //defStyleAttr：样式文件主题
    //由系统调用（带属性+上下文环境的构造方法+布局文件中文件样式文件构造方法）
    public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //重写获取焦点的方法,由系统调用的时候默认就会获取焦点
    @Override
    public boolean isFocused() {
        return true;
    }
}
