package com.bumptech.glide.load.data.mediastore;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/* loaded from: classes.dex */
class ThumbnailStreamOpener {
    private static final FileService DEFAULT_SERVICE = new FileService();
    private static final String TAG = "ThumbStreamOpener";
    private final ArrayPool byteArrayPool;
    private final ContentResolver contentResolver;
    private final List<ImageHeaderParser> parsers;
    private final ThumbnailQuery query;
    private final FileService service;

    ThumbnailStreamOpener(List<ImageHeaderParser> list, ThumbnailQuery thumbnailQuery, ArrayPool arrayPool, ContentResolver contentResolver) {
        this(list, DEFAULT_SERVICE, thumbnailQuery, arrayPool, contentResolver);
    }

    ThumbnailStreamOpener(List<ImageHeaderParser> list, FileService fileService, ThumbnailQuery thumbnailQuery, ArrayPool arrayPool, ContentResolver contentResolver) {
        this.service = fileService;
        this.query = thumbnailQuery;
        this.byteArrayPool = arrayPool;
        this.contentResolver = contentResolver;
        this.parsers = list;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0047 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    int getOrientation(android.net.Uri r7) throws java.lang.Throwable {
        /*
            r6 = this;
            r0 = 0
            android.content.ContentResolver r1 = r6.contentResolver     // Catch: java.lang.Throwable -> L17 java.lang.Throwable -> L1a
            java.io.InputStream r1 = r1.openInputStream(r7)     // Catch: java.lang.Throwable -> L17 java.lang.Throwable -> L1a
            java.util.List<com.bumptech.glide.load.ImageHeaderParser> r0 = r6.parsers     // Catch: java.lang.Throwable -> L15 java.lang.Throwable -> L44
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r2 = r6.byteArrayPool     // Catch: java.lang.Throwable -> L15 java.lang.Throwable -> L44
            int r0 = com.bumptech.glide.load.ImageHeaderParserUtils.getOrientation(r0, r1, r2)     // Catch: java.lang.Throwable -> L15 java.lang.Throwable -> L44
            if (r1 == 0) goto L14
            r1.close()     // Catch: java.io.IOException -> L14
        L14:
            return r0
        L15:
            r0 = move-exception
            goto L1e
        L17:
            r7 = move-exception
            r1 = r0
            goto L45
        L1a:
            r1 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
        L1e:
            java.lang.String r2 = "ThumbStreamOpener"
            r3 = 3
            boolean r2 = android.util.Log.isLoggable(r2, r3)     // Catch: java.lang.Throwable -> L44
            if (r2 == 0) goto L3d
            java.lang.String r2 = "ThumbStreamOpener"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L44
            r3.<init>()     // Catch: java.lang.Throwable -> L44
            java.lang.String r4 = "Failed to open uri: "
            r3.append(r4)     // Catch: java.lang.Throwable -> L44
            r3.append(r7)     // Catch: java.lang.Throwable -> L44
            java.lang.String r7 = r3.toString()     // Catch: java.lang.Throwable -> L44
            android.util.Log.d(r2, r7, r0)     // Catch: java.lang.Throwable -> L44
        L3d:
            if (r1 == 0) goto L42
            r1.close()     // Catch: java.io.IOException -> L42
        L42:
            r7 = -1
            return r7
        L44:
            r7 = move-exception
        L45:
            if (r1 == 0) goto L4a
            r1.close()     // Catch: java.io.IOException -> L4a
        L4a:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.data.mediastore.ThumbnailStreamOpener.getOrientation(android.net.Uri):int");
    }

    public InputStream open(Uri uri) throws FileNotFoundException {
        String path = getPath(uri);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = this.service.get(path);
        if (!isValid(file)) {
            return null;
        }
        Uri uriFromFile = Uri.fromFile(file);
        try {
            return this.contentResolver.openInputStream(uriFromFile);
        } catch (NullPointerException e) {
            throw ((FileNotFoundException) new FileNotFoundException("NPE opening uri: " + uri + " -> " + uriFromFile).initCause(e));
        }
    }

    @Nullable
    private String getPath(@NonNull Uri uri) {
        Cursor cursorQuery = this.query.query(uri);
        if (cursorQuery != null) {
            try {
                if (cursorQuery.moveToFirst()) {
                    return cursorQuery.getString(0);
                }
            } finally {
                if (cursorQuery != null) {
                    cursorQuery.close();
                }
            }
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return null;
    }

    private boolean isValid(File file) {
        return this.service.exists(file) && 0 < this.service.length(file);
    }
}
