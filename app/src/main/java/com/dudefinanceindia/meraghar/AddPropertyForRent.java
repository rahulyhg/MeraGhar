package com.dudefinanceindia.meraghar;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class AddPropertyForRent extends AppCompatActivity implements View.OnClickListener, ChooseLocationFromMap.SelectedLocationListener {

    private MaterialSpinner furnished_s;
    private EditText price_et, bedrooms_et, bathrooms_et, builtup_area_et, carpet_area_et,
            total_floors_et, floor_for_rent_et, address_et;
    private ImageView image_1_iv, image_2_iv, image_3_iv, image_4_iv, image_5_iv, image_6_iv, image_7_iv, image_8_iv, image_9_iv, image_10_iv;
    private RadioRealButtonGroup bachelor_rb;
    private ImageButton built_info_ib, back_ib, cancel1_ib, cancel2_ib, cancel3_ib, cancel4_ib, cancel5_ib, cancel6_ib, cancel7_ib, cancel8_ib, cancel9_ib, cancel10_ib;
    private ImageView success_1, success_2, success_3, success_4, success_5, success_6, success_7, success_8, success_9, success_10;

    String furnish = "Not Known";
    private String bachelor_allowed = "yes";

    private LinearLayout linearLayout_next_five_images;

    private static final String PROPERTIES = "properties";
    private static final String RENT = "rent";
    private final String ADDRESS = "address";
    private final String LOCATION = "LOCATION";
    private final String LATITUDE = "latitude";
    private final String LONGITUDE = "Longitude";
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
    private RelativeLayout relativeLayout_location;
    private TextView location_tv;

    SpinKitView loading, loading1, loading2, loading3, loading4, loading5, loading6, loading7, loading8, loading9, loading10;
    int images_successfully_loaded = 0, uri_count = 0;

    int image_tag, image_picker_decider, cancel_tag;
    Uri ImageFilePath1, ImageFilePath2, ImageFilePath3, ImageFilePath4, ImageFilePath5, ImageFilePath6, ImageFilePath7, ImageFilePath8, ImageFilePath9, ImageFilePath10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property_for_rent);

        relativeLayout_location = findViewById(R.id.fr_rl_1);
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
        image_6_iv = findViewById(R.id.fr_image_6_iv);
        image_7_iv = findViewById(R.id.fr_image_7_iv);
        image_8_iv = findViewById(R.id.fr_image_8_iv);
        image_9_iv = findViewById(R.id.fr_image_9_iv);
        image_10_iv = findViewById(R.id.fr_image_10_iv);
        bachelor_rb = findViewById(R.id.fr_radio_bachelor);
        submit_b = findViewById(R.id.fr_submit_b);
        loading = findViewById(R.id.fr_spin_kit);
        cancel1_ib = findViewById(R.id.fr_cancel_image_1);
        cancel2_ib = findViewById(R.id.fr_cancel_image_2);
        cancel3_ib = findViewById(R.id.fr_cancel_image_3);
        cancel4_ib = findViewById(R.id.fr_cancel_image_4);
        cancel5_ib = findViewById(R.id.fr_cancel_image_5);
        cancel6_ib = findViewById(R.id.fr_cancel_image_6);
        cancel7_ib = findViewById(R.id.fr_cancel_image_7);
        cancel8_ib = findViewById(R.id.fr_cancel_image_8);
        cancel9_ib = findViewById(R.id.fr_cancel_image_9);
        cancel10_ib = findViewById(R.id.fr_cancel_image_10);
        loading1 = findViewById(R.id.fr_spin_kit1);
        loading2 = findViewById(R.id.fr_spin_kit2);
        loading3 = findViewById(R.id.fr_spin_kit3);
        loading4 = findViewById(R.id.fr_spin_kit4);
        loading5 = findViewById(R.id.fr_spin_kit5);
        loading6 = findViewById(R.id.fr_spin_kit6);
        loading7 = findViewById(R.id.fr_spin_kit7);
        loading8 = findViewById(R.id.fr_spin_kit8);
        loading9 = findViewById(R.id.fr_spin_kit9);
        loading10 = findViewById(R.id.fr_spin_kit10);
        success_1 = findViewById(R.id.fr_success_1);
        success_2 = findViewById(R.id.fr_success_2);
        success_3 = findViewById(R.id.fr_success_3);
        success_4 = findViewById(R.id.fr_success_4);
        success_5 = findViewById(R.id.fr_success_5);
        success_6 = findViewById(R.id.fr_success_6);
        success_7 = findViewById(R.id.fr_success_7);
        success_8 = findViewById(R.id.fr_success_8);
        success_9 = findViewById(R.id.fr_success_9);
        success_10 = findViewById(R.id.fr_success_10);
        location_tv = findViewById(R.id.fr_location_tv);
        back_ib = findViewById(R.id.fr_back_ib);
        address_et = findViewById(R.id.fr_address_et);
        built_info_ib = findViewById(R.id.fr_built_info_ib);

        relativeLayout_location = findViewById(R.id.fr_rl_1);

        spinners();
        radioRealButtons();

        submit_b.setOnClickListener(this);
        image_1_iv.setOnClickListener(this);
        image_2_iv.setOnClickListener(this);
        image_3_iv.setOnClickListener(this);
        image_4_iv.setOnClickListener(this);
        image_5_iv.setOnClickListener(this);
        image_6_iv.setOnClickListener(this);
        image_7_iv.setOnClickListener(this);
        image_8_iv.setOnClickListener(this);
        image_9_iv.setOnClickListener(this);
        image_10_iv.setOnClickListener(this);
        cancel1_ib.setOnClickListener(this);
        cancel2_ib.setOnClickListener(this);
        cancel3_ib.setOnClickListener(this);
        cancel4_ib.setOnClickListener(this);
        cancel5_ib.setOnClickListener(this);
        cancel6_ib.setOnClickListener(this);
        cancel7_ib.setOnClickListener(this);
        cancel8_ib.setOnClickListener(this);
        cancel9_ib.setOnClickListener(this);
        cancel10_ib.setOnClickListener(this);
        loading1.setOnClickListener(this);
        loading2.setOnClickListener(this);
        loading3.setOnClickListener(this);
        loading4.setOnClickListener(this);
        loading5.setOnClickListener(this);
        loading6.setOnClickListener(this);
        loading7.setOnClickListener(this);
        loading8.setOnClickListener(this);
        loading9.setOnClickListener(this);
        loading10.setOnClickListener(this);
        relativeLayout_location.setOnClickListener(this);
        back_ib.setOnClickListener(this);
        built_info_ib.setOnClickListener(this);

    }

     protected void onStart(){
        super.onStart();
        MySharedPrefs mySharedPrefs = new MySharedPrefs(AddPropertyForRent.this);
        String location = mySharedPrefs.getLocationFromMap();
        if (!TextUtils.isEmpty(location)){
            location_tv.setText(location);
            location_tv.setTextColor(getResources().getColor(R.color.appColor));
        }
    }

