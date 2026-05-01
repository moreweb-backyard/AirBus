package com.shima.smartbushome.assist.FilePicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.shima.smartbushome.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class FolderFilePicker extends Dialog {
    private static final String ROOT = Environment.getExternalStorageDirectory().toString();
    private final String ALL_FILE;
    private final int SELECTED_COLOR;
    private String mCurrentPath;
    private PickPathEvent mEvent;
    private String[] mExtensions;
    private FileLvAdapter mFileLvAdapter;
    private ListView mFilesLv;
    private TextView mTitle;

    public interface PickPathEvent {
        void onPickEvent(String str);
    }

    public FolderFilePicker(Context context, PickPathEvent pickPathEvent, String... strArr) {
        super(context);
        this.SELECTED_COLOR = 2132127231;
        this.ALL_FILE = "*";
        this.mEvent = pickPathEvent;
        init();
        this.mExtensions = (strArr == null || strArr.length == 0) ? null : strArr;
    }

    private void init() {
        requestWindowFeature(1);
        setContentView(R.layout.activity_select_folder);
        this.mTitle = (TextView) findViewById(R.id.title_tv);
        this.mFilesLv = (ListView) findViewById(R.id.folder_lv);
        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.assist.FilePicker.FolderFilePicker.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FolderFilePicker.this.setResult();
                FolderFilePicker.this.dismiss();
            }
        });
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() { // from class: com.shima.smartbushome.assist.FilePicker.FolderFilePicker.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FolderFilePicker.this.dismiss();
            }
        });
        setCurrentPath(ROOT);
        this.mFileLvAdapter = new FileLvAdapter();
        setListAdapterData();
        this.mFilesLv.setAdapter((ListAdapter) this.mFileLvAdapter);
        this.mFilesLv.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.shima.smartbushome.assist.FilePicker.FolderFilePicker.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (FolderFilePicker.this.mFileLvAdapter.getItem(i).isDirectory()) {
                    FolderFilePicker.this.setCurrentPath(FolderFilePicker.this.mFileLvAdapter.getItem(i).getAbsolutePath());
                    FolderFilePicker.this.setListAdapterData();
                    FolderFilePicker.this.mFileLvAdapter.notifyDataSetChanged();
                } else {
                    if (!FolderFilePicker.this.isRootPath(FolderFilePicker.this.mFileLvAdapter.getItem(i).getAbsolutePath())) {
                        FolderFilePicker.this.mFileLvAdapter.getItem(i).isDirectory();
                    }
                    FolderFilePicker.this.mFileLvAdapter.setSelectedIndex(i);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setResult() {
        if (this.mEvent != null) {
            if (this.mExtensions == null) {
                this.mEvent.onPickEvent(this.mCurrentPath);
            } else {
                File item = this.mFileLvAdapter.getItem(this.mFileLvAdapter.getSelectedIndex());
                this.mEvent.onPickEvent(item == null ? null : item.getAbsolutePath());
            }
            dismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCurrentPath(String str) {
        this.mCurrentPath = str;
        this.mTitle.setText(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setListAdapterData() {
        ArrayList arrayList = new ArrayList();
        File[] fileArrListFiles = new File(this.mCurrentPath).listFiles();
        if (fileArrListFiles == null) {
            return;
        }
        for (File file : fileArrListFiles) {
            if (file.isDirectory()) {
                arrayList.add(file);
            } else if (this.mExtensions != null && this.mExtensions.length > 0) {
                if ("*".equals(this.mExtensions[0])) {
                    arrayList.add(file);
                } else {
                    for (int i = 0; i < this.mExtensions.length; i++) {
                        if (file.getAbsolutePath().endsWith(this.mExtensions[i])) {
                            arrayList.add(file);
                        }
                    }
                }
            }
        }
        Collections.sort(arrayList, new Comparator<File>() { // from class: com.shima.smartbushome.assist.FilePicker.FolderFilePicker.4
            @Override // java.util.Comparator
            public int compare(File file2, File file3) {
                if (file2.isDirectory() && file3.isFile()) {
                    return -1;
                }
                if (file2.isFile() && file3.isDirectory()) {
                    return 1;
                }
                return -file3.getName().toLowerCase().compareTo(file2.getName().toLowerCase());
            }
        });
        if (!isRootPath(this.mCurrentPath)) {
            arrayList.add(0, new File(this.mCurrentPath).getParentFile());
        }
        this.mFileLvAdapter.setList(arrayList);
        this.mFileLvAdapter.setSelectedIndex(-1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRootPath(String str) {
        return ROOT.equals(str);
    }

    private class FileLvAdapter extends BaseAdapter {
        public List<File> list;
        private int mSelectedIndex;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        private FileLvAdapter() {
            this.mSelectedIndex = -1;
        }

        public void setList(List<File> list) {
            this.list = list;
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (this.list == null) {
                return 0;
            }
            return this.list.size();
        }

        @Override // android.widget.Adapter
        public File getItem(int i) {
            if (i >= getCount() || i < 0) {
                return null;
            }
            return this.list.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewInflate;
            ViewHolder viewHolder;
            if (view == null) {
                viewHolder = new ViewHolder();
                viewInflate = LayoutInflater.from(FolderFilePicker.this.getContext()).inflate(R.layout.folder_list_item, (ViewGroup) null);
                viewHolder.mIcon = (ImageView) viewInflate.findViewById(R.id.icon);
                viewHolder.mText = (TextView) viewInflate.findViewById(R.id.text);
                viewInflate.setTag(viewHolder);
            } else {
                viewInflate = view;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.mIcon.setBackgroundResource(getItem(i).isFile() ? R.mipmap.ic_file : R.mipmap.ic_folder);
            viewHolder.mText.setText((FolderFilePicker.this.isRootPath(FolderFilePicker.this.mCurrentPath) || i != 0) ? getItem(i).getName() : "../");
            if (i == this.mSelectedIndex) {
                viewInflate.setBackgroundColor(2132127231);
            } else {
                viewInflate.setBackgroundColor(0);
            }
            return viewInflate;
        }

        public void setSelectedIndex(int i) {
            this.mSelectedIndex = i;
            notifyDataSetChanged();
        }

        public int getSelectedIndex() {
            return this.mSelectedIndex;
        }

        class ViewHolder {
            public ImageView mIcon;
            public TextView mText;

            ViewHolder() {
            }
        }
    }
}
