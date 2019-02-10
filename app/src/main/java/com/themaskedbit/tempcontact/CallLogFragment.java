package com.themaskedbit.tempcontact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentContainer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CallLogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CallLogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallLogFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Contact> contactList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAdapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final String[] NECESSARY_PERMISSIONS = new String[] {Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS };
    final int [] PERMISSION_CODE = new int[123];

    private OnFragmentInteractionListener mListener;

    public CallLogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CallLogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CallLogFragment newInstance(String param1, String param2) {
        CallLogFragment fragment = new CallLogFragment();
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
        return inflater.inflate(R.layout.fragment_call_log, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);

        mAdapter = new ContactsAdapter(contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        getCallDetails();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE[0]) {
            if (permissions[0].equals(Manifest.permission.READ_CALL_LOG)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[1].equals(Manifest.permission.READ_CONTACTS)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getCallDetails();
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getCallDetails() {
        String[] projection = new String[]{
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.CACHED_PHOTO_URI,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION,
                CallLog.Calls.TYPE
        };

        int count = 1;
        if (hasPermissions(this.getActivity(),NECESSARY_PERMISSIONS)) {
            //Permission is granted
            @SuppressLint("MissingPermission")
            Cursor c = getActivity().getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null,
                    null, CallLog.Calls.DATE + " DESC");

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
                        mAdapter.notifyDataSetChanged();
                    }
                    prevNumber = callerNumber;
                    preType = callType;
                }while(c.moveToNext());
                mAdapter.notifyDataSetChanged();
            }
        } else {
            //ask for permission
            requestPermissions(
                    NECESSARY_PERMISSIONS, PERMISSION_CODE[0]);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
