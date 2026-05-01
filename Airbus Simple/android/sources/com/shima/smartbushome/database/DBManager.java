package com.shima.smartbushome.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import com.shima.smartbushome.MainActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class DBManager {
    private static String TAG = "DBManager";
    private SQLiteDatabase db;
    private DBHelper helper;

    public DBManager(Context context) throws IOException, SQLException {
        DatabaseContext databaseContext = new DatabaseContext(context);
        databaseContext.getDatabasePath("");
        this.helper = new DBHelper(databaseContext);
        this.db = this.helper.getWritableDatabase();
        this.helper.onCreate(this.db);
        if (!checkColumnExists(this.db, "song", "music_id")) {
            this.db.execSQL("alter table song add column music_id integer default 1");
        }
        if (!checkColumnExists(this.db, "music", "music_remark")) {
            this.db.execSQL("alter table music add column music_remark text default ' '");
        }
        if (!checkColumnExists(this.db, "room", "area_id")) {
            this.db.execSQL("alter table room add column area_id integer default 0");
        }
        this.db.execSQL("CREATE TABLE IF NOT EXISTS area(_id INTEGER PRIMARY KEY AUTOINCREMENT,area_name TEXT,area_icon TEXT,area_bg TEXT,area_remark TEXT)");
    }

    private boolean checkColumnExists(SQLiteDatabase sQLiteDatabase, String str, String str2) throws Throwable {
        boolean z = false;
        Cursor cursor = null;
        try {
            try {
                Cursor cursorRawQuery = sQLiteDatabase.rawQuery("select * from sqlite_master where name = ? and sql like ?", new String[]{str, "%" + str2 + "%"});
                if (cursorRawQuery != null) {
                    try {
                        if (cursorRawQuery.moveToFirst()) {
                            z = true;
                        }
                    } catch (Exception e) {
                        e = e;
                        cursor = cursorRawQuery;
                        e.printStackTrace();
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                        return z;
                    } catch (Throwable th) {
                        th = th;
                        cursor = cursorRawQuery;
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
                if (cursorRawQuery != null && !cursorRawQuery.isClosed()) {
                    cursorRawQuery.close();
                }
            } catch (Exception e2) {
                e = e2;
            }
            return z;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public void addroom(List<Saveroom> list) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            for (Saveroom saveroom : list) {
                this.db.execSQL("INSERT INTO room VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)", new Object[]{Integer.valueOf(saveroom.room_id), saveroom.room_name, Integer.valueOf(saveroom.light), Integer.valueOf(saveroom.hvac), Integer.valueOf(saveroom.mood), Integer.valueOf(saveroom.fan), Integer.valueOf(saveroom.curtain), Integer.valueOf(saveroom.music), saveroom.room_icon, saveroom.room_icon_bg, Integer.valueOf(saveroom.other), Integer.valueOf(saveroom.media), Integer.valueOf(saveroom.nio), Integer.valueOf(saveroom.fh), Integer.valueOf(saveroom.area_id)});
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addlight(List<Savelight> list) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            for (Savelight savelight : list) {
                this.db.execSQL("INSERT INTO light VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?,?)", new Object[]{Integer.valueOf(savelight.room_id), Integer.valueOf(savelight.subnetID), Integer.valueOf(savelight.deviceID), Integer.valueOf(savelight.light_id), savelight.light_statement, Integer.valueOf(savelight.channel), Integer.valueOf(savelight.brightvalue), Integer.valueOf(savelight.lightType), savelight.light_icon});
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addhvac(List<Savehvac> list) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            for (Savehvac savehvac : list) {
                this.db.execSQL("INSERT INTO hvac VALUES(null, ?, ?, ?, ?, ?)", new Object[]{Integer.valueOf(savehvac.room_id), Integer.valueOf(savehvac.hvac_id), Integer.valueOf(savehvac.subnetID), Integer.valueOf(savehvac.deviceID), savehvac.hvac_remark});
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addfloorheat(List<Savefloorheat> list) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            for (Savefloorheat savefloorheat : list) {
                this.db.execSQL("INSERT INTO floorheat VALUES(null, ?, ?, ?,?, ?, ?)", new Object[]{Integer.valueOf(savefloorheat.room_id), Integer.valueOf(savefloorheat.floorheat_id), Integer.valueOf(savefloorheat.subnetID), Integer.valueOf(savefloorheat.deviceID), Integer.valueOf(savefloorheat.channel), savefloorheat.floorheat_remark});
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addmood(Savemood savemood) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO mood VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)", new Object[]{Integer.valueOf(savemood.room_id), Integer.valueOf(savemood.mood_id), Integer.valueOf(savemood.subnetID), Integer.valueOf(savemood.deviceID), Integer.valueOf(savemood.control_type), Integer.valueOf(savemood.value_1), Integer.valueOf(savemood.value_2), Integer.valueOf(savemood.value_3), Integer.valueOf(savemood.value_4), Integer.valueOf(savemood.value_5), Integer.valueOf(savemood.value_6)});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addmoodbutton(Savemoodbutton savemoodbutton) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO moodbutton VALUES(null, ?, ?, ?, ?)", new Object[]{Integer.valueOf(savemoodbutton.room_id), Integer.valueOf(savemoodbutton.mood_id), savemoodbutton.mood_name, savemoodbutton.mood_icon});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addmusic(List<Savemusic> list) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            for (Savemusic savemusic : list) {
                this.db.execSQL("INSERT INTO music VALUES(null, ?, ?, ?, ?,?)", new Object[]{Integer.valueOf(savemusic.room_id), Integer.valueOf(savemusic.music_id), Integer.valueOf(savemusic.subnetID), Integer.valueOf(savemusic.deviceID), savemusic.music_remark});
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addsong(Savesong savesong) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO song VALUES(null, ?, ?, ?, ?, ?, ?,?)", new Object[]{Integer.valueOf(savesong.room_id), Integer.valueOf(savesong.album_num), savesong.album_name, Integer.valueOf(savesong.song_num), savesong.song_name, Integer.valueOf(savesong.like), Integer.valueOf(savesong.music_id)});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addradio(Saveradio saveradio) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO radio VALUES(null, ?, ?, ?, ?)", new Object[]{Integer.valueOf(saveradio.room_id), Integer.valueOf(saveradio.channel_num), saveradio.channel_name, saveradio.channel_value});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addArea(SaveArea saveArea) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO area VALUES(null, ?, ?, ?, ?)", new Object[]{saveArea.getArea_name(), saveArea.getArea_icon(), saveArea.getArea_bg(), saveArea.getArea_remark()});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addcurtain(List<Savecurtain> list) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            for (Savecurtain savecurtain : list) {
                this.db.execSQL("INSERT INTO curtain VALUES(null, ?, ?, ?, ?,?, ?, ?, ?,?)", new Object[]{Integer.valueOf(savecurtain.room_id), Integer.valueOf(savecurtain.curtain_id), Integer.valueOf(savecurtain.subnetID), Integer.valueOf(savecurtain.deviceID), savecurtain.curtain_remark, Integer.valueOf(savecurtain.curtain_type), Integer.valueOf(savecurtain.channel_1), Integer.valueOf(savecurtain.channel_2), savecurtain.current_state});
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addother(List<Saveother> list) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            for (Saveother saveother : list) {
                this.db.execSQL("INSERT INTO other VALUES(null, ?, ?, ?, ?, ?, ?, ?,?,?)", new Object[]{Integer.valueOf(saveother.room_id), Integer.valueOf(saveother.subnetID), Integer.valueOf(saveother.deviceID), Integer.valueOf(saveother.other_id), saveother.other_statement, Integer.valueOf(saveother.channel_1), Integer.valueOf(saveother.channel_2), saveother.other_icon, Integer.valueOf(saveother.other_type)});
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addmarco(Savemarco savemarco) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO marco VALUES(null, ?,?,?,?,?,?,?,?,?,?,?)", new Object[]{Integer.valueOf(savemarco.marco_id), Integer.valueOf(savemarco.room_id), savemarco.room, savemarco.device, Integer.valueOf(savemarco.subnetID), Integer.valueOf(savemarco.deviceID), Integer.valueOf(savemarco.control_type), Integer.valueOf(savemarco.value1), Integer.valueOf(savemarco.value2), Integer.valueOf(savemarco.value3), Integer.valueOf(savemarco.sentorder)});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addmarcobutton(Savemarcobutton savemarcobutton) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO marcobutton VALUES(null, ?,?,?)", new Object[]{Integer.valueOf(savemarcobutton.marco_id), savemarcobutton.marco_remark, savemarcobutton.marco_icon});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addfan(Savefan savefan) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO fan VALUES(null, ?,?,?,?,?,?,?,?)", new Object[]{Integer.valueOf(savefan.room_id), Integer.valueOf(savefan.subnetID), Integer.valueOf(savefan.deviceID), Integer.valueOf(savefan.fan_id), savefan.fan_statement, Integer.valueOf(savefan.channel), Integer.valueOf(savefan.fan_Type), savefan.fan_icon});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addmedia(Savemedia savemedia) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO media VALUES(null, ?,?,?,?,?,?)", new Object[]{Integer.valueOf(savemedia.room_id), Integer.valueOf(savemedia.subnetID), Integer.valueOf(savemedia.deviceID), Integer.valueOf(savemedia.media_id), savemedia.media_statement, savemedia.media_icon});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addmediabutton(List<Savemediabutton> list) {
        this.db.beginTransaction();
        try {
            for (Savemediabutton savemediabutton : list) {
                this.db.execSQL("INSERT INTO mediabutton VALUES(null, ?,?,?,?,?,?,?)", new Object[]{Integer.valueOf(savemediabutton.room_id), Integer.valueOf(savemediabutton.media_id), Integer.valueOf(savemediabutton.button_num), Integer.valueOf(savemediabutton.media_swno), Integer.valueOf(savemediabutton.media_controltype), Integer.valueOf(savemediabutton.media_type), Integer.valueOf(savemediabutton.ifIRmarco)});
            }
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addsecurity(Savesecurity savesecurity) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO security VALUES(null, ?,?,?,?,?)", new Object[]{Integer.valueOf(savesecurity.room_id), Integer.valueOf(savesecurity.security_id), Integer.valueOf(savesecurity.subnetID), Integer.valueOf(savesecurity.deviceID), Integer.valueOf(savesecurity.password)});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addsecurityarea(SavesecurityArea savesecurityArea) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO securityarea VALUES(null, ?,?,?)", new Object[]{Integer.valueOf(savesecurityArea.security_id), Integer.valueOf(savesecurityArea.areaNO), savesecurityArea.areaName});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addenergy(Saveenergy saveenergy) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO energy VALUES(null, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{Integer.valueOf(saveenergy.energy_id), Integer.valueOf(saveenergy.subnetID), Integer.valueOf(saveenergy.deviceID), saveenergy.energyname, saveenergy.channel1_name, saveenergy.channel2_name, saveenergy.channel3_name, saveenergy.channel4_name, saveenergy.channel5_name, saveenergy.channel6_name, saveenergy.channel7_name, saveenergy.channel8_name, saveenergy.channel9_name, saveenergy.channel10_name, saveenergy.channel11_name, saveenergy.channel12_name, saveenergy.channel13_name, saveenergy.channel14_name, saveenergy.channel15_name, saveenergy.channel16_name, saveenergy.channel17_name, saveenergy.channel18_name, saveenergy.channel19_name, saveenergy.channel20_name, saveenergy.channel21_name, saveenergy.channel22_name, saveenergy.channel23_name, saveenergy.channel24_name});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addenergydata(Saveenergydata saveenergydata) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO energydata VALUES(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{Integer.valueOf(saveenergydata.energy_id), Integer.valueOf(saveenergydata.channel1_value), Integer.valueOf(saveenergydata.channel2_value), Integer.valueOf(saveenergydata.channel3_value), Integer.valueOf(saveenergydata.channel4_value), Integer.valueOf(saveenergydata.channel5_value), Integer.valueOf(saveenergydata.channel6_value), Integer.valueOf(saveenergydata.channel7_value), Integer.valueOf(saveenergydata.channel8_value), Integer.valueOf(saveenergydata.channel9_value), Integer.valueOf(saveenergydata.channel10_value), Integer.valueOf(saveenergydata.channel11_value), Integer.valueOf(saveenergydata.channel12_value), Integer.valueOf(saveenergydata.channel13_value), Integer.valueOf(saveenergydata.channel14_value), Integer.valueOf(saveenergydata.channel15_value), Integer.valueOf(saveenergydata.channel16_value), Integer.valueOf(saveenergydata.channel17_value), Integer.valueOf(saveenergydata.channel18_value), Integer.valueOf(saveenergydata.channel19_value), Integer.valueOf(saveenergydata.channel20_value), Integer.valueOf(saveenergydata.channel21_value), Integer.valueOf(saveenergydata.channel22_value), Integer.valueOf(saveenergydata.channel23_value), Integer.valueOf(saveenergydata.channel24_value), saveenergydata.Time});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addnio(Savenio savenio) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO nio VALUES(null,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[]{Integer.valueOf(savenio.room_id), Integer.valueOf(savenio.subnetID), Integer.valueOf(savenio.deviceID), Integer.valueOf(savenio.nio_id), savenio.nio_remark, savenio.name1, savenio.name2, savenio.name3, savenio.name4, savenio.name5, savenio.name6, savenio.name7, savenio.name8, savenio.name9, savenio.name10, savenio.name11, savenio.name12, savenio.name13, savenio.name14, savenio.name15, Integer.valueOf(savenio.value1), Integer.valueOf(savenio.value2), Integer.valueOf(savenio.value3), Integer.valueOf(savenio.value4), Integer.valueOf(savenio.value5), Integer.valueOf(savenio.value6), Integer.valueOf(savenio.value7), Integer.valueOf(savenio.value8), Integer.valueOf(savenio.value9), Integer.valueOf(savenio.value10), Integer.valueOf(savenio.value11), Integer.valueOf(savenio.value12), Integer.valueOf(savenio.value13), Integer.valueOf(savenio.value14), Integer.valueOf(savenio.value15), Integer.valueOf(savenio.value16), Integer.valueOf(savenio.value17), Integer.valueOf(savenio.value18), Integer.valueOf(savenio.value19), Integer.valueOf(savenio.value20), Integer.valueOf(savenio.value21), Integer.valueOf(savenio.value22), Integer.valueOf(savenio.value23), Integer.valueOf(savenio.value24), Integer.valueOf(savenio.value25), Integer.valueOf(savenio.value26), Integer.valueOf(savenio.value27)});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addstatus(Savestatus savestatus) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO status VALUES(null, ?,?,?,?,?,?,?,?)", new Object[]{Integer.valueOf(savestatus.status_id), savestatus.name, Integer.valueOf(savestatus.subnetID), Integer.valueOf(savestatus.deviceID), Integer.valueOf(savestatus.type), Integer.valueOf(savestatus.channel), savestatus.status_icon, Integer.valueOf(savestatus.unit)});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addschedule(Saveschedule saveschedule) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO schedule VALUES(null, ?,?,?,?, ?,?,?,?)", new Object[]{Integer.valueOf(saveschedule.schedule_id), saveschedule.schedule_name, saveschedule.schedule_icon, Integer.valueOf(saveschedule.marco_ID), saveschedule.alarm_time, Integer.valueOf(saveschedule.repeat_option1), Integer.valueOf(saveschedule.repeat_option2), Integer.valueOf(saveschedule.last_status)});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void addnfc(Savenfc savenfc) {
        this.db.beginTransaction();
        try {
            this.db.execSQL("INSERT INTO nfc VALUES(null, ?,?,?,?, ?,?,?,? ,?,?,?,? ,?)", new Object[]{Integer.valueOf(savenfc.nfc_id), savenfc.nfc_name, savenfc.nfc_icon, savenfc.nfc_content, Integer.valueOf(savenfc.state), Integer.valueOf(savenfc.action_type), Integer.valueOf(savenfc.marco_ID), savenfc.marco_name, savenfc.call_num, savenfc.message, Integer.valueOf(savenfc.resume1), savenfc.resume2, Integer.valueOf(savenfc.delaytime)});
            this.db.setTransactionSuccessful();
        } finally {
            this.db.endTransaction();
        }
    }

    public void updateArea(SaveArea saveArea) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("area_name", saveArea.getArea_name());
        contentValues.put("area_icon", saveArea.getArea_icon());
        contentValues.put("area_bg", saveArea.getArea_bg());
        contentValues.put("area_remark", saveArea.getArea_remark());
        this.db.update("area", contentValues, "_id = ?", new String[]{String.valueOf(saveArea.getId())});
    }

    public void updataroominfo(Saveroom saveroom) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("room_name", saveroom.room_name);
        contentValues.put("room_icon", saveroom.room_icon);
        contentValues.put("room_icon_bg", saveroom.room_icon_bg);
        contentValues.put("area_id", Integer.valueOf(saveroom.area_id));
        this.db.update("room", contentValues, "room_id = ?", new String[]{String.valueOf(saveroom.room_id)});
    }

    public void updateroom(Saveroom saveroom, String str) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        if (str.equals("light")) {
            contentValues.put(str, Integer.valueOf(saveroom.light));
        } else if (str.equals("hvac")) {
            contentValues.put(str, Integer.valueOf(saveroom.hvac));
        } else if (str.equals("mood")) {
            contentValues.put(str, Integer.valueOf(saveroom.mood));
        } else if (str.equals("fan")) {
            contentValues.put(str, Integer.valueOf(saveroom.fan));
        } else if (str.equals("curtain")) {
            contentValues.put(str, Integer.valueOf(saveroom.curtain));
        } else if (str.equals("music")) {
            contentValues.put(str, Integer.valueOf(saveroom.music));
        } else if (str.equals("other")) {
            contentValues.put(str, Integer.valueOf(saveroom.other));
        } else if (str.equals("media")) {
            contentValues.put(str, Integer.valueOf(saveroom.media));
        } else if (str.equals("9 in 1")) {
            contentValues.put("nio", Integer.valueOf(saveroom.nio));
        } else if (str.equals("floor heat")) {
            contentValues.put("fh", Integer.valueOf(saveroom.fh));
        }
        this.db.update("room", contentValues, "room_id = ?", new String[]{String.valueOf(saveroom.room_id)});
    }

    public void updatelight(Savelight savelight) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("subnetID", Integer.valueOf(savelight.subnetID));
        contentValues.put("deviceID", Integer.valueOf(savelight.deviceID));
        contentValues.put("channel", Integer.valueOf(savelight.channel));
        contentValues.put("light_statement", savelight.light_statement);
        contentValues.put("light_icon", savelight.light_icon);
        contentValues.put("lightType", Integer.valueOf(savelight.lightType));
        this.db.update("light", contentValues, "light_id = ? and room_id = ?", new String[]{String.valueOf(savelight.light_id), String.valueOf(savelight.room_id)});
    }

    public void updatehvac(Savehvac savehvac) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("subnetID", Integer.valueOf(savehvac.subnetID));
        contentValues.put("deviceID", Integer.valueOf(savehvac.deviceID));
        contentValues.put("hvac_statement", savehvac.hvac_remark);
        this.db.update("hvac", contentValues, "hvac_id = ? and room_id = ?", new String[]{String.valueOf(savehvac.hvac_id), String.valueOf(savehvac.room_id)});
    }

    public void updatefloorheat(Savefloorheat savefloorheat) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("subnetID", Integer.valueOf(savefloorheat.subnetID));
        contentValues.put("deviceID", Integer.valueOf(savefloorheat.deviceID));
        contentValues.put("channel", Integer.valueOf(savefloorheat.channel));
        contentValues.put("floorheat_statement", savefloorheat.floorheat_remark);
        this.db.update("floorheat", contentValues, "_id = ? and room_id = ?", new String[]{String.valueOf(savefloorheat._id), String.valueOf(savefloorheat.room_id)});
    }

    public void updatemoodbutton(Savemoodbutton savemoodbutton) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("mood_name", savemoodbutton.mood_name);
        contentValues.put("mood_icon", savemoodbutton.mood_icon);
        this.db.update("moodbutton", contentValues, "mood_id = ? and room_id = ?", new String[]{String.valueOf(savemoodbutton.mood_id), String.valueOf(savemoodbutton.room_id)});
    }

    public void updatemusic(Savemusic savemusic) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("music_id", Integer.valueOf(savemusic.music_id));
        contentValues.put("subnetID", Integer.valueOf(savemusic.subnetID));
        contentValues.put("deviceID", Integer.valueOf(savemusic.deviceID));
        contentValues.put("music_remark", savemusic.music_remark);
        this.db.update("music", contentValues, "music_id = ? and room_id = ?", new String[]{String.valueOf(savemusic.music_id), String.valueOf(savemusic.room_id)});
    }

    public void updatesong(Savesong savesong) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("like", Integer.valueOf(savesong.like));
        this.db.update("song", contentValues, "album_num = ? and room_id = ? and song_num= ?", new String[]{String.valueOf(savesong.album_num), String.valueOf(savesong.room_id), String.valueOf(savesong.song_num)});
    }

    public void updateradio(Saveradio saveradio) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("channel_value", saveradio.channel_value);
        contentValues.put("channel_name", saveradio.channel_name);
        this.db.update("radio", contentValues, "channel_num = ? and room_id = ?", new String[]{String.valueOf(saveradio.channel_num), String.valueOf(saveradio.room_id)});
    }

    public void updatecurtain(Savecurtain savecurtain) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("subnetID", Integer.valueOf(savecurtain.subnetID));
        contentValues.put("deviceID", Integer.valueOf(savecurtain.deviceID));
        contentValues.put("channel_1", Integer.valueOf(savecurtain.channel_1));
        contentValues.put("channel_2", Integer.valueOf(savecurtain.channel_2));
        contentValues.put("curtain_remark", savecurtain.curtain_remark);
        this.db.update("curtain", contentValues, "curtain_id = ? and room_id = ?", new String[]{String.valueOf(savecurtain.curtain_id), String.valueOf(savecurtain.room_id)});
    }

    public void updatecurtainstate(Savecurtain savecurtain) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("current_state", savecurtain.current_state);
        this.db.update("curtain", contentValues, "curtain_id = ? and room_id = ?", new String[]{String.valueOf(savecurtain.curtain_id), String.valueOf(savecurtain.room_id)});
    }

    public void updateother(Saveother saveother) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("subnetID", Integer.valueOf(saveother.subnetID));
        contentValues.put("deviceID", Integer.valueOf(saveother.deviceID));
        contentValues.put("channel_1", Integer.valueOf(saveother.channel_1));
        contentValues.put("channel_2", Integer.valueOf(saveother.channel_2));
        contentValues.put("other_statement", saveother.other_statement);
        contentValues.put("other_icon", saveother.other_icon);
        this.db.update("other", contentValues, "other_id = ? and room_id = ?", new String[]{String.valueOf(saveother.other_id), String.valueOf(saveother.room_id)});
    }

    public void updatemarco(Savemarco savemarco, int i) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("sentorder", Integer.valueOf(i));
        this.db.update("marco", contentValues, "marco_id = ? and sentorder = ?", new String[]{String.valueOf(savemarco.marco_id), String.valueOf(savemarco.sentorder)});
    }

    public void updatemarcobutton(Savemarcobutton savemarcobutton) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("marco_remark", savemarcobutton.marco_remark);
        contentValues.put("marco_icon", savemarcobutton.marco_icon);
        this.db.update("marcobutton", contentValues, "marco_id = ? ", new String[]{String.valueOf(savemarcobutton.marco_id)});
    }

    public void updatefan(Savefan savefan) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("subnetID", Integer.valueOf(savefan.subnetID));
        contentValues.put("deviceID", Integer.valueOf(savefan.deviceID));
        contentValues.put("channel", Integer.valueOf(savefan.channel));
        contentValues.put("fan_statement", savefan.fan_statement);
        this.db.update("fan", contentValues, "fan_id = ? and room_id = ?", new String[]{String.valueOf(savefan.fan_id), String.valueOf(savefan.room_id)});
    }

    public void updatemedia(Savemedia savemedia) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("subnetID", Integer.valueOf(savemedia.subnetID));
        contentValues.put("deviceID", Integer.valueOf(savemedia.deviceID));
        contentValues.put("media_statement", savemedia.media_statement);
        contentValues.put("media_icon", savemedia.media_icon);
        this.db.update("media", contentValues, "media_id = ? and room_id = ? ", new String[]{String.valueOf(savemedia.media_id), String.valueOf(savemedia.room_id)});
    }

    public void updatemediabutton(Savemediabutton savemediabutton) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("media_swno", Integer.valueOf(savemediabutton.media_swno));
        contentValues.put("media_controltype", Integer.valueOf(savemediabutton.media_controltype));
        contentValues.put("ifIRmarco", Integer.valueOf(savemediabutton.ifIRmarco));
        this.db.update("mediabutton", contentValues, "media_id = ? and room_id = ? and button_num=?", new String[]{String.valueOf(savemediabutton.media_id), String.valueOf(savemediabutton.room_id), String.valueOf(savemediabutton.button_num)});
    }

    public void updatesecurity(Savesecurity savesecurity) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("subnetID", Integer.valueOf(savesecurity.subnetID));
        contentValues.put("deviceID", Integer.valueOf(savesecurity.deviceID));
        contentValues.put("password", Integer.valueOf(savesecurity.password));
        this.db.update("security", contentValues, " room_id = ? and security_id=?", new String[]{String.valueOf(0), String.valueOf(0)});
    }

    public void updatesecurityarea(SavesecurityArea savesecurityArea) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("areaName", savesecurityArea.areaName);
        this.db.update("securityarea", contentValues, " security_id=? and areaNO=?", new String[]{String.valueOf(0), String.valueOf(savesecurityArea.areaNO)});
    }

    public void updateenergy(Saveenergy saveenergy) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("subnetID", Integer.valueOf(saveenergy.subnetID));
        contentValues.put("deviceID", Integer.valueOf(saveenergy.deviceID));
        contentValues.put("energyname", saveenergy.energyname);
        this.db.update("energy", contentValues, " energy_id=? ", new String[]{String.valueOf(saveenergy.energy_id)});
    }

    public void updateenergychannelname(int i, String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(str, str2);
        this.db.update("energy", contentValues, " energy_id=? ", new String[]{String.valueOf(i)});
    }

    public void updateniosetting(Savenio savenio) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("subnetID", Integer.valueOf(savenio.subnetID));
        contentValues.put("deviceID", Integer.valueOf(savenio.deviceID));
        contentValues.put("nio_remark", savenio.nio_remark);
        this.db.update("nio", contentValues, "room_id = ? and nio_id=? ", new String[]{String.valueOf(savenio.room_id), String.valueOf(savenio.nio_id)});
    }

    public void updateniovalue(int i, int i2, String str, int i3, int i4) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        switch (i4) {
            case 1:
                contentValues.put("name1", str);
                contentValues.put("value1", Integer.valueOf(i3));
                break;
            case 2:
                contentValues.put("name2", str);
                contentValues.put("value2", Integer.valueOf(i3));
                break;
            case 3:
                contentValues.put("name3", str);
                contentValues.put("value3", Integer.valueOf(i3));
                break;
            case 4:
                contentValues.put("name4", str);
                contentValues.put("value4", Integer.valueOf(i3));
                break;
            case 5:
                contentValues.put("name5", str);
                contentValues.put("value5", Integer.valueOf(i3));
                break;
            case 6:
                contentValues.put("name6", str);
                contentValues.put("value6", Integer.valueOf(i3));
                break;
            case 7:
                contentValues.put("name7", str);
                contentValues.put("value7", Integer.valueOf(i3));
                break;
            case 8:
                contentValues.put("name8", str);
                contentValues.put("value8", Integer.valueOf(i3));
                break;
            case 9:
                contentValues.put("name9", str);
                contentValues.put("value9", Integer.valueOf(i3));
                break;
            case 10:
                contentValues.put("name10", str);
                contentValues.put("value10", Integer.valueOf(i3));
                break;
            case 11:
                contentValues.put("name11", str);
                contentValues.put("value11", Integer.valueOf(i3));
                break;
            case 12:
                contentValues.put("name12", str);
                contentValues.put("value12", Integer.valueOf(i3));
                break;
            case 13:
                contentValues.put("name13", str);
                contentValues.put("value13", Integer.valueOf(i3));
                break;
            case 14:
                contentValues.put("name14", str);
                contentValues.put("value14", Integer.valueOf(i3));
                break;
            case 15:
                contentValues.put("name15", str);
                contentValues.put("value15", Integer.valueOf(i3));
                break;
            case 16:
                contentValues.put("value16", Integer.valueOf(i3));
                break;
            case 17:
                contentValues.put("value17", Integer.valueOf(i3));
                break;
            case 18:
                contentValues.put("value18", Integer.valueOf(i3));
                break;
            case 19:
                contentValues.put("value19", Integer.valueOf(i3));
                break;
            case 20:
                contentValues.put("value20", Integer.valueOf(i3));
                break;
            case 21:
                contentValues.put("value21", Integer.valueOf(i3));
                break;
            case 22:
                contentValues.put("value22", Integer.valueOf(i3));
                break;
            case 23:
                contentValues.put("value23", Integer.valueOf(i3));
                break;
            case 24:
                contentValues.put("value24", Integer.valueOf(i3));
                break;
            case 25:
                contentValues.put("value25", Integer.valueOf(i3));
                break;
            case 26:
                contentValues.put("value26", Integer.valueOf(i3));
                break;
            case 27:
                contentValues.put("value27", Integer.valueOf(i3));
                break;
        }
        this.db.update("nio", contentValues, " room_id = ? and nio_id=?  ", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void updatestatus(Savestatus savestatus) {
        if (MainActivity.islockchangeid) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseUtil.KEY_NAME, savestatus.name);
        contentValues.put("subnetID", Integer.valueOf(savestatus.subnetID));
        contentValues.put("deviceID", Integer.valueOf(savestatus.deviceID));
        contentValues.put("status_icon", savestatus.status_icon);
        contentValues.put("channel", Integer.valueOf(savestatus.channel));
        contentValues.put("unit", Integer.valueOf(savestatus.unit));
        this.db.update("status", contentValues, " status_id=? ", new String[]{String.valueOf(savestatus.status_id)});
    }

    public void updateschedule(Saveschedule saveschedule) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("schedule_name", saveschedule.schedule_name);
        contentValues.put("schedule_icon", saveschedule.schedule_icon);
        contentValues.put("marco_ID", Integer.valueOf(saveschedule.marco_ID));
        contentValues.put("alarm_time", saveschedule.alarm_time);
        contentValues.put("last_status", Integer.valueOf(saveschedule.last_status));
        contentValues.put("repeat_option1", Integer.valueOf(saveschedule.repeat_option1));
        contentValues.put("repeat_option2", Integer.valueOf(saveschedule.repeat_option2));
        this.db.update("schedule", contentValues, " schedule_id=? ", new String[]{String.valueOf(saveschedule.schedule_id)});
    }

    public void updateschedule_status(Saveschedule saveschedule) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("last_status", Integer.valueOf(saveschedule.last_status));
        this.db.update("schedule", contentValues, " schedule_id=? ", new String[]{String.valueOf(saveschedule.schedule_id)});
    }

    public void updatenfc(Savenfc savenfc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nfc_name", savenfc.nfc_name);
        contentValues.put("action_type", Integer.valueOf(savenfc.action_type));
        contentValues.put("marco_ID", Integer.valueOf(savenfc.marco_ID));
        contentValues.put("marco_name", savenfc.marco_name);
        contentValues.put("call_num", savenfc.call_num);
        contentValues.put("message", savenfc.message);
        contentValues.put("delaytime", Integer.valueOf(savenfc.delaytime));
        this.db.update("nfc", contentValues, " nfc_id=? ", new String[]{String.valueOf(savenfc.nfc_id)});
    }

    public void updatenfc_status(Savenfc savenfc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("state", Integer.valueOf(savenfc.state));
        this.db.update("nfc", contentValues, " nfc_id=? ", new String[]{String.valueOf(savenfc.nfc_id)});
    }

    public void deleteArea(int i) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete("area", "_id = ?", new String[]{String.valueOf(i)});
    }

    public void deletefounction(String str, int i) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "room_id = ?", new String[]{String.valueOf(i)});
    }

    public void deletelight(String str, int i, int i2) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "light_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletefc(String str, int i) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "room_id = ?", new String[]{String.valueOf(i)});
    }

    public void deletehvac(String str, int i, int i2) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "hvac_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletefloorheat(String str, int i, int i2) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletemood(String str, int i, int i2) {
        this.db.delete(str, "mood_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletemoodbutton(String str, int i, int i2) {
        this.db.delete(str, "mood_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletemusic(String str, int i, int i2) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "music_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletesong(String str, int i) {
        this.db.delete(str, "room_id = ?", new String[]{String.valueOf(i)});
    }

    public void deletesong(String str, int i, int i2) {
        this.db.delete(str, "room_id = ? and music_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deleteradio(String str, int i) {
        this.db.delete(str, "room_id = ?", new String[]{String.valueOf(i)});
    }

    public void deletecurtain(String str, int i, int i2) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "curtain_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deleteother(String str, int i, int i2) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "other_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletemarco(String str, int i, int i2) {
        this.db.delete(str, "marco_id = ? and sentorder = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletemarcobutton(String str, int i) {
        this.db.delete(str, "marco_id = ? ", new String[]{String.valueOf(i)});
    }

    public void deletefan(String str, int i, int i2) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "fan_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletemedia(String str, int i, int i2) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "media_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletemediabutton(String str, int i, int i2) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "media_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletesecurityarea(String str) {
        this.db.delete(str, "security_id = ? ", new String[]{String.valueOf(0)});
    }

    public void deleteenergy(String str, Saveenergy saveenergy) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "energy_id = ? ", new String[]{String.valueOf(saveenergy.energy_id)});
    }

    public void deleteenergydata(String str, Saveenergydata saveenergydata) {
        this.db.delete(str, "energy_id = ? ", new String[]{String.valueOf(saveenergydata.energy_id)});
    }

    public void deletenio(String str, int i, int i2) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "nio_id = ? and room_id = ?", new String[]{String.valueOf(i), String.valueOf(i2)});
    }

    public void deletestatus(String str, int i) {
        if (MainActivity.islockchangeid) {
            return;
        }
        this.db.delete(str, "status_id = ? ", new String[]{String.valueOf(i)});
    }

    public void deleteschedule(int i) {
        this.db.delete("schedule", "schedule_id = ? ", new String[]{String.valueOf(i)});
    }

    public void deletenfc(int i) {
        this.db.delete("nfc", "nfc_id = ? ", new String[]{String.valueOf(i)});
    }

    public List<SaveTips> query() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("tip");
        while (cursorQueryTheCursor.moveToNext()) {
            SaveTips saveTips = new SaveTips();
            saveTips._id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("_id"));
            saveTips.title = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("title"));
            saveTips.date = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("date"));
            saveTips.detail = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("detail"));
            saveTips.url = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("url"));
            arrayList.add(saveTips);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<SaveArea> queryArea() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("area");
        while (cursorQueryTheCursor.moveToNext()) {
            SaveArea saveArea = new SaveArea();
            saveArea.setId(cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("_id")));
            saveArea.setArea_name(cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("area_name")));
            saveArea.setArea_icon(cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("area_icon")));
            saveArea.setArea_bg(cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("area_bg")));
            saveArea.setArea_remark(cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("area_remark")));
            arrayList.add(saveArea);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savelight> querylight() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("light");
        while (cursorQueryTheCursor.moveToNext()) {
            Savelight savelight = new Savelight();
            savelight.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savelight.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savelight.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savelight.light_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("light_id"));
            savelight.channel = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel"));
            savelight.brightvalue = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("brightvalue"));
            savelight.lightType = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("lightType"));
            savelight.light_statement = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("light_statement"));
            savelight.light_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("light_icon"));
            arrayList.add(savelight);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savehvac> queryhvac() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("hvac");
        while (cursorQueryTheCursor.moveToNext()) {
            Savehvac savehvac = new Savehvac();
            savehvac._id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("_id"));
            savehvac.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savehvac.hvac_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("hvac_id"));
            savehvac.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savehvac.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savehvac.hvac_remark = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("hvac_statement"));
            arrayList.add(savehvac);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savefloorheat> queryfloorheat() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("floorheat");
        while (cursorQueryTheCursor.moveToNext()) {
            Savefloorheat savefloorheat = new Savefloorheat();
            savefloorheat._id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("_id"));
            savefloorheat.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savefloorheat.floorheat_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("floorheat_id"));
            savefloorheat.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savefloorheat.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savefloorheat.channel = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel"));
            savefloorheat.floorheat_remark = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("floorheat_statement"));
            arrayList.add(savefloorheat);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savemood> querymood() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("mood");
        while (cursorQueryTheCursor.moveToNext()) {
            Savemood savemood = new Savemood();
            savemood.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savemood.mood_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("mood_id"));
            savemood.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savemood.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savemood.control_type = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("control_type"));
            savemood.value_1 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value_1"));
            savemood.value_2 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value_2"));
            savemood.value_3 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value_3"));
            savemood.value_4 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value_4"));
            savemood.value_5 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value_5"));
            savemood.value_6 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value_6"));
            arrayList.add(savemood);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savemoodbutton> querymoodbutton() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("moodbutton");
        while (cursorQueryTheCursor.moveToNext()) {
            Savemoodbutton savemoodbutton = new Savemoodbutton();
            savemoodbutton.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savemoodbutton.mood_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("mood_id"));
            savemoodbutton.mood_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("mood_name"));
            savemoodbutton.mood_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("mood_icon"));
            arrayList.add(savemoodbutton);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savemusic> querymusic() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("music");
        while (cursorQueryTheCursor.moveToNext()) {
            Savemusic savemusic = new Savemusic();
            savemusic.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savemusic.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savemusic.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savemusic.music_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("music_id"));
            savemusic.music_remark = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("music_remark"));
            arrayList.add(savemusic);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savesong> querysong() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("song");
        while (cursorQueryTheCursor.moveToNext()) {
            Savesong savesong = new Savesong();
            savesong.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savesong.album_num = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("album_num"));
            savesong.album_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("album_name"));
            savesong.song_num = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("song_num"));
            savesong.song_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("song_name"));
            savesong.like = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("like"));
            savesong.music_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("music_id"));
            arrayList.add(savesong);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Saveradio> queryradio() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("radio");
        while (cursorQueryTheCursor.moveToNext()) {
            Saveradio saveradio = new Saveradio();
            saveradio.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            saveradio.channel_num = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel_num"));
            saveradio.channel_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel_name"));
            saveradio.channel_value = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel_value"));
            arrayList.add(saveradio);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savecurtain> querycurtain() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("curtain");
        while (cursorQueryTheCursor.moveToNext()) {
            Savecurtain savecurtain = new Savecurtain();
            savecurtain.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savecurtain.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savecurtain.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savecurtain.curtain_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("curtain_id"));
            savecurtain.curtain_remark = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("curtain_remark"));
            savecurtain.curtain_type = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("curtain_type"));
            savecurtain.channel_1 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel_1"));
            savecurtain.channel_2 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel_2"));
            savecurtain.current_state = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("current_state"));
            arrayList.add(savecurtain);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Saveroom> queryroom() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("room");
        while (cursorQueryTheCursor.moveToNext()) {
            Saveroom saveroom = new Saveroom();
            saveroom.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            saveroom.room_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("room_name"));
            saveroom.light = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("light"));
            saveroom.hvac = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("hvac"));
            saveroom.mood = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("mood"));
            saveroom.curtain = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("curtain"));
            saveroom.music = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("music"));
            saveroom.other = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("other"));
            saveroom.media = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("media"));
            saveroom.room_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("room_icon"));
            saveroom.room_icon_bg = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("room_icon_bg"));
            saveroom.area_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("area_id"));
            arrayList.add(saveroom);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Saveroom> queryRoomByAreaId(int i) {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("room");
        while (cursorQueryTheCursor.moveToNext()) {
            Saveroom saveroom = new Saveroom();
            saveroom.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            saveroom.room_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("room_name"));
            saveroom.light = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("light"));
            saveroom.hvac = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("hvac"));
            saveroom.mood = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("mood"));
            saveroom.curtain = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("curtain"));
            saveroom.music = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("music"));
            saveroom.other = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("other"));
            saveroom.media = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("media"));
            saveroom.room_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("room_icon"));
            saveroom.room_icon_bg = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("room_icon_bg"));
            saveroom.area_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("area_id"));
            if (i == saveroom.area_id) {
                arrayList.add(saveroom);
            }
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Saveother> queryother() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("other");
        while (cursorQueryTheCursor.moveToNext()) {
            Saveother saveother = new Saveother();
            saveother.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            saveother.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            saveother.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            saveother.other_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("other_id"));
            saveother.channel_1 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel_1"));
            saveother.channel_2 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel_2"));
            saveother.other_statement = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("other_statement"));
            saveother.other_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("other_icon"));
            saveother.other_type = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("other_type"));
            arrayList.add(saveother);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savemarcobutton> querymarcobutton() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("marcobutton");
        while (cursorQueryTheCursor.moveToNext()) {
            Savemarcobutton savemarcobutton = new Savemarcobutton();
            savemarcobutton.marco_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("marco_id"));
            savemarcobutton.marco_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("marco_icon"));
            savemarcobutton.marco_remark = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("marco_remark"));
            arrayList.add(savemarcobutton);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savemarco> querymarco() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("marco");
        while (cursorQueryTheCursor.moveToNext()) {
            Savemarco savemarco = new Savemarco();
            savemarco.marco_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("marco_id"));
            savemarco.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savemarco.room = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("room"));
            savemarco.device = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("device"));
            savemarco.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savemarco.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savemarco.control_type = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("control_type"));
            savemarco.value1 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value1"));
            savemarco.value2 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value2"));
            savemarco.value3 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value3"));
            savemarco.sentorder = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("sentorder"));
            arrayList.add(savemarco);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savefan> queryfan() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("fan");
        while (cursorQueryTheCursor.moveToNext()) {
            Savefan savefan = new Savefan();
            savefan.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savefan.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savefan.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savefan.fan_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("fan_id"));
            savefan.fan_statement = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("fan_statement"));
            savefan.channel = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel"));
            savefan.fan_Type = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("fan_Type"));
            savefan.fan_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("fan_icon"));
            arrayList.add(savefan);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savemedia> querymedia() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("media");
        while (cursorQueryTheCursor.moveToNext()) {
            Savemedia savemedia = new Savemedia();
            savemedia.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savemedia.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savemedia.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savemedia.media_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("media_id"));
            savemedia.media_statement = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("media_statement"));
            savemedia.media_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("media_icon"));
            arrayList.add(savemedia);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savemediabutton> querymediabutton() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("mediabutton");
        while (cursorQueryTheCursor.moveToNext()) {
            Savemediabutton savemediabutton = new Savemediabutton();
            savemediabutton.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savemediabutton.media_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("media_id"));
            savemediabutton.button_num = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("button_num"));
            savemediabutton.media_swno = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("media_swno"));
            savemediabutton.media_controltype = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("media_controltype"));
            savemediabutton.media_type = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("media_type"));
            savemediabutton.ifIRmarco = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("ifIRmarco"));
            arrayList.add(savemediabutton);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savesecurity> querysecurity() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("security");
        while (cursorQueryTheCursor.moveToNext()) {
            Savesecurity savesecurity = new Savesecurity();
            savesecurity.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savesecurity.security_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("security_id"));
            savesecurity.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savesecurity.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savesecurity.password = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("password"));
            arrayList.add(savesecurity);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<SavesecurityArea> querysecurityarea() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("securityarea");
        while (cursorQueryTheCursor.moveToNext()) {
            SavesecurityArea savesecurityArea = new SavesecurityArea();
            savesecurityArea.security_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("security_id"));
            savesecurityArea.areaNO = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("areaNO"));
            savesecurityArea.areaName = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("areaName"));
            arrayList.add(savesecurityArea);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Saveenergy> queryenergy() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("energy");
        while (cursorQueryTheCursor.moveToNext()) {
            Saveenergy saveenergy = new Saveenergy();
            saveenergy.energy_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("energy_id"));
            saveenergy.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            saveenergy.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            saveenergy.energyname = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("energyname"));
            saveenergy.channel1_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel1_name"));
            saveenergy.channel2_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel2_name"));
            saveenergy.channel3_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel3_name"));
            saveenergy.channel4_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel4_name"));
            saveenergy.channel5_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel5_name"));
            saveenergy.channel6_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel6_name"));
            saveenergy.channel7_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel7_name"));
            saveenergy.channel8_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel8_name"));
            saveenergy.channel9_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel9_name"));
            saveenergy.channel10_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel10_name"));
            saveenergy.channel11_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel11_name"));
            saveenergy.channel12_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel12_name"));
            saveenergy.channel13_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel13_name"));
            saveenergy.channel14_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel14_name"));
            saveenergy.channel15_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel15_name"));
            saveenergy.channel16_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel16_name"));
            saveenergy.channel17_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel17_name"));
            saveenergy.channel18_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel18_name"));
            saveenergy.channel19_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel19_name"));
            saveenergy.channel20_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel20_name"));
            saveenergy.channel21_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel21_name"));
            saveenergy.channel22_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel22_name"));
            saveenergy.channel23_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel23_name"));
            saveenergy.channel24_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("channel24_name"));
            arrayList.add(saveenergy);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Saveenergydata> queryenergydata() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("energydata");
        while (cursorQueryTheCursor.moveToNext()) {
            Saveenergydata saveenergydata = new Saveenergydata();
            saveenergydata.energy_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("energy_id"));
            saveenergydata.channel1_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel1_value"));
            saveenergydata.channel2_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel2_value"));
            saveenergydata.channel3_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel3_value"));
            saveenergydata.channel4_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel4_value"));
            saveenergydata.channel5_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel5_value"));
            saveenergydata.channel6_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel6_value"));
            saveenergydata.channel7_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel7_value"));
            saveenergydata.channel8_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel8_value"));
            saveenergydata.channel9_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel9_value"));
            saveenergydata.channel10_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel10_value"));
            saveenergydata.channel11_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel11_value"));
            saveenergydata.channel12_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel12_value"));
            saveenergydata.channel13_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel13_value"));
            saveenergydata.channel14_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel14_value"));
            saveenergydata.channel15_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel15_value"));
            saveenergydata.channel16_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel16_value"));
            saveenergydata.channel17_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel17_value"));
            saveenergydata.channel18_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel18_value"));
            saveenergydata.channel19_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel19_value"));
            saveenergydata.channel20_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel20_value"));
            saveenergydata.channel21_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel21_value"));
            saveenergydata.channel22_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel22_value"));
            saveenergydata.channel23_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel23_value"));
            saveenergydata.channel24_value = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel24_value"));
            saveenergydata.Time = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("Time"));
            arrayList.add(saveenergydata);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savenio> querynio() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("nio");
        while (cursorQueryTheCursor.moveToNext()) {
            Savenio savenio = new Savenio();
            savenio.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
            savenio.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savenio.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savenio.nio_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("nio_id"));
            savenio.nio_remark = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("nio_remark"));
            savenio.name1 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name1"));
            savenio.name2 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name2"));
            savenio.name3 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name3"));
            savenio.name4 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name4"));
            savenio.name5 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name5"));
            savenio.name6 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name6"));
            savenio.name7 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name7"));
            savenio.name8 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name8"));
            savenio.name9 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name9"));
            savenio.name10 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name10"));
            savenio.name11 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name11"));
            savenio.name12 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name12"));
            savenio.name13 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name13"));
            savenio.name14 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name14"));
            savenio.name15 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("name15"));
            savenio.value1 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value1"));
            savenio.value2 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value2"));
            savenio.value3 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value3"));
            savenio.value4 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value4"));
            savenio.value5 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value5"));
            savenio.value6 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value6"));
            savenio.value7 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value7"));
            savenio.value8 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value8"));
            savenio.value9 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value9"));
            savenio.value10 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value10"));
            savenio.value11 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value11"));
            savenio.value12 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value12"));
            savenio.value13 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value13"));
            savenio.value14 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value14"));
            savenio.value15 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value15"));
            savenio.value16 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value16"));
            savenio.value17 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value17"));
            savenio.value18 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value18"));
            savenio.value19 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value19"));
            savenio.value20 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value20"));
            savenio.value21 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value21"));
            savenio.value22 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value22"));
            savenio.value23 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value23"));
            savenio.value24 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value24"));
            savenio.value25 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value25"));
            savenio.value26 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value26"));
            savenio.value27 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("value27"));
            arrayList.add(savenio);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savestatus> querystatus() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("status");
        while (cursorQueryTheCursor.moveToNext()) {
            Savestatus savestatus = new Savestatus();
            savestatus.status_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("status_id"));
            savestatus.name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex(DatabaseUtil.KEY_NAME));
            savestatus.subnetID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("subnetID"));
            savestatus.deviceID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("deviceID"));
            savestatus.type = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex(DatabaseUtil.KEY_TYPE));
            savestatus.channel = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("channel"));
            savestatus.status_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("status_icon"));
            savestatus.unit = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("unit"));
            arrayList.add(savestatus);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Saveschedule> queryschedule() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("schedule");
        while (cursorQueryTheCursor.moveToNext()) {
            Saveschedule saveschedule = new Saveschedule();
            saveschedule.schedule_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("schedule_id"));
            saveschedule.schedule_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("schedule_name"));
            saveschedule.schedule_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("schedule_icon"));
            saveschedule.marco_ID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("marco_ID"));
            saveschedule.alarm_time = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("alarm_time"));
            saveschedule.repeat_option1 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("repeat_option1"));
            saveschedule.repeat_option2 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("repeat_option2"));
            saveschedule.last_status = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("last_status"));
            arrayList.add(saveschedule);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<Savenfc> querynfc() {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("nfc");
        while (cursorQueryTheCursor.moveToNext()) {
            Savenfc savenfc = new Savenfc();
            savenfc.nfc_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("nfc_id"));
            savenfc.nfc_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("nfc_name"));
            savenfc.nfc_icon = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("nfc_icon"));
            savenfc.nfc_content = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("nfc_content"));
            savenfc.state = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("state"));
            savenfc.action_type = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("action_type"));
            savenfc.marco_ID = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("marco_ID"));
            savenfc.marco_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("marco_name"));
            savenfc.call_num = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("call_num"));
            savenfc.message = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("message"));
            savenfc.resume1 = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("resume1"));
            savenfc.resume2 = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("resume2"));
            savenfc.delaytime = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("delaytime"));
            arrayList.add(savenfc);
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public List<String> querylist(String str) {
        ArrayList arrayList = new ArrayList();
        Cursor cursorQueryTheCursor = queryTheCursor("room");
        while (cursorQueryTheCursor.moveToNext()) {
            Saveroom saveroom = new Saveroom();
            saveroom._id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("_id"));
            if (str.equals("room_id")) {
                saveroom.room_id = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("room_id"));
                arrayList.add(String.valueOf(saveroom.room_id));
            } else if (str.equals("room_name")) {
                saveroom.room_name = cursorQueryTheCursor.getString(cursorQueryTheCursor.getColumnIndex("room_name"));
                arrayList.add(saveroom.room_name);
            } else if (str.equals("light")) {
                saveroom.light = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("light"));
                arrayList.add(String.valueOf(saveroom.light));
            } else if (str.equals("hvac")) {
                saveroom.hvac = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("hvac"));
                arrayList.add(String.valueOf(saveroom.hvac));
            } else if (str.equals("mood")) {
                saveroom.mood = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("mood"));
                arrayList.add(String.valueOf(saveroom.mood));
            } else if (str.equals("curtain")) {
                saveroom.curtain = cursorQueryTheCursor.getInt(cursorQueryTheCursor.getColumnIndex("curtain"));
                arrayList.add(String.valueOf(saveroom.curtain));
            }
        }
        cursorQueryTheCursor.close();
        return arrayList;
    }

    public Cursor queryTheCursor(String str) {
        return this.db.rawQuery("SELECT * FROM " + str, null);
    }

    public int querydata(SaveTips saveTips) {
        Cursor cursorRawQuery = this.db.rawQuery("SELECT * FROM tip where date=?", new String[]{String.valueOf(saveTips.date)});
        int count = cursorRawQuery.getCount();
        cursorRawQuery.close();
        return count;
    }

    public String[] queryroomfounction(int i) {
        Cursor cursorRawQuery = this.db.rawQuery("SELECT * FROM room where room_id=?", new String[]{String.valueOf(i)});
        String[] strArr = {"", "", "", "", "", "", "", "", "", ""};
        System.out.println(cursorRawQuery);
        if (cursorRawQuery.moveToNext()) {
            strArr[0] = cursorRawQuery.getString(3);
            strArr[1] = cursorRawQuery.getString(4);
            strArr[2] = cursorRawQuery.getString(6);
            strArr[3] = cursorRawQuery.getString(7);
            strArr[4] = cursorRawQuery.getString(8);
            strArr[5] = cursorRawQuery.getString(5);
            strArr[6] = cursorRawQuery.getString(11);
            strArr[7] = cursorRawQuery.getString(12);
            strArr[8] = cursorRawQuery.getString(13);
            strArr[9] = cursorRawQuery.getString(14);
        }
        cursorRawQuery.close();
        return strArr;
    }

    public void closeDB() {
        this.db.close();
    }
}
