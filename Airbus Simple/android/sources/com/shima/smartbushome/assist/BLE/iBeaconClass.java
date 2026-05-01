package com.shima.smartbushome.assist.BLE;

import android.bluetooth.BluetoothDevice;

/* loaded from: classes.dex */
public class iBeaconClass {

    public static class iBeacon {
        public String bluetoothAddress;
        public int major;
        public int minor;
        public String name;
        public String proximityUuid;
        public int rssi;
        public int txPower;
    }

    public static boolean isBeacon(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0087, code lost:
    
        if (r0 != false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0089, code lost:
    
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x008b, code lost:
    
        r0 = new com.shima.smartbushome.assist.BLE.iBeaconClass.iBeacon();
        r0.major = ((r9[r1 + 20] & 255) * 256) + (r9[r1 + 21] & 255);
        r0.minor = ((r9[r1 + 22] & 255) * 256) + (r9[r1 + 23] & 255);
        r0.txPower = r9[r1 + 24];
        r0.rssi = r8;
        r2 = new byte[16];
        java.lang.System.arraycopy(r9, r1 + 4, r2, 0, 16);
        r9 = bytesToHexString(r2);
        r0.proximityUuid = r9.substring(0, 8) + "-" + r9.substring(8, 12) + "-" + r9.substring(12, 16) + "-" + r9.substring(16, 20) + "-" + r9.substring(20, 32);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0111, code lost:
    
        if (r7 == null) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0113, code lost:
    
        r0.bluetoothAddress = r7.getAddress();
        r0.name = r7.getName();
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x011f, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.shima.smartbushome.assist.BLE.iBeaconClass.iBeacon fromScanData(android.bluetooth.BluetoothDevice r7, int r8, byte[] r9) {
        /*
            Method dump skipped, instructions count: 288
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.assist.BLE.iBeaconClass.fromScanData(android.bluetooth.BluetoothDevice, int, byte[]):com.shima.smartbushome.assist.BLE.iBeaconClass$iBeacon");
    }

    private static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }
}
