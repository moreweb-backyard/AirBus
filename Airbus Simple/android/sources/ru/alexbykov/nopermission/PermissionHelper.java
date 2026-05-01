package ru.alexbykov.nopermission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class PermissionHelper {
    private static final int DIALOG_WITHOUT_CUSTOM_COLOR = 0;
    private static final int PERMISSION_REQUEST_CODE = 98;
    private Activity activity;
    private Runnable deniedListener;
    private int dialogBeforeAskPositiveButton;
    private int dialogBeforeAskPositiveButtonColor = 0;
    private AlertDialog.Builder dialogBeforeRunBuilder;
    private Fragment fragment;
    private Runnable neverAskAgainListener;
    private String[] permissions;
    private Runnable successListener;

    public PermissionHelper(Activity activity) {
        this.activity = activity;
    }

    public PermissionHelper(Fragment fragment) {
        this.fragment = fragment;
    }

    public PermissionHelper check(String str) {
        this.permissions = new String[1];
        this.permissions[0] = str;
        return this;
    }

    public PermissionHelper check(String... strArr) {
        this.permissions = strArr;
        return this;
    }

    public PermissionHelper onSuccess(Runnable runnable) {
        this.successListener = runnable;
        return this;
    }

    public PermissionHelper onDenied(Runnable runnable) {
        this.deniedListener = runnable;
        return this;
    }

    @Deprecated
    public PermissionHelper onFailure(Runnable runnable) {
        this.deniedListener = runnable;
        return this;
    }

    public PermissionHelper onNeverAskAgain(Runnable runnable) {
        this.neverAskAgainListener = runnable;
        return this;
    }

    public PermissionHelper withDialogBeforeRun(@StringRes int i, @StringRes int i2, @StringRes int i3) {
        this.dialogBeforeAskPositiveButton = i3;
        this.dialogBeforeRunBuilder = getDialogBuilder(i, i2);
        return this;
    }

    public PermissionHelper setDialogPositiveButtonColor(@ColorRes int i) {
        this.dialogBeforeAskPositiveButtonColor = ContextCompat.getColor(getContext(), i);
        return this;
    }

    private AlertDialog.Builder getDialogBuilder(@StringRes int i, @StringRes int i2) {
        Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(context.getString(i));
        builder.setMessage(context.getString(i2));
        builder.setCancelable(false);
        return builder;
    }

    private Context getContext() {
        return this.activity == null ? this.fragment.getContext() : this.activity;
    }

    public void run() {
        if (isListenersCorrect()) {
            runSuccessOrAskPermissions();
            return;
        }
        throw new RuntimeException("permissionSuccessListener or permissionDeniedListener have null reference. You must realize onSuccess and onDenied methods");
    }

    private void runSuccessOrAskPermissions() {
        if (isNeedToAskPermissions()) {
            checkPermissions();
        } else {
            this.successListener.run();
        }
    }

    @RequiresApi(api = 23)
    private void checkPermissions() {
        String[] permissionsForRequest = getPermissionsForRequest();
        if (permissionsForRequest.length > 0) {
            checkDialogAndAskPermissions(permissionsForRequest);
        } else {
            this.successListener.run();
        }
    }

    @SuppressLint({"NewApi"})
    private void checkDialogAndAskPermissions(String[] strArr) {
        if (this.dialogBeforeRunBuilder != null && isNotContainsNeverAskAgain(strArr)) {
            showDialogBeforeRun(strArr);
        } else {
            askPermissions(strArr);
        }
    }

    private boolean isNotContainsNeverAskAgain(String[] strArr) {
        for (String str : strArr) {
            if (isNeverAskAgain(str)) {
                return false;
            }
        }
        return true;
    }

    private void showDialogBeforeRun(final String[] strArr) {
        this.dialogBeforeRunBuilder.setPositiveButton(this.dialogBeforeAskPositiveButton, new DialogInterface.OnClickListener() { // from class: ru.alexbykov.nopermission.PermissionHelper.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                PermissionHelper.this.askPermissions(strArr);
            }
        });
        AlertDialog alertDialogCreate = this.dialogBeforeRunBuilder.create();
        alertDialogCreate.show();
        if (this.dialogBeforeAskPositiveButtonColor != 0) {
            alertDialogCreate.getButton(-1).setTextColor(this.dialogBeforeAskPositiveButtonColor);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"NewApi"})
    public void askPermissions(String[] strArr) {
        if (this.activity != null) {
            this.activity.requestPermissions(strArr, 98);
        } else {
            this.fragment.requestPermissions(strArr, 98);
        }
    }

    private boolean isListenersCorrect() {
        return (this.successListener == null || this.deniedListener == null) ? false : true;
    }

    private boolean isNeedToAskPermissions() {
        return Build.VERSION.SDK_INT >= 23;
    }

    private String[] getPermissionsForRequest() {
        ArrayList arrayList = new ArrayList();
        for (String str : this.permissions) {
            if (isPermissionNotGranted(str)) {
                arrayList.add(str);
            }
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    @SuppressLint({"NewApi"})
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 98) {
            for (String str : strArr) {
                if (isPermissionNotGranted(str)) {
                    runDeniedOrNeverAskAgain(str);
                    return;
                }
            }
        }
        this.successListener.run();
        unbind();
    }

    @SuppressLint({"NewApi"})
    private void runDeniedOrNeverAskAgain(String str) {
        if (isNeverAskAgain(str)) {
            runNeverAskAgain();
        } else {
            this.deniedListener.run();
        }
        unbind();
    }

    private void runNeverAskAgain() {
        if (this.neverAskAgainListener != null) {
            this.neverAskAgainListener.run();
        }
    }

    private boolean isPermissionNotGranted(String str) {
        return this.activity != null ? ActivityCompat.checkSelfPermission(this.activity, str) != 0 : ActivityCompat.checkSelfPermission(this.fragment.getContext(), str) != 0;
    }

    @SuppressLint({"NewApi"})
    private boolean isNeverAskAgain(String str) {
        if (this.activity != null) {
            return !this.activity.shouldShowRequestPermissionRationale(str);
        }
        return !this.fragment.shouldShowRequestPermissionRationale(str);
    }

    public void startApplicationSettingsActivity() {
        Context context = getContext();
        Intent intent = new Intent();
        Uri uriFromParts = Uri.fromParts("package", context.getPackageName(), null);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(uriFromParts);
        context.startActivity(intent);
    }

    @Deprecated
    public void unsubscribe() {
        unbind();
    }

    private void unbind() {
        this.deniedListener = null;
        this.successListener = null;
        if (this.dialogBeforeRunBuilder != null) {
            this.dialogBeforeRunBuilder = null;
            this.dialogBeforeAskPositiveButton = 0;
        }
        if (this.neverAskAgainListener != null) {
            this.neverAskAgainListener = null;
        }
    }
}
