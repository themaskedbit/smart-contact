package com.themaskedbit.tempcontact;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

public class PermissionHandlerAndroid implements PermissionHandler {
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean checkHasPermission(FragmentActivity activity, String[] permissions) {
        for(String permission : permissions ){
            if(activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void requestPermission(FragmentActivity activity, String[] permissions, int requestCode){
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    @Override
    public boolean onPermissionResult(FragmentActivity activity, int requestCode, String[] permissions, int[] grantResults, int permissionCode, String[] requestedPermission) {
        if (requestCode == permissionCode) {
            for(int i=0; i< permissions.length; i++) {
                if (permissions[i].equals(requestedPermission[i]) && grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
