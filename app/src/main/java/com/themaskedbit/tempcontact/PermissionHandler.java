package com.themaskedbit.tempcontact;
;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

public interface PermissionHandler {
    boolean checkHasPermission(FragmentActivity activity, String[] permission);
    void requestPermission(FragmentActivity activity, String[] permissions, int requestCode);
    boolean onPermissionResult(FragmentActivity activity, int requestCode, String[] permissions, int[] grantResults, int permissionCode, String[] requestedPermission);
}
