package com.csz.permission.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import com.csz.permission.core.support.ManufacturerSupportUtil;
import com.csz.permission.core.support.PermissionsPageManager;
import com.csz.permission.core.support.apply.PermissionsChecker;

import java.lang.reflect.Method;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Created by csz on 2018/5/18.
 */

public class PermissionHelper {

    /**
     * 对国产orm的定制化权限适配
     *
     * @return 返回未授权的权限集合
     */
    public static boolean checkPermissions(SmartPermission.Body requestBody) {
        return getNoGrantPermissions(requestBody).isEmpty();
    }


    /**
     * 对国产orm的定制化权限适配
     *
     * @return 返回未授权的权限集合
     */
    public static List<String> getNoGrantPermissions(SmartPermission.Body requestBody) {
        List<String> noGrantPermission = new ArrayList<>();
        //匹配5.0-6.0的国产定制orm
        if (ManufacturerSupportUtil.isUnderMNeedChecked(true)) {
            for (String permission : requestBody.getPers()) {
                if (PermissionsChecker.isPermissionGranted(requestBody.getActivity(), permission)) {
                    //允许
                } else {
                    //拒绝
                    noGrantPermission.add(permission);
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : requestBody.getPers()) {
                if (ContextCompat.checkSelfPermission(requestBody.getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    noGrantPermission.add(permission);
                }
            }
        } else {
            //其他情况都为允许
        }
        return noGrantPermission;
    }


    /**
     * 对国产orm的定制化权限适配
     */
    public static void requestPermissionWithListener(SmartPermission.Body requestBody, List<String> noGrantList) {
        List<String> noGrantPermission = noGrantList;
        //被用户拒绝权限后做额外的处理
        if (!noGrantPermission.isEmpty()) {
            int pageType = requestBody.getPageType();
            Intent intent = null;
            if (pageType == SmartPermission.PageType.MANAGER_PAGE) {
                intent = PermissionsPageManager.getIntent(requestBody.getActivity());
            } else if (pageType == SmartPermission.PageType.ANDROID_SETTING_PAGE) {
                intent = PermissionsPageManager.getSettingIntent(requestBody.getActivity());
            }

            //国产5.0-6.0
            if (ManufacturerSupportUtil.isUnderMNeedChecked(true)) {
                //  requestPermission(requestBody.getActivity(), noGrantPermission, requestBody.getRequestCode());
                int[] unGrants = new int[noGrantPermission.size()];
                Arrays.fill(unGrants,PackageManager.PERMISSION_DENIED);
                PermissionHelper.onReqPermissionsResult(requestBody.getRequestCode(), noGrantList.toArray(new String[0]), unGrants);
            } else {
                //国产6.0及以上版本 及 被系统拒绝
                requestPermission(requestBody.getActivity(), noGrantPermission, requestBody.getRequestCode());
            }
        }
    }

    /**
     * 判断权限是否已经授权
     *
     * @param activity
     * @param permission
     * @return
     */
    public static boolean checkPermissionsGrant(Activity activity, String... permission) {
        List<String> noGrantPermissions = getNoGrantPermissions(new SmartPermission.Body(activity, null, 0, permission, null, null, null, 0));
        if (!noGrantPermissions.isEmpty()){
            for (String s : noGrantPermissions) {
                Log.i("csz","nogrant :"+s);
            }
        }
        return noGrantPermissions.isEmpty();
    }

    /**
     * 检查运行时权限并且请求权限 (Activity)
     *
     * @param activity
     * @param permissions
     * @param requestCode
     * @return
     */
    public static boolean requestAftercheckPermission(Activity activity, String[] permissions, int requestCode) {
        List<String> list = new ArrayList<>();
        for (String per : permissions) {
            int code = ContextCompat.checkSelfPermission(activity, per);
            if (code != PackageManager.PERMISSION_GRANTED) {
                list.add(per);
            }
        }
        if (list.isEmpty()) {
            return true;
        }
        String[] ungrabted = new String[list.size()];
        ActivityCompat.requestPermissions(activity, list.toArray(ungrabted), requestCode);
        return false;
    }

    /**
     * 检查运行时权限并且请求权限 (Fragment)
     *
     * @param fragment
     * @param permissions
     * @param requestCode
     * @return
     */
    public static boolean requestAftercheckPermission(Fragment fragment, String[] permissions, int requestCode) {
        List<String> list = new ArrayList<>();
        for (String per : permissions) {
            int code = ContextCompat.checkSelfPermission(fragment.getActivity(), per);
            if (code != PackageManager.PERMISSION_GRANTED) {
                list.add(per);
            }
        }
        if (list.isEmpty()) {
            return true;
        }
        String[] ungrabted = new String[list.size()];
        fragment.requestPermissions(list.toArray(ungrabted), requestCode);
        return false;
    }

    /**
     * 请求运行时权限 (Activity)
     *
     * @param activity
     * @param permissions
     * @param requestCode
     * @return
     */
    public static boolean requestPermission(Activity activity, List<String> permissions, int requestCode) {
        String[] ungrabted = new String[permissions.size()];
        ActivityCompat.requestPermissions(activity, permissions.toArray(ungrabted), requestCode);
        return false;
    }


    /**
     * 检查权限列表
     *
     * @param op 这个值被hide了，去AppOpsManager类源码找，如位置权限  AppOpsManager.OP_GPS==2
     *           0是网络定位权限，1是gps定位权限，2是所有定位权限
     *           返回值：0代表有权限，1代表拒绝权限 ，3代表询问是否有 ，-1代表出错
     */
    public static int checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            Class c = object.getClass();
            try {
                Class[] cArg = new Class[3];
                cArg[0] = int.class;
                cArg[1] = int.class;
                cArg[2] = String.class;
                Method lMethod = c.getDeclaredMethod("checkOp", cArg);
                return (Integer) lMethod.invoke(object, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * Activity调用这个方法监听
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onReqPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //DialogHelper.dismiss();
        if (grantResults.length <= 0) {
            return;
        }
        ISuccess success = RequestManager.removeSuccess(requestCode);
        IFailure failure = RequestManager.removeFailure(requestCode);
        String[] ungrantPermissions = checkGranted(permissions, grantResults);
        boolean allowed = ungrantPermissions.length == 0;
        if (allowed) {
            if (success != null) {
                success.onSuccess();
            }
        } else {
            if (failure != null) {
                failure.onFailure(ungrantPermissions);
            }
        }
        //因为onSuccess可能会重新调用权限请求，然后走到这步把后续的回调移除，所以保险起见，删除这两部
        /**
         * RequestManager.removeSuccess(requestCode);
         * RequestManager.removeFailure(requestCode);
         */
    }

    /**
     * 用户的授权结果
     * @return 未申请的权限
     */
    private static String[] checkGranted(String[] permissions,int[] grantResults) {
        List<String> per = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                per.add(permissions[i]);
            }
        }
        return per.toArray(new String[0]);
    }

    /**
     * 一个对外提供权限请求的简易方法
     */
    public static final void simpleRequest(Activity activity, ResultCallback callback, String... per) {
        SmartPermission.activity(activity).code(0).premissions(per)
                .onSuccess(callback).onFailure(callback).request();
    }
}
