package com.example.messenger.MyAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.example.messenger.R;
import com.example.messenger.UserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyUserHolder> {


    List<UserModel> userModelList;

    OnUserClicked interfaceforuser;

    public UserAdapter(OnUserClicked interfaceforuser) {
        this.interfaceforuser = interfaceforuser;
    }

    // we will add our list from the viewmodel into the adapter
    public void setUserModelList(List<UserModel> userModelList) {

        this.userModelList = userModelList;

    }

    @NonNull
    @Override
    public MyUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userstyle, parent, false);
        return new MyUserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUserHolder holder, int position) {

        holder.userName.setText(userModelList.get(position).getUsername());

        Glide.with(holder.itemView.getContext()).load(userModelList.get(position).getImageUrl()).centerCrop().into(holder.iamgeView);

        if (userModelList.get(position).getStatus().equals("Online")) {

            holder.status.setImageResource(R.drawable.online);


        } else {

            holder.status.setImageResource(R.drawable.offline);

        }


    }

    @Override
    public int getItemCount() {
        if (userModelList == null) {

            return 0;


        } else {

            return userModelList.size();
        }
    }

    class MyUserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView userName;
        CircleImageView iamgeView;
        ImageView status;

        public MyUserHolder(@NonNull View itemView) {
            super(itemView);


            userName = itemView.findViewById(R.id.userNameFrag);
            iamgeView  = itemView.findViewById(R.id.imageViewUser);
            status = itemView.findViewById(R.id.status);

            userName.setOnClickListener(this);
            iamgeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            interfaceforuser.UserisCicked(getAdapterPosition(), userModelList);

        }
    }

    public interface OnUserClicked {
        void UserisCicked(int position, List<UserModel> userModels);




    }
}
