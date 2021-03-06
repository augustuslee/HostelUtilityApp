package com.medeveloper.ayaz.hostelutility.officials;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.medeveloper.ayaz.hostelutility.R;
import com.medeveloper.ayaz.hostelutility.classes_and_adapters.StaffAdapter;
import com.medeveloper.ayaz.hostelutility.classes_and_adapters.StaffDetailsClass;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaffDetails extends Fragment {


    private DatabaseReference baseRef;
    private SweetAlertDialog pDialog;

    public StaffDetails() {
        // Required empty public constructor
    }


    View rootView;
    ArrayList<StaffDetailsClass> staffList;
    RecyclerView mRecyclerView;
    StaffAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_with_a_recycler, container, false);
        pDialog=new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
        pDialog.show();
        staffList=new ArrayList<>();
        mRecyclerView=rootView.findViewById(R.id.my_recycler_view);
        baseRef= FirebaseDatabase.getInstance().getReference(getString(R.string.college_id)).child(getString(R.string.hostel_id)).child(getString(R.string.staff_ref));
        /*String Array[]=getResources().getStringArray(R.array.complaint_array);
            for(int i=0;i<Array.length;i++)*/

        baseRef.keepSynced(true);
        baseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot d:dataSnapshot.getChildren())
                        staffList.add(d.getValue(StaffDetailsClass.class));

                    pDialog.dismiss();
                    if(staffList.size()>0)
                    {
                        (rootView.findViewById(R.id.no_data_found)).setVisibility(View.GONE);
                        (rootView.findViewById(R.id.no_data_found_text)).setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }

                    adapter=new StaffAdapter(getContext(),staffList);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    pDialog.dismiss();
                    mRecyclerView.setVisibility(View.GONE);
                    (rootView.findViewById(R.id.no_data_found)).setVisibility(View.VISIBLE);
                    (rootView.findViewById(R.id.no_data_found_text)).setVisibility(View.VISIBLE);
                    ((TextView)rootView.findViewById(R.id.no_data_found_text)).setText("It seems that there's no staff data yet");

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return rootView;
    }

}
