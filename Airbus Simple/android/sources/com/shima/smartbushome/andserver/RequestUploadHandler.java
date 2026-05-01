package com.shima.smartbushome.andserver;

import android.os.Environment;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.upload.HttpFileUpload;
import com.yanzhenjie.andserver.upload.HttpUploadContext;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

/* loaded from: classes.dex */
public class RequestUploadHandler implements RequestHandler {
    @Override // com.yanzhenjie.andserver.RequestHandler
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IllegalStateException, IOException {
        if (!HttpFileUpload.isMultipartContentWithPost(httpRequest)) {
            response(403, "You must upload file.", httpResponse);
            return;
        }
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/smartbus_backup/database");
        if (file.isDirectory()) {
            try {
                if (processFileUpload(httpRequest, file)) {
                    response(200, "Upload successfully,Need to import the latest date database on your phone.", httpResponse);
                } else {
                    response(500, "The file type is incorrect.", httpResponse);
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                response(500, "Save the file when the error occurs.", httpResponse);
                return;
            }
        }
        response(500, "The server can not save the file.", httpResponse);
    }

    private void response(int i, String str, HttpResponse httpResponse) throws IllegalStateException, IOException {
        httpResponse.setStatusCode(i);
        httpResponse.setEntity(new StringEntity(str, "utf-8"));
    }

    private boolean processFileUpload(HttpRequest httpRequest, File file) throws Exception {
        for (FileItem fileItem : new HttpFileUpload(new DiskFileItemFactory(1048576, file)).parseRequest(new HttpUploadContext((HttpEntityEnclosingRequest) httpRequest))) {
            if (!fileItem.isFormField()) {
                if (!"db".equals(getExtensionName(fileItem.getName()))) {
                    return false;
                }
                fileItem.write(new File(file, "smartbus_dbbackup_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())) + ".db"));
                return true;
            }
            fileItem.getName();
            fileItem.getString();
        }
        return true;
    }

    public static String getExtensionName(String str) {
        int iLastIndexOf;
        return (str == null || str.length() <= 0 || (iLastIndexOf = str.lastIndexOf(46)) <= -1 || iLastIndexOf >= str.length() + (-1)) ? str : str.substring(iLastIndexOf + 1);
    }
}
