package org.apache.commons.fileupload;

import com.litesuits.http.data.Consts;
import com.pzj.ipcdemo.utils.DatabaseUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.commons.fileupload.util.Closeable;
import org.apache.commons.fileupload.util.FileItemHeadersImpl;
import org.apache.commons.fileupload.util.LimitedInputStream;
import org.apache.commons.fileupload.util.Streams;

/* loaded from: classes.dex */
public abstract class FileUploadBase {
    public static final String ATTACHMENT = "attachment";
    public static final String CONTENT_DISPOSITION = "Content-disposition";
    public static final String CONTENT_LENGTH = "Content-length";
    public static final String CONTENT_TYPE = "Content-type";
    public static final String FORM_DATA = "form-data";

    @Deprecated
    public static final int MAX_HEADER_SIZE = 1024;
    public static final String MULTIPART = "multipart/";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    public static final String MULTIPART_MIXED = "multipart/mixed";
    private String headerEncoding;
    private ProgressListener listener;
    private long sizeMax = -1;
    private long fileSizeMax = -1;

    public abstract FileItemFactory getFileItemFactory();

    public abstract void setFileItemFactory(FileItemFactory fileItemFactory);

    public static final boolean isMultipartContent(RequestContext requestContext) {
        String contentType = requestContext.getContentType();
        return contentType != null && contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART);
    }

    public long getSizeMax() {
        return this.sizeMax;
    }

    public void setSizeMax(long j) {
        this.sizeMax = j;
    }

    public long getFileSizeMax() {
        return this.fileSizeMax;
    }

    public void setFileSizeMax(long j) {
        this.fileSizeMax = j;
    }

    public String getHeaderEncoding() {
        return this.headerEncoding;
    }

    public void setHeaderEncoding(String str) {
        this.headerEncoding = str;
    }

    public FileItemIterator getItemIterator(RequestContext requestContext) throws FileUploadException, IOException {
        try {
            return new FileItemIteratorImpl(requestContext);
        } catch (FileUploadIOException e) {
            throw ((FileUploadException) e.getCause());
        }
    }

    public List<FileItem> parseRequest(RequestContext requestContext) throws FileUploadException {
        ArrayList arrayList = new ArrayList();
        try {
            try {
                FileItemIterator itemIterator = getItemIterator(requestContext);
                FileItemFactory fileItemFactory = getFileItemFactory();
                if (fileItemFactory == null) {
                    throw new NullPointerException("No FileItemFactory has been set.");
                }
                while (itemIterator.hasNext()) {
                    FileItemStream next = itemIterator.next();
                    FileItem fileItemCreateItem = fileItemFactory.createItem(next.getFieldName(), next.getContentType(), next.isFormField(), ((FileItemIteratorImpl.FileItemStreamImpl) next).name);
                    arrayList.add(fileItemCreateItem);
                    try {
                        Streams.copy(next.openStream(), fileItemCreateItem.getOutputStream(), true);
                        fileItemCreateItem.setHeaders(next.getHeaders());
                    } catch (FileUploadIOException e) {
                        throw ((FileUploadException) e.getCause());
                    } catch (IOException e2) {
                        throw new IOFileUploadException(String.format("Processing of %s request failed. %s", "multipart/form-data", e2.getMessage()), e2);
                    }
                }
                return arrayList;
            } catch (FileUploadIOException e3) {
                throw ((FileUploadException) e3.getCause());
            } catch (IOException e4) {
                throw new FileUploadException(e4.getMessage(), e4);
            }
        } catch (Throwable th) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    ((FileItem) it.next()).delete();
                } catch (Throwable unused) {
                }
            }
            throw th;
        }
    }

    public Map<String, List<FileItem>> parseParameterMap(RequestContext requestContext) throws FileUploadException {
        List<FileItem> request = parseRequest(requestContext);
        HashMap map = new HashMap(request.size());
        for (FileItem fileItem : request) {
            String fieldName = fileItem.getFieldName();
            List arrayList = (List) map.get(fieldName);
            if (arrayList == null) {
                arrayList = new ArrayList();
                map.put(fieldName, arrayList);
            }
            arrayList.add(fileItem);
        }
        return map;
    }

    protected byte[] getBoundary(String str) {
        ParameterParser parameterParser = new ParameterParser();
        parameterParser.setLowerCaseNames(true);
        String str2 = parameterParser.parse(str, new char[]{';', ','}).get("boundary");
        if (str2 == null) {
            return null;
        }
        try {
            return str2.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException unused) {
            return str2.getBytes();
        }
    }

    @Deprecated
    protected String getFileName(Map<String, String> map) {
        return getFileName(getHeader(map, CONTENT_DISPOSITION));
    }

    protected String getFileName(FileItemHeaders fileItemHeaders) {
        return getFileName(fileItemHeaders.getHeader(CONTENT_DISPOSITION));
    }

    private String getFileName(String str) {
        if (str != null) {
            String lowerCase = str.toLowerCase(Locale.ENGLISH);
            if (lowerCase.startsWith(FORM_DATA) || lowerCase.startsWith(ATTACHMENT)) {
                ParameterParser parameterParser = new ParameterParser();
                parameterParser.setLowerCaseNames(true);
                Map<String, String> map = parameterParser.parse(str, ';');
                if (map.containsKey("filename")) {
                    String str2 = map.get("filename");
                    return str2 != null ? str2.trim() : "";
                }
            }
        }
        return null;
    }

    protected String getFieldName(FileItemHeaders fileItemHeaders) {
        return getFieldName(fileItemHeaders.getHeader(CONTENT_DISPOSITION));
    }

    private String getFieldName(String str) {
        if (str == null || !str.toLowerCase(Locale.ENGLISH).startsWith(FORM_DATA)) {
            return null;
        }
        ParameterParser parameterParser = new ParameterParser();
        parameterParser.setLowerCaseNames(true);
        String str2 = parameterParser.parse(str, ';').get(DatabaseUtil.KEY_NAME);
        return str2 != null ? str2.trim() : str2;
    }

    @Deprecated
    protected String getFieldName(Map<String, String> map) {
        return getFieldName(getHeader(map, CONTENT_DISPOSITION));
    }

    @Deprecated
    protected FileItem createItem(Map<String, String> map, boolean z) throws FileUploadException {
        return getFileItemFactory().createItem(getFieldName(map), getHeader(map, CONTENT_TYPE), z, getFileName(map));
    }

    protected FileItemHeaders getParsedHeaders(String str) {
        int length = str.length();
        FileItemHeadersImpl fileItemHeadersImplNewFileItemHeaders = newFileItemHeaders();
        int i = 0;
        while (true) {
            int endOfLine = parseEndOfLine(str, i);
            if (i == endOfLine) {
                return fileItemHeadersImplNewFileItemHeaders;
            }
            StringBuilder sb = new StringBuilder(str.substring(i, endOfLine));
            i = endOfLine + 2;
            while (i < length) {
                int i2 = i;
                while (i2 < length) {
                    char cCharAt = str.charAt(i2);
                    if (cCharAt != ' ' && cCharAt != '\t') {
                        break;
                    }
                    i2++;
                }
                if (i2 == i) {
                    break;
                }
                int endOfLine2 = parseEndOfLine(str, i2);
                sb.append(" ");
                sb.append(str.substring(i2, endOfLine2));
                i = endOfLine2 + 2;
            }
            parseHeaderLine(fileItemHeadersImplNewFileItemHeaders, sb.toString());
        }
    }

    protected FileItemHeadersImpl newFileItemHeaders() {
        return new FileItemHeadersImpl();
    }

    @Deprecated
    protected Map<String, String> parseHeaders(String str) {
        FileItemHeaders parsedHeaders = getParsedHeaders(str);
        HashMap map = new HashMap();
        Iterator<String> headerNames = parsedHeaders.getHeaderNames();
        while (headerNames.hasNext()) {
            String next = headerNames.next();
            Iterator<String> headers = parsedHeaders.getHeaders(next);
            StringBuilder sb = new StringBuilder(headers.next());
            while (headers.hasNext()) {
                sb.append(Consts.SECOND_LEVEL_SPLIT);
                sb.append(headers.next());
            }
            map.put(next, sb.toString());
        }
        return map;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0023, code lost:
    
        throw new java.lang.IllegalStateException("Expected headers to be terminated by an empty line.");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int parseEndOfLine(java.lang.String r4, int r5) {
        /*
            r3 = this;
        L0:
            r0 = 13
            int r5 = r4.indexOf(r0, r5)
            r0 = -1
            if (r5 == r0) goto L1c
            int r0 = r5 + 1
            int r1 = r4.length()
            if (r0 >= r1) goto L1c
            char r1 = r4.charAt(r0)
            r2 = 10
            if (r1 != r2) goto L1a
            return r5
        L1a:
            r5 = r0
            goto L0
        L1c:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "Expected headers to be terminated by an empty line."
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.fileupload.FileUploadBase.parseEndOfLine(java.lang.String, int):int");
    }

    private void parseHeaderLine(FileItemHeadersImpl fileItemHeadersImpl, String str) {
        int iIndexOf = str.indexOf(58);
        if (iIndexOf == -1) {
            return;
        }
        fileItemHeadersImpl.addHeader(str.substring(0, iIndexOf).trim(), str.substring(str.indexOf(58) + 1).trim());
    }

    @Deprecated
    protected final String getHeader(Map<String, String> map, String str) {
        return map.get(str.toLowerCase(Locale.ENGLISH));
    }

    private class FileItemIteratorImpl implements FileItemIterator {
        private final byte[] boundary;
        private String currentFieldName;
        private FileItemStreamImpl currentItem;
        private boolean eof;
        private boolean itemValid;
        private final MultipartStream multi;
        private final MultipartStream.ProgressNotifier notifier;
        private boolean skipPreamble;

        class FileItemStreamImpl implements FileItemStream {
            private final String contentType;
            private final String fieldName;
            private final boolean formField;
            private FileItemHeaders headers;
            private final String name;
            private boolean opened;
            private final InputStream stream;

            FileItemStreamImpl(String str, String str2, String str3, boolean z, long j) throws IOException {
                InputStream inputStream;
                this.name = str;
                this.fieldName = str2;
                this.contentType = str3;
                this.formField = z;
                final MultipartStream.ItemInputStream itemInputStreamNewInputStream = FileItemIteratorImpl.this.multi.newInputStream();
                if (FileUploadBase.this.fileSizeMax == -1) {
                    inputStream = itemInputStreamNewInputStream;
                } else {
                    if (j != -1 && j > FileUploadBase.this.fileSizeMax) {
                        FileSizeLimitExceededException fileSizeLimitExceededException = new FileSizeLimitExceededException(String.format("The field %s exceeds its maximum permitted size of %s bytes.", this.fieldName, Long.valueOf(FileUploadBase.this.fileSizeMax)), j, FileUploadBase.this.fileSizeMax);
                        fileSizeLimitExceededException.setFileName(str);
                        fileSizeLimitExceededException.setFieldName(str2);
                        throw new FileUploadIOException(fileSizeLimitExceededException);
                    }
                    inputStream = new LimitedInputStream(itemInputStreamNewInputStream, FileUploadBase.this.fileSizeMax) { // from class: org.apache.commons.fileupload.FileUploadBase.FileItemIteratorImpl.FileItemStreamImpl.1
                        @Override // org.apache.commons.fileupload.util.LimitedInputStream
                        protected void raiseError(long j2, long j3) throws IOException {
                            itemInputStreamNewInputStream.close(true);
                            FileSizeLimitExceededException fileSizeLimitExceededException2 = new FileSizeLimitExceededException(String.format("The field %s exceeds its maximum permitted size of %s bytes.", FileItemStreamImpl.this.fieldName, Long.valueOf(j2)), j3, j2);
                            fileSizeLimitExceededException2.setFieldName(FileItemStreamImpl.this.fieldName);
                            fileSizeLimitExceededException2.setFileName(FileItemStreamImpl.this.name);
                            throw new FileUploadIOException(fileSizeLimitExceededException2);
                        }
                    };
                }
                this.stream = inputStream;
            }

            @Override // org.apache.commons.fileupload.FileItemStream
            public String getContentType() {
                return this.contentType;
            }

            @Override // org.apache.commons.fileupload.FileItemStream
            public String getFieldName() {
                return this.fieldName;
            }

            @Override // org.apache.commons.fileupload.FileItemStream
            public String getName() {
                return Streams.checkFileName(this.name);
            }

            @Override // org.apache.commons.fileupload.FileItemStream
            public boolean isFormField() {
                return this.formField;
            }

            @Override // org.apache.commons.fileupload.FileItemStream
            public InputStream openStream() throws IOException {
                if (this.opened) {
                    throw new IllegalStateException("The stream was already opened.");
                }
                if (((Closeable) this.stream).isClosed()) {
                    throw new FileItemStream.ItemSkippedException();
                }
                return this.stream;
            }

            void close() throws IOException {
                this.stream.close();
            }

            @Override // org.apache.commons.fileupload.FileItemHeadersSupport
            public FileItemHeaders getHeaders() {
                return this.headers;
            }

            @Override // org.apache.commons.fileupload.FileItemHeadersSupport
            public void setHeaders(FileItemHeaders fileItemHeaders) {
                this.headers = fileItemHeaders;
            }
        }

        FileItemIteratorImpl(RequestContext requestContext) throws IOException, FileUploadException {
            InputStream inputStream;
            if (requestContext == null) {
                throw new NullPointerException("ctx parameter");
            }
            String contentType = requestContext.getContentType();
            if (contentType == null || !contentType.toLowerCase(Locale.ENGLISH).startsWith(FileUploadBase.MULTIPART)) {
                throw new InvalidContentTypeException(String.format("the request doesn't contain a %s or %s stream, content type header is %s", "multipart/form-data", FileUploadBase.MULTIPART_MIXED, contentType));
            }
            InputStream inputStream2 = requestContext.getInputStream();
            long jContentLength = UploadContext.class.isAssignableFrom(requestContext.getClass()) ? ((UploadContext) requestContext).contentLength() : requestContext.getContentLength();
            if (FileUploadBase.this.sizeMax < 0) {
                inputStream = inputStream2;
            } else {
                if (jContentLength != -1 && jContentLength > FileUploadBase.this.sizeMax) {
                    throw new SizeLimitExceededException(String.format("the request was rejected because its size (%s) exceeds the configured maximum (%s)", Long.valueOf(jContentLength), Long.valueOf(FileUploadBase.this.sizeMax)), jContentLength, FileUploadBase.this.sizeMax);
                }
                inputStream = new LimitedInputStream(inputStream2, FileUploadBase.this.sizeMax) { // from class: org.apache.commons.fileupload.FileUploadBase.FileItemIteratorImpl.1
                    @Override // org.apache.commons.fileupload.util.LimitedInputStream
                    protected void raiseError(long j, long j2) throws IOException {
                        throw new FileUploadIOException(new SizeLimitExceededException(String.format("the request was rejected because its size (%s) exceeds the configured maximum (%s)", Long.valueOf(j2), Long.valueOf(j)), j2, j));
                    }
                };
            }
            String characterEncoding = FileUploadBase.this.headerEncoding;
            characterEncoding = characterEncoding == null ? requestContext.getCharacterEncoding() : characterEncoding;
            this.boundary = FileUploadBase.this.getBoundary(contentType);
            if (this.boundary == null) {
                throw new FileUploadException("the request was rejected because no multipart boundary was found");
            }
            this.notifier = new MultipartStream.ProgressNotifier(FileUploadBase.this.listener, jContentLength);
            try {
                this.multi = new MultipartStream(inputStream, this.boundary, this.notifier);
                this.multi.setHeaderEncoding(characterEncoding);
                this.skipPreamble = true;
                findNextItem();
            } catch (IllegalArgumentException e) {
                throw new InvalidContentTypeException(String.format("The boundary specified in the %s header is too long", FileUploadBase.CONTENT_TYPE), e);
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:28:0x0075, code lost:
        
            r7 = r13.this$0.getFileName(r0);
            r9 = r0.getHeader(org.apache.commons.fileupload.FileUploadBase.CONTENT_TYPE);
         */
        /* JADX WARN: Code restructure failed: missing block: B:29:0x0083, code lost:
        
            if (r7 != null) goto L31;
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x0085, code lost:
        
            r10 = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:31:0x0087, code lost:
        
            r10 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x0088, code lost:
        
            r13.currentItem = new org.apache.commons.fileupload.FileUploadBase.FileItemIteratorImpl.FileItemStreamImpl(r13, r7, r8, r9, r10, getContentLength(r0));
            r13.currentItem.setHeaders(r0);
            r13.notifier.noteItem();
            r13.itemValid = true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x009f, code lost:
        
            return true;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private boolean findNextItem() throws java.io.IOException {
            /*
                r13 = this;
                boolean r0 = r13.eof
                r1 = 0
                if (r0 == 0) goto L6
                return r1
            L6:
                org.apache.commons.fileupload.FileUploadBase$FileItemIteratorImpl$FileItemStreamImpl r0 = r13.currentItem
                r2 = 0
                if (r0 == 0) goto L12
                org.apache.commons.fileupload.FileUploadBase$FileItemIteratorImpl$FileItemStreamImpl r0 = r13.currentItem
                r0.close()
                r13.currentItem = r2
            L12:
                boolean r0 = r13.skipPreamble
                if (r0 == 0) goto L1d
                org.apache.commons.fileupload.MultipartStream r0 = r13.multi
                boolean r0 = r0.skipPreamble()
                goto L23
            L1d:
                org.apache.commons.fileupload.MultipartStream r0 = r13.multi
                boolean r0 = r0.readBoundary()
            L23:
                r3 = 1
                if (r0 != 0) goto L37
                java.lang.String r0 = r13.currentFieldName
                if (r0 != 0) goto L2d
                r13.eof = r3
                return r1
            L2d:
                org.apache.commons.fileupload.MultipartStream r0 = r13.multi
                byte[] r3 = r13.boundary
                r0.setBoundary(r3)
                r13.currentFieldName = r2
                goto L12
            L37:
                org.apache.commons.fileupload.FileUploadBase r0 = org.apache.commons.fileupload.FileUploadBase.this
                org.apache.commons.fileupload.MultipartStream r4 = r13.multi
                java.lang.String r4 = r4.readHeaders()
                org.apache.commons.fileupload.FileItemHeaders r0 = r0.getParsedHeaders(r4)
                java.lang.String r4 = r13.currentFieldName
                if (r4 != 0) goto La0
                org.apache.commons.fileupload.FileUploadBase r4 = org.apache.commons.fileupload.FileUploadBase.this
                java.lang.String r8 = r4.getFieldName(r0)
                if (r8 == 0) goto Lcb
                java.lang.String r4 = "Content-type"
                java.lang.String r4 = r0.getHeader(r4)
                if (r4 == 0) goto L75
                java.util.Locale r5 = java.util.Locale.ENGLISH
                java.lang.String r5 = r4.toLowerCase(r5)
                java.lang.String r6 = "multipart/mixed"
                boolean r5 = r5.startsWith(r6)
                if (r5 == 0) goto L75
                r13.currentFieldName = r8
                org.apache.commons.fileupload.FileUploadBase r0 = org.apache.commons.fileupload.FileUploadBase.this
                byte[] r0 = r0.getBoundary(r4)
                org.apache.commons.fileupload.MultipartStream r4 = r13.multi
                r4.setBoundary(r0)
                r13.skipPreamble = r3
                goto L12
            L75:
                org.apache.commons.fileupload.FileUploadBase r2 = org.apache.commons.fileupload.FileUploadBase.this
                java.lang.String r7 = r2.getFileName(r0)
                org.apache.commons.fileupload.FileUploadBase$FileItemIteratorImpl$FileItemStreamImpl r2 = new org.apache.commons.fileupload.FileUploadBase$FileItemIteratorImpl$FileItemStreamImpl
                java.lang.String r4 = "Content-type"
                java.lang.String r9 = r0.getHeader(r4)
                if (r7 != 0) goto L87
                r10 = 1
                goto L88
            L87:
                r10 = 0
            L88:
                long r11 = r13.getContentLength(r0)
                r5 = r2
                r6 = r13
                r5.<init>(r7, r8, r9, r10, r11)
                r13.currentItem = r2
                org.apache.commons.fileupload.FileUploadBase$FileItemIteratorImpl$FileItemStreamImpl r1 = r13.currentItem
                r1.setHeaders(r0)
                org.apache.commons.fileupload.MultipartStream$ProgressNotifier r0 = r13.notifier
                r0.noteItem()
                r13.itemValid = r3
                return r3
            La0:
                org.apache.commons.fileupload.FileUploadBase r4 = org.apache.commons.fileupload.FileUploadBase.this
                java.lang.String r7 = r4.getFileName(r0)
                if (r7 == 0) goto Lcb
                org.apache.commons.fileupload.FileUploadBase$FileItemIteratorImpl$FileItemStreamImpl r1 = new org.apache.commons.fileupload.FileUploadBase$FileItemIteratorImpl$FileItemStreamImpl
                java.lang.String r8 = r13.currentFieldName
                java.lang.String r2 = "Content-type"
                java.lang.String r9 = r0.getHeader(r2)
                r10 = 0
                long r11 = r13.getContentLength(r0)
                r5 = r1
                r6 = r13
                r5.<init>(r7, r8, r9, r10, r11)
                r13.currentItem = r1
                org.apache.commons.fileupload.FileUploadBase$FileItemIteratorImpl$FileItemStreamImpl r1 = r13.currentItem
                r1.setHeaders(r0)
                org.apache.commons.fileupload.MultipartStream$ProgressNotifier r0 = r13.notifier
                r0.noteItem()
                r13.itemValid = r3
                return r3
            Lcb:
                org.apache.commons.fileupload.MultipartStream r0 = r13.multi
                r0.discardBodyData()
                goto L12
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.fileupload.FileUploadBase.FileItemIteratorImpl.findNextItem():boolean");
        }

        private long getContentLength(FileItemHeaders fileItemHeaders) {
            try {
                return Long.parseLong(fileItemHeaders.getHeader(FileUploadBase.CONTENT_LENGTH));
            } catch (Exception unused) {
                return -1L;
            }
        }

        @Override // org.apache.commons.fileupload.FileItemIterator
        public boolean hasNext() throws FileUploadException, IOException {
            if (this.eof) {
                return false;
            }
            if (this.itemValid) {
                return true;
            }
            try {
                return findNextItem();
            } catch (FileUploadIOException e) {
                throw ((FileUploadException) e.getCause());
            }
        }

        @Override // org.apache.commons.fileupload.FileItemIterator
        public FileItemStream next() throws FileUploadException, IOException {
            if (this.eof || (!this.itemValid && !hasNext())) {
                throw new NoSuchElementException();
            }
            this.itemValid = false;
            return this.currentItem;
        }
    }

    public static class FileUploadIOException extends IOException {
        private static final long serialVersionUID = -7047616958165584154L;
        private final FileUploadException cause;

        public FileUploadIOException(FileUploadException fileUploadException) {
            this.cause = fileUploadException;
        }

        @Override // java.lang.Throwable
        public Throwable getCause() {
            return this.cause;
        }
    }

    public static class InvalidContentTypeException extends FileUploadException {
        private static final long serialVersionUID = -9073026332015646668L;

        public InvalidContentTypeException() {
        }

        public InvalidContentTypeException(String str) {
            super(str);
        }

        public InvalidContentTypeException(String str, Throwable th) {
            super(str, th);
        }
    }

    public static class IOFileUploadException extends FileUploadException {
        private static final long serialVersionUID = 1749796615868477269L;
        private final IOException cause;

        public IOFileUploadException(String str, IOException iOException) {
            super(str);
            this.cause = iOException;
        }

        @Override // org.apache.commons.fileupload.FileUploadException, java.lang.Throwable
        public Throwable getCause() {
            return this.cause;
        }
    }

    protected static abstract class SizeException extends FileUploadException {
        private static final long serialVersionUID = -8776225574705254126L;
        private final long actual;
        private final long permitted;

        protected SizeException(String str, long j, long j2) {
            super(str);
            this.actual = j;
            this.permitted = j2;
        }

        public long getActualSize() {
            return this.actual;
        }

        public long getPermittedSize() {
            return this.permitted;
        }
    }

    @Deprecated
    public static class UnknownSizeException extends FileUploadException {
        private static final long serialVersionUID = 7062279004812015273L;

        public UnknownSizeException() {
        }

        public UnknownSizeException(String str) {
            super(str);
        }
    }

    public static class SizeLimitExceededException extends SizeException {
        private static final long serialVersionUID = -2474893167098052828L;

        @Override // org.apache.commons.fileupload.FileUploadBase.SizeException
        public /* bridge */ /* synthetic */ long getActualSize() {
            return super.getActualSize();
        }

        @Override // org.apache.commons.fileupload.FileUploadBase.SizeException
        public /* bridge */ /* synthetic */ long getPermittedSize() {
            return super.getPermittedSize();
        }

        @Deprecated
        public SizeLimitExceededException() {
            this(null, 0L, 0L);
        }

        @Deprecated
        public SizeLimitExceededException(String str) {
            this(str, 0L, 0L);
        }

        public SizeLimitExceededException(String str, long j, long j2) {
            super(str, j, j2);
        }
    }

    public static class FileSizeLimitExceededException extends SizeException {
        private static final long serialVersionUID = 8150776562029630058L;
        private String fieldName;
        private String fileName;

        @Override // org.apache.commons.fileupload.FileUploadBase.SizeException
        public /* bridge */ /* synthetic */ long getActualSize() {
            return super.getActualSize();
        }

        @Override // org.apache.commons.fileupload.FileUploadBase.SizeException
        public /* bridge */ /* synthetic */ long getPermittedSize() {
            return super.getPermittedSize();
        }

        public FileSizeLimitExceededException(String str, long j, long j2) {
            super(str, j, j2);
        }

        public String getFileName() {
            return this.fileName;
        }

        public void setFileName(String str) {
            this.fileName = str;
        }

        public String getFieldName() {
            return this.fieldName;
        }

        public void setFieldName(String str) {
            this.fieldName = str;
        }
    }

    public ProgressListener getProgressListener() {
        return this.listener;
    }

    public void setProgressListener(ProgressListener progressListener) {
        this.listener = progressListener;
    }
}
