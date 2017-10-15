package com.practice.android.firstaid.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.android.firstaid.Adapters.CityRecyclerAdapter;
import com.practice.android.firstaid.Models.UserInfo;
import com.practice.android.firstaid.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    //    private FirebaseAuth.AuthStateListener mAuthListener;
//    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabase;
    String UserID;
    //    private GoogleApiClient mGoogleApiClient;
    ArrayList<String> cityList;
    CityRecyclerAdapter cityRecyclerAdapter;
    RecyclerView cityRecycler;
    TextView nameTv, fa_btnTv, genderTv, dobTv, bgTv, phoneTv, langTv, noCity;
    Switch donateSwitch;
    LinearLayout cityView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        nameTv = view.findViewById(R.id.nameTv);
        fa_btnTv = view.findViewById(R.id.fa_btnTv);
        genderTv = view.findViewById(R.id.genderTv);
        dobTv = view.findViewById(R.id.dobTv);
        bgTv = view.findViewById(R.id.bgTv);
        phoneTv = view.findViewById(R.id.phoneTv);
        langTv = view.findViewById(R.id.langTv);
        cityRecycler = view.findViewById(R.id.city_recycler_profile);
        noCity = view.findViewById(R.id.noCity);
        cityView = view.findViewById(R.id.your_cities);
        cityView.setVisibility(View.GONE);


        donateSwitch = view.findViewById(R.id.donateSwitch);

        //handles alert dialog popup

//        donateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(getContext(), "Working!!", Toast.LENGTH_SHORT).show();
//                createAlertDialog();
//            }
//        });

//        donateSwitch.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                createAlertDialog();
//
//                return true;
//            }
//        });

        donateSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (donateSwitch.isChecked())
                    createAlertDialog();
                else
                    cityView.setVisibility(View.GONE);
            }
        });

        cityList = new ArrayList<>();
        cityRecyclerAdapter = new CityRecyclerAdapter(cityList);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            UserID = user.getUid();
            final String curremail = user.getEmail();
            Log.d("ProfileFragment", curremail);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("userinfo");

        getDetails();

        setHasOptionsMenu(true);
        Toolbar toolbar = view.findViewById(R.id.tool);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        toolbar.setLogo(R.drawable.settings);
        toolbar.inflateMenu(R.menu.menu_main);

        return view;
    }


//    @Override
//        public void OnCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            inflater.inflate(R.menu.menu_main, menu);
//            super.onCreateOptionsMenu(menu,inflater);
//
//        }

    public void getDetails() {
        mDatabase.child(UserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);

                ch(userInfo);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
// handle creating dialog and its functioning
    public void createAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_message).setTitle(R.string.title);

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                donateSwitch.setChecked(true);
                cityView.setVisibility(View.VISIBLE);

            }

        });

        builder.setNegativeButton(R.string.reject, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                donateSwitch.setChecked(false);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                donateSwitch.setChecked(false);
            }
        });


        dialog.show();


    }

    public void ch(UserInfo userInfo) {

        cityList.clear();

        cityList = (ArrayList<String>) userInfo.getCities();
        cityRecyclerAdapter = new CityRecyclerAdapter(cityList);
        cityRecyclerAdapter.notifyDataSetChanged();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        cityRecycler.setLayoutManager(linearLayoutManager);
        cityRecycler.setAdapter(cityRecyclerAdapter);
        if(cityList.isEmpty()){
                noCity.setVisibility(View.VISIBLE);
                cityRecycler.setVisibility(View.GONE);
            } else {
                noCity.setVisibility(View.GONE);
                cityRecycler.setVisibility(View.VISIBLE);
            }

        try {



//            if(cityList.isEmpty()){
//                noCity.setVisibility(View.VISIBLE);
//                cityRecycler.setVisibility(View.GONE);
//            } else {
//                noCity.setVisibility(View.GONE);
//                cityRecycler.setVisibility(View.VISIBLE);
//            }
//
//            noCity.setVisibility(View.GONE);
//            cityRecycler.setVisibility(View.VISIBLE);

            nameTv.setText(userInfo.getName());
            fa_btnTv.setText(userInfo.getBloodGroup());
            genderTv.setText(userInfo.getGender());
            dobTv.setText(userInfo.getDOB());
            bgTv.setText(userInfo.getBloodGroup());
            phoneTv.setText(userInfo.getPhoneNumber());
            langTv.setText(userInfo.getLanguages());

            if (userInfo.getInterestedinDonating().equals("true")) {
                donateSwitch.setChecked(true);
                cityView.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {
            Log.e("ProfileFragment: ", e.getMessage());
        }


    }

}


