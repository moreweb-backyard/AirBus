package com.shima.smartbushome.udp;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class GetInfoThread extends Thread {
    private Handler handler;
    private Context mcontext;
    private DatagramSocket socket;
    private int msgType = 11;
    private int PORT = 6000;
    private boolean receive = true;

    private void sendErrorMsg(String str) {
    }

    public GetInfoThread(Context context, Handler handler) throws SocketException {
        this.handler = handler;
        this.mcontext = context;
        init();
    }

    public void init() throws SocketException {
        try {
            this.socket = new DatagramSocket((SocketAddress) null);
            this.socket.setBroadcast(true);
            this.socket.setReuseAddress(true);
            if (udp_socket.rsipip) {
                Context context = this.mcontext;
                Context context2 = this.mcontext;
                WifiInfo connectionInfo = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo();
                Context context3 = this.mcontext;
                Context context4 = this.mcontext;
                if (connectionInfo.getSSID().equals(context3.getSharedPreferences("wifiinfo", 0).getString("wifiname", "not set wifiname"))) {
                    this.socket.bind(new InetSocketAddress(this.PORT));
                    System.out.println("------------------rev-1-------------------");
                    return;
                } else {
                    this.socket.bind(new InetSocketAddress(8888));
                    System.out.println("------------------rev-2-------------------");
                    return;
                }
            }
            this.socket.bind(new InetSocketAddress(this.PORT));
            System.out.println("------------------rev-4-------------------");
        } catch (SocketException e) {
            e.printStackTrace();
            sendErrorMsg("Search Thread init fail");
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException {
        if (this.socket == null) {
            return;
        }
        try {
            byte[] bArr = new byte[2048];
            DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
            while (this.receive) {
                this.socket.receive(datagramPacket);
                if (this.handler != null) {
                    byte[] bArr2 = new byte[datagramPacket.getLength()];
                    System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
                    this.handler.sendMessage(this.handler.obtainMessage(this.msgType, bArr2));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.socket.close();
        }
    }

    public void close() {
        if (this.socket == null) {
            return;
        }
        this.socket.close();
    }

    public void sendMsg(DatagramPacket datagramPacket) throws IOException {
        if (this.socket != null) {
            try {
                this.socket.send(datagramPacket);
            } catch (UnknownHostException e) {
                e.printStackTrace();
                Log.d("tag", "发送失败");
            } catch (IOException e2) {
                e2.printStackTrace();
                Log.d("tag", "发送失败");
            }
        }
    }

    public void setReceive(boolean z) {
        this.receive = z;
    }

    public void setMsgType(int i) {
        this.msgType = i;
    }
}
