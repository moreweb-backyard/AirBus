package com.pzj.ipcdemo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class SensorTimeUtil {
    public static Map<String, String> planMap = new HashMap();
    static StringBuffer sb;

    public static int getPassMinutes(int i, int i2) {
        return (i * 60) + i2;
    }

    public static boolean compare_date(String str, String str2) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        try {
            Date date = simpleDateFormat.parse(str);
            Date date2 = simpleDateFormat.parse(str2);
            if (date.getTime() > date2.getTime()) {
                return false;
            }
            return date.getTime() < date2.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkTime(String str, String str2, String str3) {
        String binary = getBinary(trsforTimes(str), trsforTimes(str2) - 1);
        if (!planMap.containsKey(str3)) {
            return false;
        }
        String str4 = planMap.get(str3);
        for (int i = 0; i < 96; i++) {
            char cCharAt = str4.charAt(i);
            char cCharAt2 = binary.charAt(i);
            if (cCharAt == '1' && cCharAt2 == '1') {
                System.out.println("有冲突:" + i);
                return true;
            }
        }
        System.out.println("没有冲突");
        return false;
    }

    public static void addNewDayToMap(String str, String str2) {
        if (planMap.containsKey(str)) {
            String str3 = planMap.get(str);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < 96; i++) {
                char cCharAt = str2.charAt(i);
                char cCharAt2 = str3.charAt(i);
                if (cCharAt == '1' || cCharAt2 == '1') {
                    stringBuffer.append("1");
                } else {
                    stringBuffer.append("0");
                }
            }
            planMap.put(str, stringBuffer.toString());
            return;
        }
        planMap.put(str, str2);
    }

    public static String getOneDay96Strings(String str, String str2) {
        return getBinary(trsforTimes(str), trsforTimes(str2) - 1);
    }

    public static int getMinute(String str) {
        return Integer.parseInt(str.split(":")[1]);
    }

    public static int getHour(String str) {
        return Integer.parseInt(str.split(":")[0]);
    }

    public static String op2(String str) {
        int length = str.length();
        sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int iCharAt = str.charAt((length - i) - 1) & 1;
            sb.append(iCharAt + "");
        }
        return sb.toString();
    }

    public static String comString(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 96; i++) {
            char cCharAt = str.charAt(i);
            char cCharAt2 = str2.charAt(i);
            if (cCharAt == '1' || cCharAt2 == '1') {
                stringBuffer.append("1");
            } else {
                stringBuffer.append("0");
            }
        }
        return stringBuffer.toString();
    }

    public static String getBinary(int i, int i2) {
        System.out.println("s1:" + i + ",s2:" + i2);
        sb = new StringBuffer();
        for (int i3 = 0; i3 < 96; i3++) {
            if (i3 >= i && i3 <= i2) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    public static int trsforTimes(String str) throws NumberFormatException {
        String[] strArrSplit = str.split(":");
        return (Integer.parseInt(strArrSplit[0]) * 4) + (Integer.parseInt(strArrSplit[1]) / 15);
    }

    public static int string32toInt(String str) {
        return Integer.valueOf(str, 2).intValue();
    }

    public static String getMinutesString(int i, int i2) {
        String binaryString = Integer.toBinaryString(i);
        String binaryString2 = Integer.toBinaryString(i2);
        String str = "";
        String str2 = "";
        int length = binaryString.length();
        int length2 = binaryString2.length();
        if (length != 12) {
            str = getInvalidBit(12 - binaryString.length()) + binaryString;
        }
        if (length2 != 12) {
            str2 = getInvalidBit(12 - binaryString2.length()) + binaryString2;
        }
        if (i == 0) {
            str = "000000000000";
        }
        if (i2 == 0) {
            str2 = "000000000000";
        }
        System.out.println("s1:" + str + ",s2:" + str2);
        return str + str2;
    }

    public static String getInvalidBit(int i) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append("0");
        }
        return stringBuffer.toString();
    }

    public static String replaceIndex(int i, String str) {
        return str.substring(0, i) + 1 + str.substring(i + 1);
    }

    public static String get2Strings(String str, String str2, String str3) {
        return str + str2 + str3;
    }
}
