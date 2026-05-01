package com.shima.smartbushome.assist.CrashHandle;

import android.os.Environment;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;

/* loaded from: classes.dex */
public final class FileUtil {
    public static final String FILE_EXTENSION_SEPARATOR = ".";
    public static final String SEP = File.separator;
    public static final String SDPATH = Environment.getExternalStorageDirectory() + File.separator;

    private FileUtil() {
        throw new Error("￣﹏￣");
    }

    public static boolean hasSdcard() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static String readFile(String str) throws IOException {
        return readFile(str, "utf-8");
    }

    public static String readFile(String str, String str2) throws Throwable {
        BufferedReader bufferedReader;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = "utf-8";
        }
        File file = new File(str);
        StringBuilder sb = new StringBuilder("");
        if (!file.isFile()) {
            return null;
        }
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), str2));
            while (true) {
                try {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (!sb.toString().equals("")) {
                        sb.append(IOUtils.LINE_SEPARATOR_WINDOWS);
                    }
                    sb.append(line);
                } catch (Throwable th) {
                    th = th;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
            String string = sb.toString();
            try {
                bufferedReader.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            return string;
        } catch (Throwable th2) {
            th = th2;
            bufferedReader = null;
        }
    }

    public static List<String> readFileToList(String str) throws IOException {
        return readFileToList(str, "utf-8");
    }

    public static List<String> readFileToList(String str, String str2) throws Throwable {
        BufferedReader bufferedReader = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (TextUtils.isEmpty(str2)) {
            str2 = "utf-8";
        }
        File file = new File(str);
        ArrayList arrayList = new ArrayList();
        if (!file.isFile()) {
            return null;
        }
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file), str2));
            while (true) {
                try {
                    String line = bufferedReader2.readLine();
                    if (line != null) {
                        arrayList.add(line);
                    } else {
                        try {
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    throw th;
                }
            }
            bufferedReader2.close();
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static boolean writeFile(String str, String str2, boolean z) throws Throwable {
        FileWriter fileWriter;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        FileWriter fileWriter2 = null;
        try {
            createFile(str);
            fileWriter = new FileWriter(str, z);
        } catch (Throwable th) {
            th = th;
        }
        try {
            fileWriter.write(str2);
            fileWriter.flush();
            try {
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } catch (Throwable th2) {
            th = th2;
            fileWriter2 = fileWriter;
            if (fileWriter2 != null) {
                try {
                    fileWriter2.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            throw th;
        }
    }

    public static boolean writeFile(String str, InputStream inputStream) throws IOException {
        return writeFile(str, inputStream, false);
    }

    public static boolean writeFile(String str, InputStream inputStream, boolean z) throws IOException {
        if (TextUtils.isEmpty(str)) {
            throw new NullPointerException("filePath is Empty");
        }
        if (inputStream == null) {
            throw new NullPointerException("InputStream is null");
        }
        return writeFile(new File(str), inputStream, z);
    }

    public static boolean writeFile(File file, InputStream inputStream) throws IOException {
        return writeFile(file, inputStream, false);
    }

    public static boolean writeFile(File file, InputStream inputStream, boolean z) throws Throwable {
        if (file == null) {
            throw new NullPointerException("file = null");
        }
        FileOutputStream fileOutputStream = null;
        try {
            createFile(file.getAbsolutePath());
            FileOutputStream fileOutputStream2 = new FileOutputStream(file, z);
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int i = inputStream.read(bArr);
                    if (i == -1) {
                        break;
                    }
                    fileOutputStream2.write(bArr, 0, i);
                }
                fileOutputStream2.flush();
                try {
                    fileOutputStream2.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            } catch (Throwable th) {
                th = th;
                fileOutputStream = fileOutputStream2;
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        inputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static boolean copyFile(String str, String str2) throws IOException {
        return writeFile(str2, new FileInputStream(str));
    }

    public static List<String> getFileNameList(String str, FilenameFilter filenameFilter) {
        if (filenameFilter == null) {
            return getFileNameList(str);
        }
        if (TextUtils.isEmpty(str)) {
            return Collections.emptyList();
        }
        File[] fileArrListFiles = new File(str).listFiles(filenameFilter);
        if (fileArrListFiles == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (File file : fileArrListFiles) {
            if (file.isFile()) {
                arrayList.add(file.getName());
            }
        }
        return arrayList;
    }

    public static List<String> getFileNameList(String str) {
        if (TextUtils.isEmpty(str)) {
            return Collections.emptyList();
        }
        File[] fileArrListFiles = new File(str).listFiles();
        if (fileArrListFiles == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (File file : fileArrListFiles) {
            if (file.isFile()) {
                arrayList.add(file.getName());
            }
        }
        return arrayList;
    }

    public static List<String> getFileNameList(String str, final String str2) {
        if (TextUtils.isEmpty(str)) {
            return Collections.emptyList();
        }
        File[] fileArrListFiles = new File(str).listFiles(new FilenameFilter() { // from class: com.shima.smartbushome.assist.CrashHandle.FileUtil.1
            @Override // java.io.FilenameFilter
            public boolean accept(File file, String str3) {
                StringBuilder sb = new StringBuilder();
                sb.append(FileUtil.FILE_EXTENSION_SEPARATOR);
                sb.append(str2);
                return str3.indexOf(sb.toString()) > 0;
            }
        });
        if (fileArrListFiles == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (File file : fileArrListFiles) {
            if (file.isFile()) {
                arrayList.add(file.getName());
            }
        }
        return arrayList;
    }

    public static String getFileExtension(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int iLastIndexOf = str.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        return (iLastIndexOf != -1 && str.lastIndexOf(File.separator) < iLastIndexOf) ? str.substring(iLastIndexOf + 1) : "";
    }

    public static boolean createFile(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return createFile(new File(str));
    }

    public static boolean createFile(File file) {
        if (file == null || !makeDirs(getFolderName(file.getAbsolutePath())) || file.exists()) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean makeDirs(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        if (file.exists() && file.isDirectory()) {
            return true;
        }
        return file.mkdirs();
    }

    public static boolean makeDirs(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists() && file.isDirectory()) {
            return true;
        }
        return file.mkdirs();
    }

    public static boolean isFileExist(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        return file.exists() && file.isFile();
    }

    public static String getFileNameWithoutExtension(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int iLastIndexOf = str.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int iLastIndexOf2 = str.lastIndexOf(File.separator);
        if (iLastIndexOf2 == -1) {
            return iLastIndexOf == -1 ? str : str.substring(0, iLastIndexOf);
        }
        if (iLastIndexOf == -1) {
            return str.substring(iLastIndexOf2 + 1);
        }
        if (iLastIndexOf2 < iLastIndexOf) {
            return str.substring(iLastIndexOf2 + 1, iLastIndexOf);
        }
        return str.substring(iLastIndexOf2 + 1);
    }

    public static String getFileName(String str) {
        int iLastIndexOf;
        return (TextUtils.isEmpty(str) || (iLastIndexOf = str.lastIndexOf(File.separator)) == -1) ? str : str.substring(iLastIndexOf + 1);
    }

    public static String getFolderName(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int iLastIndexOf = str.lastIndexOf(File.separator);
        return iLastIndexOf == -1 ? "" : str.substring(0, iLastIndexOf);
    }

    public static boolean isFolderExist(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        File file = new File(str);
        return file.exists() && file.isDirectory();
    }

    public static boolean deleteFile(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        return deleteFile(new File(str));
    }

    public static boolean deleteFile(File file) {
        if (file == null) {
            throw new NullPointerException("file is null");
        }
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles == null) {
            return true;
        }
        for (File file2 : fileArrListFiles) {
            if (file2.isFile()) {
                file2.delete();
            } else if (file2.isDirectory()) {
                deleteFile(file2.getAbsolutePath());
            }
        }
        return file.delete();
    }

    public static void delete(String str, FilenameFilter filenameFilter) {
        File[] fileArrListFiles;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        File file = new File(str);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
            if (file.isDirectory()) {
                if (filenameFilter != null) {
                    fileArrListFiles = file.listFiles(filenameFilter);
                } else {
                    fileArrListFiles = file.listFiles();
                }
                if (fileArrListFiles == null) {
                    return;
                }
                for (File file2 : fileArrListFiles) {
                    if (file2.isFile()) {
                        file2.delete();
                    }
                }
            }
        }
    }

    public static long getFileSize(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1L;
        }
        File file = new File(str);
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return -1L;
    }
}
