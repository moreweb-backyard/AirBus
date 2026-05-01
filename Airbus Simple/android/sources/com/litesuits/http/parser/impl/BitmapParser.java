package com.litesuits.http.parser.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.litesuits.http.parser.FileCacheableParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class BitmapParser extends FileCacheableParser<Bitmap> {
    public BitmapParser() {
    }

    public BitmapParser(File file) {
        this.file = file;
    }

    @Override // com.litesuits.http.parser.DataParser
    public Bitmap parseNetStream(InputStream inputStream, long j, String str) throws IOException {
        this.file = streamToFile(inputStream, j);
        return BitmapFactory.decodeFile(this.file.getAbsolutePath());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.litesuits.http.parser.FileCacheableParser
    public Bitmap parseDiskCache(File file) {
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }
}
