package com.themaskedbit.tempcontact;

import android.content.ContentResolver;

import com.themaskedbit.tempcontact.model.Contact;
import com.themaskedbit.tempcontact.presenter.BasePresenter;
import com.themaskedbit.tempcontact.view.BaseView;

import java.util.List;

public interface ContactContract {
    interface View extends BaseView<Presenter> {
        void addData(List<Contact> contactList);
    }
    interface Presenter extends BasePresenter {
        List<Contact> fetchContact(ContentResolver cr);
    }
}
