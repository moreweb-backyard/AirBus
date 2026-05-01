package com.tencent.bugly.proguard;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class p extends SQLiteOpenHelper {
    private static int a = 12;
    private Context b;
    private List<com.tencent.bugly.a> c;

    public p(Context context, List<com.tencent.bugly.a> list) {
        StringBuilder sb = new StringBuilder("bugly_db_");
        com.tencent.bugly.crashreport.common.info.a.a(context).getClass();
        super(context, sb.toString(), (SQLiteDatabase.CursorFactory) null, a);
        this.b = context;
        this.c = list;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final synchronized void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS t_ui");
            sb.append(" ( _id");
            sb.append(" INTEGER PRIMARY KEY");
            sb.append(" , _tm");
            sb.append(" int");
            sb.append(" , _ut");
            sb.append(" int");
            sb.append(" , _tp");
            sb.append(" int");
            sb.append(" , _dt");
            sb.append(" blob");
            sb.append(" , _pc");
            sb.append(" text");
            sb.append(" ) ");
            w.c("create %s", sb.toString());
            sQLiteDatabase.execSQL(sb.toString());
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS t_lr");
            sb.append(" ( _id");
            sb.append(" INTEGER PRIMARY KEY");
            sb.append(" , _tp");
            sb.append(" int");
            sb.append(" , _tm");
            sb.append(" int");
            sb.append(" , _pc");
            sb.append(" text");
            sb.append(" , _th");
            sb.append(" text");
            sb.append(" , _dt");
            sb.append(" blob");
            sb.append(" ) ");
            w.c("create %s", sb.toString());
            sQLiteDatabase.execSQL(sb.toString());
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS t_pf");
            sb.append(" ( _id");
            sb.append(" integer");
            sb.append(" , _tp");
            sb.append(" text");
            sb.append(" , _tm");
            sb.append(" int");
            sb.append(" , _dt");
            sb.append(" blob");
            sb.append(",primary key(_id");
            sb.append(",_tp");
            sb.append(" )) ");
            w.c("create %s", sb.toString());
            sQLiteDatabase.execSQL(sb.toString());
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS t_crd");
            sb.append(" ( _id");
            sb.append(" integer");
            sb.append(" , _pc");
            sb.append(" text");
            sb.append(" , _tm");
            sb.append(" int");
            sb.append(" , _fl");
            sb.append(" int");
            sb.append(" , _sv");
            sb.append(" text");
            sb.append(" , _av");
            sb.append(" text");
            sb.append(" , _uid");
            sb.append(" integer");
            sb.append(",primary key(_id");
            sb.append(",_pc");
            sb.append(",_uid");
            sb.append(" )) ");
            w.c("create %s", sb.toString());
            sQLiteDatabase.execSQL(sb.toString());
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS t_cr");
            sb.append(" ( _id");
            sb.append(" INTEGER PRIMARY KEY");
            sb.append(" , _tm");
            sb.append(" int");
            sb.append(" , _s1");
            sb.append(" text");
            sb.append(" , _up");
            sb.append(" int");
            sb.append(" , _me");
            sb.append(" int");
            sb.append(" , _uc");
            sb.append(" int");
            sb.append(" , _dt");
            sb.append(" blob");
            sb.append(" ) ");
            w.c("create %s", sb);
            sQLiteDatabase.execSQL(sb.toString());
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS dl_1002");
            sb.append(" (_id");
            sb.append(" integer primary key autoincrement, _dUrl");
            sb.append(" varchar(100), _sFile");
            sb.append(" varchar(100), _sLen");
            sb.append(" INTEGER, _tLen");
            sb.append(" INTEGER, _MD5");
            sb.append(" varchar(100), _DLTIME");
            sb.append(" INTEGER)");
            w.c("create %s", sb);
            sQLiteDatabase.execSQL(sb.toString());
            sb.setLength(0);
            sb.append("CREATE TABLE IF NOT EXISTS ge_1002");
            sb.append(" (_id");
            sb.append(" integer primary key autoincrement, _time");
            sb.append(" INTEGER, _datas");
            sb.append(" blob)");
            w.c("create %s", sb);
            sQLiteDatabase.execSQL(sb.toString());
            sb.setLength(0);
            sb.append(" CREATE TABLE IF NOT EXISTS st_1002");
            sb.append(" ( _id");
            sb.append(" integer");
            sb.append(" , _tp");
            sb.append(" text");
            sb.append(" , _tm");
            sb.append(" int");
            sb.append(" , _dt");
            sb.append(" blob");
            sb.append(",primary key(_id");
            sb.append(",_tp");
            sb.append(" )) ");
            w.c("create %s", sb.toString());
            sQLiteDatabase.execSQL(sb.toString());
        } catch (Throwable th) {
            if (!w.b(th)) {
                th.printStackTrace();
            }
        }
        if (this.c == null) {
            return;
        }
        Iterator<com.tencent.bugly.a> it = this.c.iterator();
        while (it.hasNext()) {
            try {
                it.next().onDbCreate(sQLiteDatabase);
            } catch (Throwable th2) {
                if (!w.b(th2)) {
                    th2.printStackTrace();
                }
            }
        }
    }

    private synchronized boolean a(SQLiteDatabase sQLiteDatabase) {
        try {
            for (String str : new String[]{"t_crd", "t_lr", "t_ui", "t_pf"}) {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + str);
            }
        } catch (Throwable th) {
            if (!w.b(th)) {
                th.printStackTrace();
            }
            return false;
        }
        return true;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final synchronized void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        w.d("upgrade %d to %d , drop tables!", Integer.valueOf(i), Integer.valueOf(i2));
        if (this.c != null) {
            Iterator<com.tencent.bugly.a> it = this.c.iterator();
            while (it.hasNext()) {
                try {
                    it.next().onDbUpgrade(sQLiteDatabase, i, i2);
                } catch (Throwable th) {
                    if (!w.b(th)) {
                        th.printStackTrace();
                    }
                }
            }
        }
        if (a(sQLiteDatabase)) {
            onCreate(sQLiteDatabase);
            return;
        }
        w.d("drop fail delete db", new Object[0]);
        File databasePath = this.b.getDatabasePath("bugly_db");
        if (databasePath != null && databasePath.canWrite()) {
            databasePath.delete();
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    @TargetApi(11)
    public final synchronized void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (com.tencent.bugly.crashreport.common.info.b.c() >= 11) {
            w.d("drowngrade %d to %d drop tables!}", Integer.valueOf(i), Integer.valueOf(i2));
            if (this.c != null) {
                Iterator<com.tencent.bugly.a> it = this.c.iterator();
                while (it.hasNext()) {
                    try {
                        it.next().onDbDowngrade(sQLiteDatabase, i, i2);
                    } catch (Throwable th) {
                        if (!w.b(th)) {
                            th.printStackTrace();
                        }
                    }
                }
            }
            if (a(sQLiteDatabase)) {
                onCreate(sQLiteDatabase);
                return;
            }
            w.d("drop fail delete db", new Object[0]);
            File databasePath = this.b.getDatabasePath("bugly_db");
            if (databasePath != null && databasePath.canWrite()) {
                databasePath.delete();
            }
        }
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final synchronized SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase readableDatabase;
        readableDatabase = null;
        int i = 0;
        while (readableDatabase == null && i < 5) {
            i++;
            try {
                readableDatabase = super.getReadableDatabase();
            } catch (Throwable unused) {
                w.d("try db count %d", Integer.valueOf(i));
                if (i == 5) {
                    w.e("get db fail!", new Object[0]);
                }
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return readableDatabase;
    }

    @Override // android.database.sqlite.SQLiteOpenHelper
    public final synchronized SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase writableDatabase;
        writableDatabase = null;
        int i = 0;
        while (writableDatabase == null && i < 5) {
            i++;
            try {
                writableDatabase = super.getWritableDatabase();
            } catch (Throwable unused) {
                w.d("try db %d", Integer.valueOf(i));
                if (i == 5) {
                    w.e("get db fail!", new Object[0]);
                }
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (writableDatabase == null) {
            w.d("db error delay error record 1min", new Object[0]);
        }
        return writableDatabase;
    }
}
