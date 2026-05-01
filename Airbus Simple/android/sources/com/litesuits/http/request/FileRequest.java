package com.litesuits.http.request;

import com.litesuits.http.parser.DataParser;
import com.litesuits.http.parser.impl.FileParser;
import com.litesuits.http.request.param.HttpParamModel;
import com.litesuits.http.request.param.NonHttpParam;
import java.io.File;

/* loaded from: classes.dex */
public class FileRequest extends AbstractRequest<File> {

    @NonHttpParam
    private File saveToFile;

    public FileRequest(String str) {
        super(str);
    }

    public FileRequest(HttpParamModel httpParamModel, String str) {
        super(httpParamModel);
        setFileSavePath(str);
    }

    public FileRequest(HttpParamModel httpParamModel, File file) {
        super(httpParamModel);
        this.saveToFile = file;
    }

    public FileRequest(String str, File file) {
        super(str);
        this.saveToFile = file;
    }

    public FileRequest(String str, String str2) {
        super(str);
        setFileSavePath(str2);
    }

    public FileRequest setFileSavePath(String str) {
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
    public DataParser<File> createDataParser() {
        return new FileParser(this.saveToFile);
    }
}
