package org.apache.commons.fileupload;

/* loaded from: classes.dex */
public class FileUpload extends FileUploadBase {
    private FileItemFactory fileItemFactory;

    public FileUpload() {
    }

    public FileUpload(FileItemFactory fileItemFactory) {
        this.fileItemFactory = fileItemFactory;
    }

    @Override // org.apache.commons.fileupload.FileUploadBase
    public FileItemFactory getFileItemFactory() {
        return this.fileItemFactory;
    }

    @Override // org.apache.commons.fileupload.FileUploadBase
    public void setFileItemFactory(FileItemFactory fileItemFactory) {
        this.fileItemFactory = fileItemFactory;
    }
}
