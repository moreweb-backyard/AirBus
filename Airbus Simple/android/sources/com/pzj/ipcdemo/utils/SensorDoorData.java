package com.pzj.ipcdemo.utils;

import com.pzj.ipcdemo.bean.DoorBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class SensorDoorData {
    public static final String TAG = "SensorDoorData";
    public static ArrayList<Map<String, ArrayList<DoorBean>>> sensordoorItems = new ArrayList<>();

    public static void AddSensor(String str, DoorBean doorBean) {
        if (CheckSensorDoor(str, doorBean.getSensoridTag())) {
            int size = sensordoorItems.size();
            if (size == 0) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(doorBean);
                HashMap map = new HashMap();
                map.put(str, arrayList);
                sensordoorItems.add(map);
                return;
            }
            for (int i = 0; i < size; i++) {
                if (sensordoorItems.get(i).containsKey(str)) {
                    sensordoorItems.get(i).get(str).add(doorBean);
                } else {
                    ArrayList arrayList2 = new ArrayList();
                    arrayList2.add(doorBean);
                    HashMap map2 = new HashMap();
                    map2.put(str, arrayList2);
                    sensordoorItems.add(map2);
                }
            }
        }
    }

    private static boolean CheckSensorDoor(String str, String str2) {
        int size = sensordoorItems.size();
        for (int i = 0; i < size; i++) {
            if (sensordoorItems.get(i).containsKey(str)) {
                ArrayList<DoorBean> arrayList = sensordoorItems.get(i).get(str);
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    if (str2.equals(arrayList.get(i2).getSensoridTag())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static ArrayList<DoorBean> getSensorDoorBeanList(String str) {
        int size = sensordoorItems.size();
        for (int i = 0; i < size; i++) {
            if (sensordoorItems.get(i).containsKey(str)) {
                return sensordoorItems.get(i).get(str);
            }
        }
        return null;
    }

    public static void ChangeDoorOpenCloseStatus(String str, String str2, int i) {
        int size = sensordoorItems.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (sensordoorItems.get(i2).containsKey(str)) {
                ArrayList<DoorBean> arrayList = sensordoorItems.get(i2).get(str);
                int size2 = arrayList.size();
                for (int i3 = 0; i3 < size2; i3++) {
                    DoorBean doorBean = arrayList.get(i3);
                    if (doorBean.getSensoridTag().equals(str2)) {
                        doorBean.setStatus(i);
                    }
                }
            }
        }
    }

    public static void ChangeDoorName(String str, String str2, String str3) {
        int size = sensordoorItems.size();
        for (int i = 0; i < size; i++) {
            if (sensordoorItems.get(i).containsKey(str)) {
                ArrayList<DoorBean> arrayList = sensordoorItems.get(i).get(str);
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    DoorBean doorBean = arrayList.get(i2);
                    if (doorBean.getSensoridTag().equals(str2)) {
                        doorBean.setName(str3);
                    }
                }
            }
        }
    }

    public static void removeSnesorDoor(String str, String str2) {
        int size = sensordoorItems.size();
        for (int i = 0; i < size; i++) {
            if (sensordoorItems.get(i).containsKey(str)) {
                ArrayList<DoorBean> arrayList = sensordoorItems.get(i).get(str);
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    if (arrayList.get(i2).getSensoridTag().equals(str2)) {
                        sensordoorItems.remove(i);
                    }
                }
            }
        }
    }
}
