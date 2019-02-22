package com.themaskedbit.tempcontact.presenter;

import android.database.Cursor;
import android.provider.CallLog;

import com.themaskedbit.tempcontact.CallLogContract;
import com.themaskedbit.tempcontact.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class CallLogPresenterImpl implements CallLogContract.Presenter {

    private final CallLogContract.View callLogContractView;
    public CallLogPresenterImpl(CallLogContract.View callLogContractView) {
        this.callLogContractView = callLogContractView;
        this.callLogContractView.setPresenter(this);
    }
    @Override
    public List<Contact> fetchCallLog(Cursor c) {

        List<Contact> contactList = new ArrayList<>();

        int count = 1;

        String prevNumber = null;
        int preType = -1;
        int index = 0;

        if (c.getCount() > 0)
        {
            c.moveToFirst();
            //Intialize with first number
            do{
                String callerName = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String callerNumber = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                String callerPhoto = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_PHOTO_URI));
                long callDateandTime = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
                long callDuration = c.getLong(c.getColumnIndex(CallLog.Calls.DURATION));
                int callType = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));
                if (prevNumber !=null && preType != -1 && prevNumber.equals(callerNumber) && preType == callType ) {
                    count++;
                    Contact contact = new Contact(callerName, callerNumber, callerPhoto, count, callType);
                    contactList.set(index,contact);
                }
                else {
                    count = 1;
                    Contact contact = new Contact(callerName, callerNumber, callerPhoto, count, callType);
                    contactList.add(contact);
                    index = contactList.indexOf(contact);
                }
                prevNumber = callerNumber;
                preType = callType;
            }while(c.moveToNext());
        }
        return contactList;
}
}
