package com.pzj.ipcdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/* loaded from: classes.dex */
public class DatabaseUtil {
    private static final String CREATE_ALARMLOG_TABLE = "create table alarmlog(id integer primary key autoincrement, did text not null, content text not null, createtime text not null);";
    private static final String CREATE_FIRSTPIC_TABLE = "create table firstpic(id integer primary key autoincrement,did text not null, filepath text not null)";
    private static final String CREATE_PRESENT_TABLE = "create table present(id integer primary key autoincrement,did text not null, position text not null, filepath text not null)";
    private static final String CREATE_STUDENT_TABLE = "create table cameralist (id integer primary key autoincrement, name text not null, did text not null, user text not null,pwd text);";
    private static final String CREATE_VIDEO_PICTURE_TABLE = "create table cameravidpic(id integer primary key autoincrement, did text not null, filepath text not null, createtime text not null, type text not null);";
    private static final String DATABASE_ALARMLOG_TABLE = "alarmlog";
    private static final String DATABASE_FIRSTPIC_TABLE = "firstpic";
    private static final String DATABASE_NAME = "p2p_camera_database";
    private static final String DATABASE_PRESENT_TABLE = "present";
    private static final String DATABASE_TABLE = "cameralist";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASW_VIDEOPICTURE_TABLE = "cameravidpic";
    public static final String KEY_ALARMLOG_CONTENT = "content";
    public static final String KEY_CREATETIME = "createtime";
    public static final String KEY_DID = "did";
    public static final String KEY_FILEPATH = "filepath";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_POSITION = "position";
    public static final String KEY_PWD = "pwd";
    public static final String KEY_TYPE = "type";
    public static final String KEY_USER = "user";
    private static final String TAG = "DatabaseUtil";
    public static final String TYPE_PICTURE = "picture";
    public static final String TYPE_VIDEO = "video";
    private final Context mCtx;
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DatabaseUtil.DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) throws SQLException {
            Log.i(DatabaseUtil.TAG, "Creating student_table: create table cameralist (id integer primary key autoincrement, name text not null, did text not null, user text not null,pwd text);");
            Log.i(DatabaseUtil.TAG, "Creating Video_Picture_Table: create table cameravidpic(id integer primary key autoincrement, did text not null, filepath text not null, createtime text not null, type text not null);");
            Log.i(DatabaseUtil.TAG, "Creating alarmlog_table: create table alarmlog(id integer primary key autoincrement, did text not null, content text not null, createtime text not null);");
            Log.i(DatabaseUtil.TAG, "Creating Firstpic_tablecreate table firstpic(id integer primary key autoincrement,did text not null, filepath text not null)");
            sQLiteDatabase.execSQL(DatabaseUtil.CREATE_STUDENT_TABLE);
            sQLiteDatabase.execSQL(DatabaseUtil.CREATE_VIDEO_PICTURE_TABLE);
            sQLiteDatabase.execSQL(DatabaseUtil.CREATE_ALARMLOG_TABLE);
            sQLiteDatabase.execSQL(DatabaseUtil.CREATE_FIRSTPIC_TABLE);
            sQLiteDatabase.execSQL(DatabaseUtil.CREATE_PRESENT_TABLE);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>!!!!!!!!!!!!!!!!1");
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            Log.w(DatabaseUtil.TAG, "Upgrading database from version " + i + " to " + i2);
        }
    }

    public DatabaseUtil(Context context) {
        this.mCtx = context;
    }

    public DatabaseUtil open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        this.mDbHelper.close();
    }

    public long createCamera(String str, String str2, String str3, String str4) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, str);
        contentValues.put(KEY_DID, str2);
        contentValues.put(KEY_USER, str3);
        contentValues.put(KEY_PWD, str4);
        return this.mDb.insert(DATABASE_TABLE, null, contentValues);
    }

    public boolean deleteCamera(long j) {
        SQLiteDatabase sQLiteDatabase = this.mDb;
        StringBuilder sb = new StringBuilder();
        sb.append("id=");
        sb.append(j);
        return sQLiteDatabase.delete(DATABASE_TABLE, sb.toString(), null) > 0;
    }

    public boolean deleteCamera(String str) {
        this.mDb.delete(DATABASE_FIRSTPIC_TABLE, "did='" + str + "'", null);
        this.mDb.delete(DATABASW_VIDEOPICTURE_TABLE, "did='" + str + "'", null);
        this.mDb.delete(DATABASE_ALARMLOG_TABLE, "did='" + str + "'", null);
        this.mDb.delete(DATABASE_PRESENT_TABLE, "did='" + str + "'", null);
        SQLiteDatabase sQLiteDatabase = this.mDb;
        StringBuilder sb = new StringBuilder();
        sb.append("did='");
        sb.append(str);
        sb.append("'");
        return sQLiteDatabase.delete(DATABASE_TABLE, sb.toString(), null) > 0;
    }

    public Cursor fetchAllCameras() {
        return this.mDb.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_DID, KEY_USER, KEY_PWD}, null, null, null, null, null);
    }

    public Cursor fetchCamera(long j) throws SQLException {
        Cursor cursorQuery = this.mDb.query(true, DATABASE_TABLE, new String[]{KEY_ID, KEY_NAME, KEY_DID, KEY_USER, KEY_PWD}, "id=" + j, null, null, null, null, null);
        if (cursorQuery != null) {
            cursorQuery.moveToFirst();
        }
        return cursorQuery;
    }

    public boolean updateCamera(String str, String str2, String str3, String str4, String str5) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, str2);
        contentValues.put(KEY_DID, str3);
        contentValues.put(KEY_USER, str4);
        contentValues.put(KEY_PWD, str5);
        SQLiteDatabase sQLiteDatabase = this.mDb;
        StringBuilder sb = new StringBuilder();
        sb.append("did='");
        sb.append(str);
        sb.append("'");
        return sQLiteDatabase.update(DATABASE_TABLE, contentValues, sb.toString(), null) > 0;
    }

    public boolean updateCameraUser(String str, String str2, String str3) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER, str2);
        contentValues.put(KEY_PWD, str3);
        SQLiteDatabase sQLiteDatabase = this.mDb;
        StringBuilder sb = new StringBuilder();
        sb.append("did='");
        sb.append(str);
        sb.append("'");
        return sQLiteDatabase.update(DATABASE_TABLE, contentValues, sb.toString(), null) > 0;
    }

    public long createPresent(String str, String str2, String str3) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DID, str);
        contentValues.put(KEY_POSITION, str2);
        contentValues.put(KEY_FILEPATH, str3);
        return this.mDb.insert(DATABASE_PRESENT_TABLE, null, contentValues);
    }

    public Cursor queryAllPresent(String str, String str2) {
        return this.mDb.rawQuery("select filepath from present where  position='" + str2 + "' and " + KEY_DID + "='" + str + "'", null);
    }

    public boolean updatePresent(String str, String str2, String str3) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DID, str);
        contentValues.put(KEY_POSITION, str2);
        contentValues.put(KEY_FILEPATH, str3);
        SQLiteDatabase sQLiteDatabase = this.mDb;
        StringBuilder sb = new StringBuilder();
        sb.append("position='");
        sb.append(str2);
        sb.append("' and ");
        sb.append(KEY_DID);
        sb.append("='");
        sb.append(str);
        sb.append("'");
        return sQLiteDatabase.update(DATABASE_PRESENT_TABLE, contentValues, sb.toString(), null) > 0;
    }

    public boolean deleteOnePresent(String str, String str2) {
        return this.mDb.delete(DATABASE_PRESENT_TABLE, "did=? and position=?", new String[]{str, str2}) > 0;
    }

    public boolean deletePresent(String str) {
        return this.mDb.delete(DATABASE_PRESENT_TABLE, "did=?", new String[]{str}) > 0;
    }

    public long createVideoOrPic(String str, String str2, String str3, String str4) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DID, str);
        contentValues.put(KEY_FILEPATH, str2);
        contentValues.put(KEY_TYPE, str3);
        contentValues.put(KEY_CREATETIME, str4);
        return this.mDb.insert(DATABASW_VIDEOPICTURE_TABLE, null, contentValues);
    }

    public Cursor queryAllVideo(String str) {
        return this.mDb.rawQuery("select * from cameravidpic where  type='video' and did='" + str + "' order by " + KEY_FILEPATH + " desc", null);
    }

    public Cursor queryAllPicture(String str) {
        String str2 = "select * from cameravidpic where  type='picture' and did='" + str + "'";
        Log.d("tag", "queryAllPictrue sql:" + str2);
        return this.mDb.rawQuery(str2, null);
    }

    public Cursor queryVideoOrPictureByDate(String str, String str2, String str3) {
        return this.mDb.rawQuery("select * from cameravidpic where  type='picture' and did='" + str + "' and " + KEY_CREATETIME + "='" + str2 + "'", null);
    }

    public boolean deleteVideoOrPicture(String str, String str2, String str3) {
        return this.mDb.delete(DATABASW_VIDEOPICTURE_TABLE, "did=? and filepath=? and type=?", new String[]{str, str2, str3}) > 0;
    }

    public boolean deleteAllVideoOrPicture(String str, String str2) {
        return this.mDb.delete(DATABASW_VIDEOPICTURE_TABLE, "did=? and type=?", new String[]{str, str2}) > 0;
    }

    public boolean deldteAllVideoPicture(String str) {
        return this.mDb.delete(DATABASW_VIDEOPICTURE_TABLE, "did=?", new String[]{str}) > 0;
    }

    public long insertAlarmLogToDB(String str, String str2, String str3) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DID, str);
        contentValues.put(KEY_ALARMLOG_CONTENT, str2);
        contentValues.put(KEY_CREATETIME, str3);
        return this.mDb.insertOrThrow(DATABASE_ALARMLOG_TABLE, null, contentValues);
    }

    public Cursor queryAllAlarmLog(String str) {
        return this.mDb.rawQuery("select * from alarmlog where did='" + str + "' order by " + KEY_CREATETIME + " desc", null);
    }

    public boolean delAlarmLog(String str, String str2) {
        return this.mDb.delete(DATABASE_ALARMLOG_TABLE, "did=? and createtime=?", new String[]{str, str2}) > 0;
    }

    public boolean addFirstpic(String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_DID, str);
        contentValues.put(KEY_FILEPATH, str2);
        return this.mDb.insert(DATABASE_FIRSTPIC_TABLE, null, contentValues) > 0;
    }

    public Cursor queryFirstpic(String str) {
        return this.mDb.rawQuery("select *  from firstpic where did='" + str + "'", null);
    }

    public boolean delFirstpic(String str, String str2) {
        return this.mDb.delete(DATABASE_FIRSTPIC_TABLE, "did=? and filepath=? ", new String[]{str, str2}) > 0;
    }
}
