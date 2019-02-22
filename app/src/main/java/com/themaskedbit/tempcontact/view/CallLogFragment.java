package com.themaskedbit.tempcontact.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.themaskedbit.tempcontact.CallLogContract;
import com.themaskedbit.tempcontact.model.Contact;
import com.themaskedbit.tempcontact.PermissionHandler;
import com.themaskedbit.tempcontact.PermissionHandlerAndroid;
import com.themaskedbit.tempcontact.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CallLogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CallLogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallLogFragment extends Fragment implements CallLogContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Contact> contactList = new ArrayList<>();
    private List<Contact> returnContacts;
    private RecyclerView recyclerView;
    private CallLogAdapter mAdapter;
    private FloatingActionButton floatingActionButton;
    private CallLogContract.Presenter presenter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final String[] NECESSARY_PERMISSIONS = new String[] {Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS };
    final int [] PERMISSION_CODE = new int[123];
    private String[] projection = new String[]{
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_PHOTO_URI,
            CallLog.Calls.DATE,
            CallLog.Calls.DURATION,
            CallLog.Calls.TYPE
    };
    private PermissionHandler permissionHandler;

    private OnFragmentInteractionListener mListener;

    public CallLogFragment() {
        // Required empty public constructor
    }


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
        permissionHandler = new PermissionHandlerAndroid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call_log, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recycler_view);

        floatingActionButton    = (FloatingActionButton) getActivity().findViewById(R.id.fab);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                                             @Override
                                             public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                                 super.onScrolled(recyclerView, dx, dy);
                                                 if(dy == 0){
                                                     floatingActionButton.hide();
                                                 }
                                                 else if (dy < 0) {
                                                     floatingActionButton.hide();
                                                 } else{
                                                     floatingActionButton.show();
                                                 }
                                             }
                                         });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                recyclerView.smoothScrollToPosition(0);
            }
        });

        mAdapter = new CallLogAdapter(contactList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        SwipeController swipeController = new SwipeController();
        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);
        if (permissionHandler.checkHasPermission(this.getActivity(),NECESSARY_PERMISSIONS)) {
            @SuppressLint("MissingPermission") Cursor c = getActivity().getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null,
                    null, CallLog.Calls.DATE + " DESC");
            returnContacts = presenter.fetchCallLog(c);
            addData(returnContacts);
        }
        else {
            permissionHandler.requestPermission(this.getActivity(), NECESSARY_PERMISSIONS,PERMISSION_CODE[0]);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permissionHandler.onPermissionResult(this.getActivity(),requestCode,permissions,grantResults,PERMISSION_CODE[0],new String[]{Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS})){
            @SuppressLint("MissingPermission") Cursor c = getActivity().getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null,
                    null, CallLog.Calls.DATE + " DESC");
            returnContacts=presenter.fetchCallLog(c);
            addData(returnContacts);
        }
    }

    @Override
    public void setPresenter(CallLogContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void addData(List<Contact> returnContacts) {
        for(Contact data : returnContacts) {
            contactList.add(data);
        }

        mAdapter.notifyDataSetChanged();
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



//    public static boolean hasPermissions(Context context, String... permissions) {
//        if (context != null && permissions != null) {
//            for (String permission : permissions) {
//                if (checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
}
