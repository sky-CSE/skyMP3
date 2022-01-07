package com.example.skymp3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
     Context context;
     ArrayList<String> list;

    public MusicAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item,parent,false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String filePath = list.get(position);
        String title = filePath.substring(filePath.lastIndexOf("/")+1);
        holder.songName.setText(title);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,MusicActivity.class);
                i.putExtra("title",title);
                i.putExtra("filepath",filePath);
                i.putExtra("position",position);
                i.putExtra("list",list);

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{
        private TextView songName;
        private CardView cardView;

       public MusicViewHolder(@NonNull View itemView) {
           super(itemView);
           songName = itemView.findViewById(R.id.songname);
           cardView = itemView.findViewById(R.id.card_view);
       }
   }
}
