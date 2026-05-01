package voice.decoder;

/* loaded from: classes.dex */
public interface VoiceRecognitionListener {
    void onRecognitionEnd(int i, String str);

    void onRecognitionStart();
}
