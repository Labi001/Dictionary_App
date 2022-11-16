package com.labinot.dictionary.about;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.labinot.dictionary.BuildConfig;
import com.labinot.dictionary.R;

import java.util.Calendar;

public class AboutActivity extends AppCompatActivity {

    private RecyclerView about_recyclerView;
    private TextView copyright_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        about_recyclerView = findViewById(R.id.mRecyclerView);
        copyright_txt = findViewById(R.id.copyRightTextView);


        String version_number = getString(R.string.versionNumber, BuildConfig.VERSION_NAME);
        String cr_text = getString(R.string.copyRightMessage, Calendar.getInstance().get(Calendar.YEAR));
        copyright_txt.setText(cr_text);
        AboutModel aboutModel =new AboutModel(AboutActivity.this);
        aboutModel.addCustomItem(R.drawable.ic_information,getResources().getColor(R.color.primary_dark),version_number);
        aboutModel.addCustomItem(R.drawable.creative_hub,0,getString(R.string.creative_hub));
        aboutModel.addFacebook(getString(R.string.facebook_username),getString(R.string.facebook_message));
        aboutModel.addInstagram(getString(R.string.insta_username),getString(R.string.instagram_message));
        aboutModel.addGithub(getString(R.string.github_username),getString(R.string.github));
        aboutModel.addEmail(getString(R.string.email_address),getString(R.string.email_dev));
        aboutModel.addWebside(getString(R.string.website_url),getString(R.string.website));
        aboutModel.addYoutube(getString(R.string.youtube_channel_id),getString(R.string.sub_on_youtube));
        aboutModel.addPlayStore(getPackageName(),getString(R.string.play_store));

        AboutRecyclerAdapter aboutRecyclerAdapter =new AboutRecyclerAdapter(AboutActivity.this,aboutModel.getAboutModelsList());
        about_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        aboutRecyclerAdapter.setItemClickListener(new AboutRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position, Intent intent) {

                switch (position){

                    case 0:
                        SnackBarMessage(getString(R.string.latest_version));
                        break;
                    case 1:
                        SnackBarMessage(getString(R.string.join_the_academy));
                        break;
                    default:


                        if(intent != null){

                            try{

                                startActivity(intent);

                            }catch (ActivityNotFoundException e){

                                SnackBarMessage(getString(R.string.not_installed));

                            }

                        }

                }

            }
        });

        about_recyclerView.setAdapter(aboutRecyclerAdapter);


    }

    private void SnackBarMessage(String message) {

        Snackbar snackbar = Snackbar.make(findViewById(R.id.aboutParentLayout),message,Snackbar.LENGTH_SHORT);
        snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        snackbar.setBackgroundTint(getResources().getColor(R.color.accent));
        snackbar.setTextColor(Color.WHITE);
        snackbar.show();

    }
}