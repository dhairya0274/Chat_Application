package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.chatapplication.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;
    String receiverId,senderRoom,receiverRoom,receiverName;
    DatabaseReference databaseReferenceSender,databaseReferenceReceiver;
    TextView leftChatView,rightChatView;
    MessageAdapter messageAdapter;
    ImageButton back;
    ArrayList<MessageModel> messageModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        receiverId=getIntent().getStringExtra("id");
        receiverName=getIntent().getStringExtra("name");
        if(receiverName!=null){
            binding.chatName.setText(receiverName);
        }

        senderRoom= FirebaseAuth.getInstance().getUid()+receiverId;
        receiverRoom=receiverId+FirebaseAuth.getInstance().getUid();
        messageModelArrayList=new ArrayList<>();
        leftChatView=findViewById(R.id.left_message);
        rightChatView=findViewById(R.id.right_message);

        messageAdapter=new MessageAdapter(this,messageModelArrayList);
        binding.recycler.setAdapter(messageAdapter);
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        back=findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            Intent intent=new Intent(ChatActivity.this, MainActivity.class);
            startActivity(intent);
        });
        //data loading

        databaseReferenceSender= FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        databaseReferenceReceiver= FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);
        databaseReferenceSender.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageAdapter.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessageModel messageModel=dataSnapshot.getValue(MessageModel.class);
                    messageAdapter.addIn(messageModel);
                }
                messageAdapter.notifyDataSetChanged();
                binding.recycler.scrollToPosition(messageAdapter.getItemCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=binding.messageEnd.getText().toString();
                if(message.trim().length()>0){
                    sendMessage(message);
                }
            }
        });

    }

    private void sendMessage(String message) {
        long timestamp=System.currentTimeMillis();
        String messageId= UUID.randomUUID().toString();
        MessageModel messageModel=new MessageModel(messageId,FirebaseAuth.getInstance().getUid(),message,timestamp);
        messageAdapter.clear();
        messageAdapter.addIn(messageModel);
        messageAdapter.notifyDataSetChanged();
        databaseReferenceSender.child(messageId).setValue(messageModel);
        messageAdapter.notifyDataSetChanged();
        databaseReferenceReceiver.child(messageId).setValue(messageModel);
    }
}