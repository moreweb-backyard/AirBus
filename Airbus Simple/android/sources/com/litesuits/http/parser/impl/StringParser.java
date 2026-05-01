package com.litesuits.http.parser.impl;

import com.litesuits.http.parser.MemCacheableParser;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class StringParser extends MemCacheableParser<String> {
    @Override // com.litesuits.http.parser.DataParser
    public String parseNetStream(InputStream inputStream, long j, String str) throws IOException {
        return streamToString(inputStream, j, str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.litesuits.http.parser.MemCacheableParser
    public String parseDiskCache(InputStream inputStream, long j) throws IOException {
        return streamToString(inputStream, j, this.charSet);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.litesuits.http.parser.MemCacheableParser
    public boolean tryKeepToCache(String str) throws IOException {
        return keepToCache(str);
    }
}
