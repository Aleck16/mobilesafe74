package com.itheima.mobilesafe74.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aleck_ on 2016/10/12.
 */
public class SpUtil {
    private static SharedPreferences sp;

    /**
     * 写入一个boolean变量至sp中
     * @param ctx   上下文环境
     * @param key   存储节点的名称
     * @param value 存储节点的值
     */
    //写
    public static void putBoolean(Context ctx, String key, boolean value) {
        //（存储节点文件的名称，读写方式）
        if(sp==null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }

    /**
     * 在sp中读取一个boolean变量
     * @param ctx   上下文环境
     * @param key   存储节点的名称
     * @param defValue  没有此节点默认值
     * @return          返回节点值
     */
    //读
    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        //（存储节点文件的名称，读写方式）
        if(sp==null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue);
    }

    /**
     * 写入一个String变量至sp中
     * @param ctx   上下文环境
     * @param key   存储节点的名称
     * @param value 存储节点的值
     */
    //写
    public static void putString(Context ctx, String key, String value) {
        //（存储节点文件的名称，读写方式）
        if(sp==null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }

    /**
     * 在sp中读取一个String变量
     * @param ctx   上下文环境
     * @param key   存储节点的名称
     * @param defValue  没有此节点默认值
     * @return          返回节点值
     */
    //读
    public static String getString(Context ctx, String key, String defValue) {
        //（存储节点文件的名称，读写方式）
        if(sp==null){
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key,defValue);
    }
}
