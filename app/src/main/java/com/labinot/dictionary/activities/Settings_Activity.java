package com.labinot.dictionary.activities;

import static com.labinot.dictionary.utils.Constants.ABOUT_KEY;
import static com.labinot.dictionary.utils.Constants.CLEAR_HISTORY_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.labinot.dictionary.R;
import com.labinot.dictionary.about.AboutActivity;
import com.labinot.dictionary.dataBaseHelper.DataBaseHelper;
import com.labinot.dictionary.utils.Constants;

import java.io.IOException;

public class Settings_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_in_left,R.anim.push_out_left);
            }
        });

        MySettingsFragment mySettingsFragment = new MySettingsFragment();
        mySettingsFragment.setActivity(Settings_Activity.this);
        getSupportFragmentManager().beginTransaction().replace(R.id.settings_container, mySettingsFragment).commit();

    }

    public static class MySettingsFragment extends PreferenceFragmentCompat implements PreferenceManager.OnPreferenceTreeClickListener,SharedPreferences.OnSharedPreferenceChangeListener {

        DataBaseHelper dataBaseHelper;
        private AppCompatActivity mParentActivity;
        private SharedPreferences sharedPreferences;
        private ListPreference listPreference;

        public void setActivity(AppCompatActivity appCompatActivity) {

            this.mParentActivity = appCompatActivity;
        }

        @Override
        public void onResume() {
            super.onResume();

            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {

            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {

            setPreferencesFromResource(R.xml.preferences, rootKey);
            dataBaseHelper = new DataBaseHelper(mParentActivity, null);
            sharedPreferences = getPreferenceManager().getSharedPreferences();
            listPreference = findPreference("text_size_key");

            SharedPreferences sharedPreferences = mParentActivity.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
            String text_size = sharedPreferences.getString(Constants.TEXT_SIZE_PREF,"15");

            listPreference.setSummary(getTextName(text_size));

        }

        @Override
        public boolean onPreferenceTreeClick(@NonNull Preference preference) {
            switch (preference.getKey()) {

                case CLEAR_HISTORY_KEY:
                    
                    dataBaseHelper.openDatabase();
                    showAlertDialog();

                    break;

                case ABOUT_KEY:
                    startActivity(new Intent(mParentActivity, AboutActivity.class));
                    mParentActivity.overridePendingTransition(R.anim.push_in_right_anim,R.anim.push_out_right);
                    break;
            }


            return false;
        }

        private String getTextName(String text_size) {

            switch (text_size) {
                case "10":
                    return "Tiny";
                case "15":
                    return "Normal";
                case "30":
                    return "Big";
                default:
                    return "Normal";
            }

        }

        private void showAlertDialog() {

            AlertDialog.Builder alertdialog = new AlertDialog.Builder(mParentActivity);
            alertdialog.setTitle("Are you sure ?");
            alertdialog.setMessage("All the history will be deleted");

            alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dataBaseHelper.deleteHistory();
                    Toast.makeText(mParentActivity, "List deleted successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                }
            });

            AlertDialog dialog = alertdialog.create();

            dialog.show();


        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            String value = sharedPreferences.getString(key,"");

            SharedPreferences my_pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = my_pref.edit();
            editor.putString(Constants.TEXT_SIZE_PREF,value);
            editor.apply();


            listPreference.setSummary(getTextName(value));

        }
    }



}