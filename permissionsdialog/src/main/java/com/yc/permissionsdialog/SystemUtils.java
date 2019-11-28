package com.yc.permissionsdialog;

/**
 * @author yangchao
 * @description: TODO
 * @date 2019-11-22
 */
public class SystemUtils {
    /**
     * 有FLAG_ACTIVITY_NEW_TASK 其实可以直接用application  context 直接跳转
     */
    public static void turn2ApplicationInfoIntent() {
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        if (Build.VERSION.SDK_INT >= 9) {
//            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//            intent.setData(Uri.fromParts("package", AppUtils.getApp().getPackageName(), null));
//        } else if (Build.VERSION.SDK_INT <= 8) {
//            intent.setAction(Intent.ACTION_VIEW);
//            intent
//                    .setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
//            intent.putExtra("com.android.settings.ApplicationPkgName",
//                    AppUtils.getApp().getPackageName());
//        }
//
//        AppUtils.getApp().startActivity(intent);

    }
}
