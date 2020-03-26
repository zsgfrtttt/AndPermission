package com.csz.permission.core.support;

import android.os.Build;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.csz.permission.core.support.PermissionsPageManager.MANUFACTURER_HUAWEI;
import static com.csz.permission.core.support.PermissionsPageManager.MANUFACTURER_MEIZU;
import static com.csz.permission.core.support.PermissionsPageManager.MANUFACTURER_OPPO;
import static com.csz.permission.core.support.PermissionsPageManager.MANUFACTURER_VIVO;
import static com.csz.permission.core.support.PermissionsPageManager.MANUFACTURER_XIAOMI;

/**
 * Created by csz on 2017/9/16.
 */

public class ManufacturerSupportUtil {
    private static String[] forceManufacturers = {MANUFACTURER_XIAOMI,MANUFACTURER_HUAWEI,MANUFACTURER_VIVO,
            MANUFACTURER_MEIZU, MANUFACTURER_OPPO};
    private static Set<String> forceSet = new HashSet<>(Arrays.asList(forceManufacturers));
    private static String[] underMHasPermissionsRequestManufacturer = {MANUFACTURER_XIAOMI,MANUFACTURER_HUAWEI,MANUFACTURER_VIVO,
            MANUFACTURER_MEIZU, MANUFACTURER_OPPO};
    private static Set<String> underMSet = new HashSet<>(Arrays.asList
            (underMHasPermissionsRequestManufacturer));

    /**
     * those manufacturer that need request by some special measures, above
     * {@link Build.VERSION_CODES#M}
     *
     * @return
     */
    public static boolean isForceManufacturer() {
        return forceSet.contains(PermissionsPageManager.getManufacturer());
    }

    /**
     * those manufacturer that need request permissions under {@link Build.VERSION_CODES#M},
     * above {@link Build.VERSION_CODES#LOLLIPOP}
     *
     * @return
     */
    public static boolean isUnderMHasPermissionRequestManufacturer() {
        return underMSet.contains(PermissionsPageManager.getManufacturer());
    }

    public static boolean isLocationMustNeedGpsManufacturer() {
        return PermissionsPageManager.getManufacturer().equalsIgnoreCase(MANUFACTURER_OPPO);
    }

    public static boolean isUnderMNeedChecked(boolean isUnderMNeedChecked) {
        return isUnderMHasPermissionRequestManufacturer() && isUnderMNeedChecked &&
                isAndroidL();
    }

    public static boolean isAndroidL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES
                .LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }

    public static Set<String> getForceSet() {
        return forceSet;
    }

    public static Set<String> getUnderMSet() {
        return underMSet;
    }
}
