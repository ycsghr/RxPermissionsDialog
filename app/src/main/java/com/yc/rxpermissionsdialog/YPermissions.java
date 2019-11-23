package com.yc.rxpermissionsdialog;

/**
 * @author yangchao
 * @description: TODO
 * @date 2019-11-22
 */
public class YPermissions {
    private String permissionsName;//申请的权限
    private String beforeContent;//申请前提示语
    private boolean isMust;//是否为必须的权限
    private String afterRefusingContent;//拒绝后提示语

    public String getPermissionsName() {
        return permissionsName;
    }

    public void setPermissionsName(String permissionsName) {
        this.permissionsName = permissionsName;
    }

    public String getBeforeContent() {
        return beforeContent;
    }

    public void setBeforeContent(String beforeContent) {
        this.beforeContent = beforeContent;
    }

    public boolean isMust() {
        return isMust;
    }

    public void setMust(boolean must) {
        isMust = must;
    }

    public String getAfterRefusingContent() {
        return afterRefusingContent;
    }

    public void setAfterRefusingContent(String afterRefusingContent) {
        this.afterRefusingContent = afterRefusingContent;
    }

    public YPermissions(String permissionsName, String beforeContent, String afterRefusingContent, boolean isMust) {
        this.permissionsName = permissionsName;
        this.beforeContent = beforeContent;
        this.afterRefusingContent = afterRefusingContent;
        this.isMust = isMust;
    }

    /**
     * 不传值默认为必要权限
     * @param permissionsName
     * @param beforeContent
     * @param afterRefusingContent
     */
    public YPermissions(String permissionsName, String beforeContent, String afterRefusingContent) {
      this(permissionsName,beforeContent,afterRefusingContent,true);
    }
}
