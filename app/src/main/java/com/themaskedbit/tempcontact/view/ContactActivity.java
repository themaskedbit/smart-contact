package com.themaskedbit.tempcontact.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.themaskedbit.tempcontact.ContactContract;
import com.themaskedbit.tempcontact.presenter.ContactPresenterImpl;
import com.themaskedbit.tempcontact.PermissionHandler;
import com.themaskedbit.tempcontact.PermissionHandlerAndroid;
import com.themaskedbit.tempcontact.R;
import com.themaskedbit.tempcontact.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements ContactContract.View {

    private ContactAdapter mAdapter;

    final String[] NECESSARY_PERMISSIONS = new String[] { Manifest.permission.READ_CONTACTS ,Manifest.permission.WRITE_CONTACTS};
    final int [] PERMISSION_CODE = new int[123];
    private List<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactContract.Presenter presenter;
    private List<Contact> returnContacts;
    private PermissionHandler permissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new ContactAdapter(contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        permissionHandler = new PermissionHandlerAndroid();

        //TODO: Use dagger for injecting this.
        presenter = new ContactPresenterImpl(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();



        if (permissionHandler.checkHasPermission(this,NECESSARY_PERMISSIONS)) {
            @SuppressLint("MissingPermission") ContentResolver cr = getContentResolver();
            returnContacts = presenter.fetchContact(cr);
            addData(returnContacts);
        }
        else {
            permissionHandler.requestPermission(this, NECESSARY_PERMISSIONS,PERMISSION_CODE[0]);
        }

    }


    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermission(){
        requestPermissions(
                NECESSARY_PERMISSIONS, PERMISSION_CODE[0]);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public  boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (getApplicationContext().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permissionHandler.onPermissionResult(this,requestCode,permissions,grantResults,PERMISSION_CODE[0],new String[]{Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS})){
            @SuppressLint("MissingPermission") ContentResolver cr = getContentResolver();
            returnContacts = presenter.fetchContact(cr);
            addData(returnContacts);
        }
    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addData(List<Contact> returnContacts) {
        for(Contact data : returnContacts) {
            contactList.add(data);
        }

        mAdapter.notifyDataSetChanged();
    }
}
