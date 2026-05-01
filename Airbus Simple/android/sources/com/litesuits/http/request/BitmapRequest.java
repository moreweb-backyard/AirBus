package com.litesuits.http.request;

import android.graphics.Bitmap;
import com.litesuits.http.parser.DataParser;
import com.litesuits.http.parser.impl.BitmapParser;
import com.litesuits.http.request.param.HttpParamModel;
import com.litesuits.http.request.param.NonHttpParam;
import java.io.File;

/* loaded from: classes.dex */
public class BitmapRequest extends AbstractRequest<Bitmap> {

    @NonHttpParam
    protected File saveToFile;

    public BitmapRequest(HttpParamModel httpParamModel) {
        super(httpParamModel);
    }

    public BitmapRequest(HttpParamModel httpParamModel, File file) {
        super(httpParamModel);
        this.saveToFile = file;
    }

    public BitmapRequest(HttpParamModel httpParamModel, String str) {
        super(httpParamModel);
        setFileSavePath(str);
    }

    public BitmapRequest(String str) {
        super(str);
    }

    public BitmapRequest(String str, File file) {
        super(str);
        this.saveToFile = file;
    }

    public BitmapRequest(String str, String str2) {
        super(str);
        setFileSavePath(str2);
    }

    public BitmapRequest setFileSavePath(String str) {
        if (str != null) {
            this.saveToFile = new File(str);
        }
        return this;
    }

    @Override // com.litesuits.http.request.AbstractRequest
    public File getCachedFile() {
        return this.saveToFile != null ? this.saveToFile : super.getCachedFile();
    }

    @Override // com.litesuits.http.request.AbstractRequest
    public DataParser<Bitmap> createDataParser() {
        return new BitmapParser(this.saveToFile);
    }
}
