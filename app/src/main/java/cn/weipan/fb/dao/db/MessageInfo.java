package cn.weipan.fb.dao.db;

/**
 * Author：雨沐风  2016-08-12 11:04
 * Email：admin@yumufeng.com
 */
public interface MessageInfo {
    String DB_NAME = "message.db";
    int SATRT_VERSION = 1;
    String CREATE_TABLE_SQL = "create table message (_id integer primary key autoincrement,pid varchar,title varchar,content text,time int(12),type int(2),link varchar, is_read int(1))";
    String COLUMN_PID = "pid";
    String COLUMN_TITLE = "title";
    String COLUMN_CONTENT = "content";
    String COLUMN_TIME = "time";
    String COLUMN_TYPE ="type";
    String COLUMN_LINK = "link";
    String COLUMN_IS_READ = "is_read";
    String TABLE = "message";
}
