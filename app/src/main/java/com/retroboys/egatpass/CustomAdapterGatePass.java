package com.retroboys.egatpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CustomAdapterGatePass extends RecyclerView.Adapter<CustomAdapterGatePass.MyViewHolder>  {

    private Context contex;
    private List<GatePass> data;
    private OnItemClickListener  listener;


    public CustomAdapterGatePass(Context contex, List<GatePass> data, OnItemClickListener listener) {
        this.contex = contex;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pass,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GatePass item=data.get(position);
        holder.time.setText(item.getRequestDate());
        holder.name.setText(item.getName().toUpperCase());
        holder.itemView.setOnClickListener(v->{
            listener.onItemClick(item);
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void filterList(List<GatePass> filteredList) {
        data = filteredList;
        notifyDataSetChanged();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView time,name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time=itemView.findViewById(R.id.pass_time);
            name=itemView.findViewById(R.id.pass_name);

        }


    }
    public interface OnItemClickListener {
        void onItemClick(GatePass gatePass);
    }
}
