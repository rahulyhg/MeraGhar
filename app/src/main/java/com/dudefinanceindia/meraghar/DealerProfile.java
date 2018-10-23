package com.dudefinanceindia.meraghar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DealerProfile extends AppCompatActivity implements View.OnClickListener{

    private EditText registered_number_et, name_et, alternate_number_et, state_et, city_et,
            pin_code_et;

    private Button submit_b;
    private ImageButton back_ib;
    private SpinKitView loading;
    private RelativeLayout profile_verification_rl;

    private FirebaseAuth mAuth;
    private final static String DEALER_PROFILES = "dealer_profiles";
    private final static String PROFILE = "profile";
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(DEALER_PROFILES);

    private final String ALTERNATE_NUMBER = "alternate_number";
    private final String STATE = "state";
    private final String CITY = "city";
    private final String PIN_CODE = "pin_code";
    private final String NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_profile);

        alternate_number_et = findViewById(R.id.dp_alternate_number_et);
        name_et = findViewById(R.id.dp_name_et);
        registered_number_et = findViewById(R.id.dp_registered_number_et);
        state_et = findViewById(R.id.dp_state_et);
        city_et = findViewById(R.id.dp_city_et);
        pin_code_et = findViewById(R.id.dp_pin_code_et);
        back_ib = findViewById(R.id.dp_back_ib);
        submit_b = findViewById(R.id.dp_submit_b);
        profile_verification_rl = findViewById(R.id.dp_profile_verification_rl);
        loading = findViewById(R.id.dp_spin_kit);

        mAuth = FirebaseAuth.getInstance();

        submit_b.setOnClickListener(this);
        back_ib.setOnClickListener(this);
        profile_verification_rl.setOnClickListener(this);

        LoadData();

    }

    protected void onStart(){
        super.onStart();
    }

    private void LoadData() {

        show();
        MySharedPrefs mySharedPrefs = new MySharedPrefs(DealerProfile.this);
        final String uid = mySharedPrefs.getUID();

        String registered_number = Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber();
        registered_number_et.setText(registered_number);
        registered_number_et.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_phone),null, getResources().getDrawable(R.drawable.ic_verified), null);

        databaseReference.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        alternate_number_et.setText(dataSnapshot.child(PROFILE).child(ALTERNATE_NUMBER).getValue(String.class));
                        name_et.setText(dataSnapshot.child(PROFILE).child(NAME).getValue(String.class));
                        city_et.setText(dataSnapshot.child(PROFILE).child(CITY).getValue(String.class));
                        state_et.setText(dataSnapshot.child(PROFILE).child(STATE).getValue(String.class));
                        pin_code_et.setText(dataSnapshot.child(PROFILE).child(PIN_CODE).getValue(String.class));
                        dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dismiss();
                    }
                });
    }

    private void submitProfileData() {
        MySharedPrefs mySharedPrefs = new MySharedPrefs(DealerProfile.this);
        final String uid = mySharedPrefs.getUID();

        String alternate_number, name, state, city, pin_code;
        alternate_number = alternate_number_et.getText().toString().trim();
        name = name_et.getText().toString().trim();
        state = state_et.getText().toString().trim();
        city = city_et.getText().toString().trim();
        pin_code = pin_code_et.getText().toString().trim();

        databaseReference.child(uid).child(PROFILE).child(NAME).setValue(name);
        databaseReference.child(uid).child(PROFILE).child(ALTERNATE_NUMBER).setValue(alternate_number);
        databaseReference.child(uid).child(PROFILE).child(STATE).setValue(state);
        databaseReference.child(uid).child(PROFILE).child(CITY).setValue(city);
        databaseReference.child(uid).child(PROFILE).child(PIN_CODE).setValue(pin_code);
        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
        dismiss();
    }



//    onclick
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
//            back button
            case R.id.dp_back_ib:
                DealerProfile.this.finish();
                break;
//            submit
            case R.id.dp_submit_b:
                show();
                new CheckNetworkConnection(DealerProfile.this, new CheckNetworkConnection.OnConnectionCallback() {
                    @Override
                    public void onConnectionSuccess() {
                        submitProfileData();
                    }
                    @Override
                    public void onConnectionFail(String msg) {
                        NoInternetConnectionAlert noInternetConnectionAlert = new NoInternetConnectionAlert(DealerProfile.this);
                        noInternetConnectionAlert.DisplayNoInternetConnection();
                        dismiss();
                    }
                }).execute();
                break;
            //                profile verification
            case R.id.dp_profile_verification_rl:
                startActivity(new Intent(DealerProfile.this, ProfileVerification.class));
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


//    ends
}
