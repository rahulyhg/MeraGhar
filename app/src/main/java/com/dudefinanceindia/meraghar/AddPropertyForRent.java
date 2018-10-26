package com.dudefinanceindia.meraghar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class AddPropertyForRent extends AppCompatActivity implements View.OnClickListener{

    private MaterialSpinner furnished_s;
    private EditText price_et, bedrooms_et, bathrooms_et, builtup_area_et, carpet_area_et,
            total_floors_et, floor_for_rent_et;
    private ImageView image_1_iv, image_2_iv, image_3_iv, image_4_iv, image_5_iv;
    private RadioRealButtonGroup bachelor_rb;
    private ImageButton cancell_ib, cancel2_ib, cancel3_ib, cancel4_ib, cancel5_ib;
    private ImageView success_1, success_2, success_3, success_4, success_5;

    String furnish = "Not Known";
    private String bachelor_allowed = "yes";

    private static final String PROPERTIES = "properties";
    private static final String RENT = "rent";
    private final String PRICE = "price";
    private final String FURNISHED = "furnished";
    private final String BEDROOMS = "bedrooms";
    private final String BATHROOMS = "bathrooms";
    private final String BUILTUPAREA = "builtup_area";
    private final String CARPET_AREA = "carpet_area";
    private final String TOTAL_FLOORS = "total_floors";
    private final String FLOOR_FOR_RENT = "floor_for_rent";
    private final String BACHELOR_ALLOWED = "bachelor_allowed";
    private DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child(PROPERTIES).child(RENT);
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private Button submit_b;

    SpinKitView loading, loading1, loading2, loading3, loading4, loading5;
    int images_successfully_loaded = 0, uri_count = 0;

    int image_tag, image_picker_decider, cancel_tag;
    Uri ImageFilePath1, ImageFilePath2, ImageFilePath3, ImageFilePath4, ImageFilePath5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_for_rent);

        furnished_s = findViewById(R.id.fr_furnished_s);
        price_et = findViewById(R.id.fr_price_et);
        bedrooms_et = findViewById(R.id.fr_bedrooms_et);
        bathrooms_et = findViewById(R.id.fr_bathrooms_et);
        builtup_area_et = findViewById(R.id.fr_builtup_area_et);
        carpet_area_et = findViewById(R.id.fr_carpet_area_et);
        total_floors_et = findViewById(R.id.fr_total_floors_et);
        floor_for_rent_et = findViewById(R.id.fr_floor_rent_et);
        image_1_iv = findViewById(R.id.fr_image_1_iv);
        image_2_iv = findViewById(R.id.fr_image_2_iv);
        image_3_iv = findViewById(R.id.fr_image_3_iv);
        image_4_iv = findViewById(R.id.fr_image_4_iv);
        image_5_iv = findViewById(R.id.fr_image_5_iv);
        bachelor_rb = findViewById(R.id.fr_radio_bachelor);
        submit_b = findViewById(R.id.fr_submit_b);
        loading = findViewById(R.id.fr_spin_kit);
        cancell_ib = findViewById(R.id.fr_cancel_image_1);
        cancel2_ib = findViewById(R.id.fr_cancel_image_2);
        cancel3_ib = findViewById(R.id.fr_cancel_image_3);
        cancel4_ib = findViewById(R.id.fr_cancel_image_4);
        cancel5_ib = findViewById(R.id.fr_cancel_image_5);
        loading1 = findViewById(R.id.fr_spin_kit1);
        loading2 = findViewById(R.id.fr_spin_kit2);
        loading3 = findViewById(R.id.fr_spin_kit3);
        loading4 = findViewById(R.id.fr_spin_kit4);
        loading5 = findViewById(R.id.fr_spin_kit5);
        success_1 = findViewById(R.id.fr_success_1);
        success_2 = findViewById(R.id.fr_success_2);
        success_3 = findViewById(R.id.fr_success_3);
        success_4 = findViewById(R.id.fr_success_4);
        success_5 = findViewById(R.id.fr_success_5);


        spinners();
        radioRealButtons();

        submit_b.setOnClickListener(this);
        image_1_iv.setOnClickListener(this);
        image_2_iv.setOnClickListener(this);
        image_3_iv.setOnClickListener(this);
        image_4_iv.setOnClickListener(this);
        image_5_iv.setOnClickListener(this);
        cancell_ib.setOnClickListener(this);
        cancel2_ib.setOnClickListener(this);
        cancel3_ib.setOnClickListener(this);
        cancel4_ib.setOnClickListener(this);
        cancel5_ib.setOnClickListener(this);
        loading1.setOnClickListener(this);
        loading2.setOnClickListener(this);
        loading3.setOnClickListener(this);
        loading4.setOnClickListener(this);
        loading5.setOnClickListener(this);
    }