//    getting result from map fragment
    public void onLocationSelected(String location, String latitude, String longitude) {
        if (!TextUtils.isEmpty(location)){
            location_tv.setText(location);
            location_tv.setTextColor(getResources().getColor(R.color.appColor));
        }
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
                        cancel1_ib.setVisibility(View.VISIBLE);
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
                    cancel1_ib.setVisibility(View.GONE);
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
                    if (cancel_tag != 6){
                        image_picker_decider = 5;
                        cancel5_ib.setVisibility(View.VISIBLE);
                    }
                    cancel4_ib.setVisibility(View.GONE);
                    ImageFilePath5 = result.getUri();
                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath5)
                            .centerCrop()
                            .fit()
                            .into(image_5_iv);
                }
                else if (image_tag == 6){
                    if (cancel_tag != 6){
                        image_picker_decider = 6;
                        cancel6_ib.setVisibility(View.VISIBLE);
                    }
                    cancel5_ib.setVisibility(View.GONE);
                    ImageFilePath6 = result.getUri();
                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath6)
                            .centerCrop()
                            .fit()
                            .into(image_6_iv);
                }
                else if (image_tag == 7){
                    if (cancel_tag != 7){
                        image_picker_decider = 7;
                        cancel7_ib.setVisibility(View.VISIBLE);
                    }
                    cancel6_ib.setVisibility(View.GONE);
                    ImageFilePath7 = result.getUri();
                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath7)
                            .centerCrop()
                            .fit()
                            .into(image_7_iv);
                }
                else if (image_tag == 8){
                    if (cancel_tag != 8){
                        image_picker_decider = 8;
                        cancel8_ib.setVisibility(View.VISIBLE);
                    }
                    cancel7_ib.setVisibility(View.GONE);
                    ImageFilePath8 = result.getUri();
                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath8)
                            .centerCrop()
                            .fit()
                            .into(image_8_iv);
                }
                else if (image_tag == 9){
                    if (cancel_tag != 9){
                        image_picker_decider = 9;
                        cancel9_ib.setVisibility(View.VISIBLE);
                    }
                    cancel8_ib.setVisibility(View.GONE);
                    ImageFilePath9 = result.getUri();
                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath9)
                            .centerCrop()
                            .fit()
                            .into(image_9_iv);
                }
                else if (image_tag == 10){
                    cancel9_ib.setVisibility(View.GONE);
                    image_picker_decider = 10;
                    ImageFilePath10 = result.getUri();
                    cancel10_ib.setVisibility(View.VISIBLE);
                    Picasso.with(AddPropertyForRent.this)
                            .load(ImageFilePath10)
                            .centerCrop()
                            .fit()
                            .into(image_10_iv);
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
        String price, bedrooms, bathrooms, builtup_area, carpet_area, total_floors, floor_for_rent, location, latitude, longitude, address;
        MySharedPrefs mySharedPrefs = new MySharedPrefs(AddPropertyForRent.this);
        location = mySharedPrefs.getLocationFromMap();
        latitude = mySharedPrefs.getLatitude();
        longitude = mySharedPrefs.getLongitude();
        price = price_et.getText().toString().trim();
        address = address_et.getText().toString().trim();
        bedrooms = bedrooms_et.getText().toString().trim();
        bathrooms = bathrooms_et.getText().toString().trim();
        builtup_area = builtup_area_et.getText().toString().trim();
        carpet_area = carpet_area_et.getText().toString().trim();
        total_floors = total_floors_et.getText().toString().trim();
        floor_for_rent = floor_for_rent_et.getText().toString().trim();

        if (!checkConditions(address, price, bedrooms, bathrooms, location, latitude, longitude)){
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
        if (ImageFilePath6 != null){
            uri_count++;
        }
        if (ImageFilePath7 != null){
            uri_count++;
        }
        if (ImageFilePath8 != null){
            uri_count++;
        }
        if (ImageFilePath9 != null){
            uri_count++;
        }
        if (ImageFilePath10 != null){
            uri_count++;
        }

        cancel1_ib.setVisibility(View.GONE);
        cancel2_ib.setVisibility(View.GONE);
        cancel3_ib.setVisibility(View.GONE);
        cancel4_ib.setVisibility(View.GONE);
        cancel5_ib.setVisibility(View.GONE);
        cancel6_ib.setVisibility(View.GONE);
        cancel7_ib.setVisibility(View.GONE);
        cancel8_ib.setVisibility(View.GONE);
        cancel9_ib.setVisibility(View.GONE);
        cancel10_ib.setVisibility(View.GONE);

        databaseReference = databaseReference1.child(Objects.requireNonNull(key));

        databaseReference.child(ADDRESS).setValue(address);
        databaseReference.child(PRICE).setValue(price);
        databaseReference.child(BEDROOMS).setValue(bedrooms);
        databaseReference.child(BATHROOMS).setValue(bathrooms);
        databaseReference.child(BUILTUPAREA).setValue(builtup_area);
        databaseReference.child(CARPET_AREA).setValue(carpet_area);
        databaseReference.child(TOTAL_FLOORS).setValue(total_floors);
        databaseReference.child(FLOOR_FOR_RENT).setValue(floor_for_rent);
        databaseReference.child(FURNISHED).setValue(furnish);
        databaseReference.child(BACHELOR_ALLOWED).setValue(bachelor_allowed);
        databaseReference.child(LOCATION).setValue(location);
        databaseReference.child(LATITUDE).setValue(latitude);
        databaseReference.child(LONGITUDE).setValue(longitude);

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
    private Boolean checkConditions(String address, String  price, String bedrooms, String bathrooms, String location, String latitude, String longitude) {

        if (TextUtils.isEmpty(address)){
            Toast.makeText(this, "Enter address", Toast.LENGTH_SHORT).show();
            return false;
        }

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
        if (TextUtils.isEmpty(location)){
            Toast.makeText(this, "Select Location Name from Map", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)){
            Toast.makeText(this, "Unable to get longitude and latitude. Select Map Location Again!", Toast.LENGTH_LONG).show();
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
                if (ImageFilePath5 != null){
                    selectImageToUpload();
                    image_tag = 5;
                    return;
                }
                if (cancel5_ib.getVisibility() == View.VISIBLE){
                    selectImageToUpload();
                    cancel_tag = 6;
                    image_tag = 5;
                    return;
                }
                if (image_picker_decider != 4){
                    Toast.makeText(this, "select image 4", Toast.LENGTH_SHORT).show();
                    return;
                }
                image_tag = 5;
                selectImageToUpload();
                break;
            case R.id.fr_image_6_iv:
                if (ImageFilePath6 != null){
                    selectImageToUpload();
                    image_tag = 6;
                    return;
                }
                if (cancel6_ib.getVisibility() == View.VISIBLE){
                    selectImageToUpload();
                    cancel_tag = 6;
                    image_tag = 6;
                    return;
                }
                if (image_picker_decider != 5){
                    Toast.makeText(this, "select image 5", Toast.LENGTH_SHORT).show();
                    return;
                }
                image_tag = 6;
                selectImageToUpload();
                break;
            case R.id.fr_image_7_iv:
                if (ImageFilePath7 != null){
                    selectImageToUpload();
                    image_tag = 7;
                    return;
                }
                if (cancel6_ib.getVisibility() == View.VISIBLE){
                    selectImageToUpload();
                    cancel_tag = 6;
                    image_tag = 7;
                    return;
                }
                if (image_picker_decider != 6){
                    Toast.makeText(this, "select image 6", Toast.LENGTH_SHORT).show();
                    return;
                }
                image_tag = 7;
                selectImageToUpload();
                break;
            case R.id.fr_image_8_iv:
                if (ImageFilePath8 != null){
                    selectImageToUpload();
                    image_tag = 8;
                    return;
                }
                if (cancel8_ib.getVisibility() == View.VISIBLE){
                    selectImageToUpload();
                    cancel_tag = 6;
                    image_tag = 8;
                    return;
                }
                if (image_picker_decider != 7){
                    Toast.makeText(this, "select image 7", Toast.LENGTH_SHORT).show();
                    return;
                }
                image_tag = 8;
                selectImageToUpload();
                break;
            case R.id.fr_image_9_iv:
                if (ImageFilePath9 != null){
                    selectImageToUpload();
                    image_tag = 9;
                    return;
                }
                if (cancel8_ib.getVisibility() == View.VISIBLE){
                    selectImageToUpload();
                    cancel_tag = 6;
                    image_tag = 9;
                    return;
                }
                if (image_picker_decider != 8){
                    Toast.makeText(this, "select image 8", Toast.LENGTH_SHORT).show();
                    return;
                }
                image_tag = 9;
                selectImageToUpload();
                break;
            case R.id.fr_image_10_iv:
                if (image_picker_decider != 9){
                    Toast.makeText(this, "select image 9", Toast.LENGTH_SHORT).show();
                    return;
                }
                image_tag = 10;
                selectImageToUpload();
                break;
            case R.id.fr_cancel_image_1:
                ImageFilePath1 = null;
                image_1_iv.setImageResource(0);
                cancel1_ib.setVisibility(View.GONE);
                break;
            case R.id.fr_cancel_image_2:
                cancel1_ib.setVisibility(View.VISIBLE);
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
            case R.id.fr_cancel_image_6:
                cancel5_ib.setVisibility(View.VISIBLE);
                ImageFilePath6 = null;
                image_6_iv.setImageResource(0);
                cancel6_ib.setVisibility(View.GONE);
                image_picker_decider = 5;
                break;
            case R.id.fr_cancel_image_7:
                cancel6_ib.setVisibility(View.VISIBLE);
                ImageFilePath7 = null;
                image_7_iv.setImageResource(0);
                cancel7_ib.setVisibility(View.GONE);
                image_picker_decider = 6;
                break;
            case R.id.fr_cancel_image_8:
                cancel7_ib.setVisibility(View.VISIBLE);
                ImageFilePath8 = null;
                image_8_iv.setImageResource(0);
                cancel8_ib.setVisibility(View.GONE);
                image_picker_decider = 7;
                break;
            case R.id.fr_cancel_image_9:
                cancel8_ib.setVisibility(View.VISIBLE);
                ImageFilePath9 = null;
                image_9_iv.setImageResource(0);
                cancel9_ib.setVisibility(View.GONE);
                image_picker_decider = 8;
                break;
            case R.id.fr_cancel_image_10:
                cancel9_ib.setVisibility(View.VISIBLE);
                ImageFilePath10 = null;
                image_10_iv.setImageResource(0);
                cancel10_ib.setVisibility(View.GONE);
                image_picker_decider = 9;
                break;
            case R.id.fr_rl_1:
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container_add_property_rent, new ChooseLocationFromMap(), "location_map").addToBackStack("location_map").commit();
                break;
            case R.id.fr_back_ib:
                go_back();
                break;
            case R.id.fr_built_info_ib:
                new MaterialDialog.Builder(this)
                        .title("Information")
                        .titleColor(Color.BLACK)
                        .content("Carpet area is the area enclosed within the walls, actual area to lay the carpet.\n\n" +
                                "Built-up area is the carpet area plus the thickness of outer walls and the balcony.")
                        .contentColor(Color.BLACK)
                        .positiveText("Okay")
                        .positiveColor(getResources().getColor(R.color.lightGreen))
                        .icon(getResources().getDrawable(R.drawable.ic_state_city))
                        .backgroundColor(getResources().getColor(R.color.white))
                        .show();
                break;
        }

    }
    //    on click

//    go back
    private void go_back() {
        new MaterialDialog.Builder(this)
                .title("Cancel")
                .titleColor(Color.BLACK)
                .content("Are You Sure you do not want to add property for rent?")
                .contentColor(Color.BLACK)
                .positiveText("Yes")
                .positiveColor(getResources().getColor(R.color.red))
                .negativeText("No")
                .icon(getResources().getDrawable(R.drawable.ic_warning))
                .backgroundColor(getResources().getColor(R.color.white))
                .negativeColor(getResources().getColor(R.color.lightGreen))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                        AddPropertyForRent.this.finish();
                    }
                })
                .show();

    }
