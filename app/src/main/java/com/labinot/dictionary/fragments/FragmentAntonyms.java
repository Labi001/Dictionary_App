package com.labinot.dictionary.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.labinot.dictionary.R;
import com.labinot.dictionary.activities.WordMeaning_Activity;
import com.labinot.dictionary.utils.Constants;

public class FragmentAntonyms extends Fragment {

    private AppCompatActivity appCompatActivity;

    public FragmentAntonyms(AppCompatActivity appCompatActivity) {

        this.appCompatActivity = appCompatActivity;
    }

    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_global_layout,container,false);


        TextView textView = view.findViewById(R.id.text);
        ImageView images_empty = view.findViewById(R.id.image_empty);

        SharedPreferences sharedPreferences = appCompatActivity.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        String text_size = sharedPreferences.getString(Constants.TEXT_SIZE_PREF,"15");
        textView.setTextSize(Float.parseFloat(text_size));

        String getText = ((WordMeaning_Activity)appCompatActivity).antonyms;

        if(((WordMeaning_Activity)appCompatActivity).checkDataEmpty(getText)){

            images_empty.setVisibility(View.VISIBLE);
            textView.setText(R.string.no_txt_found);

        }else{
            textView.setText(getText);
        }






        return view;
    }
}
