package com.example.chatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder>{

     Context context;
    ArrayList<MessageModel> messageModelArrayList;

    public MessageAdapter(Context context , ArrayList<MessageModel> messageModelArrayList) {
        this.context = context;
        this.messageModelArrayList=messageModelArrayList;
    }

    public void addIn(MessageModel messageModel) {
        messageModelArrayList.add(messageModel);
        notifyItemInserted(messageModelArrayList.size()-1);
    }

    public void clear(){
        messageModelArrayList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.message_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MessageModel messageModel = messageModelArrayList.get(position);
        if (messageModel.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
            holder.leftChatLayout.setVisibility(View.GONE);
            holder.rightChatLayout.setVisibility(View.VISIBLE);
            holder.rightChatView.setText(messageModel.getMessage());

        }else{
            holder.rightChatLayout.setVisibility(View.GONE);
            holder.leftChatLayout.setVisibility(View.VISIBLE);
            holder.leftChatView.setText(messageModel.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return messageModelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
         LinearLayout leftChatLayout,rightChatLayout;
        TextView leftChatView,rightChatView;
         RelativeLayout main;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatLayout=itemView.findViewById(R.id.left_chat_layout);
            rightChatLayout=itemView.findViewById(R.id.right_chat_layout);
            leftChatView=itemView.findViewById(R.id.left_message);
            rightChatView=itemView.findViewById(R.id.right_message);
            main=itemView.findViewById(R.id.mainMessageLayout);
            //password=itemView.findViewById(R.id.userPassword);
        }
    }
}
