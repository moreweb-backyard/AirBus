package com.litesuits.http.impl.huc;

import com.litesuits.http.HttpClient;
import com.litesuits.http.HttpConfig;
import com.litesuits.http.request.AbstractRequest;
import com.litesuits.http.request.content.HttpBody;
import com.litesuits.http.request.param.HttpMethods;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/* loaded from: classes.dex */
public class HttpUrlClient implements HttpClient {
    private static final String TAG = "HttpUrlClient";
    private HostnameVerifier hostnameVerifier;
    private SSLSocketFactory sslSocketFactory;

    @Override // com.litesuits.http.HttpClient
    public void config(HttpConfig httpConfig) {
    }

    public SSLSocketFactory getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    public HttpUrlClient setSslSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.sslSocketFactory = sSLSocketFactory;
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public HttpUrlClient setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:118:0x025e A[Catch: all -> 0x0262, TRY_ENTER, TryCatch #0 {all -> 0x0262, blocks: (B:107:0x0243, B:118:0x025e, B:121:0x0264), top: B:128:0x00b7 }] */
    /* JADX WARN: Removed duplicated region for block: B:126:0x026a  */
    /* JADX WARN: Type inference failed for: r8v0 */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v3 */
    @Override // com.litesuits.http.HttpClient
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public <T> void connect(com.litesuits.http.request.AbstractRequest<T> r18, com.litesuits.http.response.InternalResponse r19) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 622
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.litesuits.http.impl.huc.HttpUrlClient.connect(com.litesuits.http.request.AbstractRequest, com.litesuits.http.response.InternalResponse):void");
    }

    private String getCharsetByContentType(String str, String str2) {
        if (str != null) {
            for (String str3 : str.split(";")) {
                String strTrim = str3.trim();
                if (strTrim.toLowerCase().startsWith("charset=")) {
                    return strTrim.substring("charset=".length());
                }
            }
        }
        return str2 == null ? "UTF-8" : str2;
    }

    private void writeDataIfNecessary(HttpURLConnection httpURLConnection, AbstractRequest<?> abstractRequest) throws IOException {
        HttpBody httpBody;
        HttpMethods method = abstractRequest.getMethod();
        if ((method == HttpMethods.Post || method == HttpMethods.Put || method == HttpMethods.Patch) && (httpBody = abstractRequest.getHttpBody()) != null) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", httpBody.getContentType());
            OutputStream outputStream = httpURLConnection.getOutputStream();
            httpBody.writeTo(outputStream);
            outputStream.close();
        }
    }

    public static void trustAllCertificate() throws NoSuchAlgorithmException, KeyManagementException {
        try {
            TrustManager[] trustManagerArr = {new X509TrustManager() { // from class: com.litesuits.http.impl.huc.HttpUrlClient.1
                @Override // javax.net.ssl.X509TrustManager
                public void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
                }

                @Override // javax.net.ssl.X509TrustManager
                public void checkServerTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
                }

                @Override // javax.net.ssl.X509TrustManager
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
            SSLContext sSLContext = SSLContext.getInstance("TLS");
            sSLContext.init(null, trustManagerArr, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sSLContext.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HostnameVerifier trustAllVerifier() {
        return new HostnameVerifier() { // from class: com.litesuits.http.impl.huc.HttpUrlClient.2
            @Override // javax.net.ssl.HostnameVerifier
            public boolean verify(String str, SSLSession sSLSession) {
                return true;
            }
        };
    }
}
