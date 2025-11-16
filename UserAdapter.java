package com.example.chatapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

     Context context;
    ArrayList<UserModel> userModelList;

    public UserAdapter(Context context ,ArrayList<UserModel> userModelList) {
        this.context = context;
        this.userModelList=userModelList;
    }

    public void addIn(UserModel userModel) {
        userModelList.add(userModel);
        notifyItemInserted(userModelList.size()-1);
    }

    public void clear(){
        userModelList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserModel userModel=userModelList.get(position);
        holder.name.setText(userModel.getUserName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(context, ChatActivity.class);
                intent.putExtra("id",userModel.getUserId());
                intent.putExtra("name",userModel.getUserName());
                context.startActivity(intent);
            }
        });
        if(userModel.getUserId().equals(FirebaseAuth.getInstance().getUid())){
            holder.name.setText(userModel.getUserName()+" (Self) ");
        }
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
         TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.userName);
            //password=itemView.findViewById(R.id.userPassword);
        }
    }
}