//    bachelor button
    private void radioRealButtons() {
        bachelor_rb.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
            @Override
            public void onClickedButton(RadioRealButton button, int position) {
               if (position == 0){
                   bachelor_allowed = "yes";
               }
               else if (position == 1){
                    bachelor_allowed = "no";
                }
            }
        });
    }
//    bachelor button


    //    select image to upload to firebase storage
    private void selectImageToUpload() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                .setOutputCompressQuality(40)
                .setInitialCropWindowPaddingRatio(0)
                .start(AddPropertyForRent.this);
    }
//    select image to upload to firebase storage

    //wait for result after selecting image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (image_tag == 1){
                    if (cancel_tag != 6){
                        image_picker_decider = 1;
                        cancell_ib.setVisibility(View.VISIBLE);
                    }

                    ImageFilePath1 = result.getUri();

                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath1)
                            .centerCrop()
                            .fit()
                            .into(image_1_iv);
                }
                else if (image_tag == 2){
                    if (cancel_tag != 6){
                        image_picker_decider = 2;
                        cancel2_ib.setVisibility(View.VISIBLE);
                    }
                    cancell_ib.setVisibility(View.GONE);
                    ImageFilePath2 = result.getUri();
                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath2)
                            .centerCrop()
                            .fit()
                            .into(image_2_iv);
                }
                else if (image_tag == 3){
                    if (cancel_tag != 6){
                        image_picker_decider = 3;
                        cancel3_ib.setVisibility(View.VISIBLE);
                    }
                    cancel2_ib.setVisibility(View.GONE);
                    ImageFilePath3 = result.getUri();
                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath3)
                            .centerCrop()
                            .fit()
                            .into(image_3_iv);
                }
                else if (image_tag == 4){
                    if (cancel_tag != 6){
                        image_picker_decider = 4;
                        cancel4_ib.setVisibility(View.VISIBLE);
                    }
                    cancel3_ib.setVisibility(View.GONE);
                    ImageFilePath4 = result.getUri();
                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath4)
                            .centerCrop()
                            .fit()
                            .into(image_4_iv);
                }
                else if (image_tag == 5){
                    cancel4_ib.setVisibility(View.GONE);
                    image_picker_decider = 5;
                    ImageFilePath5 = result.getUri();
                    cancel5_ib.setVisibility(View.VISIBLE);
                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath5)
                            .centerCrop()
                            .fit()
                            .into(image_5_iv);
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                new MaterialDialog.Builder(AddPropertyForRent.this)
                        .title("Failed")
                        .content(error.getLocalizedMessage())
                        .contentColorRes(R.color.black)
                        .titleColor(getResources().getColor(R.color.googleRed))
                        .titleColor(getResources().getColor(R.color.black))
                        .positiveText("Try Again")
                        .positiveColorRes(R.color.black)
                        .backgroundColor(getResources().getColor(R.color.white))
                        .icon(getResources().getDrawable(R.drawable.ic_warning))
                        .show();
            }
        }
    }
        //wait for result after selecting image


    private void spinners() {
        furnished_s.setItems("Furnished", "Semi-Furnished", "Not-Furnished", "Not Known");
        furnished_s.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                furnish = item;
            }
        });
    }

    private void setData(){
        String price, bedrooms, bathrooms, builtup_area, carpet_area, total_floors, floor_for_rent;
        price = price_et.getText().toString().trim();
        bedrooms = bedrooms_et.getText().toString().trim();
        bathrooms = bathrooms_et.getText().toString().trim();
        builtup_area = builtup_area_et.getText().toString().trim();
        carpet_area = carpet_area_et.getText().toString().trim();
        total_floors = total_floors_et.getText().toString().trim();
        floor_for_rent = floor_for_rent_et.getText().toString().trim();

        if (!checkConditions(price, bedrooms, bathrooms)){
            dismiss();
            return;
        }
        final String key = databaseReference.push().getKey();

        if (TextUtils.isEmpty(key)){
            Toast.makeText(this, "Something went gone try again...", Toast.LENGTH_SHORT).show();
            dismiss();
            return;
        }

        images_successfully_loaded = uri_count = 0;

        if (ImageFilePath1 != null){
            uri_count++;
        }
        if (ImageFilePath2 != null){
            uri_count++;
        }
        if (ImageFilePath3 != null){
            uri_count++;
        }
        if (ImageFilePath4 != null){
            uri_count++;
        }
        if (ImageFilePath5 != null){
            uri_count++;
        }

        cancell_ib.setVisibility(View.GONE);
        cancel2_ib.setVisibility(View.GONE);
        cancel3_ib.setVisibility(View.GONE);
        cancel4_ib.setVisibility(View.GONE);
        cancel5_ib.setVisibility(View.GONE);

        databaseReference = databaseReference1.child(Objects.requireNonNull(key));

        databaseReference.child(PRICE).setValue(price);
        databaseReference.child(BEDROOMS).setValue(bedrooms);
        databaseReference.child(BATHROOMS).setValue(bathrooms);
        databaseReference.child(BUILTUPAREA).setValue(builtup_area);
        databaseReference.child(CARPET_AREA).setValue(carpet_area);
        databaseReference.child(TOTAL_FLOORS).setValue(total_floors);
        databaseReference.child(FLOOR_FOR_RENT).setValue(floor_for_rent);
        databaseReference.child(FURNISHED).setValue(furnish);
        databaseReference.child(BACHELOR_ALLOWED).setValue(bachelor_allowed);

        MySharedPrefs sharedPrefs = new MySharedPrefs(AddPropertyForRent.this);
        final String dealer_uid = sharedPrefs.getUID();
        DatabaseReference databaseReference_uid = FirebaseDatabase.getInstance().getReference();
        databaseReference_uid.child("dealer_profiles").child(dealer_uid).child("properties_fo_rent").child(key).setValue(key);
        databaseReference.child("posted_by").setValue(dealer_uid);

        dismiss();

        ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
        showMessage.successMessage("Property For Rent Added.", "Please wait while images are being uploaded.");

        UploadImageFileToFirebaseStorage(key, dealer_uid);

    }

