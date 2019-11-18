package com.inz.z.dial_phone.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.inz.z.base.view.AbsBaseActivity;
import com.inz.z.dial_phone.view.widget.DialLayoutView;
import com.inz.z.dial_phone.view.widget.DialNumberView;

public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new DialLayoutView(this);
        setContentView(view);
    }
}