//    go back

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


        //        6
        try{
            loading6.setVisibility(View.VISIBLE);
            if (ImageFilePath6 != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference imageStorageReference = storageReference.child(PROPERTIES).child(RENT).child(dealer_uid)
                        .child(key).child("property_image_6.jpg");

                imageStorageReference.putFile(ImageFilePath6)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child(PROPERTIES).child(RENT).child(key).child("property_image_6")
                                                .setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading6.setVisibility(View.GONE);
                                                success_6.setVisibility(View.VISIBLE);
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
                                loading6.setVisibility(View.GONE);
                                success_6.setVisibility(View.VISIBLE);
                                success_6.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
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
            loading6.setVisibility(View.GONE);
            success_6.setVisibility(View.VISIBLE);
            success_6.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
            images_successfully_loaded++;
        }


        //        7
        try{
            loading7.setVisibility(View.VISIBLE);
            if (ImageFilePath7 != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference imageStorageReference = storageReference.child(PROPERTIES).child(RENT).child(dealer_uid)
                        .child(key).child("property_image_7.jpg");

                imageStorageReference.putFile(ImageFilePath7)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child(PROPERTIES).child(RENT).child(key).child("property_image_7")
                                                .setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading7.setVisibility(View.GONE);
                                                success_7.setVisibility(View.VISIBLE);
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
                                loading7.setVisibility(View.GONE);
                                success_7.setVisibility(View.VISIBLE);
                                success_7.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
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
            loading7.setVisibility(View.GONE);
            success_7.setVisibility(View.VISIBLE);
            success_7.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
            images_successfully_loaded++;
        }


        //        8
        try{
            loading8.setVisibility(View.VISIBLE);
            if (ImageFilePath8 != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference imageStorageReference = storageReference.child(PROPERTIES).child(RENT).child(dealer_uid)
                        .child(key).child("property_image_8.jpg");

                imageStorageReference.putFile(ImageFilePath8)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child(PROPERTIES).child(RENT).child(key).child("property_image_8")
                                                .setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading8.setVisibility(View.GONE);
                                                success_8.setVisibility(View.VISIBLE);
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
                                loading8.setVisibility(View.GONE);
                                success_8.setVisibility(View.VISIBLE);
                                success_8.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
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


        //        9
        try{
            loading9.setVisibility(View.VISIBLE);
            if (ImageFilePath9 != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference imageStorageReference = storageReference.child(PROPERTIES).child(RENT).child(dealer_uid)
                        .child(key).child("property_image_9.jpg");

                imageStorageReference.putFile(ImageFilePath9)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child(PROPERTIES).child(RENT).child(key).child("property_image_9")
                                                .setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading9.setVisibility(View.GONE);
                                                success_9.setVisibility(View.VISIBLE);
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
                                loading9.setVisibility(View.GONE);
                                success_9.setVisibility(View.VISIBLE);
                                success_9.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
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
            loading9.setVisibility(View.GONE);
            success_9.setVisibility(View.VISIBLE);
            success_9.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
            images_successfully_loaded++;
        }


        //        10
        try{
            loading10.setVisibility(View.VISIBLE);
            if (ImageFilePath10 != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference imageStorageReference = storageReference.child(PROPERTIES).child(RENT).child(dealer_uid)
                        .child(key).child("property_image_10.jpg");

                imageStorageReference.putFile(ImageFilePath10)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url = uri.toString();

                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child(PROPERTIES).child(RENT).child(key).child("property_image_10")
                                                .setValue(url).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                loading10.setVisibility(View.GONE);
                                                success_10.setVisibility(View.VISIBLE);
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
                                loading10.setVisibility(View.GONE);
                                success_10.setVisibility(View.VISIBLE);
                                success_10.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
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
            loading10.setVisibility(View.GONE);
            success_10.setVisibility(View.VISIBLE);
            success_10.setBackground(getResources().getDrawable(R.drawable.ic_error_black));
            images_successfully_loaded++;
        }


    }//upload image



//    end
}
