package org.apache.commons.fileupload;

import java.io.File;

@Deprecated
/* loaded from: classes.dex */
public class DiskFileUpload extends FileUploadBase {
    private DefaultFileItemFactory fileItemFactory;

    @Deprecated
    public DiskFileUpload() {
        this.fileItemFactory = new DefaultFileItemFactory();
    }

    @Deprecated
    public DiskFileUpload(DefaultFileItemFactory defaultFileItemFactory) {
        this.fileItemFactory = defaultFileItemFactory;
    }

    @Override // org.apache.commons.fileupload.FileUploadBase
    @Deprecated
    public FileItemFactory getFileItemFactory() {
        return this.fileItemFactory;
    }

    @Override // org.apache.commons.fileupload.FileUploadBase
    @Deprecated
    public void setFileItemFactory(FileItemFactory fileItemFactory) {
        this.fileItemFactory = (DefaultFileItemFactory) fileItemFactory;
    }

    @Deprecated
    public int getSizeThreshold() {
        return this.fileItemFactory.getSizeThreshold();
    }

    @Deprecated
    public void setSizeThreshold(int i) {
        this.fileItemFactory.setSizeThreshold(i);
    }

    @Deprecated
    public String getRepositoryPath() {
        return this.fileItemFactory.getRepository().getPath();
    }

    @Deprecated
    public void setRepositoryPath(String str) {
        this.fileItemFactory.setRepository(new File(str));
    }
}
