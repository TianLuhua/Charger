package com.example.administrator.changer.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import java.util.List;

public class ActivityUtils {


    /**
     * 启动对应的Activity根据不同的Action
     *
     * @param action
     */
    public static void setActivityConfig(Context context, String action) {
        Intent intent = new Intent(action);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            try {
                context.startActivity(intent);

            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "Start Activity Error", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Not Found Activity", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据包名启动 APP
     *
     * @param packagename 包名
     */
    public static void startApplicationWithPackageName(Context context, String packagename) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageManager manager = context.getPackageManager();
        PackageInfo packageinfo = null;

        try {
            packageinfo = manager.getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = manager
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = createComponentName(packageName, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Not Found Activity", Toast.LENGTH_SHORT).show();
        }
    }

    private static ComponentName createComponentName(String packageName, String className) {
        return new ComponentName(packageName, className);
    }


}
