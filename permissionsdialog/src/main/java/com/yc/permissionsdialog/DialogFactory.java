package com.yc.permissionsdialog;

/**
 * @author yangchao
 * @description: TODO
 * @date 2019-11-22
 */
public class DialogFactory {

    public static RxPermissionsDialog getDialog(YPermissions... permissions){
        return  new RxPermissionsDialog().setPermissions(permissions);

    }
}
