package cz.msebera.android.httpclient.entity.mime;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.content.ByteArrayBody;
import cz.msebera.android.httpclient.entity.mime.content.ContentBody;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;
import cz.msebera.android.httpclient.entity.mime.content.InputStreamBody;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.Args;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.fileupload.FileUploadBase;

/* loaded from: classes.dex */
public class MultipartEntityBuilder {
    private static final String DEFAULT_SUBTYPE = "form-data";
    private static final char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private ContentType contentType;
    private HttpMultipartMode mode = HttpMultipartMode.STRICT;
    private String boundary = null;
    private Charset charset = null;
    private List<FormBodyPart> bodyParts = null;

    public static MultipartEntityBuilder create() {
        return new MultipartEntityBuilder();
    }

    MultipartEntityBuilder() {
    }

    public MultipartEntityBuilder setMode(HttpMultipartMode httpMultipartMode) {
        this.mode = httpMultipartMode;
        return this;
    }

    public MultipartEntityBuilder setLaxMode() {
        this.mode = HttpMultipartMode.BROWSER_COMPATIBLE;
        return this;
    }

    public MultipartEntityBuilder setStrictMode() {
        this.mode = HttpMultipartMode.STRICT;
        return this;
    }

    public MultipartEntityBuilder setBoundary(String str) {
        this.boundary = str;
        return this;
    }

    public MultipartEntityBuilder setMimeSubtype(String str) {
        Args.notBlank(str, "MIME subtype");
        this.contentType = ContentType.create(FileUploadBase.MULTIPART + str);
        return this;
    }

    public MultipartEntityBuilder seContentType(ContentType contentType) {
        Args.notNull(contentType, "Content type");
        this.contentType = contentType;
        return this;
    }

    public MultipartEntityBuilder setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public MultipartEntityBuilder addPart(FormBodyPart formBodyPart) {
        if (formBodyPart == null) {
            return this;
        }
        if (this.bodyParts == null) {
            this.bodyParts = new ArrayList();
        }
        this.bodyParts.add(formBodyPart);
        return this;
    }

    public MultipartEntityBuilder addPart(String str, ContentBody contentBody) {
        Args.notNull(str, "Name");
        Args.notNull(contentBody, "Content body");
        return addPart(FormBodyPartBuilder.create(str, contentBody).build());
    }

    public MultipartEntityBuilder addTextBody(String str, String str2, ContentType contentType) {
        return addPart(str, new StringBody(str2, contentType));
    }

    public MultipartEntityBuilder addTextBody(String str, String str2) {
        return addTextBody(str, str2, ContentType.DEFAULT_TEXT);
    }

    public MultipartEntityBuilder addBinaryBody(String str, byte[] bArr, ContentType contentType, String str2) {
        return addPart(str, new ByteArrayBody(bArr, contentType, str2));
    }

    public MultipartEntityBuilder addBinaryBody(String str, byte[] bArr) {
        return addBinaryBody(str, bArr, ContentType.DEFAULT_BINARY, (String) null);
    }

    public MultipartEntityBuilder addBinaryBody(String str, File file, ContentType contentType, String str2) {
        return addPart(str, new FileBody(file, contentType, str2));
    }

    public MultipartEntityBuilder addBinaryBody(String str, File file) {
        return addBinaryBody(str, file, ContentType.DEFAULT_BINARY, file != null ? file.getName() : null);
    }

    public MultipartEntityBuilder addBinaryBody(String str, InputStream inputStream, ContentType contentType, String str2) {
        return addPart(str, new InputStreamBody(inputStream, contentType, str2));
    }

    public MultipartEntityBuilder addBinaryBody(String str, InputStream inputStream) {
        return addBinaryBody(str, inputStream, ContentType.DEFAULT_BINARY, (String) null);
    }

    private String generateBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int iNextInt = random.nextInt(11) + 30;
        for (int i = 0; i < iNextInt; i++) {
            sb.append(MULTIPART_CHARS[random.nextInt(MULTIPART_CHARS.length)]);
        }
        return sb.toString();
    }

    MultipartFormEntity buildEntity() throws UnsupportedCharsetException {
        ContentType contentTypeCreate;
        AbstractMultipartForm httpBrowserCompatibleMultipart;
        String strGenerateBoundary = this.boundary;
        if (strGenerateBoundary == null && this.contentType != null) {
            strGenerateBoundary = this.contentType.getParameter("boundary");
        }
        if (strGenerateBoundary == null) {
            strGenerateBoundary = generateBoundary();
        }
        Charset charset = this.charset;
        if (charset == null && this.contentType != null) {
            charset = this.contentType.getCharset();
        }
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(new BasicNameValuePair("boundary", strGenerateBoundary));
        if (charset != null) {
            arrayList.add(new BasicNameValuePair("charset", charset.name()));
        }
        NameValuePair[] nameValuePairArr = (NameValuePair[]) arrayList.toArray(new NameValuePair[arrayList.size()]);
        if (this.contentType != null) {
            contentTypeCreate = this.contentType.withParameters(nameValuePairArr);
        } else {
            contentTypeCreate = ContentType.create("multipart/form-data", nameValuePairArr);
        }
        List arrayList2 = this.bodyParts != null ? new ArrayList(this.bodyParts) : Collections.emptyList();
        switch (this.mode != null ? this.mode : HttpMultipartMode.STRICT) {
            case BROWSER_COMPATIBLE:
                httpBrowserCompatibleMultipart = new HttpBrowserCompatibleMultipart(charset, strGenerateBoundary, arrayList2);
                break;
            case RFC6532:
                httpBrowserCompatibleMultipart = new HttpRFC6532Multipart(charset, strGenerateBoundary, arrayList2);
                break;
            default:
                httpBrowserCompatibleMultipart = new HttpStrictMultipart(charset, strGenerateBoundary, arrayList2);
                break;
        }
        return new MultipartFormEntity(httpBrowserCompatibleMultipart, contentTypeCreate, httpBrowserCompatibleMultipart.getTotalLength());
    }

    public HttpEntity build() {
        return buildEntity();
    }
}
