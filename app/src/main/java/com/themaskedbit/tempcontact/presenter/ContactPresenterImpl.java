package com.themaskedbit.tempcontact.presenter;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;

import com.themaskedbit.tempcontact.ContactContract;
import com.themaskedbit.tempcontact.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactPresenterImpl implements ContactContract.Presenter {
    private final ContactContract.View contactContractView;
    public ContactPresenterImpl(ContactContract.View contactContractView) {
        this.contactContractView = contactContractView;
        this.contactContractView.setPresenter(this);
    }

    @Override
    public List<Contact> fetchContact(ContentResolver cr) {
        final String DISPLAY_NAME = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY : ContactsContract.Contacts.DISPLAY_NAME;
        final String[] PROJECTION = {
                ContactsContract.Contacts._ID,
                DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.PHOTO_URI,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };
        final String FILTER = DISPLAY_NAME + " NOT LIKE '%@%'";

        final String ORDER = String.format("%1$s COLLATE NOCASE", DISPLAY_NAME);
        List<Contact> contactList = new ArrayList<>();

        Log.d("time", "started");
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, PROJECTION, FILTER, null, ORDER);

        if (cursor != null && cursor.moveToFirst()) {
            do {

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String callerName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                ArrayList<String> callerNumbers = new ArrayList<>();
                Log.d("time", "got item");
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
            } while (cursor.moveToNext());
            cursor.close();
        }
        return contactList;
    }
}
