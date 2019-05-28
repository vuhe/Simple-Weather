package com.simpleweather.android.View.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.simpleweather.android.MyApplication;
import com.simpleweather.android.R;
import com.simpleweather.android.service.LocationService;
import com.simpleweather.android.util.ContentUtil;

import java.util.ArrayList;
import java.util.List;

import static com.simpleweather.android.util.ContentUtil.NO_LOCATION_PERMISSION;
import static com.simpleweather.android.util.ContentUtil.POSITIONING;

public class SplashActivity extends AppCompatActivity {

    private Context context = MyApplication.getContext();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setPermissions();
    }

    /**
     * 权限请求相关
     */
    private void setPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SplashActivity.this, permissions, 1);
        } else {
            ContentUtil.LOCATE_STATUS = POSITIONING;
            startService(new Intent(this, LocationService.class));
            startIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            ContentUtil.LOCATE_STATUS = NO_LOCATION_PERMISSION;
                            Toast.makeText(context, "无法使用定位", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    ContentUtil.LOCATE_STATUS = POSITIONING;
                    startService(new Intent(this, LocationService.class));
                    startIntent();
                } else {
                    Toast.makeText(context, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
            default:
        }
    }


    private void startIntent() {
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
        finish();
    }
}
