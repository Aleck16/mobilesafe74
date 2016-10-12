package com.itheima.mobilesafe74.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Aleck_ on 2016/10/11.
 */
public class ToastUtil {
    /**
     * @param ctx   上下文环境
     * @param msg   打印文本内容
     */
    //打印吐司,默认时长较短的吐司
    public static void show(Context ctx, String msg){
        Toast.makeText(ctx,msg,Toast.LENGTH_SHORT).show();
    }
}