//    check conditions
    private Boolean checkConditions(String  price, String bedrooms, String bathrooms) {

        if (TextUtils.isEmpty(price) || TextUtils.equals(price, "0")){
            Toast.makeText(this, "Enter Price", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(bedrooms)){
            Toast.makeText(this, "Enter Number of Bedroom", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(bathrooms)){
            Toast.makeText(this, "Enter Number of Bathroom", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
//    check conditions

    private void show(){
        loading.setVisibility(View.VISIBLE);
    }
    private void dismiss(){
        loading.setVisibility(View.GONE);
    }

//    on click
    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.fr_submit_b:
                new MaterialDialog.Builder(AddPropertyForRent.this)
                        .title("Confirm")
                        .titleColor(Color.BLACK)
                        .content("Are You Sure to Add This Property for rent?")
                        .contentColor(Color.BLACK)
                        .positiveText("Yes")
                        .positiveColor(getResources().getColor(R.color.red))
                        .negativeText("No")
                        .backgroundColor(getResources().getColor(R.color.white))
                        .negativeColor(getResources().getColor(R.color.lightGreen))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                                show();
                                new CheckNetworkConnection(AddPropertyForRent.this, new CheckNetworkConnection.OnConnectionCallback() {
                                    @Override
                                    public void onConnectionSuccess() {
                                        setData();
                                    }
                                    @Override
                                    public void onConnectionFail(String msg) {
                                        dismiss();
                                        NoInternetConnectionAlert noInternetConnectionAlert = new NoInternetConnectionAlert(AddPropertyForRent.this);
                                        noInternetConnectionAlert.DisplayNoInternetConnection();
                                    }
                                }).execute();
                            }
                        })
                        .show();
                break;

            case R.id.fr_image_1_iv:
                image_tag = 1;
                selectImageToUpload();
                break;
            case R.id.fr_image_2_iv:
                if (ImageFilePath2 != null){
                    selectImageToUpload();
                    image_tag = 2;
                    return;
                }

                if (cancel3_ib.getVisibility() == View.VISIBLE){
                    selectImageToUpload();
                    cancel_tag = 6;
                    image_tag = 2;
                    return;
                }

                if (image_picker_decider != 1){
                  Toast.makeText(this, "select image 1", Toast.LENGTH_SHORT).show();
                  return;
                }
                image_tag = 2;
                selectImageToUpload();
                break;
            case R.id.fr_image_3_iv:
                if (ImageFilePath3 != null){
                    selectImageToUpload();
                    image_tag = 3;
                    return;
                }
                if (cancel4_ib.getVisibility() == View.VISIBLE){
                    selectImageToUpload();
                    cancel_tag = 6;
                    image_tag = 3;
                    return;
                }

                if (image_picker_decider != 2){
                    Toast.makeText(this, "select image 2", Toast.LENGTH_SHORT).show();
                    return;
                }
                image_tag = 3;
                selectImageToUpload();
                break;
            case R.id.fr_image_4_iv:
                if (ImageFilePath4 != null){
                    selectImageToUpload();
                    image_tag = 4;
                    return;
                }
                if (cancel5_ib.getVisibility() == View.VISIBLE){
                    selectImageToUpload();
                    cancel_tag = 6;
                    image_tag = 4;
                    return;
                }
                if (image_picker_decider != 3){
                    Toast.makeText(this, "select image 3", Toast.LENGTH_SHORT).show();
                    return;
                }
                image_tag = 4;
                selectImageToUpload();
                break;
            case R.id.fr_image_5_iv:
                if (image_picker_decider != 4){
                    Toast.makeText(this, "select image 4", Toast.LENGTH_SHORT).show();
                    return;
                }
                image_tag = 5;
                selectImageToUpload();
                break;
            case R.id.fr_cancel_image_1:
                ImageFilePath1 = null;
                image_1_iv.setImageResource(0);
                cancell_ib.setVisibility(View.GONE);
                break;
            case R.id.fr_cancel_image_2:
                cancell_ib.setVisibility(View.VISIBLE);
                ImageFilePath2 = null;
                image_2_iv.setImageResource(0);
                cancel2_ib.setVisibility(View.GONE);
                image_picker_decider = 1;
                break;
            case R.id.fr_cancel_image_3:
                cancel2_ib.setVisibility(View.VISIBLE);
                ImageFilePath3 = null;
                image_3_iv.setImageResource(0);
                cancel3_ib.setVisibility(View.GONE);
                image_picker_decider = 2;
                break;
            case R.id.fr_cancel_image_4:
                cancel3_ib.setVisibility(View.VISIBLE);
                ImageFilePath4 = null;
                image_4_iv.setImageResource(0);
                cancel4_ib.setVisibility(View.GONE);
                image_picker_decider = 3;
                break;
            case R.id.fr_cancel_image_5:
                cancel4_ib.setVisibility(View.VISIBLE);
                ImageFilePath5 = null;
                image_5_iv.setImageResource(0);
                cancel5_ib.setVisibility(View.GONE);
                image_picker_decider = 4;
                break;
        }

    }
