package com.themaskedbit.tempcontact;

import com.themaskedbit.tempcontact.presenter.BasePresenter;
import com.themaskedbit.tempcontact.view.BaseView;

public interface TempContract {
    interface View extends BaseView<Presenter> {

    }
    interface Presenter extends BasePresenter {
        void textChanged();
    }
}
