package com.themaskedbit.tempcontact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    private ContactAdapter mAdapter;

    final String[] NECESSARY_PERMISSIONS = new String[] { Manifest.permission.READ_CONTACTS ,Manifest.permission.WRITE_CONTACTS};
    final int [] PERMISSION_CODE = new int[123];
    private List<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;

    private final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;

    private final String FILTER = DISPLAY_NAME + " NOT LIKE '%@%'";

    private final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);

    @SuppressLint("InlinedApi")
    private final String[] PROJECTION = {
            ContactsContract.Contacts._ID,
            DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
    };

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
        getCallDetails();
    }


    private void getCallDetails() {
        if (hasPermissions(this,NECESSARY_PERMISSIONS)) {
            //Permission is granted
            @SuppressLint("MissingPermission")
            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER);

            if (cursor != null && cursor.moveToFirst()) {
                do{
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String callerName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                    ArrayList<String> callerNumbers  = new ArrayList<>();

                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        Cursor crPhones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                        + " = ?", new String[]{id}, null);

                        Log.d("contact", "NAME: " + callerName);

                        while (crPhones.moveToNext()) {

                            String callerNumber = crPhones.getString(crPhones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            callerNumbers.add(callerNumber);
                            Log.d("contact", "\tPHONE: " + callerNumber);
                        }
                        crPhones.close();
                    }
                    Contact contact = new Contact(callerName, callerNumbers, photoUri);
                    contactList.add(contact);
                    mAdapter.notifyDataSetChanged();
                }while(cursor.moveToNext());
                cursor.close();
                mAdapter.notifyDataSetChanged();
            }
        } else {
            //ask for permission
            requestPermission();
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

}