//    on click


    //    upload image
    public void UploadImageFileToFirebaseStorage(final String key, final String dealer_uid) {
//      1
        try{
            loading1.setVisibility(View.VISIBLE);
            if (ImageFilePath1 != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference imageStorageReference = storageReference.child(PROPERTIES).child(RENT).child(dealer_uid)
                        .child(key).child("property_image_1.jpg");

                imageStorageReference.putFile(ImageFilePath1)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {
                                        String url = uri.toString();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child(PROPERTIES).child(RENT).child(key).child("property_image_1")
                                                .setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading1.setVisibility(View.GONE);
                                                success_1.setVisibility(View.VISIBLE);
                                                images_successfully_loaded++;
                                                if (uri_count == images_successfully_loaded){
                                                    ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
                                                    showMessage.successMessage("Success", "Images uploaded Successfully");
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                loading1.setVisibility(View.GONE);
                                success_1.setVisibility(View.VISIBLE);
                                success_1.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
                                images_successfully_loaded++;
                                if (uri_count == images_successfully_loaded){
                                    ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
                                    showMessage.successMessage("Success", "Some images may not be uploaded.");
                                }
                            }
                        });
            }

        }
        catch (IllegalArgumentException | NullPointerException e){
            loading1.setVisibility(View.GONE);
            success_1.setVisibility(View.VISIBLE);
            success_1.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
            images_successfully_loaded++;
        }




//        2
        try{
            loading2.setVisibility(View.VISIBLE);
            if (ImageFilePath2 != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference imageStorageReference = storageReference.child(PROPERTIES).child(RENT).child(dealer_uid)
                        .child(key).child("property_image_2.jpg");

                imageStorageReference.putFile(ImageFilePath2)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child(PROPERTIES).child(RENT).child(key).child("property_image_2")
                                                .setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading2.setVisibility(View.GONE);
                                                success_2.setVisibility(View.VISIBLE);
                                                images_successfully_loaded++;
                                                if (uri_count == images_successfully_loaded){
                                                    ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
                                                    showMessage.successMessage("Success", "Images uploaded Successfully");
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                loading1.setVisibility(View.GONE);
                                success_1.setVisibility(View.VISIBLE);
                                success_1.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
                                images_successfully_loaded++;
                                if (uri_count == images_successfully_loaded){
                                    ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
                                    showMessage.successMessage("Success", "Some images may not be uploaded.");
                                }
                            }
                        });
            }

        }
        catch (IllegalArgumentException | NullPointerException e){
            loading2.setVisibility(View.GONE);
            success_2.setVisibility(View.VISIBLE);
            success_2.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
            images_successfully_loaded++;
        }



