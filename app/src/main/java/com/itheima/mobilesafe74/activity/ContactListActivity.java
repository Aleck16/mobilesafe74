package com.itheima.mobilesafe74.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.mobilesafe74.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aleck_ on 2016/10/15.
 */
public class ContactListActivity extends Activity {

    protected static final String tag = "ContactListActivity";

    private List<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
    private ListView lv_contact;
    private MyAdapter mAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //8，填充数据适配器
            mAdapter = new MyAdapter();
            lv_contact.setAdapter(mAdapter);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initUI();
        initData();
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int i) {
            return contactList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view = View.inflate(getApplicationContext(), R.layout.listview_contact_item, null);

            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);

            tv_name.setText(getItem(i).get("name"));
            tv_phone.setText(getItem(i).get("phone"));


            return view;
        }
    }

    /**
     * 获取系统联系人数据的方法
     */
    private void initData() {
        //因为读取联系人可能是个耗时操作，放置到子线程中
        new Thread() {
            @Override
            public void run() {
                //1，获取内容解析器的对象
                ContentResolver contentResolver = getContentResolver();
                //2，查询我们系统联系人数据库表的过程（读取联系人权限）
                Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null, null, null);
                //成员变量，在使用之前清空一次
                contactList.clear();
                //3，循环游标，直到没有数据为止
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    if (TextUtils.isEmpty(id)) {
                        continue;
                    }
//                    Log.i(tag, "id=" + id);
                    //4，根据用户唯一Id值，查询data表和mimetype表生成的视图，获取data以及mimetype字段
                    Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1", "mimetype"},
                            "raw_contact_id = ?", new String[]{id}, null);
                    //5，循环获取每一个联系人的电话号码以及姓名，数据类型
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    while (indexCursor.moveToNext()) {
                        String data = indexCursor.getString(0);
                        String type = indexCursor.getString(1);

                        //6，区分类型去给HashMap填充数据
                        if (type.equals("vnd.android.cursor.item/phone_v2")) {
                            //数据非空的判断
                            if (!TextUtils.isEmpty(data)) {
                                hashMap.put("phone", data);
                            }
                        } else if (type.equals("vnd.android.cursor.item/name")) {
                            if (!TextUtils.isEmpty(data)) {
                                hashMap.put("name", data);
                            }
                        }
                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                }
                cursor.close();
                //7，消息机制填充数据，属于UI范围，所以线程里面不能操作
                //发送一个空的消息，告诉主线程可以使用主线程填充好的数据
                mHandler.sendEmptyMessage(0);
            }
        }.start();

    }

    private void initUI() {
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1，获取点中条目的索引指向集合中的对象
                if (mAdapter != null) {
                    HashMap<String, String> hashMap = mAdapter.getItem(i);
                    //2，获取当前条目只想集合对应的电话号码
                    String phone = hashMap.get("phone");
                    //3,此电话号码需要给第三个导航界面使用

                    //4，在结束此界面回到前一个导航界面的时候，需要将数据返回
                    Intent intent=new Intent();
                    intent.putExtra("phone",phone);
                    setResult(0,intent);

                    finish();

                }

            }
        });

    }


}
