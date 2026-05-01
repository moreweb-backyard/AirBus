package com.shima.smartbushome.founction_command;

import com.shima.smartbushome.udp.udp_socket;
import org.apache.commons.fileupload.MultipartStream;

/* loaded from: classes.dex */
public class audio_incontrol {
    public void SwitchtoAudioIn(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {1, 2, 0, 0};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void MusicControl(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2, b3, b4};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b5, b6, false);
        } catch (Exception unused) {
        }
    }

    public void GetMusicState(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {42, 90, 49, 83, 84, 65, 84, 85, 83, 63, MultipartStream.CR};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 6446, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
