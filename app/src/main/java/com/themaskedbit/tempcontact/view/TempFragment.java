package com.themaskedbit.tempcontact.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.themaskedbit.tempcontact.R;
import com.themaskedbit.tempcontact.TempContract;
import com.themaskedbit.tempcontact.model.Contact;

import java.util.ArrayList;
import java.util.List;



public class TempFragment extends Fragment implements TempContract.View {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textView;
    private OnFragmentInteractionListener mListener;
    private TempContract.Presenter presenter;

    public TempFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TempFragment newInstance(String param1, String param2) {
        TempFragment fragment = new TempFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temp, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // TODO: Rename method, update argument and hook method into UI event

        //textView = (TextView) getActivity().findViewById(R.id.action_search);
        final List<Contact> ContactList = new ArrayList<>();
        final CallLogAdapter mAdapter = new CallLogAdapter(ContactList);
//        textView.addTextChangedListener(new TextWatcher() {
//           @Override
//           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//           }
//
//           @Override
//           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//           }
//
//           @Override
//           public void afterTextChanged(Editable editable) {
//               //REVISIT : Use the presenter TempPresenterImpl to do any update to model.
//               //List<Contact> filterContact = mAdapter.getFilter(editable.toString());
//           }
//       });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setPresenter(TempContract.Presenter presenter) {
        this.presenter =presenter;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
