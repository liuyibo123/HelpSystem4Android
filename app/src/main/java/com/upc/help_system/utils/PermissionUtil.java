package com.upc.help_system.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.upc.help_system.view.activity.DetailActivity;

/**
 * Created by Administrator on 2017/6/19.
 */

public class PermissionUtil {

    public static int  checkPermission(Activity context, String permission,String toast){
        if (ContextCompat.checkSelfPermission(context,
                permission) != PackageManager.PERMISSION_GRANTED) {
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    permission)) {
                Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            } else {
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(context,
                        new String[]{permission},
                        1);
            }
            return 0;
        } else {
            return 1;
        }
    }
}
