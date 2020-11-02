package com.app.friendlist.Adupter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.friendlist.MainActivity;
import com.app.friendlist.Model.Friend;
import com.app.friendlist.Model.User;
import com.app.friendlist.R;
import com.app.friendlist.ViewModel.CreateAccountViewModel;
import com.app.friendlist.ViewModel.FriendViewModel;

import java.util.List;

public class FriendListAdupter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Friend> friendList;
    private FriendViewModel friendViewModel;
    private Observer<String> observerDeleteFriend;
    private User currentUser;
    private Context lifecycleOwner;
    private int index=-1;

    public FriendListAdupter(final List<Friend> friendList, final Context lifecycleOwner, User currentUser) {
        this.friendList = friendList;
        this.lifecycleOwner=lifecycleOwner;
        this.currentUser=currentUser;
        friendViewModel=new ViewModelProvider((MainActivity)lifecycleOwner).get(FriendViewModel.class);
        observerDeleteFriend=new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(CreateAccountViewModel.SUCCESS)){
                   if (index!=-1){
                       friendList.remove(index);
                       notifyItemRemoved(index);
                       notifyDataSetChanged();
                       index=-1;
                   }
                }else {
                    Toast.makeText(lifecycleOwner,"unable to delete: "+s,Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_friend, parent, false);
        return new FriendListViewHodler(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        FriendListViewHodler friendListViewHodler= (FriendListViewHodler) holder;
        friendListViewHodler.tvName.setText(friendList.get(holder.getAdapterPosition()).getDisplayName());
        friendListViewHodler.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=holder.getAdapterPosition();
                friendViewModel.deleteFriend(currentUser,friendList.get(holder.getAdapterPosition())).observe((MainActivity)lifecycleOwner,observerDeleteFriend);
            }
        });
        if (!friendList.get(holder.getAdapterPosition()).isRegisterUser()){
            friendListViewHodler.tvName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_close_24,0);

        }else {
            friendListViewHodler.tvName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_check_circle_24,0);

        }
    }

    @Override
    public int getItemCount() {
        return this.friendList.size();
    }

    public static class FriendListViewHodler extends RecyclerView.ViewHolder{
        public TextView tvName;
        public ImageButton btDelete;
        public FriendListViewHodler(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tvDisplayName);
            btDelete=itemView.findViewById(R.id.btDelete);
        }
    }
}
