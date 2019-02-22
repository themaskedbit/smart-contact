package com.themaskedbit.tempcontact.presenter;

import com.themaskedbit.tempcontact.TempContract;

public class TempPresenterImpl implements TempContract.Presenter {
    private final TempContract.View tempContractView;
    public TempPresenterImpl(TempContract.View tempContractView) {
        this.tempContractView = tempContractView;
        this.tempContractView.setPresenter(this);
    }
    @Override
    public void textChanged() {

    }
}
