package com.app.friendlist.Adupter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.app.friendlist.Fragments.MyFriendsListFragment;
import com.app.friendlist.MainActivity;
import com.app.friendlist.Model.User;
import com.app.friendlist.R;
import com.app.friendlist.ViewModel.CreateAccountViewModel;
import com.app.friendlist.ViewModel.FriendViewModel;

import java.util.List;

public class SearchResultListAdapter extends RecyclerView.Adapter<SearchResultListAdapter.SearchResultViewHoder> {

    private List<User> searchUserList;
    private FriendViewModel friendViewModel;
    private User currentUser;
    private Observer<String> observerFriend;
    private Context context;

    public SearchResultListAdapter(List<User> searchUserList, final Context context, User currentUser) {
        this.searchUserList = searchUserList;
        this.context=context;
        this.currentUser=currentUser;
        friendViewModel=new ViewModelProvider((MainActivity)context).get(FriendViewModel.class);
        observerFriend=new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(CreateAccountViewModel.SUCCESS)){
                    ((MainActivity)context).replaceFragments(MyFriendsListFragment.class,"MyFriendsListFragment");
                }else {
                    Toast.makeText(context,"Error"+s,Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @NonNull
    @Override
    public SearchResultViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_search_result, parent, false);
        return new SearchResultViewHoder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchResultViewHoder holder, int position) {
        holder.tvUserName.setText(searchUserList.get(holder.getAdapterPosition()).getDisplayName());
        holder.btAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"please wait",Toast.LENGTH_LONG).show();
                friendViewModel.makeFriend(currentUser,searchUserList.get(holder.getAdapterPosition())).observe((MainActivity)context,observerFriend);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchUserList.size();
    }

    public static class SearchResultViewHoder extends  RecyclerView.ViewHolder{
        public TextView tvUserName;
        private ImageButton btAddFriend;

        public SearchResultViewHoder(@NonNull View itemView) {
            super(itemView);
            btAddFriend=itemView.findViewById(R.id.imageButton);
            tvUserName=itemView.findViewById(R.id.tvUserName);
        }
    }
}
