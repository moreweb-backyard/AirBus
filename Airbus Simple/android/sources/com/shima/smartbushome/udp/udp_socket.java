package com.shima.smartbushome.udp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.shima.smartbushome.assist.CrashHandle.FileUtil;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

/* loaded from: classes.dex */
public class udp_socket {
    public static final String ACTION_DATA_IN = "com.example.udp.DATA_IN";
    private static final int CONST_CMD_TYPE_BR_CH = 6;
    private static final int CONST_CMD_TYPE_BR_SCENE = 5;
    private static final int CONST_CMD_TYPE_CURTAIN = 7;
    private static final int CONST_CMD_TYPE_GPRS = 9;
    private static final int CONST_CMD_TYPE_INVALID = 3;
    private static final int CONST_CMD_TYPE_PANEL = 10;
    private static final int CONST_CMD_TYPE_SCENE = 0;
    private static final int CONST_CMD_TYPE_SEQUENCE = 1;
    private static final int CONST_CMD_TYPE_SINGLE_CH = 4;
    private static final int CONST_CMD_TYPE_TIMER = 8;
    private static final int CONST_CMD_TYPE_UNIVERSAL_SWITCH = 2;
    private static final int CONST_MAX_TIMES_OF_SEND = 2;
    private static final int CONST_MAX_UPD_PACKET_LEN = 2048;
    private static final short CONST_SELF_DEVICE_ID = 187;
    private static final short CONST_SELF_DEVICE_TYPE_H = 204;
    private static final short CONST_SELF_DEVICE_TYPE_L = 204;
    private static final short CONST_SELF_SUBNET_ID = 187;
    private static final int CONST_TIME_OUT_FOR_EACH_WAIT = 1000;
    private static final int CONST_TIME_OUT_FOR_TOTAL_WAIT = 4000;
    private static final int CONST_TIME_OUT_FOR_TOTAL_WAIT_ALBUM = 5000;
    private static final int CONST_TIME_OUT_FOR_TOTAL_WAIT_RS232 = 4000;
    private static final int CONST_UDP_UDP_PORT = 6000;
    public static int SMARTSERVICE_PORT = 8888;
    public static DatagramSocket moUDPSocket = null;
    public static DatagramPacket packet = null;
    public static boolean startrec = false;
    public static boolean startsent = false;
    private GetInfoThread getInfoThread;
    Context mcontext;
    DatagramPacket oDataPacket;
    private SendMsgThread smt;
    private static final int[] mbufintCRCTable = {0, 4129, 8258, 12387, 16516, 20645, 24774, 28903, 33032, 37161, 41290, 45419, 49548, 53677, 57806, 61935, 4657, 528, 12915, 8786, 21173, 17044, 29431, 25302, 37689, 33560, 45947, 41818, 54205, 50076, 62463, 58334, 9314, 13379, 1056, 5121, 25830, 29895, 17572, 21637, 42346, 46411, 34088, 38153, 58862, 62927, 50604, 54669, 13907, 9842, 5649, 1584, 30423, 26358, 22165, 18100, 46939, 42874, 38681, 34616, 63455, 59390, 55197, 51132, 18628, 22757, 26758, 30887, 2112, 6241, 10242, 14371, 51660, 55789, 59790, 63919, 35144, 39273, 43274, 47403, 23285, 19156, 31415, 27286, 6769, 2640, 14899, 10770, 56317, 52188, 64447, 60318, 39801, 35672, 47931, 43802, 27814, 31879, 19684, 23749, 11298, 15363, 3168, 7233, 60846, 64911, 52716, 56781, 44330, 48395, 36200, 40265, 32407, 28342, 24277, 20212, 15891, 11826, 7761, 3696, 65439, 61374, 57309, 53244, 48923, 44858, 40793, 36728, 37256, 33193, 45514, 41451, 53516, 49453, 61774, 57711, 4224, 161, 12482, 8419, 20484, 16421, 28742, 24679, 33721, 37784, 41979, 46042, 49981, 54044, 58239, 62302, 689, 4752, 8947, 13010, 16949, 21012, 25207, 29270, 46570, 42443, 38312, 34185, 62830, 58703, 54572, 50445, 13538, 9411, 5280, 1153, 29798, 25671, 21540, 17413, 42971, 47098, 34713, 38840, 59231, 63358, 50973, 55100, 9939, 14066, 1681, 5808, 26199, 30326, 17941, 22068, 55628, 51565, 63758, 59695, 39368, 35305, 47498, 43435, 22596, 18533, 30726, 26663, 6336, 2273, 14466, 10403, 52093, 56156, 60223, 64286, 35833, 39896, 43963, 48026, 19061, 23124, 27191, 31254, 2801, 6864, 10931, 14994, 64814, 60687, 56684, 52557, 48554, 44427, 40424, 36297, 31782, 27655, 23652, 19525, 15522, 11395, 7392, 3265, 61215, 65342, 53085, 57212, 44955, 49082, 36825, 40952, 28183, 32310, 20053, 24180, 11923, 16050, 3793, 7920};
    public static boolean mansetip = false;
    public static boolean ddnssetip = false;
    public static boolean rsipip = false;
    public static byte[] MacAddress = new byte[8];
    public static byte[] manlocalip = new byte[4];
    public static byte[] mantargetip = new byte[4];
    public static byte[] ddnstargetip = new byte[4];
    Handler senthandler = new Handler();
    Handler revhandler = new Handler();
    byte[] buf = new byte[2048];
    Handler handler2 = new Handler() { // from class: com.shima.smartbushome.udp.udp_socket.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            message.what = 11;
            udp_socket.this.getInfoThread.setMsgType(11);
            udp_socket.this.broadcastUpdate(udp_socket.ACTION_DATA_IN, (byte[]) message.obj);
        }
    };
    int a = 0;
    int netchange = 0;
    Runnable sentrun = new Runnable() { // from class: com.shima.smartbushome.udp.udp_socket.2
        @Override // java.lang.Runnable
        public void run() {
            udp_socket.this.a = 0;
            udp_socket.this.senthandler.removeCallbacks(udp_socket.this.sentrun);
        }
    };
    public String g4Realm = "www.g4cloud.ir";
    public byte[] remoteByte = new byte[4];
    Runnable networkTask = new Runnable() { // from class: com.shima.smartbushome.udp.udp_socket.3
        @Override // java.lang.Runnable
        public void run() {
            Context context = udp_socket.this.mcontext;
            Context context2 = udp_socket.this.mcontext;
            SharedPreferences sharedPreferences = context.getSharedPreferences("remoteinfo", 0);
            udp_socket.this.g4Realm = sharedPreferences.getString("g4Realm", "www.g4cloud.ir");
            udp_socket.this.remoteByte = udp_socket.this.GetInetAddress(udp_socket.this.g4Realm);
        }
    };

    public void initprocess() {
        this.getInfoThread = new GetInfoThread(this.mcontext, this.handler2);
        this.smt = new SendMsgThread(this.getInfoThread);
        this.smt.start();
        this.getInfoThread.start();
    }

    public udp_socket(Context context) {
        this.mcontext = context;
        new Thread(this.networkTask).start();
    }

    public void StopAllThread() {
        startsent = false;
        startrec = false;
        this.getInfoThread.close();
    }

    public boolean SendUDPBuffer(byte[] bArr, short s, int i, byte b, byte b2, boolean z) {
        byte[] bArr2 = new byte[4];
        byte[] bArr3 = new byte[4];
        try {
            byte[] bArrGetLocalIP = GetLocalIP();
            Log.v("breakPoint", "SendUDPBuffer 正在获得目标IP地址");
            byte[] bArrGetTargetIP = GetTargetIP(bArrGetLocalIP);
            System.out.println("arraybytetargetIP=" + (bArrGetTargetIP[0] & 255) + FileUtil.FILE_EXTENSION_SEPARATOR + (bArrGetTargetIP[1] & 255) + FileUtil.FILE_EXTENSION_SEPARATOR + (bArrGetTargetIP[2] & 255) + FileUtil.FILE_EXTENSION_SEPARATOR + (bArrGetTargetIP[3] & 255) + FileUtil.FILE_EXTENSION_SEPARATOR);
            int i2 = (short) (s + 11);
            int i3 = (short) (((short) (i2 + 2)) + 14);
            byte[] bArr4 = new byte[i3];
            byte[] bArr5 = new byte[i2];
            bArr4[0] = bArrGetLocalIP[0];
            bArr4[1] = bArrGetLocalIP[1];
            bArr4[2] = bArrGetLocalIP[2];
            bArr4[3] = bArrGetLocalIP[3];
            bArr4[4] = 83;
            bArr4[5] = 77;
            bArr4[6] = 65;
            bArr4[7] = 82;
            bArr4[8] = 84;
            bArr4[9] = 67;
            bArr4[10] = 76;
            bArr4[11] = 79;
            bArr4[12] = 85;
            bArr4[13] = 68;
            bArr4[14] = -86;
            bArr4[15] = -86;
            if (z) {
                bArr5[0] = -1;
            } else {
                bArr5[0] = (byte) i2;
            }
            bArr5[0] = bArr5[0];
            bArr5[1] = -69;
            bArr5[2] = -69;
            bArr5[3] = -52;
            bArr5[4] = -52;
            bArr5[5] = (byte) (i / 256);
            bArr5[6] = (byte) (i % 256);
            bArr5[7] = b;
            bArr5[8] = b2;
            if (s > 0) {
                for (short s2 = 0; s2 <= s - 1; s2 = (short) (s2 + 1)) {
                    bArr5[s2 + 9] = bArr[s2];
                }
            }
            if (!z) {
                PackCRC(bArr5, (short) (bArr5.length - 2));
            } else {
                PackCRC(bArr5, (short) (bArr5.length - 2));
            }
            for (short s3 = 0; s3 <= bArr5.length - 1; s3 = (short) (s3 + 1)) {
                bArr4[s3 + 16] = bArr5[s3];
            }
            if (rsipip) {
                Context context = this.mcontext;
                Context context2 = this.mcontext;
                WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
                Context context3 = this.mcontext;
                Context context4 = this.mcontext;
                if (connectionInfo.getSSID().equals(context3.getSharedPreferences("wifiinfo", 0).getString("wifiname", "not set wifiname"))) {
                    if (this.netchange != 1) {
                        initprocess();
                    }
                    this.oDataPacket = new DatagramPacket(bArr4, i3, InetAddress.getByAddress(bArrGetTargetIP), CONST_UDP_UDP_PORT);
                    this.netchange = 1;
                    System.out.println("------------------1-------------------");
                } else {
                    if (this.netchange != 2) {
                        initprocess();
                    }
                    byte[] bArr6 = new byte[bArr4.length + 9];
                    bArr6[0] = this.remoteByte[0];
                    bArr6[1] = this.remoteByte[1];
                    bArr6[2] = this.remoteByte[2];
                    bArr6[3] = this.remoteByte[3];
                    System.arraycopy(bArr4, 4, bArr6, 4, bArr4.length - 6);
                    bArr6[16] = (byte) ((bArr6[16] % 255) + 9);
                    System.arraycopy(new byte[]{2}, 0, bArr6, bArr4.length - 2, 1);
                    System.arraycopy(MacAddress, 0, bArr6, bArr4.length - 1, 8);
                    System.arraycopy(new byte[]{0, 0}, 0, bArr6, bArr6.length - 2, 2);
                    this.oDataPacket = new DatagramPacket(bArr6, bArr6.length, InetAddress.getByAddress(bArrGetTargetIP), SMARTSERVICE_PORT);
                    this.netchange = 2;
                    System.out.println("------------------2-------------------");
                }
            } else {
                if (this.netchange != 4) {
                    initprocess();
                }
                this.oDataPacket = new DatagramPacket(bArr4, i3, InetAddress.getByAddress(bArrGetTargetIP), CONST_UDP_UDP_PORT);
                this.netchange = 4;
                System.out.println("------------------4-------------------");
            }
            this.smt.putMsg(this.oDataPacket);
            this.senthandler.postDelayed(this.sentrun, 15L);
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastUpdate(String str, byte[] bArr) {
        Intent intent = new Intent(str);
        intent.putExtra(ACTION_DATA_IN, bArr);
        this.mcontext.sendBroadcast(intent);
    }

    public byte[] GetLocalIP() throws SocketException {
        byte[] bArr = new byte[4];
        byte[] bArr2 = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddressNextElement = inetAddresses.nextElement();
                    if (!inetAddressNextElement.isLoopbackAddress() && !inetAddressNextElement.isLinkLocalAddress()) {
                        byte[] address = inetAddressNextElement.getAddress();
                        try {
                            inetAddressNextElement.getHostAddress().toString();
                            return address;
                        } catch (Exception unused) {
                            bArr2 = address;
                            if (mansetip) {
                                bArr2[0] = manlocalip[0];
                                bArr2[1] = manlocalip[1];
                                bArr2[2] = manlocalip[2];
                                bArr2[3] = manlocalip[3];
                            }
                            return bArr2;
                        }
                    }
                }
            }
            return null;
        } catch (Exception unused2) {
        }
    }

    public byte[] GetTargetIP(byte[] bArr) {
        byte[] bArr2 = {-1, -1, -1, -1};
        if (mansetip) {
            Context context = this.mcontext;
            Context context2 = this.mcontext;
            WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
            Context context3 = this.mcontext;
            Context context4 = this.mcontext;
            if (!connectionInfo.getSSID().equals(context3.getSharedPreferences("wifiinfo", 0).getString("wifiname", "not set wifiname"))) {
                bArr2[0] = mantargetip[0];
                bArr2[1] = mantargetip[1];
                bArr2[2] = mantargetip[2];
                bArr2[3] = mantargetip[3];
            }
        }
        if (ddnssetip) {
            bArr2[0] = ddnstargetip[0];
            bArr2[1] = ddnstargetip[1];
            bArr2[2] = ddnstargetip[2];
            bArr2[3] = ddnstargetip[3];
        }
        if (rsipip) {
            Context context5 = this.mcontext;
            Context context6 = this.mcontext;
            WifiInfo connectionInfo2 = ((WifiManager) context5.getSystemService("wifi")).getConnectionInfo();
            Context context7 = this.mcontext;
            Context context8 = this.mcontext;
            SharedPreferences sharedPreferences = context7.getSharedPreferences("wifiinfo", 0);
            if (connectionInfo2.getBSSID() == null || !connectionInfo2.getSSID().equals(sharedPreferences.getString("wifiname", "not set wifiname"))) {
                bArr2[0] = this.remoteByte[0];
                bArr2[1] = this.remoteByte[1];
                bArr2[2] = this.remoteByte[2];
                bArr2[3] = this.remoteByte[3];
            }
        }
        return bArr2;
    }

    protected void PackCRC(byte[] bArr, short s) {
        short s2 = 0;
        short s3 = 0;
        while (s != 0) {
            byte b = (byte) (s2 >> 8);
            try {
                s2 = (short) (((short) (s2 << 8)) ^ mbufintCRCTable[((byte) (b ^ bArr[s3])) & 255]);
                s3 = (short) (s3 + 1);
                s = (short) (s - 1);
            } catch (Exception unused) {
                return;
            }
        }
        bArr[s3] = (byte) (s2 >> 8);
        bArr[(short) (s3 + 1)] = (byte) (s2 & 255);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:14:0x0032
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1178)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public static java.lang.String GetCRC(byte[] r6) {
        /*
            java.lang.String r0 = ""
            int r1 = r6.length     // Catch: java.lang.Exception -> L69
            r2 = 0
            r3 = 0
        L5:
            if (r1 == 0) goto L1f
            int r4 = r2 >> 8
            byte r4 = (byte) r4     // Catch: java.lang.Exception -> L69
            int r2 = r2 << 8
            short r2 = (short) r2     // Catch: java.lang.Exception -> L69
            r5 = r6[r3]     // Catch: java.lang.Exception -> L69
            r4 = r4 ^ r5
            byte r4 = (byte) r4     // Catch: java.lang.Exception -> L69
            int[] r5 = com.shima.smartbushome.udp.udp_socket.mbufintCRCTable     // Catch: java.lang.Exception -> L69
            r4 = r4 & 255(0xff, float:3.57E-43)
            r4 = r5[r4]     // Catch: java.lang.Exception -> L69
            r2 = r2 ^ r4
            short r2 = (short) r2     // Catch: java.lang.Exception -> L69
            int r3 = r3 + 1
            short r3 = (short) r3     // Catch: java.lang.Exception -> L69
            int r1 = r1 + (-1)
            goto L5
        L1f:
            r6 = 65535(0xffff, float:9.1834E-41)
            r6 = r6 & r2
            java.lang.String r6 = java.lang.Integer.toHexString(r6)     // Catch: java.lang.Exception -> L69
            java.lang.String r6 = r6.toUpperCase()     // Catch: java.lang.Exception -> L69
            int r0 = r6.length()     // Catch: java.lang.Exception -> L32
            switch(r0) {
                case 1: goto L58;
                case 2: goto L46;
                case 3: goto L34;
                default: goto L32;
            }     // Catch: java.lang.Exception -> L32
        L32:
            r0 = r6
            goto L69
        L34:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L32
            r0.<init>()     // Catch: java.lang.Exception -> L32
            java.lang.String r1 = "0"
            r0.append(r1)     // Catch: java.lang.Exception -> L32
            r0.append(r6)     // Catch: java.lang.Exception -> L32
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Exception -> L32
            goto L69
        L46:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L32
            r0.<init>()     // Catch: java.lang.Exception -> L32
            java.lang.String r1 = "00"
            r0.append(r1)     // Catch: java.lang.Exception -> L32
            r0.append(r6)     // Catch: java.lang.Exception -> L32
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Exception -> L32
            goto L69
        L58:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L32
            r0.<init>()     // Catch: java.lang.Exception -> L32
            java.lang.String r1 = "000"
            r0.append(r1)     // Catch: java.lang.Exception -> L32
            r0.append(r6)     // Catch: java.lang.Exception -> L32
            java.lang.String r0 = r0.toString()     // Catch: java.lang.Exception -> L32
        L69:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.shima.smartbushome.udp.udp_socket.GetCRC(byte[]):java.lang.String");
    }

    public boolean CheckCRC(byte[] bArr, int i) {
        try {
            if (IsSocketClose()) {
                return false;
            }
            short s = 0;
            short s2 = 0;
            while (i != 0) {
                s = (short) (((short) (s << 8)) ^ mbufintCRCTable[((byte) (((byte) (s >> 8)) ^ bArr[s2])) & 255]);
                s2 = (short) (s2 + 1);
                i = (short) (i - 1);
            }
            if (bArr[s2] == (s >> 8)) {
                return bArr[s2 + 1] == ((short) (s & 255));
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean IsSocketClose() {
        try {
            if (moUDPSocket != null) {
                if (!moUDPSocket.isClosed()) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] GetInetAddress(String str) {
        byte[] bArr = {89, 42, -48, 100};
        try {
            String[] strArrSplit = InetAddress.getByName(new URL("http://wwww." + str).getHost()).getHostAddress().split("\\.");
            if (strArrSplit.length == 4) {
                bArr[0] = (byte) Integer.parseInt(strArrSplit[0]);
                bArr[1] = (byte) Integer.parseInt(strArrSplit[1]);
                bArr[2] = (byte) Integer.parseInt(strArrSplit[2]);
                bArr[3] = (byte) Integer.parseInt(strArrSplit[3]);
            }
        } catch (Exception e) {
            String[] strArrSplit2 = this.g4Realm.split("\\.");
            if (strArrSplit2.length == 4) {
                bArr[0] = (byte) Integer.parseInt(strArrSplit2[0]);
                bArr[1] = (byte) Integer.parseInt(strArrSplit2[1]);
                bArr[2] = (byte) Integer.parseInt(strArrSplit2[2]);
                bArr[3] = (byte) Integer.parseInt(strArrSplit2[3]);
            }
            e.printStackTrace();
        }
        Log.d("GetInetAddress", ((int) bArr[0]) + FileUtil.FILE_EXTENSION_SEPARATOR + ((int) bArr[1]) + FileUtil.FILE_EXTENSION_SEPARATOR + ((int) bArr[2]) + FileUtil.FILE_EXTENSION_SEPARATOR + ((int) bArr[3]));
        return bArr;
    }
}
