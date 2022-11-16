package com.labinot.dictionary.dataBaseHelper;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.labinot.dictionary.MainActivity;
import com.labinot.dictionary.R;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class LoadDatabaseAsyn extends AsyncTask<String,String,Boolean> {

    private WeakReference<MainActivity> mainActivity;
    private AlertDialog dialog;
    private ProgressBar progressBar;
    private TextView progress_txt;

    public LoadDatabaseAsyn(MainActivity mainActivity) {
        this.mainActivity = new WeakReference<>(mainActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity.get());
        LayoutInflater inflater = LayoutInflater.from(mainActivity.get());
        View view = inflater.inflate(R.layout.alert_dialog_database_copying,null);

        progressBar = view.findViewById(R.id.progress_bar);
        progress_txt = view.findViewById(R.id.progress_textview);


        alert.setTitle("Loading Database...");
        alert.setView(view);
        dialog = alert.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(String... voids) {

        DataBaseHelper dataBaseHelper = new DataBaseHelper(mainActivity.get(), new ProgressInterface() {
            @Override
            public void onProgressUpdate(String progress) {
                publishProgress(progress);
            }
        });
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Error("Database was not created !");
        }

        dataBaseHelper.close();

        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        super.onProgressUpdate(progress);

        if(progressBar != null){

            progress_txt.setText(progress[0]+"%");
            progressBar.setProgress(Integer.parseInt(progress[0]));

        }

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        dialog.dismiss();

        MainActivity activity = mainActivity.get();
        if(activity==null || activity.isFinishing() || activity.isDestroyed())
            return;

        activity.openDatabase();
    }
}
