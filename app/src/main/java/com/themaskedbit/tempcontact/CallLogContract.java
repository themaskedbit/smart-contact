package com.themaskedbit.tempcontact;

import android.database.Cursor;

import com.themaskedbit.tempcontact.model.Contact;
import com.themaskedbit.tempcontact.presenter.BasePresenter;
import com.themaskedbit.tempcontact.view.BaseView;

import java.util.List;

public interface CallLogContract {
    interface View extends BaseView<Presenter> {
        void addData(List<Contact> contactList);
    }
    interface Presenter extends BasePresenter {
        List<Contact> fetchCallLog(Cursor c);
    }
}
