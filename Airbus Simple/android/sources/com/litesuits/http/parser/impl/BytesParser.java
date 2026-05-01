package com.litesuits.http.parser.impl;

import com.litesuits.http.parser.MemCacheableParser;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class BytesParser extends MemCacheableParser<byte[]> {
    @Override // com.litesuits.http.parser.DataParser
    public byte[] parseNetStream(InputStream inputStream, long j, String str) throws IOException {
        return streamToByteArray(inputStream, j);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.litesuits.http.parser.MemCacheableParser
    public byte[] parseDiskCache(InputStream inputStream, long j) throws IOException {
        return streamToByteArray(inputStream, j);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.litesuits.http.parser.MemCacheableParser
    public boolean tryKeepToCache(byte[] bArr) throws IOException {
        return keepToCache(bArr);
    }
}
