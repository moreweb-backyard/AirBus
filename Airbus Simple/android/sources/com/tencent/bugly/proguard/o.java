package com.tencent.bugly.proguard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: BUGLY */
/* loaded from: classes.dex */
public final class o {
    private static o a = null;
    private static p b = null;
    private static boolean c = false;

    private o(Context context, List<com.tencent.bugly.a> list) {
        b = new p(context, list);
    }

    public static synchronized o a(Context context, List<com.tencent.bugly.a> list) {
        if (a == null) {
            a = new o(context, list);
        }
        return a;
    }

    public static synchronized o a() {
        return a;
    }

    public final long a(String str, ContentValues contentValues, n nVar, boolean z) {
        return a(str, contentValues, (n) null);
    }

    public final Cursor a(String str, String[] strArr, String str2, String[] strArr2, n nVar, boolean z) {
        return a(false, str, strArr, str2, null, null, null, null, null, null);
    }

    public final int a(String str, String str2, String[] strArr, n nVar, boolean z) {
        return a(str, str2, (String[]) null, (n) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long a(String str, ContentValues contentValues, n nVar) {
        long j;
        j = 0;
        try {
            try {
                try {
                    SQLiteDatabase writableDatabase = b.getWritableDatabase();
                    if (writableDatabase != null && contentValues != null) {
                        long jReplace = writableDatabase.replace(str, "_id", contentValues);
                        if (jReplace >= 0) {
                            w.c("[db] insert %s success", str);
                        } else {
                            w.d("[db] replace %s error", str);
                        }
                        j = jReplace;
                    }
                } catch (Throwable th) {
                    if (!w.a(th)) {
                        th.printStackTrace();
                    }
                    if (nVar != null) {
                    }
                }
                if (nVar != null) {
                    Long.valueOf(j);
                }
            } catch (Throwable th2) {
                if (nVar != null) {
                    Long.valueOf(0L);
                }
                throw th2;
            }
        } catch (Throwable th3) {
            throw th3;
        }
        return j;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Cursor a(boolean z, String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6, n nVar) {
        Cursor cursorQuery;
        cursorQuery = null;
        try {
            try {
                SQLiteDatabase writableDatabase = b.getWritableDatabase();
                if (writableDatabase != null) {
                    cursorQuery = writableDatabase.query(z, str, strArr, str2, strArr2, str3, str4, str5, str6);
                }
            } catch (Throwable th) {
                if (!w.a(th)) {
                    th.printStackTrace();
                }
            }
        } finally {
        }
        return cursorQuery;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int a(String str, String str2, String[] strArr, n nVar) {
        int iDelete;
        try {
            try {
                try {
                    SQLiteDatabase writableDatabase = b.getWritableDatabase();
                    iDelete = writableDatabase != null ? writableDatabase.delete(str, str2, strArr) : 0;
                } catch (Throwable th) {
                    if (!w.a(th)) {
                        th.printStackTrace();
                    }
                    if (nVar != null) {
                    }
                }
                if (nVar != null) {
                    Integer.valueOf(iDelete);
                }
            } catch (Throwable th2) {
                if (nVar != null) {
                    Integer.valueOf(0);
                }
                throw th2;
            }
        } catch (Throwable th3) {
            throw th3;
        }
        return iDelete;
    }

    public final boolean a(int i, String str, byte[] bArr, n nVar, boolean z) {
        if (!z) {
            a aVar = new a(4, null);
            aVar.a(i, str, bArr);
            v.a().b(aVar);
            return true;
        }
        return a(i, str, bArr, (n) null);
    }

    public final Map<String, byte[]> a(int i, n nVar, boolean z) {
        return a(i, (n) null);
    }

    public final boolean a(int i, String str, n nVar, boolean z) {
        return a(555, str, (n) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(int i, String str, byte[] bArr, n nVar) {
        try {
            try {
                q qVar = new q();
                qVar.a = i;
                qVar.f = str;
                qVar.e = System.currentTimeMillis();
                qVar.g = bArr;
                boolean zB = b(qVar);
                if (nVar == null) {
                    return zB;
                }
                Boolean.valueOf(zB);
                return zB;
            } catch (Throwable th) {
                if (!w.a(th)) {
                    th.printStackTrace();
                }
                if (nVar != null) {
                    Boolean.valueOf(false);
                }
                return false;
            }
        } catch (Throwable th2) {
            if (nVar != null) {
                Boolean.valueOf(false);
            }
            throw th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Map<String, byte[]> a(int i, n nVar) {
        HashMap map;
        List<q> listC;
        try {
            listC = c(i);
            map = new HashMap();
        } catch (Throwable th) {
            th = th;
            map = null;
        }
        try {
            for (q qVar : listC) {
                byte[] bArr = qVar.g;
                if (bArr != null) {
                    map.put(qVar.f, bArr);
                }
            }
        } catch (Throwable th2) {
            th = th2;
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return map;
        }
        return map;
    }

    public final boolean a(q qVar) {
        ContentValues contentValuesC;
        if (qVar == null) {
            return false;
        }
        try {
            SQLiteDatabase writableDatabase = b.getWritableDatabase();
            if (writableDatabase == null || (contentValuesC = c(qVar)) == null) {
                return false;
            }
            long jReplace = writableDatabase.replace("t_lr", "_id", contentValuesC);
            if (jReplace < 0) {
                return false;
            }
            w.c("insert %s success!", "t_lr");
            qVar.a = jReplace;
            return true;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }

    private boolean b(q qVar) {
        ContentValues contentValuesD;
        if (qVar == null) {
            return false;
        }
        try {
            SQLiteDatabase writableDatabase = b.getWritableDatabase();
            if (writableDatabase == null || (contentValuesD = d(qVar)) == null) {
                return false;
            }
            long jReplace = writableDatabase.replace("t_pf", "_id", contentValuesD);
            if (jReplace < 0) {
                return false;
            }
            w.c("insert %s success!", "t_pf");
            qVar.a = jReplace;
            return true;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x00b6 A[Catch: all -> 0x00c5, TRY_LEAVE, TryCatch #1 {all -> 0x00c5, blocks: (B:18:0x0040, B:19:0x004a, B:21:0x0051, B:23:0x0057, B:25:0x005b, B:27:0x0073, B:28:0x007b, B:30:0x0085, B:37:0x00b0, B:39:0x00b6), top: B:51:0x0040 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.List<com.tencent.bugly.proguard.q> a(int r11) {
        /*
            r10 = this;
            com.tencent.bugly.proguard.p r0 = com.tencent.bugly.proguard.o.b
            android.database.sqlite.SQLiteDatabase r0 = r0.getWritableDatabase()
            r9 = 0
            if (r0 == 0) goto Ld2
            if (r11 < 0) goto L25
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L1b java.lang.Throwable -> L20
            java.lang.String r2 = "_tp = "
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L1b java.lang.Throwable -> L20
            r1.append(r11)     // Catch: java.lang.Throwable -> L1b java.lang.Throwable -> L20
            java.lang.String r11 = r1.toString()     // Catch: java.lang.Throwable -> L1b java.lang.Throwable -> L20
            r4 = r11
            goto L26
        L1b:
            r11 = move-exception
            r0 = r11
            r11 = r9
            goto Lc6
        L20:
            r11 = move-exception
            r0 = r11
            r11 = r9
            goto Lb0
        L25:
            r4 = r9
        L26:
            java.lang.String r2 = "t_lr"
            r3 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            r8 = 0
            r1 = r0
            android.database.Cursor r11 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L1b java.lang.Throwable -> L20
            if (r11 != 0) goto L40
            if (r11 == 0) goto L3f
            boolean r0 = r11.isClosed()
            if (r0 != 0) goto L3f
            r11.close()
        L3f:
            return r9
        L40:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            r1.<init>()     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            r2.<init>()     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
        L4a:
            boolean r3 = r11.moveToNext()     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            r4 = 0
            if (r3 == 0) goto L7b
            com.tencent.bugly.proguard.q r3 = a(r11)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            if (r3 == 0) goto L5b
            r2.add(r3)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            goto L4a
        L5b:
            java.lang.String r3 = "_id"
            int r3 = r11.getColumnIndex(r3)     // Catch: java.lang.Throwable -> L73 java.lang.Throwable -> Lc5
            long r5 = r11.getLong(r3)     // Catch: java.lang.Throwable -> L73 java.lang.Throwable -> Lc5
            java.lang.String r3 = " or _id"
            r1.append(r3)     // Catch: java.lang.Throwable -> L73 java.lang.Throwable -> Lc5
            java.lang.String r3 = " = "
            r1.append(r3)     // Catch: java.lang.Throwable -> L73 java.lang.Throwable -> Lc5
            r1.append(r5)     // Catch: java.lang.Throwable -> L73 java.lang.Throwable -> Lc5
            goto L4a
        L73:
            java.lang.String r3 = "unknown id!"
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            com.tencent.bugly.proguard.w.d(r3, r4)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            goto L4a
        L7b:
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            int r3 = r1.length()     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            if (r3 <= 0) goto La3
            r3 = 4
            java.lang.String r1 = r1.substring(r3)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            java.lang.String r3 = "t_lr"
            int r0 = r0.delete(r3, r1, r9)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            java.lang.String r1 = "deleted %s illegle data %d"
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            java.lang.String r5 = "t_lr"
            r3[r4] = r5     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            r4 = 1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            r3[r4] = r0     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
            com.tencent.bugly.proguard.w.d(r1, r3)     // Catch: java.lang.Throwable -> Laf java.lang.Throwable -> Lc5
        La3:
            if (r11 == 0) goto Lae
            boolean r0 = r11.isClosed()
            if (r0 != 0) goto Lae
            r11.close()
        Lae:
            return r2
        Laf:
            r0 = move-exception
        Lb0:
            boolean r1 = com.tencent.bugly.proguard.w.a(r0)     // Catch: java.lang.Throwable -> Lc5
            if (r1 != 0) goto Lb9
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Lc5
        Lb9:
            if (r11 == 0) goto Ld2
            boolean r0 = r11.isClosed()
            if (r0 != 0) goto Ld2
            r11.close()
            goto Ld2
        Lc5:
            r0 = move-exception
        Lc6:
            if (r11 == 0) goto Ld1
            boolean r1 = r11.isClosed()
            if (r1 != 0) goto Ld1
            r11.close()
        Ld1:
            throw r0
        Ld2:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.o.a(int):java.util.List");
    }

    public static void a(List<q> list) {
        SQLiteDatabase writableDatabase;
        if (list == null || list.size() == 0 || (writableDatabase = b.getWritableDatabase()) == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (q qVar : list) {
            sb.append(" or _id");
            sb.append(" = ");
            sb.append(qVar.a);
        }
        String string = sb.toString();
        if (string.length() > 0) {
            string = string.substring(4);
        }
        sb.setLength(0);
        try {
            w.c("deleted %s data %d", "t_lr", Integer.valueOf(writableDatabase.delete("t_lr", string, null)));
        } catch (Throwable th) {
            if (w.a(th)) {
                return;
            }
            th.printStackTrace();
        }
    }

    public static void b(int i) {
        String str;
        SQLiteDatabase writableDatabase = b.getWritableDatabase();
        if (writableDatabase != null) {
            if (i >= 0) {
                try {
                    try {
                        str = "_tp = " + i;
                    } catch (Throwable th) {
                        if (w.a(th)) {
                            return;
                        }
                        th.printStackTrace();
                        return;
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            } else {
                str = null;
            }
            w.c("deleted %s data %d", "t_lr", Integer.valueOf(writableDatabase.delete("t_lr", str, null)));
        }
    }

    private static ContentValues c(q qVar) {
        if (qVar == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (qVar.a > 0) {
                contentValues.put("_id", Long.valueOf(qVar.a));
            }
            contentValues.put("_tp", Integer.valueOf(qVar.b));
            contentValues.put("_pc", qVar.c);
            contentValues.put("_th", qVar.d);
            contentValues.put("_tm", Long.valueOf(qVar.e));
            if (qVar.g != null) {
                contentValues.put("_dt", qVar.g);
            }
            return contentValues;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static q a(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            q qVar = new q();
            qVar.a = cursor.getLong(cursor.getColumnIndex("_id"));
            qVar.b = cursor.getInt(cursor.getColumnIndex("_tp"));
            qVar.c = cursor.getString(cursor.getColumnIndex("_pc"));
            qVar.d = cursor.getString(cursor.getColumnIndex("_th"));
            qVar.e = cursor.getLong(cursor.getColumnIndex("_tm"));
            qVar.g = cursor.getBlob(cursor.getColumnIndex("_dt"));
            return qVar;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private List<q> c(int i) throws Throwable {
        Cursor cursorQuery;
        try {
            SQLiteDatabase writableDatabase = b.getWritableDatabase();
            if (writableDatabase != null) {
                String str = "_id = " + i;
                cursorQuery = writableDatabase.query("t_pf", null, str, null, null, null, null);
                if (cursorQuery == null) {
                    if (cursorQuery != null && !cursorQuery.isClosed()) {
                        cursorQuery.close();
                    }
                    return null;
                }
                try {
                    try {
                        StringBuilder sb = new StringBuilder();
                        ArrayList arrayList = new ArrayList();
                        while (cursorQuery.moveToNext()) {
                            q qVarB = b(cursorQuery);
                            if (qVarB != null) {
                                arrayList.add(qVarB);
                            } else {
                                try {
                                    String string = cursorQuery.getString(cursorQuery.getColumnIndex("_tp"));
                                    sb.append(" or _tp");
                                    sb.append(" = ");
                                    sb.append(string);
                                } catch (Throwable unused) {
                                    w.d("unknown id!", new Object[0]);
                                }
                            }
                        }
                        if (sb.length() > 0) {
                            sb.append(" and _id");
                            sb.append(" = ");
                            sb.append(i);
                            w.d("deleted %s illegle data %d", "t_pf", Integer.valueOf(writableDatabase.delete("t_pf", str.substring(4), null)));
                        }
                        if (cursorQuery != null && !cursorQuery.isClosed()) {
                            cursorQuery.close();
                        }
                        return arrayList;
                    } catch (Throwable th) {
                        th = th;
                        if (cursorQuery != null && !cursorQuery.isClosed()) {
                            cursorQuery.close();
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (!w.a(th)) {
                        th.printStackTrace();
                    }
                    if (cursorQuery != null && !cursorQuery.isClosed()) {
                        cursorQuery.close();
                    }
                    return null;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            cursorQuery = null;
        }
        return null;
    }

    public static boolean a(int i, String str, n nVar) {
        String str2;
        boolean z = false;
        try {
            try {
                SQLiteDatabase writableDatabase = b.getWritableDatabase();
                if (writableDatabase != null) {
                    if (str == null || str.trim().length() <= 0) {
                        str2 = "_id = " + i;
                    } else {
                        str2 = "_id = " + i + " and _tp = \"" + str + "\"";
                    }
                    int iDelete = writableDatabase.delete("t_pf", str2, null);
                    w.c("deleted %s data %d", "t_pf", Integer.valueOf(iDelete));
                    if (iDelete > 0) {
                        z = true;
                    }
                }
            } catch (Throwable th) {
                if (!w.a(th)) {
                    th.printStackTrace();
                }
                if (nVar != null) {
                }
            }
            return z;
        } finally {
            if (nVar != null) {
                Boolean.valueOf(false);
            }
        }
    }

    private static ContentValues d(q qVar) {
        if (qVar != null) {
            String str = qVar.f;
            if (!(str == null || str.trim().length() <= 0)) {
                try {
                    ContentValues contentValues = new ContentValues();
                    if (qVar.a > 0) {
                        contentValues.put("_id", Long.valueOf(qVar.a));
                    }
                    contentValues.put("_tp", qVar.f);
                    contentValues.put("_tm", Long.valueOf(qVar.e));
                    if (qVar.g != null) {
                        contentValues.put("_dt", qVar.g);
                    }
                    return contentValues;
                } catch (Throwable th) {
                    if (!w.a(th)) {
                        th.printStackTrace();
                    }
                    return null;
                }
            }
        }
        return null;
    }

    private static q b(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            q qVar = new q();
            qVar.a = cursor.getLong(cursor.getColumnIndex("_id"));
            qVar.e = cursor.getLong(cursor.getColumnIndex("_tm"));
            qVar.f = cursor.getString(cursor.getColumnIndex("_tp"));
            qVar.g = cursor.getBlob(cursor.getColumnIndex("_dt"));
            return qVar;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    /* compiled from: BUGLY */
    class a extends Thread {
        private int a;
        private n b;
        private String c;
        private ContentValues d;
        private boolean e;
        private String[] f;
        private String g;
        private String[] h;
        private String i;
        private String j;
        private String k;
        private String l;
        private String m;
        private String[] n;
        private int o;
        private String p;
        private byte[] q;

        public a(int i, n nVar) {
            this.a = i;
            this.b = nVar;
        }

        public final void a(boolean z, String str, String[] strArr, String str2, String[] strArr2, String str3, String str4, String str5, String str6) {
            this.e = z;
            this.c = str;
            this.f = strArr;
            this.g = str2;
            this.h = strArr2;
            this.i = str3;
            this.j = str4;
            this.k = str5;
            this.l = str6;
        }

        public final void a(int i, String str, byte[] bArr) {
            this.o = i;
            this.p = str;
            this.q = bArr;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public final void run() {
            switch (this.a) {
                case 1:
                    o.this.a(this.c, this.d, this.b);
                    break;
                case 2:
                    o.this.a(this.c, this.m, this.n, this.b);
                    break;
                case 3:
                    o.this.a(this.e, this.c, this.f, this.g, this.h, this.i, this.j, this.k, this.l, this.b);
                    break;
                case 4:
                    o.this.a(this.o, this.p, this.q, this.b);
                    break;
                case 5:
                    o.this.a(this.o, this.b);
                    break;
                case 6:
                    o oVar = o.this;
                    o.a(this.o, this.p, this.b);
                    break;
            }
        }
    }

    public final boolean a(l lVar) {
        ContentValues contentValuesB;
        if (lVar == null) {
            return false;
        }
        try {
            SQLiteDatabase writableDatabase = b.getWritableDatabase();
            if (writableDatabase == null || (contentValuesB = b(lVar)) == null) {
                return false;
            }
            long jReplace = writableDatabase.replace("t_crd", "_id", contentValuesB);
            if (jReplace < 0) {
                return false;
            }
            w.c("insert %s success!", "t_crd");
            lVar.a = jReplace;
            return true;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return false;
        }
    }

    public final List<l> a(int i, String str, long j, String str2) throws Throwable {
        Cursor cursor;
        String str3;
        SQLiteDatabase writableDatabase = b.getWritableDatabase();
        Cursor cursor2 = null;
        if (writableDatabase != null) {
            try {
                StringBuilder sb = new StringBuilder("_id = ");
                sb.append(i);
                sb.append(" and _pc");
                sb.append(" = '");
                sb.append(str);
                sb.append("' and _fl");
                sb.append(" > 0 and ");
                sb.append("_sv = '");
                sb.append(str2);
                sb.append("' ");
                if (j != 0) {
                    str3 = " and _tm > " + j;
                } else {
                    str3 = "";
                }
                sb.append(str3);
                Cursor cursorQuery = writableDatabase.query("t_crd", null, sb.toString(), null, null, null, "_tm ASC");
                if (cursorQuery == null) {
                    if (cursorQuery != null && !cursorQuery.isClosed()) {
                        cursorQuery.close();
                    }
                    return null;
                }
                try {
                    ArrayList arrayList = new ArrayList();
                    while (cursorQuery.moveToNext()) {
                        l lVarC = c(cursorQuery);
                        if (lVarC != null) {
                            arrayList.add(lVarC);
                        }
                    }
                    if (cursorQuery != null && !cursorQuery.isClosed()) {
                        cursorQuery.close();
                    }
                    return arrayList;
                } catch (Throwable th) {
                    cursor2 = cursorQuery;
                    th = th;
                    if (cursor2 != null) {
                        cursor2.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                cursor = null;
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0057  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final com.tencent.bugly.proguard.l a(int r10, java.lang.String r11) throws java.lang.Throwable {
        /*
            r9 = this;
            com.tencent.bugly.proguard.p r0 = com.tencent.bugly.proguard.o.b
            android.database.sqlite.SQLiteDatabase r1 = r0.getWritableDatabase()
            r0 = 0
            if (r1 == 0) goto L8b
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            java.lang.String r3 = "_id = "
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            r2.append(r10)     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            java.lang.String r10 = " and _pc"
            r2.append(r10)     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            java.lang.String r10 = " = '"
            r2.append(r10)     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            r2.append(r11)     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            java.lang.String r10 = "' and _fl"
            r2.append(r10)     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            java.lang.String r10 = " > 0"
            r2.append(r10)     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            java.lang.String r4 = r2.toString()     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            java.lang.String r2 = "t_crd"
            r3 = 0
            r5 = 0
            r6 = 0
            r7 = 0
            java.lang.String r8 = "_tm DESC"
            android.database.Cursor r10 = r1.query(r2, r3, r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L64 java.lang.Throwable -> L66
            if (r10 != 0) goto L48
            if (r10 == 0) goto L47
            boolean r11 = r10.isClosed()
            if (r11 != 0) goto L47
            r10.close()
        L47:
            return r0
        L48:
            if (r10 == 0) goto L57
            boolean r11 = r10.moveToFirst()     // Catch: java.lang.Throwable -> L55 java.lang.Throwable -> L7d
            if (r11 == 0) goto L57
            com.tencent.bugly.proguard.l r11 = c(r10)     // Catch: java.lang.Throwable -> L55 java.lang.Throwable -> L7d
            goto L58
        L55:
            r11 = move-exception
            goto L68
        L57:
            r11 = r0
        L58:
            if (r10 == 0) goto L63
            boolean r0 = r10.isClosed()
            if (r0 != 0) goto L63
            r10.close()
        L63:
            return r11
        L64:
            r11 = move-exception
            goto L7f
        L66:
            r11 = move-exception
            r10 = r0
        L68:
            boolean r1 = com.tencent.bugly.proguard.w.a(r11)     // Catch: java.lang.Throwable -> L7d
            if (r1 != 0) goto L71
            r11.printStackTrace()     // Catch: java.lang.Throwable -> L7d
        L71:
            if (r10 == 0) goto L8b
            boolean r11 = r10.isClosed()
            if (r11 != 0) goto L8b
            r10.close()
            goto L8b
        L7d:
            r11 = move-exception
            r0 = r10
        L7f:
            if (r0 == 0) goto L8a
            boolean r10 = r0.isClosed()
            if (r10 != 0) goto L8a
            r0.close()
        L8a:
            throw r11
        L8b:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.bugly.proguard.o.a(int, java.lang.String):com.tencent.bugly.proguard.l");
    }

    public final int a(int i, String str, long j) {
        String str2;
        StringBuilder sb = new StringBuilder("_id = ");
        sb.append(i);
        sb.append(" and _pc");
        sb.append(" = '");
        sb.append(str);
        sb.append("' ");
        if (j == 0) {
            str2 = "";
        } else {
            str2 = " and _tm < " + j;
        }
        sb.append(str2);
        return a("t_crd", sb.toString(), (String[]) null, (n) null);
    }

    public final int b() {
        StringBuilder sb = new StringBuilder("_fl = 0 or _sv <> '");
        com.tencent.bugly.crashreport.common.info.a.a().getClass();
        sb.append("2.2.0'");
        return a("t_crd", sb.toString(), (String[]) null, (n) null);
    }

    private static ContentValues b(l lVar) {
        if (lVar == null) {
            return null;
        }
        try {
            ContentValues contentValues = new ContentValues();
            if (lVar.a > 0 && !TextUtils.isEmpty(lVar.b)) {
                contentValues.put("_id", Long.valueOf(lVar.a));
                contentValues.put("_pc", lVar.b);
                contentValues.put("_tm", Long.valueOf(lVar.c));
                contentValues.put("_fl", Integer.valueOf(lVar.d));
                contentValues.put("_av", lVar.f);
                contentValues.put("_sv", lVar.e);
                contentValues.put("_uid", Long.valueOf(lVar.g));
                return contentValues;
            }
            return null;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }

    private static l c(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        try {
            l lVar = new l();
            lVar.a = cursor.getLong(cursor.getColumnIndex("_id"));
            lVar.b = cursor.getString(cursor.getColumnIndex("_pc"));
            lVar.e = cursor.getString(cursor.getColumnIndex("_sv"));
            lVar.f = cursor.getString(cursor.getColumnIndex("_av"));
            lVar.c = cursor.getLong(cursor.getColumnIndex("_tm"));
            lVar.d = cursor.getInt(cursor.getColumnIndex("_fl"));
            lVar.g = cursor.getLong(cursor.getColumnIndex("_uid"));
            return lVar;
        } catch (Throwable th) {
            if (!w.a(th)) {
                th.printStackTrace();
            }
            return null;
        }
    }
}
