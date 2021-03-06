package com.dudefinanceindia.meraghar;

import android.app.Activity;
import android.content.Intent;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chaos.view.PinView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.serhatsurguvec.continuablecirclecountdownview.ContinuableCircleCountDownView;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import mehdi.sakout.fancybuttons.FancyButton;

import static android.content.Context.MODE_PRIVATE;

public class DealerPhoneLogin extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PhoneAuth";

    private EditText phone_number_et;
    private PinView codeText;
    private FancyButton verifyButton;
    private FancyButton sendButton;
    private FancyButton resendButton;
    private TextView statusText, wrong_number_tv, signed_in_as;
    private RelativeLayout otpRelativeLayout;
    private SpinKitView spinKitView;
    private static final String countryCode = "+91";
    private ContinuableCircleCountDownView continuableCircleCountDownView;
    private static final int OTP_TIME = 30000;
    private FirebaseAuth firebaseAuth;
    private ImageButton cancel_ib, info_ib;
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private final static String DEALER_PROFILES = "dealer_profiles";
    private final static String PROFILE = "profile";
    private final static String PHONE_NUMBER = "phone_number";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_phone_login);
        fetchIDs();
        TextWatcher();
        initialiseFirebaseAuth();
    }

    //    initializing firebase auth
    private void initialiseFirebaseAuth(){
        firebaseAuth = FirebaseAuth.getInstance();
    }
    //    initializing firebase auth


    //    text watcher
    private void TextWatcher() {
        phone_number_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 10){
                    sendButton.setEnabled(true);
                }
                else sendButton.setEnabled(false);
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });


        codeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 6){
                    verifyButton.setEnabled(true);
                }
                else verifyButton.setEnabled(false);
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
            }
        });
    }
