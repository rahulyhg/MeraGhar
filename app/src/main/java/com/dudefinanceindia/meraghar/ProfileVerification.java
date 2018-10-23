package com.dudefinanceindia.meraghar;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;

import com.eralp.circleprogressview.CircleProgressView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

public class ProfileVerification extends AppCompatActivity implements View.OnClickListener{

    private FancyButton visiting_card_fb, upload_adhar_fb;
    private Button submit_b;
    private SpinKitView loading;
    private CircleProgressView profile_verification_cpv;
    private ImageButton back_ib;

    private final static String USER_PROFILES = "user_profiles";
    private final static String IMAGES = "images";
    private final static String VISITING_CARD = "visiting_card";
    private final static String AADHAR_IMAGE = "aadhar_image";
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(USER_PROFILES);
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_verification);

        upload_adhar_fb = findViewById(R.id.pv_upload_adhar_fb);
        visiting_card_fb = findViewById(R.id.pv_upload_visiting_card_fb);
        submit_b = findViewById(R.id.pv_submit_b);
        back_ib = findViewById(R.id.pv_back_ib);
        loading = findViewById(R.id.pv_spin_kit);
        profile_verification_cpv = findViewById(R.id.pv_profile_verification_cpv);
        profile_verification_cpv.setInterpolator(new AccelerateDecelerateInterpolator());

        upload_adhar_fb.setOnClickListener(this);
        visiting_card_fb.setOnClickListener(this);
        submit_b.setOnClickListener(this);
        back_ib.setOnClickListener(this);
    }
    public void onStart(){
        super.onStart();
        checkPercentageComplete();
    }


    //    check % profile completion
    private void checkPercentageComplete() {
        final String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        databaseReference.child(uid).child(IMAGES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;

                if (dataSnapshot.hasChild(VISITING_CARD)) {
                    count++;
                    visiting_card_fb.setText("uploaded");
                } else{
                    visiting_card_fb.setText("upload");
                }

                if (dataSnapshot.hasChild(AADHAR_IMAGE)){
                    count++;
                    upload_adhar_fb.setText("uploaded");
                } else{
                    upload_adhar_fb.setText("upload");
                }

                submit_b.setEnabled(false);
                submit_b.setBackgroundColor(getResources().getColor(R.color.buttonDisabledColor));
                if (count == 0){
                    profile_verification_cpv.setProgress(0);
                }
                else if (count == 1){
                    profile_verification_cpv.setProgressWithAnimation(40, 2000);
                }
                else if (count == 2){
                    profile_verification_cpv.setProgressWithAnimation(80, 2000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
//    check % profile completion

    //    onclick
    @Override
    public void onClick(View view) {
        show();
        int id = view.getId();
        switch (id){
            case R.id.pv_upload_adhar_fb:
                new CheckNetworkConnection(ProfileVerification.this, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_profile_verification, new UploadAadharCard(), "aadhar_card").addToBackStack("aadhar_card").commit();
                        dismiss();
                    }
                    @Override
                    public void onConnectionFail(String msg) {
                        NoInternetConnectionAlert noInternetConnectionAlert = new NoInternetConnectionAlert(ProfileVerification.this);
                        noInternetConnectionAlert.DisplayNoInternetConnection();
                        dismiss();
                    }
                }).execute();
                break;
            case R.id.pv_upload_visiting_card_fb:
                new CheckNetworkConnection(ProfileVerification.this, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_profile_verification, new UploadVisitingCard(), "visiting_card").addToBackStack("visiting_card").commit();
                        dismiss();
                    }
                    @Override
                    public void onConnectionFail(String msg) {
                        NoInternetConnectionAlert noInternetConnectionAlert = new NoInternetConnectionAlert(ProfileVerification.this);
                        noInternetConnectionAlert.DisplayNoInternetConnection();
                        dismiss();
                    }
                }).execute();
                break;
//                back button
            case R.id.pv_back_ib:
                ProfileVerification.this.finish();
                break;

        }

    }
//    onclick

    private void show(){
        loading.setVisibility(View.VISIBLE);
    }

    private void dismiss(){
        loading.setVisibility(View.GONE);
    }

//    end
}