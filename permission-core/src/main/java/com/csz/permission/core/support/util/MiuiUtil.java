package com.csz.permission.core.support.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

public class MiuiUtil {

    private MiuiUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    public static final int REQUEST_CODE_SERVICE_SMS = 100;


    /**
     * @return whether or not is MIUI
     * @link http://dev.xiaomi.com/doc/p=254/index.html
     */
    public static boolean isMIUI() {
        String device = Build.MANUFACTURER;
        if ("Xiaomi".equalsIgnoreCase(device)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 跳转到权限设置页
     *
     * @param context
     */
    public static void goPermissionSettings(Activity context) {

        Intent intent;
        try {//MIUI8/9
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivityForResult(intent, REQUEST_CODE_SERVICE_SMS);
        } catch (ActivityNotFoundException e) {
            try {//MIUI5/6
                intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivityForResult(intent, REQUEST_CODE_SERVICE_SMS);
            } catch (ActivityNotFoundException e1) {
                //应用信息界面
                intent = new Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(),
                        null);
                intent.setData(uri);
                context.startActivityForResult(intent, REQUEST_CODE_SERVICE_SMS);
            }
        }
    }


}