//        3
        try{
            loading3.setVisibility(View.VISIBLE);
            if (ImageFilePath3 != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference imageStorageReference = storageReference.child(PROPERTIES).child(RENT).child(dealer_uid)
                        .child(key).child("property_image_3.jpg");

                imageStorageReference.putFile(ImageFilePath3)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child(PROPERTIES).child(RENT).child(key).child("property_image_3")
                                                .setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading3.setVisibility(View.GONE);
                                                success_3.setVisibility(View.VISIBLE);
                                                images_successfully_loaded++;
                                                if (uri_count == images_successfully_loaded){
                                                    ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
                                                    showMessage.successMessage("Success", "Images uploaded Successfully");
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                loading3.setVisibility(View.GONE);
                                success_3.setVisibility(View.VISIBLE);
                                success_3.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
                                images_successfully_loaded++;
                                if (uri_count == images_successfully_loaded){
                                    ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
                                    showMessage.successMessage("Success", "Some images may not be uploaded.");
                                }
                            }
                        });
            }

        }
        catch (IllegalArgumentException | NullPointerException e){
            loading3.setVisibility(View.GONE);
            success_3.setVisibility(View.VISIBLE);
            success_3.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
            images_successfully_loaded++;
        }


        //        4
        try{
            loading4.setVisibility(View.VISIBLE);
            if (ImageFilePath4 != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference imageStorageReference = storageReference.child(PROPERTIES).child(RENT).child(dealer_uid)
                        .child(key).child("property_image_4.jpg");

                imageStorageReference.putFile(ImageFilePath4)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child(PROPERTIES).child(RENT).child(key).child("property_image_4")
                                                .setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading4.setVisibility(View.GONE);
                                                success_4.setVisibility(View.VISIBLE);
                                                images_successfully_loaded++;
                                                if (uri_count == images_successfully_loaded){
                                                    ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
                                                    showMessage.successMessage("Success", "Images uploaded Successfully");
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                loading4.setVisibility(View.GONE);
                                success_4.setVisibility(View.VISIBLE);
                                success_4.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
                                images_successfully_loaded++;
                                if (uri_count == images_successfully_loaded){
                                    ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
                                    showMessage.successMessage("Success", "Some images may not be uploaded.");
                                }
                            }
                        });
            }

        }
        catch (IllegalArgumentException | NullPointerException e){
            loading4.setVisibility(View.GONE);
            success_4.setVisibility(View.VISIBLE);
            success_4.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
            images_successfully_loaded++;
        }

        //        5
        try{
            loading5.setVisibility(View.VISIBLE);
            if (ImageFilePath5 != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference imageStorageReference = storageReference.child(PROPERTIES).child(RENT).child(dealer_uid)
                        .child(key).child("property_image_5.jpg");

                imageStorageReference.putFile(ImageFilePath5)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child(PROPERTIES).child(RENT).child(key).child("property_image_5")
                                                .setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading5.setVisibility(View.GONE);
                                                success_5.setVisibility(View.VISIBLE);
                                                images_successfully_loaded++;
                                                if (uri_count == images_successfully_loaded){
                                                    ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
                                                    showMessage.successMessage("Success", "Images uploaded Successfully");
                                                }
                                            }
                                        });
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                loading5.setVisibility(View.GONE);
                                success_5.setVisibility(View.VISIBLE);
                                success_5.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
                                images_successfully_loaded++;
                                if (uri_count == images_successfully_loaded){
                                    ShowMessage showMessage = new ShowMessage(AddPropertyForRent.this);
                                    showMessage.successMessage("Success", "Some images may not be uploaded.");
                                }
                            }
                        });
            }

        }
        catch (IllegalArgumentException | NullPointerException e){
            loading5.setVisibility(View.GONE);
            success_5.setVisibility(View.VISIBLE);
            success_5.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
            images_successfully_loaded++;
        }




    }//upload image



//    end
}
