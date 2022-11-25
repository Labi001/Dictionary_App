package com.labinot.dictionary;

import static com.labinot.dictionary.dataBaseHelper.DataBaseHelper.EN_DEFINITION;
import static com.labinot.dictionary.dataBaseHelper.DataBaseHelper.EN_WORD;
import static com.labinot.dictionary.utils.Constants.EN_WORD_TRANSITION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.labinot.dictionary.activities.Settings_Activity;
import com.labinot.dictionary.activities.WordMeaning_Activity;
import com.labinot.dictionary.adapters.RecyclerViewAdapterHistory;
import com.labinot.dictionary.dataBaseHelper.DataBaseHelper;
import com.labinot.dictionary.dataBaseHelper.LoadDatabaseAsyn;
import com.labinot.dictionary.model.History;
import com.labinot.dictionary.utils.Constants;
import com.labinot.dictionary.utils.Helper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    private SearchView searchView;
    private LinearLayout empty_layout;
    private RecyclerView word_recyclerView;
    private DataBaseHelper dataBaseHelper;
    private LoadDatabaseAsyn loadDatabaseAsyn;
    private SimpleCursorAdapter suggestionAdapter;
    boolean databaseOpen = false;
    private ArrayList<History> historyList;
    private Cursor cursorHistory;
    private RecyclerViewAdapterHistory recyclerViewAdapterHistory;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

         getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.settings_id:
                startActivity(new Intent(MainActivity.this, Settings_Activity.class));
                overridePendingTransition(R.anim.push_in_right_anim,R.anim.push_out_right);
                break;

            case R.id.exit_id:
                finish();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        loadViews();
        initDataBase();
        initSuggestion();
        initSearchView();






    }

    private void loadViews() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Helper.FixFlicker(MainActivity.this,R.id.appBar_Layout);

        searchView = findViewById(R.id.search_view);
        empty_layout = findViewById(R.id.empty_layout);
        word_recyclerView = findViewById(R.id.word_recyclerView);
    }

    private void initDataBase() {

        dataBaseHelper = new DataBaseHelper(MainActivity.this,null);

        if(dataBaseHelper.CheckDatabase()){

            openDatabase();

        }{
            loadDatabaseAsyn = new LoadDatabaseAsyn(this);
            loadDatabaseAsyn.execute();
        }


    }

    private void initSuggestion() {

        final String[] from = new String[]{EN_WORD};
        final int[] to =new int[]{R.id.suggestion_text};

        suggestionAdapter = new SimpleCursorAdapter(MainActivity.this,R.layout.suggestion_row,null,from,to,0){

            @Override
            public void changeCursor(Cursor cursor) {
                super.swapCursor(cursor);
            }
        };

        searchView.setSuggestionsAdapter(suggestionAdapter);

    }

    private void initSearchView() {

        ImageView search_icon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        ImageView search_close = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        search_close.setColorFilter(Color.DKGRAY);
        search_icon.setColorFilter(Color.DKGRAY);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchView.setIconified(false);
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @SuppressLint("Range")
            @Override
            public boolean onSuggestionClick(int position) {

                CursorAdapter cursorAdapter = searchView.getSuggestionsAdapter();
                Cursor cursor = cursorAdapter.getCursor();
                cursor.moveToPosition(position);
                String clicked_words = cursor.getString(cursor.getColumnIndex(EN_WORD));

                searchView.setQuery(clicked_words,false);
                searchView.clearFocus();
                searchView.setFocusable(false);


                Intent intent = new Intent(MainActivity.this, WordMeaning_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString(EN_WORD,clicked_words);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.push_in_right_anim,R.anim.push_out_right);

                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String request = searchView.getQuery().toString();

                Cursor cursor = dataBaseHelper.getMeaning(request);

                searchView.clearFocus();
                searchView.setFocusable(false);

                if(cursor.getCount() == 0){


                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.word_not_founded);
                    builder.setMessage(R.string.search_again);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            searchView.setQuery("",false);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else{

                    Intent intent = new Intent(MainActivity.this,WordMeaning_Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(EN_WORD,request);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_in_right_anim,R.anim.push_out_right);

                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                searchView.setIconifiedByDefault(false);
                Cursor cursorSuggestion = dataBaseHelper.getSuggestion(newText);

                if(cursorSuggestion.getCount()>0)
                    suggestionAdapter.changeCursor(cursorSuggestion);

                return false;
            }

        });
        layoutManager = new LinearLayoutManager(MainActivity.this);
        fetchHistory();
    }


    @SuppressLint("Range")
    private void fetchHistory(){

        historyList = new ArrayList<>();
        recyclerViewAdapterHistory = new RecyclerViewAdapterHistory(this, historyList, new RecyclerViewAdapterHistory.ItemClickListener() {
            @Override
            public void onItemClickListener(int pos, String word, View sharedView) {

                Intent intent = new Intent(MainActivity.this,WordMeaning_Activity.class);
                intent.putExtra(EN_WORD,word);
                intent.putExtra(EN_WORD_TRANSITION,word);

                searchView.setTransitionName(word);

                ActivityOptionsCompat optionCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,
                        sharedView,word);

                startActivity(intent,optionCompat.toBundle());

            }
        });
        word_recyclerView.setLayoutManager(layoutManager);
        word_recyclerView.setAdapter(recyclerViewAdapterHistory);


        History history;

        if(databaseOpen){

            cursorHistory = dataBaseHelper.getHistory();

            if(cursorHistory.moveToFirst()){

                do{

                    history = new History(cursorHistory.getString(cursorHistory.getColumnIndex("word")),
                            cursorHistory.getString(cursorHistory.getColumnIndex(EN_DEFINITION)));
                    historyList.add(history);

                }
                while (cursorHistory.moveToNext());

            }
            recyclerViewAdapterHistory.notifyDataSetChanged();

        }

        if(recyclerViewAdapterHistory.getItemCount() == 0){

            empty_layout.setVisibility(View.VISIBLE);

        }else{

            empty_layout.setVisibility(View.GONE);
            word_recyclerView.setVisibility(View.VISIBLE);

        }






    }

    public void openDatabase() {

        try {

            dataBaseHelper.openDatabase();
            databaseOpen = true;

        }catch (SQLException e){

            e.printStackTrace();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fetchHistory();
    }
}