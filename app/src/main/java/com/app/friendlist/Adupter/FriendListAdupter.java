package com.app.friendlist.Adupter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.friendlist.Model.Friend;
import com.app.friendlist.Model.User;
import com.app.friendlist.R;

import java.util.List;

public class FriendListAdupter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Friend> friendList;

    public FriendListAdupter(List<Friend> friendList) {
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_friend, parent, false);
        return new FriendListViewHodler(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FriendListViewHodler friendListViewHodler= (FriendListViewHodler) holder;
        friendListViewHodler.tvName.setText(friendList.get(holder.getAdapterPosition()).getDisplayName());
        if (!friendList.get(holder.getAdapterPosition()).isRegisterUser()){
            friendListViewHodler.tvName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_close_24,0,0,0);

        }else {
            friendListViewHodler.tvName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_verified_user_24,0,0,0);

        }
    }

    @Override
    public int getItemCount() {
        return this.friendList.size();
    }

    public static class FriendListViewHodler extends RecyclerView.ViewHolder{
        public TextView tvName;
        public FriendListViewHodler(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvDisplayName);
        }
    }
}
