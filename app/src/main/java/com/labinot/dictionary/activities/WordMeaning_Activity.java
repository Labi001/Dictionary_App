package com.labinot.dictionary.activities;

import static com.labinot.dictionary.dataBaseHelper.DataBaseHelper.EN_WORD;
import static com.labinot.dictionary.utils.Constants.EN_WORD_TRANSITION;
import static com.labinot.dictionary.utils.Helper.FixFlicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.labinot.dictionary.R;
import com.labinot.dictionary.dataBaseHelper.DataBaseHelper;
import com.labinot.dictionary.fragments.FragmentAntonyms;
import com.labinot.dictionary.fragments.FragmentDefinition;
import com.labinot.dictionary.fragments.FragmentExample;
import com.labinot.dictionary.fragments.FragmentSynonyms;
import com.labinot.dictionary.utils.Constants;
import com.labinot.dictionary.utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordMeaning_Activity extends AppCompatActivity {

    String en_words;
    private DataBaseHelper db_helper;
    private Cursor mCursor = null;
    public String en_definition;
    public String example;
    public String synonyms;
    public String antonyms;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private FloatingActionButton btn_speak;
    private TextToSpeech textToSpeech;
    String[] mTabTitles = new String[]{"Definition","Synonyms","Antonyms","Example"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);
        
        initData();
        loadViews();
        initViewPagerTabLayout();
        readTheWord();


    }

    private void readTheWord() {

        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textToSpeech = new TextToSpeech(WordMeaning_Activity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {

                        if(status == TextToSpeech.SUCCESS){

                            int result = textToSpeech.setLanguage(Locale.getDefault());

                            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){

                                Log.e(Constants.TAG, "This language is not supported");
                            }else{

                                textToSpeech.speak(en_words,TextToSpeech.QUEUE_FLUSH,null,null);
                            }

                        }else{

                            Log.e(Constants.TAG, "Init Failed! - " + status);
                        }


                    }
                });

            }
        });

    }


    @SuppressLint("Range")
    private void initData() {

        supportPostponeEnterTransition();

        Bundle bundle = getIntent().getExtras();
        en_words = bundle.getString(EN_WORD);

        FixFlicker(WordMeaning_Activity.this,R.id.appBar_Layout);
        String transition = bundle.getString(EN_WORD_TRANSITION);
        findViewById(R.id.appBar_Layout).setTransitionName(transition);

        supportStartPostponedEnterTransition();

        db_helper = new DataBaseHelper(this,null);

        db_helper.openDatabase();

        mCursor = db_helper.getMeaning(en_words);
        if(mCursor.moveToFirst()){

            en_definition = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.EN_DEFINITION));
            example = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.EXAMPLE));
            synonyms = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.SYNONYMS));
            antonyms = mCursor.getString(mCursor.getColumnIndex(DataBaseHelper.ANTONYMS));

        }

        db_helper.insertHistory(en_words);

    }

    private void loadViews() {

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        btn_speak = findViewById(R.id.fab_btn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(en_words);

        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();

            }
        });
    }

    private void initViewPagerTabLayout() {

        tabLayout.setBackgroundColor(getResources().getColor(R.color.primary));
        tabLayout.setTabRippleColor(ColorStateList.valueOf(Color.WHITE));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.accent));
        tabLayout.setTabTextColors(Color.WHITE,Color.WHITE);


        setUpViewPager(viewPager);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                tab.setText(mTabTitles[position]);
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager2 viewPager) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPagerAdapter.addFragment(new FragmentDefinition(this));
        viewPagerAdapter.addFragment(new FragmentSynonyms(this));
        viewPagerAdapter.addFragment(new FragmentAntonyms(this));
        viewPagerAdapter.addFragment(new FragmentExample(this));

        viewPager.setAdapter(viewPagerAdapter);

    }

    public boolean checkDataEmpty(String text) {
        return text.equals("NA") || text.equals("NA,NA") || text.equals("NA,NA,NA") || text.equals("NA,NA,NA,NA");
    }

    public static class ViewPagerAdapter extends FragmentStateAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        void addFragment(Fragment fragment){

              mFragmentList.add(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }
    }
}