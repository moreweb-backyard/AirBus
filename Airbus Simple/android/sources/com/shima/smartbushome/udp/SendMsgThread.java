package com.shima.smartbushome.udp;

import java.net.DatagramPacket;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes.dex */
public class SendMsgThread extends Thread {
    private GetInfoThread ss;
    private Queue<DatagramPacket> sendMsgQuene = new LinkedList();
    private boolean send = true;

    public SendMsgThread(GetInfoThread getInfoThread) {
        this.ss = getInfoThread;
    }

    public synchronized void putMsg(DatagramPacket datagramPacket) {
        if (this.sendMsgQuene.size() == 0) {
            notify();
        }
        this.sendMsgQuene.offer(datagramPacket);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        synchronized (this) {
            while (this.send) {
                while (this.sendMsgQuene.size() > 0) {
                    DatagramPacket datagramPacketPoll = this.sendMsgQuene.poll();
                    if (this.ss != null) {
                        this.ss.sendMsg(datagramPacketPoll);
                    }
                    try {
                        Thread.sleep(150L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    wait();
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void setSend(boolean z) {
        this.send = z;
    }
}
