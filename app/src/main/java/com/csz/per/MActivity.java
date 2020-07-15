package com.csz.per;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.csz.permission.core.PermissionHelper;
import com.csz.permission.core.ResultCallback;

import java.util.Arrays;

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
            public void onFailure(String[] ungrantPermissions) {
                Toast.makeText(MActivity.this, Arrays.toString(ungrantPermissions), Toast.LENGTH_LONG).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess() {
                Toast.makeText(MActivity.this, "success", Toast.LENGTH_LONG).show();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE,
           Manifest.permission.CALL_PHONE,
           Manifest.permission.CAMERA,
           Manifest.permission.READ_CONTACTS,
           Manifest.permission.ACCESS_COARSE_LOCATION,
           Manifest.permission.ACCESS_FINE_LOCATION);
    }

}
