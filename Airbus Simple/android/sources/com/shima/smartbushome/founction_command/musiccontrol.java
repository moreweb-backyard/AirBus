package com.shima.smartbushome.founction_command;

import android.support.v4.view.MotionEventCompat;
import com.shima.smartbushome.assist.CrashHandle.FileUtil;
import com.shima.smartbushome.udp.udp_socket;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import org.apache.commons.fileupload.MultipartStream;

/* loaded from: classes.dex */
public class musiccontrol {
    public void deletesong(int i, int i2, String str, String str2, udp_socket udp_socketVar) {
    }

    public void sentsongcontent(int i, int i2, byte[] bArr, byte[] bArr2, byte[] bArr3, udp_socket udp_socketVar) {
        byte[] bArr4 = new byte[bArr2.length + 11];
        bArr4[0] = 4;
        bArr4[1] = 11;
        bArr4[2] = 1;
        bArr4[3] = bArr[0];
        bArr4[4] = bArr[1];
        for (int i3 = 0; i3 < 4; i3++) {
            bArr4[i3 + 5] = bArr3[i3];
        }
        bArr4[9] = (byte) (((bArr2.length - 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
        bArr4[10] = (byte) ((bArr2.length - 2) - (65280 & (bArr2.length - 2)));
        for (int i4 = 0; i4 < bArr2.length; i4++) {
            bArr4[i4 + 11] = bArr2[i4];
        }
        udp_socketVar.SendUDPBuffer(bArr4, (short) bArr4.length, 512, (byte) i, (byte) i2, true);
    }

    public byte[] UploadSongStart(int i, int i2, String str, String str2, int i3, udp_socket udp_socketVar) throws UnsupportedEncodingException {
        byte[] bArr = new byte[2];
        int i4 = 0;
        int length = str2.substring(0, str2.indexOf(FileUtil.FILE_EXTENSION_SEPARATOR)).length();
        try {
            int iNextInt = new Random().nextInt(65535);
            byte[] bytes = str.getBytes("ascii");
            byte[] bytes2 = str2.getBytes("UTF-16BE");
            byte[] bytes3 = str2.getBytes("ascii");
            byte[] bytes4 = str2.substring(0, str2.indexOf(FileUtil.FILE_EXTENSION_SEPARATOR)).getBytes("ascii");
            if (length > 8) {
                byte[] bArr2 = new byte[bytes.length + 29 + bytes2.length];
                bArr2[0] = (byte) (((bArr2.length - 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
                bArr2[1] = (byte) ((bArr2.length - 2) - ((bArr2.length - 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK));
                bArr2[2] = 0;
                bArr2[3] = (byte) (iNextInt / 256);
                bArr2[4] = (byte) (iNextInt % 256);
                bArr[0] = bArr2[3];
                bArr[1] = bArr2[4];
                bArr2[6] = 92;
                bArr2[7] = 0;
                for (int i5 = 0; i5 < bytes.length; i5++) {
                    bArr2[i5 + 8] = bytes[i5];
                }
                bArr2[bytes.length + 8] = 0;
                bArr2[bytes.length + 9] = 92;
                for (int i6 = 0; i6 < bytes2.length; i6++) {
                    bArr2[bytes.length + 10 + i6] = bytes2[i6];
                }
                bArr2[5] = (byte) (bytes.length + bytes2.length + 18);
                bArr2[bytes.length + 10 + bytes2.length] = 0;
                bArr2[bytes.length + 11 + bytes2.length] = 0;
                bArr2[bytes.length + 12 + bytes2.length] = 0;
                while (i4 < 6) {
                    bArr2[bytes.length + 13 + bytes2.length + i4] = bytes3[i4];
                    i4++;
                }
                bArr2[bytes.length + 19 + bytes2.length] = 126;
                bArr2[bytes.length + 20 + bytes2.length] = 49;
                bArr2[bytes.length + 21 + bytes2.length] = 77;
                bArr2[bytes.length + 22 + bytes2.length] = 80;
                bArr2[bytes.length + 23 + bytes2.length] = 51;
                bArr2[bytes.length + 24 + bytes2.length] = (byte) (((i3 / 256) / 256) / 256);
                bArr2[bytes.length + 25 + bytes2.length] = (byte) (((i3 / 256) / 256) % 256);
                bArr2[bytes.length + 26 + bytes2.length] = (byte) (i3 / 256);
                bArr2[bytes.length + 27 + bytes2.length] = (byte) (i3 % 256);
                bArr2[bytes.length + 28 + bytes2.length] = 32;
                udp_socketVar.SendUDPBuffer(bArr2, (short) bArr2.length, 512, (byte) i, (byte) i2, true);
            } else {
                byte[] bArr3 = new byte[bytes.length + 29];
                bArr3[0] = (byte) (((bArr3.length - 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8);
                bArr3[1] = (byte) ((bArr3.length - 2) - ((bArr3.length - 2) & MotionEventCompat.ACTION_POINTER_INDEX_MASK));
                bArr3[2] = 0;
                bArr3[3] = (byte) (iNextInt / 256);
                bArr3[4] = (byte) (iNextInt % 256);
                bArr[0] = bArr3[3];
                bArr[1] = bArr3[4];
                bArr3[6] = 92;
                bArr3[7] = 0;
                for (int i7 = 0; i7 < bytes.length; i7++) {
                    bArr3[i7 + 8] = bytes[i7];
                }
                bArr3[bytes.length + 8] = 0;
                bArr3[bytes.length + 9] = 92;
                bArr3[bytes.length + 10] = 0;
                bArr3[bytes.length + 11] = 0;
                bArr3[bytes.length + 12] = 0;
                while (i4 < 8) {
                    if (i4 < bytes4.length) {
                        bArr3[bytes.length + 13 + i4] = bytes4[i4];
                    } else {
                        bArr3[bytes.length + 13 + i4] = 32;
                    }
                    i4++;
                }
                bArr3[bytes.length + 21] = 77;
                bArr3[bytes.length + 22] = 80;
                bArr3[bytes.length + 23] = 51;
                bArr3[bytes.length + 24] = (byte) (((i3 / 256) / 256) / 256);
                bArr3[bytes.length + 25] = (byte) (((i3 / 256) / 256) % 256);
                bArr3[bytes.length + 26] = (byte) (i3 / 256);
                bArr3[bytes.length + 27] = (byte) (i3 % 256);
                bArr3[bytes.length + 28] = 32;
                bArr3[5] = (byte) (bytes.length + 18);
                udp_socketVar.SendUDPBuffer(bArr3, (short) bArr3.length, 512, (byte) i, (byte) i2, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bArr;
    }

    public void GetMusicState(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {42, 90, 49, 83, 84, 65, 84, 85, 83, 63, MultipartStream.CR};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 6446, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateZaudiolist(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {42, 83, 49, 85, 80, 68, 65, 84, 69, 76, 73, 83, 84, MultipartStream.CR};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 6446, b, b2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SwitchtoMusicSD(byte b, byte b2, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {1, 1, 0, 0};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b, b2, false);
        } catch (Exception unused) {
        }
    }

    public void MusicControl2(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2, b3, b4};
            udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b5, b6, false);
        } catch (Exception unused) {
        }
    }

    public void MusicControl(byte b, byte b2, byte b3, byte b4, byte b5, byte b6, udp_socket udp_socketVar) {
        try {
            switch (b & 255) {
                case 1:
                case 2:
                    byte[] bArr = {b, b2, b3, b4};
                    udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 536, b5, b6, false);
                    break;
                case 4:
                    byte[] bArr2 = {42, 83, 49, 0, 0, 0, 0, MultipartStream.CR, 85, -86, 1, 2, 83, 4, 0, 0, 0, 0, 14, 53};
                    switch (b2 & 255) {
                        case 1:
                            bArr2[3] = 80;
                            bArr2[4] = 82;
                            bArr2[5] = 69;
                            bArr2[6] = 86;
                            break;
                        case 2:
                            bArr2[3] = 78;
                            bArr2[4] = 69;
                            bArr2[5] = 88;
                            bArr2[6] = 84;
                            break;
                        case 3:
                            bArr2[3] = 80;
                            bArr2[4] = 76;
                            bArr2[5] = 65;
                            bArr2[6] = 89;
                            break;
                        case 4:
                            bArr2[3] = 83;
                            bArr2[4] = 84;
                            bArr2[5] = 79;
                            bArr2[6] = 80;
                            break;
                    }
                    udp_socketVar.SendUDPBuffer(bArr2, (short) bArr2.length, 6446, b5, b6, false);
                    break;
                case 5:
                    byte[] bArr3 = {42, 90, 49, 86, 79, 76, 0, 0, MultipartStream.CR, 85, -86, 1};
                    int i = b4 & 255;
                    bArr3[6] = String.valueOf((i / 10) % 10).getBytes("ascii")[0];
                    bArr3[7] = String.valueOf(i % 10).getBytes("ascii")[0];
                    udp_socketVar.SendUDPBuffer(bArr3, (short) bArr3.length, 6446, b5, b6, false);
                    break;
                case 6:
                    byte[] bArr4 = {42, 83, 49, 76, 73, 83, 84, 0, 0, 0, 44, 83, 79, 78, 71, 0, 0, 0, MultipartStream.CR, 85, -86, 1, 2, 83, 4, 0, 0, 0, 0, 14, 53};
                    int i2 = b2 & 255;
                    bArr4[7] = String.valueOf((i2 / 100) % 10).getBytes("ascii")[0];
                    bArr4[8] = String.valueOf((i2 / 10) % 10).getBytes("ascii")[0];
                    bArr4[9] = String.valueOf(i2 % 10).getBytes("ascii")[0];
                    int i3 = (b3 << 8) + (b4 & 255);
                    bArr4[15] = String.valueOf((i3 / 100) % 10).getBytes("ascii")[0];
                    bArr4[16] = String.valueOf((i3 / 10) % 10).getBytes("ascii")[0];
                    bArr4[17] = String.valueOf(i3 % 10).getBytes("ascii")[0];
                    udp_socketVar.SendUDPBuffer(bArr4, (short) bArr4.length, 6446, b5, b6, false);
                    break;
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadAlbumQTY(byte b, byte b2, byte b3, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b};
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, (short) bArr.length, 736, b2, b3, false);
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadAlbum(byte b, byte b2, byte b3, byte b4, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2};
            short length = (short) bArr.length;
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, length, 738, b3, b4, false);
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadSongQTY(byte b, byte b2, byte b3, byte b4, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2};
            short length = (short) bArr.length;
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, length, 740, b3, b4, false);
            }
        } catch (Exception unused) {
        }
    }

    public void MusicReadSong(byte b, byte b2, byte b3, byte b4, byte b5, udp_socket udp_socketVar) {
        try {
            byte[] bArr = {b, b2, b3};
            short length = (short) bArr.length;
            for (int i = 1; i < 2; i++) {
                udp_socketVar.SendUDPBuffer(bArr, length, 742, b4, b5, false);
            }
        } catch (Exception unused) {
        }
    }
}
