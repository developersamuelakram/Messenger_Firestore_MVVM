package com.example.messenger.MyAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.messenger.Model.MessageModel;
import com.example.messenger.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyMessageHolder> {


    List<MessageModel> messageModelList;

    public static  final int MESSAGE_RIGHT = 0; // FOR USER LAYOUT
    public static final int MESSAGE_LEFT = 1; // FOR FRIEND LAYOUT

    public void setMessageModelList(List<MessageModel> messageModelList){

        this.messageModelList = messageModelList;


    }

    @NonNull
    @Override
    public MyMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MESSAGE_RIGHT) {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitemright, parent, false);
            return new MyMessageHolder(view);

        } else {


            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitemleft, parent, false);
            return new MyMessageHolder(view);

        }



    }

    @Override
    public void onBindViewHolder(@NonNull MyMessageHolder holder, int position) {


        holder.showMessage.setVisibility(View.VISIBLE);
        holder.time.setVisibility(View.VISIBLE);
        holder.showMessage.setText(messageModelList.get(position).getMessage());
        holder.time.setText(messageModelList.get(position).getTime().substring(0,5));

    }

    @Override
    public int getItemCount() {

        if (messageModelList == null) {

            return 0;


        } else {

            return  messageModelList.size();
        }
    }

    class MyMessageHolder extends RecyclerView.ViewHolder {

        TextView showMessage, time;

        public MyMessageHolder(@NonNull View itemView) {
            super(itemView);

            showMessage = itemView.findViewById(R.id.show_message);
            time = itemView.findViewById(R.id.displaytime);



        }
    }


    @Override
    public int getItemViewType(int position) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        MessageModel messageModel = messageModelList.get(position);

        if (messageModel.getSender().equals(firebaseAuth.getCurrentUser().getUid())) {


            return MESSAGE_RIGHT;



        } else {


            return MESSAGE_LEFT;


        }

    }
}
