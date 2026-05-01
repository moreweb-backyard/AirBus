package com.shima.smartbushome.database;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public class DatabaseContext extends ContextWrapper {
    public static String dbDir = Environment.getExternalStorageDirectory().getPath() + "/smartbus/database";
    public static String dbPath = Environment.getExternalStorageDirectory().getPath() + "/smartbus/database/sbus.db";

    public DatabaseContext(Context context) {
        super(context);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getDatabasePath(String str) throws IOException {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
            return null;
        }
        File file = new File(dbDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        boolean zCreateNewFile = false;
        File file2 = new File(dbPath);
        if (file2.exists()) {
            zCreateNewFile = true;
        } else {
            try {
                zCreateNewFile = file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (zCreateNewFile) {
            return file2;
        }
        return null;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public SQLiteDatabase openOrCreateDatabase(String str, int i, SQLiteDatabase.CursorFactory cursorFactory) {
        return SQLiteDatabase.openOrCreateDatabase(dbPath, (SQLiteDatabase.CursorFactory) null);
    }
}
