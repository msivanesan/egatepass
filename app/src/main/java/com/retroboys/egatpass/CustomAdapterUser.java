package com.retroboys.egatpass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CustomAdapterUser extends RecyclerView.Adapter<CustomAdapterUser.MyViewHolder> {
    private Context contex;
    private List<Users> userList;
    private OnItemClickListener listener;

    public CustomAdapterUser(Context context, List<Users> userList, OnItemClickListener listener) {
        this.contex = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_users,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Users data=userList.get(position);
        holder.name.setText(data.getName().toUpperCase());
        holder.userName.setText(data.getUserName().toUpperCase());
        holder.hostel.setText(data.getHostel().toUpperCase());
        holder.itemView.setOnClickListener(v->{
            listener.onItemClick(data);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    public void filterList(List<Users> filteredList) {
        userList = filteredList;
        notifyDataSetChanged();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userName,name,hostel;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.view_user_name);
            name=itemView.findViewById(R.id.view_name);
            hostel=itemView.findViewById(R.id.view_hostel);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Users user);
    }
}
