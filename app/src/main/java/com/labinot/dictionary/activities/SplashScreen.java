package com.labinot.dictionary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.labinot.dictionary.MainActivity;
import com.labinot.dictionary.R;

public class SplashScreen extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.SplashScreenTheme);
        super.onCreate(savedInstanceState);


        startActivity(new Intent(SplashScreen.this, MainActivity.class));
        finish();
    }
}
