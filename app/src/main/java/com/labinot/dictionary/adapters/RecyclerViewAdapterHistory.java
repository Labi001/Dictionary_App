package com.labinot.dictionary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.labinot.dictionary.R;
import com.labinot.dictionary.model.History;

import java.util.ArrayList;

public class RecyclerViewAdapterHistory extends RecyclerView.Adapter<RecyclerViewAdapterHistory.ViewHolder> {

    private Context context;
    private final ArrayList<History> histories;
    private ItemClickListener itemClickListener;

    public RecyclerViewAdapterHistory(Context context, ArrayList<History> histories,ItemClickListener itemClickListener) {
        this.context = context;
        this.histories = histories;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.history_item_layout,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterHistory.ViewHolder holder,  int position) {

        final History history = histories.get(position);

        holder.enWord.setText(history.getEn_word());
        holder.enDef.setText(history.getDefinition());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemClickListener.onItemClickListener(position,history.getEn_word(),holder.enWord);
            }
        });

    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public interface ItemClickListener{

        void onItemClickListener(int pos,String word,View sharedView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView enWord;
        TextView enDef;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            enWord = itemView.findViewById(R.id.enWord);
            enDef = itemView.findViewById(R.id.enDef);
        }
    }
}
