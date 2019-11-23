package com.yc.rxpermissionsdialog;

import android.Manifest;
import android.animation.Animator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        DialogFactory
                .getDialog(new YPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        "申请一下",
                        "不给不让用啊"))
        .showDialog(this);
    }
}