//    text watcher

    //    getting ids
    private void fetchIDs() {
        phone_number_et = findViewById(R.id.phoneText);
        codeText = findViewById(R.id.codeText);
        verifyButton = findViewById(R.id.verifyButton);
        sendButton = findViewById(R.id.sendButton);
        resendButton = findViewById(R.id.resendButton);
        wrong_number_tv = findViewById(R.id.pl_wrong_number_tv);
        statusText = findViewById(R.id.pl_otp_text);
        otpRelativeLayout = findViewById(R.id.pl_otpRelativeLayout);
        spinKitView = findViewById(R.id.pl_spin_kit);
        continuableCircleCountDownView = findViewById(R.id.pl_circleCountDownView);
        cancel_ib = findViewById(R.id.pl_cancel_ib);

        sendButton.setEnabled(false);
        verifyButton.setEnabled(false);
        resendButton.setEnabled(false);

        sendButton.setOnClickListener(this);
        resendButton.setOnClickListener(this);
        verifyButton.setOnClickListener(this);
        wrong_number_tv.setOnClickListener(this);
        cancel_ib.setOnClickListener(this);
    }

    public void onStart(){
        super.onStart();
    }

    public void sendCode() {
        // ic
        String phoneNumber = phone_number_et.getText().toString();

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 10){
            Toast.makeText(DealerPhoneLogin.this, "incorrect Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }

        spinKitView.setVisibility(View.VISIBLE);
        phoneNumber = countryCode+phoneNumber;

        setUpVerificationCallbacksSendCode();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                DealerPhoneLogin.this,               // Activity (for callback binding)
                verificationCallbacks);
        // ic

    }

    //    to avoid abuse
    //when send button is used
    private void setUpVerificationCallbacksSendCode() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {

                        // sign out Button.setEnabled(true);
                        // statusText.setText("Signed In");
                        resendButton.setEnabled(false);
                        verifyButton.setEnabled(false);
                        codeText.setText("");

                        signInWithPhoneAuthCredentialPhone(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            new MaterialDialog.Builder(DealerPhoneLogin.this)
                                    .title("Invalid credential")
                                    .titleColor(getResources().getColor(R.color.black))
                                    .content(e.getLocalizedMessage())
                                    .contentColorRes(R.color.black)
                                    .positiveText("Try Again")
                                    .positiveColorRes(R.color.green)
                                    .backgroundColor(getResources().getColor(R.color.white))
                                    .icon(getResources().getDrawable(R.drawable.ic_warning))
                                    .show();
                            spinKitView.setVisibility(View.GONE);

                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            // Log.d(TAG, "SMS Quota exceeded.");
                            Toast.makeText(DealerPhoneLogin.this, "Too many attempts, try after some time", Toast.LENGTH_SHORT).show();
                            spinKitView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        spinKitView.setVisibility(View.GONE);
//                        disable edit text on code sent
                        phone_number_et.setEnabled(false);
                        wrong_number_tv.setVisibility(View.VISIBLE);
                        phone_number_et.setTextColor(getResources().getColor(R.color.gray));

                        otpRelativeLayout.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.SlideInUp)
                                .duration(700)
                                .repeat(0)
                                .playOn(otpRelativeLayout);

                        String phoneNumber = phone_number_et.getText().toString();


                        phoneNumber = countryCode+phoneNumber;

                        statusText.setVisibility(View.VISIBLE);

                        statusText.setText(String.format("OTP has been sent to %s. App will automatically detect the OTP if not please enter manually and verify.", phoneNumber));


                        phoneVerificationId = verificationId;
                        resendToken = token;

                        continuableCircleCountDownView.setVisibility(View.VISIBLE);
                        continuableCircleCountDownView.setTimer(OTP_TIME);
                        continuableCircleCountDownView.setListener(new ContinuableCircleCountDownView.OnCountDownCompletedListener() {
                            @Override
                            public void onTick(long passedMillis) {
                                Log.w(TAG, "Tick." + passedMillis);

                            }

                            @Override
                            public void onCompleted() {
                                Log.w(TAG, "Completed.");
                                resendButton.setEnabled(true);
                            }
                        });
                        continuableCircleCountDownView.start();


                        // verifyButton.setEnabled(true);
                        sendButton.setEnabled(false);
                        //  resendButton.setEnabled(true);

                    }
                };
    }


    //when re-send button is used
    private void setUpVerificationCallbacksReSendCode() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {

                        // sign out Button.setEnabled(true);
                        // statusText.setText("Signed In");
                        resendButton.setEnabled(false);
                        phone_number_et.setEnabled(false);
                        verifyButton.setEnabled(false);
                        wrong_number_tv.setVisibility(View.GONE);
                        codeText.setText("");

                        signInWithPhoneAuthCredentialPhone(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            new MaterialDialog.Builder(DealerPhoneLogin.this)
                                    .title("Invalid credential")
                                    .titleColor(getResources().getColor(R.color.black))
                                    .content(e.getLocalizedMessage())
                                    .contentColorRes(R.color.black)
                                    .positiveText("Okay")
                                    .positiveColorRes(R.color.green)
                                    .backgroundColor(getResources().getColor(R.color.white))
                                    .icon(getResources().getDrawable(R.drawable.ic_warning))
                                    .show();
                            spinKitView.setVisibility(View.GONE);

                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            // Log.d(TAG, "SMS Quota exceeded.");
                            Toast.makeText(DealerPhoneLogin.this, "Too many attempts, try after some time", Toast.LENGTH_SHORT).show();
                            spinKitView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        spinKitView.setVisibility(View.GONE);

                        String phoneNumber = phone_number_et.getText().toString();


                        phoneNumber = countryCode+phoneNumber;

                        statusText.setVisibility(View.VISIBLE);

                        continuableCircleCountDownView.cancel();
                        resendButton.setEnabled(false);
                        statusText.setText(String.format("New OTP has been re-sent to %s. App will automatically detect the OTP if not please enter manually and verify.", phoneNumber));

                        phoneVerificationId = verificationId;
                        resendToken = token;

                        continuableCircleCountDownView.setVisibility(View.VISIBLE);
                        continuableCircleCountDownView.setTimer(OTP_TIME);
                        continuableCircleCountDownView.setListener(new ContinuableCircleCountDownView.OnCountDownCompletedListener() {
                            @Override
                            public void onTick(long passedMillis) {
                                Log.w(TAG, "Tick." + passedMillis);

                            }

                            @Override
                            public void onCompleted() {
                                Log.w(TAG, "Completed.");
                                resendButton.setEnabled(true);
                            }
                        });
                        continuableCircleCountDownView.start();


                        // verifyButton.setEnabled(true);
                        sendButton.setEnabled(false);
                        //  resendButton.setEnabled(true);

                    }
                };
    }

    //verify code
    public void verifyCode() {
        //ic
        String code = codeText.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);

        signInWithPhoneAuthCredentialPhone(credential);
    }
    //verify code


    // resend code
    public void resendCode() {
        //ic
        String phoneNumber = phone_number_et.getText().toString();

        spinKitView.setVisibility(View.VISIBLE);

        phoneNumber = countryCode+phoneNumber;
        setUpVerificationCallbacksReSendCode();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                30,
                TimeUnit.SECONDS,
                DealerPhoneLogin.this,
                verificationCallbacks,
                resendToken);
    }
    // resend code


    //   firebase sign in with phone
    private void signInWithPhoneAuthCredentialPhone(PhoneAuthCredential credential) {
//          sign in with phone (no linking here, as google is not signed in)
        spinKitView.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(DealerPhoneLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // sign out Button.setEnabled(true);
                            codeText.setText("");
                            //  statusText.setText("Signed In");
                            resendButton.setEnabled(false);
                            verifyButton.setEnabled(false);
                            saveUserProfileDataInFirebaseDataBasePHONE(Objects.requireNonNull(task.getResult()).getUser());
                           // Toast.makeText(DealerPhoneLogin.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                        } else {
                            showSignInFailedMessage((Objects.requireNonNull(task.getException())).getLocalizedMessage());
                            spinKitView.setVisibility(View.GONE);
                        }
                    }
                });
    }
