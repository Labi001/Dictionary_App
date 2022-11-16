package com.labinot.dictionary.about;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.labinot.dictionary.R;

import java.util.List;

public class AboutRecyclerAdapter extends RecyclerView.Adapter<AboutRecyclerAdapter.AboutViewHolder> {

    private Context context;
    private final List<AboutModel> mData;
    private ItemClickListener itemClickListener;

    public AboutRecyclerAdapter(Context context, List<AboutModel> mData) {
        this.context = context;
        this.mData = mData;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public AboutRecyclerAdapter.AboutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_for_about_recycler_view,parent,false);

        return new AboutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutRecyclerAdapter.AboutViewHolder holder, int position) {

        AboutModel aboutModel = mData.get(position);

        holder.mTitleI.setText(aboutModel.getTitle());

        if(aboutModel.getIcon() != 0)
            holder.mImageI.setImageResource(aboutModel.getIcon());
        else
            holder.mImageI.setVisibility(View.GONE);

        if(aboutModel.getTintColor() != 0)
        holder.mImageI.setColorFilter(aboutModel.getTintColor());

    }

    public interface ItemClickListener{

        void onItemClickListener(View view, int position, Intent intent);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AboutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleI;
        private ImageView mImageI;

        public AboutViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleI = itemView.findViewById(R.id.myTitleText_id);
            mImageI = itemView.findViewById(R.id.myImageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(itemClickListener !=null)
                itemClickListener.onItemClickListener(v,getLayoutPosition(),mData.get(getLayoutPosition()).getIntent());

        }
    }
}
