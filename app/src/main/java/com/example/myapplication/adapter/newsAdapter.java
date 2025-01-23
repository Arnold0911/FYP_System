package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DataType.news;
import com.example.myapplication.R;

import java.util.List;

public class newsAdapter extends RecyclerView.Adapter<newsAdapter.newsViewHolder> {

    Context context;
    List<news> newsList;

    public newsAdapter(Context context, List<news> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public newsAdapter.newsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_row, parent, false);
        return new newsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newsAdapter.newsViewHolder holder, int position) {
        holder.newsName.setText(newsList.get(position).getName());
        holder.newsContext.setText(newsList.get(position).getContext());

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static final class newsViewHolder extends RecyclerView.ViewHolder{
        TextView newsName, newsContext;

        public newsViewHolder(@NonNull View itemView){
            super(itemView);
            newsName = itemView.findViewById(R.id.newsName);
            newsContext = itemView.findViewById(R.id.newsContext);
        }
    }
}
