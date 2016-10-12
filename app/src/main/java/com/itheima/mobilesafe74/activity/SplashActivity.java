package com.itheima.mobilesafe74.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.itheima.mobilesafe74.R;
import com.itheima.mobilesafe74.utils.StreamUtil;
import com.itheima.mobilesafe74.utils.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {
    protected static final String tag = "SplashActivity";

    /**
     * 更新版本的状态码
     */
    protected static final int UPDATE_VERSION = 100;

    /**
     * 进入应用程序主界面的状态码
     */
    protected static final int ENTER_HOME = 101;

    /**
     * url地址出错的状态码
     */
    protected static final int URL_ERROR = 102;

    protected static final int IO_ERROR = 103;

    protected static final int JSON_ERROR = 104;


    private TextView tv_version_name;
    private int mLocalVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;

    //自己定义的成员变量，命名规则为m+单词，首字母大写
    private Handler mHandler = new Handler() {
        /**
         * @param msg
         */
        //alt+ctrl+向下箭头，向下拷贝相同代码
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    //弹出对话框，提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入应用程序主界面，activity跳转过程
                    enterHome();
                    break;
                case URL_ERROR:
                    //getApplicationContext()也可以。
                    //这些现在打印只是给程序员看的，上线之后就把这些都删掉
                    ToastUtil.show(SplashActivity.this, "url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(SplashActivity.this, "读取异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(SplashActivity.this, "json的解析异常");
                    enterHome();
                    break;
            }
        }
    };

    /**
     * 弹出对话框，提示用户更新
     */
    private void showUpdateDialog() {
        //对话框是依赖于activity存在的,所以用this，表示当前界面
        //getApplicationContext():只是一个全文的上下文环境，不能指向任何一个activity对象,因此使用这个会报错
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("版本更新");
        //设置描述文本内容
        builder.setMessage(mVersionDes);

        //积极按钮，立即更换新
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载apk,apk链接地址，downloadUrl
                downloadApk();
            }
        });

        builder.setNeutralButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //取消对话框，进入主界面
                enterHome();
            }
        });

        //点击取消对话框按钮的事件监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //即使用户点击取消，也需要让其进入应用程序主界面
                enterHome();
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    private void downloadApk() {
        //apk的下载地址，放置apk的所在路径

        //1，判断sd卡是否可用是否挂在上
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //2,获取sd路径
            //File.separator:为分隔符“\”
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "mobilesafe74.apk";
            //3,发送请求获取apk，并且放置到指定路径
            HttpUtils httpUtils = new HttpUtils();
            //4,发送请求，传递参数（下载地址，下载应用放置位置）
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功(下载过后的放置在sd卡中的apk)
                    Log.i(tag, "下载成功");
                    File file = responseInfo.result;
                    //提示用户安装
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                    Log.i(tag, "下载失败");
                }

                //刚刚开始下载的方法
                @Override
                public void onStart() {
                    Log.i(tag, "刚刚开始下载");
                    super.onStart();
                }

                //下载过程的方法（下载apk总大小，当前下载位置，是否正在下载）
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    Log.i(tag, "下载中...");
                    Log.i(tag, "total:" + total);
                    Log.i(tag, "current:" + current);
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }

    /**
     * 安装对应apk
     *
     * @param file 安装文件
     */
    private void installApk(File file) {
        //系统应用界面，源码，安装apk的入口，隐式开启源码的应用
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        /*
        //文件作为数据源
        intent.setData(Uri.fromFile(file));
        //设置安装的类型
        intent.setType("application/vnd.android.package-archive");
        */
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        startActivity(intent);
        startActivityForResult(intent, 0);

        /*
         安装apk注意事项：
         1，将原有的应用覆盖掉，包名必须一致
         2，签名一致？？？
         从Eclipse运行到手机上的应用，使用的是bin目录下的应用，使用debug.keystore签名应用

         手机卫士版本一，右键运行至手机的，所以使用的签名是debug.keystore
         手机卫士版本二，单独打包，生成相应签名文件heima74keystore

         生成一个heima74keystore作为签名文件的apk

         签名一致，包名不同：生成两个手机卫士apk，包名是应用的唯一性标志
         签名不同，包名一致：覆盖安装失败

         1.0 keystore
         2.0

         keyStore+密码妥善保存，svn服务器
         */
    }

    //开启一个activity后，返回结果调用的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 進入程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //在開啓一個新的界面后，將導航界面關閉（導航界面只顯示一次）
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉头部
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        //初始化UI
        initUI();
        //初始化数据
        initDate();

    }

    /**
     * 获取数据方法
     */
    private void initDate() {
        //1,应用版本号
        tv_version_name.setText("版本名称：" + getVersionName());
        //2，检测（本地版本号和服务器版本号对比）是否有更新，如果有更新，提示用户下载(member)
        //使用ctrl+alt+f：相当于eclipse中的ctrl+1,自动生成定义成员变量
        //使用shift+f6:修改变量名称
        /**
         * alt+shift+k:添加方法注释
         * Ctrl+alt +F 跟Eclipse的Ctrl +1一样,Ctrl+alt+L 格式化当前代码 以下是我自己整理的快捷键：
         * 1、重命名方法或者变量 shift +F6 2、ctrl+p 3、ctrl+shift+enter 自动添加分号，光标自动跳到末尾
         * 4、Ctrl+alt+o 缺少的import语句被加入，多余的import语句被删除 5、Ctrl+ alt+M 抽取方法
         * 6、Ctrl+F12 查看文件方法结构
         * ctrl+shift+u:大小写切换
         * ctrl+x:删除行
         * 7、Ctrl+Y 删除行 8、Ctrl+/ 注释 9、Ctrl+alt +F 跟Eclipse的Ctrl +1一样 10、Ctrl+alt+ ...
         */
        mLocalVersionCode = getVersionCode();

        //3,获取服务器的版本号（客户端发请求，服务端给响应，（Json,xml））
        //http://www.oxxx.com/update74.json?key=value   返回200请求成功，流的方式将数读取出来
        /**
         * json中包含：
         *1.更新版本的版本名称
         * 2.新版本的描述信息
         * 3.服务器版本号
         * 4.新版本apk下载地址
         */
        checkVersion();
    }

    /**
     * 检测版本号
     */
    private void checkVersion() {
        new Thread() {

            private String json;

            public void run() {
                //发送请求获取数据,参数则为请求json的链接地址
                //http://192.168.173.2:8080/udate74.json 这里Ip是已经写死的,可以用但是不是最好的.
                //http://10.0.2.2:8080/udate74.json:仅限于模拟器访问电脑tmcat
//                Message msg = new Message();      //比Message.obtain()低效
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //1，封装url地址
                    URL url = new URL("http://10.1.14.141:8080/udate74.json");
                    //2，开启一个链接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3,设置常见请求参数（请求头）

                    //请求超时
                    connection.setConnectTimeout(2000);     //如果2秒钟没有链接上服务器
                    //读取超时
                    connection.setReadTimeout(2000);        //读取数据过程中信号不好，读取2秒没有读取完

                    //默认就是get请求,格式为大写
//                    connection.setRequestMethod("POST");

                    //4，获取响应码
                    if (connection.getResponseCode() == 200) {
                        //5，以流的形式，将数据获取下来
                        InputStream is = connection.getInputStream();
                        //6，将流转化成字符串（工具类封装）
                        json = StreamUtil.streamToString(is);
                        Log.i(tag, json);
                        //7，json解析

                        JSONObject jsonObject = new JSONObject(json);

                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

                        //debug
                        Log.i(tag, versionCode);

                        //8,比对版本号（服务器版本号大于本地版本号，提示更新）
                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                            //提示用户更新，弹出对话框（UI)，消息机制
                            msg.what = UPDATE_VERSION;      //ctrl+shift+u:大小写切换
                        } else {
                            //进入应用程序主界面
                            msg.what = ENTER_HOME;
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                } finally {
                    //指定睡眠时间，请求网络的时长超过4秒则不做处理
                    //请求网络小于4秒，强制其满4秒
                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);  //发送消息
                }
            }
        }.start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();

    }

    /**
     * 返回版本号
     *
     * @return 非0则返回成功
     */
    private int getVersionCode() {
        //1、获取包管理者packageManager
        PackageManager pm = getPackageManager();
        //2、获取包管理者的版本信息,0:代表基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //获取版本名称
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获取版本号：清单文件中
     *
     * @return 返回版本号    null表示异常
     */
    private String getVersionName() {
        //1、获取包管理者packageManager
        PackageManager pm = getPackageManager();
        //2、获取包管理者的版本信息,0:代表基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(this.getPackageName(), 0);
            //获取版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI方法，alt+shift+j
     */
    private void initUI() {
        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
    }
}
