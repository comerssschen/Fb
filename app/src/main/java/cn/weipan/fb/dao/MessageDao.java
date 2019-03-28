package cn.weipan.fb.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.weipan.fb.bean.Message;
import cn.weipan.fb.dao.db.MessageInfo;

public class MessageDao {

    private Context context;

    private final MessageHelper helper;
    public MessageDao(Context context) {
        helper = new MessageHelper(context);
        this.context = context;
    }

    /**
     * @param pid  编号 （唯一编码）
     * @param title 标题
     * @param content 内容
     * @param type 类型：1 系统,2 消息；
     * @param link   连接
     */
    public void addMessage(String pid,String title,String content,int type ,String link,String time){
        SQLiteDatabase db = helper.getWritableDatabase();

//        long currentTime = System.currentTimeMillis();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
//        Date date = new Date(currentTime);
//        String time = formatter.format(date);

        ContentValues contentvalues = new ContentValues();
        contentvalues.put(MessageInfo.COLUMN_PID,pid);
        contentvalues.put(MessageInfo.COLUMN_TITLE,title);
        contentvalues.put(MessageInfo.COLUMN_CONTENT,content);
        contentvalues.put(MessageInfo.COLUMN_TYPE,type);
        contentvalues.put(MessageInfo.COLUMN_LINK,link);
        contentvalues.put(MessageInfo.COLUMN_TIME,time);
        contentvalues.put(MessageInfo.COLUMN_IS_READ,0);
        long insert = db.insert(MessageInfo.TABLE, null, contentvalues);
        Log.e("addMessage", "insert: "+insert);

    }

    /**
     * 获取所有的消息
     * @param type  根据type类型获取所有信息 类型：1 系统,2 消息；
     * @return
     */
    public List<Message> getAllMessage(String type,String userName){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<Message> list = new ArrayList<>();
        if (db.isOpen()){
            Cursor query = db.query(MessageInfo.TABLE, new String[]{"title", "content", "time", "link","type"},
                    MessageInfo.COLUMN_TYPE +"=? and "+MessageInfo.COLUMN_LINK+"=?",
                    new String[]{type,userName}, null, null, "time DESC");
            ArrayList<Message> sc = new ArrayList<>();
            while (query.moveToNext()){
                Message message = new Message();
                message.setTitle(query.getString(0));
                message.setContent(query.getString(1));
                message.setTime(query.getString(2));
                message.setIs_read(query.getInt(3));
                message.setType(query.getInt(4));
                sc.add(message);
            }
            query.close();
            list.addAll(sc);
            db.close();
        }
        return list;
    }

}
