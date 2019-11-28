package com.yc.rxpermissionsdialog;

import android.Manifest;
import android.animation.Animator;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.yc.permissionsdialog.DialogFactory;
import com.yc.permissionsdialog.YPermissions;
import io.reactivex.functions.Consumer;

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
                .setLayout(R.layout.xxxxxxxxx)
                .setConsumer(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean){
                            Toast.makeText(MainActivity.this,"有权限啦",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this,"给劳资爬",Toast.LENGTH_SHORT).show();


                        }                    }
                })
        .showDialog(this);
    }
}
