package com.litesuits.http.parser.impl;

import com.litesuits.http.parser.FileCacheableParser;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class FileParser extends FileCacheableParser<File> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.litesuits.http.parser.FileCacheableParser
    public File parseDiskCache(File file) {
        return file;
    }

    public FileParser() {
    }

    public FileParser(File file) {
        this.file = file;
    }

    @Override // com.litesuits.http.parser.DataParser
    public File parseNetStream(InputStream inputStream, long j, String str) throws IOException {
        return streamToFile(inputStream, j);
    }
}
