package com.dudefinanceindia.meraghar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class DealerProfile extends AppCompatActivity {

    private EditText dp_registered_number_et, dp_name_et, dp_alternate_number_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_profile);
    }
}
