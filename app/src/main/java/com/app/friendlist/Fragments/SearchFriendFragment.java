package com.app.friendlist.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.friendlist.Adupter.SearchResultListAdapter;
import com.app.friendlist.MainActivity;
import com.app.friendlist.Model.User;
import com.app.friendlist.R;
import com.app.friendlist.SharedPref;
import com.app.friendlist.ViewModel.FriendViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class SearchFriendFragment extends Fragment {

    private String searchQurey;
    private FriendViewModel friendViewModel;
    private Observer<List<User>> searchResultListObserver;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SearchResultListAdapter searchResultListAdapter;
    private String TAG="SearchFriendFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_result_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar=view.findViewById(R.id.progressBar);
        recyclerView=view.findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated");
        ((MainActivity)getActivity()).getToolbar().setTitle("Search Result");
       String query= SharedPref.read(SharedPref.SEARCH_TERM,"");
       searchResultListObserver=new Observer<List<User>>() {
           @Override
           public void onChanged(List<User> users) {

               progressBar.setVisibility(View.INVISIBLE);
               User user= new User();
               FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
               user.setEmail(firebaseUser.getEmail());
               user.setuID(firebaseUser.getUid());
               user.setDisplayName(firebaseUser.getDisplayName());

               if (users!=null){
                   if (users.isEmpty()){
                       Toast.makeText(getActivity(),"No user found",Toast.LENGTH_LONG).show();

                   }
                  if (searchResultListAdapter==null){
                      searchResultListAdapter=new SearchResultListAdapter(users,getActivity(),user);
                  }else {
                      searchResultListAdapter.notifyDataSetChanged();
                  }
                   recyclerView.setAdapter(searchResultListAdapter);
               }else {
                   Toast.makeText(getActivity(),"No user found",Toast.LENGTH_LONG).show();
               }
           }
       };
        friendViewModel =new ViewModelProvider(this).get(FriendViewModel.class);
        if (!query.isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
           friendViewModel.findFriend(query.toLowerCase()).observe(getViewLifecycleOwner(),searchResultListObserver);
       }

    }

}