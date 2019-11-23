package com.yc;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yc.rxpermissionsdialog.PermissionsListener;
import com.yc.rxpermissionsdialog.R;
import com.yc.rxpermissionsdialog.RunToSetting;
import com.yc.rxpermissionsdialog.YPermissions;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * @author yangchao
 * @description: TODO
 * @date 2019-11-22
 */
public class RxPermissionsDialog extends DialogFragment {

    private YPermissions[] permissions;
    private Consumer<Boolean> mConsumer;
    private FragmentActivity activity;
    private boolean isSetting = false;//被拒绝后跳转到设置页面
    private PermissionsListener listener;
    private View view;
    private boolean customClickEvent = false;
    private RxPermissions rxPermissions ;
    private String settingAllowBtnName = "去设置";
    private RunToSetting runToSetting;//跳转到设置页的方法觉得不好可以自定义
    private int layout = R.layout.rxpermissions_dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layout, container, false);
    }

    public RxPermissionsDialog setPermissions(YPermissions[] permissions) {
        this.permissions = permissions;
        return this;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        if (null != listener) {
            listener.convertView(view);
        }
        initClick(view);
        initContent();
        initRun2Setting();
    }

    private void initRun2Setting() {
        if (null == runToSetting) {
            runToSetting = new RunToSetting() {
                @Override
                public void turn2ApplicationInfoIntent() {
                    /**
                     * 有FLAG_ACTIVITY_NEW_TASK 其实可以直接用application  context 直接跳转
                     */
                    Intent intent = new Intent();
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package",activity.getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        intent.setAction(Intent.ACTION_VIEW);
                        intent
                                .setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        intent.putExtra("com.android.settings.ApplicationPkgName",
                                activity.getPackageName());
                    }

                    activity.startActivity(intent);
                }
            };

        }
    }

    private void initClick(View view) {
        if (customClickEvent) {
            return;
        }
        view.findViewById(R.id.permissions_refused_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mConsumer.accept(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismiss();

            }
        });
        view.findViewById(R.id.permissions_allow_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSetting) {
                    runToSetting.turn2ApplicationInfoIntent();
                    dismiss();

                } else {
                    getPermissions();
                }

            }
        });
    }

    public void getPermissions() {
        final ArrayList<String> list = new ArrayList<>();
        for (YPermissions perm : permissions) {
            list.add(perm.getPermissionsName());
        }
        String[] permissionString = new String[list.size()];

        list.toArray(permissionString);

        rxPermissions.requestEach(permissionString).collect(new Callable<ArrayList<String>>() {
            @Override
            public ArrayList<String> call() throws Exception {
                return new ArrayList<>();
            }
        }, new BiConsumer<ArrayList<String>, Permission>() {
            @Override
            public void accept(ArrayList<String> permissions, Permission permission) throws Exception {
                //没有给予的权限
                if (!permission.granted) {
                    permissions.add(permission.name);

                }
            }
        }).subscribe(new Consumer<ArrayList<String>>() {
            @Override
            public void accept(ArrayList<String> strings) throws Exception {
                //统一处理没有允许的权限
                ArrayList<YPermissions> refusedPremList = new ArrayList<>();
                for (YPermissions perm : permissions) {
                    if (!perm.isMust()) {
                        continue;
                    }
                    for (String refusedPrem : strings) {
                        if (perm.getPermissionsName().equals(refusedPrem)) {
                            refusedPremList.add(perm);
                        }
                    }
                }
                if (refusedPremList.isEmpty()) {
                    mConsumer.accept(true);
                    dismiss();
                } else {
                    setContent(refusedPremList);
                    isSetting = true;
                    setRightBtn(settingAllowBtnName);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                dismiss();
                runToSetting.turn2ApplicationInfoIntent();

            }
        });
    }

    private void setRightBtn(String btnName) {
        View viewById = view.findViewById(R.id.permissions_allow_btn);
        if (viewById instanceof Button) {
            ((Button) viewById).setText(btnName);
        } else {
            ((TextView) viewById).setText(btnName);
        }


    }

    private void setContent(ArrayList<YPermissions> premList) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < premList.size(); i++) {
            buffer.append(premList.get(i).getAfterRefusingContent());
            if (i != premList.size() - 1) {
                buffer.append("\n");
            }

        }
        ((TextView) view.findViewById(R.id.permissions_content)).setText(buffer.toString());
    }

    private void initContent() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < permissions.length; i++) {
            buffer.append(permissions[i].getBeforeContent());
            if (i != permissions.length - 1) {
                buffer.append("\n");
            }

        }
        ((TextView) view.findViewById(R.id.permissions_content)).setText(buffer.toString());
    }

    /**
     * 判断是否显示dialog
     *
     * @param activity
     */
    public void showDialog(FragmentActivity activity) {
        this.activity = activity;
        rxPermissions=new RxPermissions(activity);
        if (hasMustPermissions()) {
            show(activity.getSupportFragmentManager(), "permissionsDialog");
            return;
        }
        try {
            mConsumer.accept(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否有权限
     *
     * @return
     */
    private boolean hasMustPermissions() {
        for (YPermissions perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat
                    .checkSelfPermission(activity, perm.getPermissionsName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 注册观察者
     *
     * @param conscumer
     */
    public RxPermissionsDialog setConsumer(Consumer<Boolean> conscumer) {
        this.mConsumer = conscumer;
        return this;
    }

    /**
     * 设置自定义布局
     *
     * @param layout xml必须有的id
     *               <item name="permissions_left_btn" type="id"/>
     *               <item name="permissions_right_btn" type="id"/>
     *               <item name="permissions_title" type="id"/>
     *               <item name="permissions_content" type="id"/>
     */
    public RxPermissionsDialog setLayout(@LayoutRes int layout) {
        this.layout = layout;
        return this;
    }

    /**
     * 设置后点击事件将不再处理将交由外部自定义处理 个人建议不要设置
     *
     * @return
     */
    public RxPermissionsDialog customClickEvent() {
        customClickEvent = true;
        return this;
    }

    public RxPermissionsDialog setSettingAllowBtnName(String settingAllowBtnName) {
        this.settingAllowBtnName = settingAllowBtnName;
        return this;
    }

    public RxPermissionsDialog setListener(PermissionsListener listener) {
        this.listener = listener;
        return this;
    }

    public RxPermissionsDialog setRunToSetting(RunToSetting runToSetting) {
        this.runToSetting = runToSetting;
        return this;
    }
}