//   firebase sign in with phone


    //    firebase login failed message
    private void showSignInFailedMessage(String errorMessage){
        try{
            new MaterialDialog.Builder(DealerPhoneLogin.this)
                    .title("Sign-in Failed")
                    .content(Objects.requireNonNull(errorMessage))
                    .contentColorRes(R.color.black)
                    .titleColor(getResources().getColor(R.color.googleRed))
                    .titleColor(getResources().getColor(R.color.black))
                    .positiveText("Okay")
                    .positiveColorRes(R.color.black)
                    .backgroundColor(getResources().getColor(R.color.white))
                    .icon(getResources().getDrawable(R.drawable.ic_error_black))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
                    .show();
        }
        catch (Exception e) { e.printStackTrace(); }
    }
//    firebase login failed message


    //    saving data at firebase database
    private void saveUserProfileDataInFirebaseDataBasePHONE(final FirebaseUser user){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(DEALER_PROFILES).child(user.getUid()).child(PROFILE)
                .child(PHONE_NUMBER).setValue(user.getPhoneNumber())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String uid = user.getUid();
                        MySharedPrefs mySharedPrefs = new MySharedPrefs(DealerPhoneLogin.this);
                        mySharedPrefs.setLoginPrefs("true", uid);
                        spinKitView.setVisibility(View.GONE);
                        DealerPhoneLogin.this.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                spinKitView.setVisibility(View.GONE);
                showSignInFailedMessage(e.getMessage());
            }
        });

    }
//    saving data at firebase database


    //    onclick
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
//            send code button
            case R.id.sendButton:
                sendCode();
                break;
//            verify code
            case R.id.verifyButton:
                verifyCode();
                break;
//            resend code
            case R.id.resendButton:
                resendCode();
                break;
//                wrong number
            case R.id.pl_wrong_number_tv:
                wrong_number_tv.setVisibility(View.GONE);
                phone_number_et.setText(null);
                phone_number_et.setEnabled(true);
                resendButton.setEnabled(false);
                sendButton.setEnabled(true);
                statusText.setVisibility(View.GONE);
                codeText.setText(null);
                otpRelativeLayout.setVisibility(View.GONE);
                phone_number_et.setTextColor(Color.BLACK);
                continuableCircleCountDownView.cancel();
                continuableCircleCountDownView.setVisibility(View.GONE);
                break;
//                cancel button
            case R.id.pl_cancel_ib:
                DealerPhoneLogin.this.finish();
                break;

        }

    }
    //    onclick

//    end
}
