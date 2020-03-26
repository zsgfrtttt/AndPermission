package com.csz.per;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.csz.permission.core.PermissionHelper;
import com.csz.permission.core.ResultCallback;

public class MActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionHelper.onReqPermissionsResult(requestCode, permissions, grantResults);
    }


    public void request(View view) {
        PermissionHelper.simpleRequest(this, new ResultCallback() {
            @Override
            public void onFailure() {
                Toast.makeText(MActivity.this,"申请权限失败",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess() {
                Toast.makeText(MActivity.this,"申请权限成功",Toast.LENGTH_LONG).show();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
