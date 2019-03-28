package cn.weipan.fb.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.weipan.fb.dao.db.MessageInfo;

public class MessageHelper extends SQLiteOpenHelper {
//    private static String dbFile = "/sdcard/sql";
    public MessageHelper(Context context) {
        super(context, MessageInfo.DB_NAME, null, MessageInfo.SATRT_VERSION);
//        File path = new File(dbFile).getParentFile();
//        if (!path.exists()) {
//            path.mkdirs();
//        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MessageInfo.CREATE_TABLE_SQL);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
