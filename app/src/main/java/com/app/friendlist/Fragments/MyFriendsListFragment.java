package com.app.friendlist.Fragments;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.friendlist.Adupter.FriendListAdupter;
import com.app.friendlist.MainActivity;
import com.app.friendlist.Model.Friend;
import com.app.friendlist.Model.User;
import com.app.friendlist.R;
import com.app.friendlist.ViewModel.FriendViewModel;
import com.app.friendlist.ViewModel.LoginViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MyFriendsListFragment extends Fragment {
    private FriendViewModel friendViewModel;
    private LoginViewModel loginViewModel;
    private Observer<User> observerCurrentUser;
    private Observer<List<Friend>> friendObsersList;
    private static final int RC_READ_CONTACTS = 533;
    private static final String TAG="MyFriendsListFragment";
    private User currentUser;

    private FloatingActionButton fabAddFromPhoneContant;
    private FriendListAdupter friendListAdupter;
    private RecyclerView recyclerView;
    private TextView tvNoFriends;
    private ProgressBar progressBar;
    public MyFriendsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_friends_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar=view.findViewById(R.id.progressBar);
        tvNoFriends=view.findViewById(R.id.tvNoFriends);
        recyclerView=view.findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         fabAddFromPhoneContant = view.findViewById(R.id.fabAddPersonFromContant);
        loginViewModel=new ViewModelProvider(this).get(LoginViewModel.class);
        observerCurrentUser=new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser=user;
                if (getActivity()!=null){
                    ((MainActivity)getActivity()).getToolbar().setTitle("Friend List ("+currentUser.getDisplayName()+")" );

                }
            }
        };
        loginViewModel.userLiveData().observe(getViewLifecycleOwner(),observerCurrentUser);


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        fabAddFromPhoneContant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission();
            }
        });
        friendObsersList=new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> users) {
                progressBar.setVisibility(View.INVISIBLE);
                if (users==null){
                    tvNoFriends.setText("Error");
                }else if (users.isEmpty()){
                    tvNoFriends.setVisibility(View.VISIBLE);
                }else {
                    if (friendListAdupter==null){
                        friendListAdupter=new FriendListAdupter(users,getActivity(),currentUser);
                    }
                    tvNoFriends.setVisibility(View.GONE);
                    recyclerView.setAdapter(friendListAdupter);
                }
            }
        };
        friendViewModel=new ViewModelProvider(this).get(FriendViewModel.class);
        friendViewModel.getFriendList().observe(getViewLifecycleOwner(),friendObsersList);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        if (fabAddFromPhoneContant!=null){
            fabAddFromPhoneContant.setVisibility(View.VISIBLE);
        }
    }

    @AfterPermissionGranted(RC_READ_CONTACTS)
    private void askPermission() {
        String[] perms = {Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            ((MainActivity)getActivity()).replaceFragments(FragmentShowPhoneContant.class,"FragmentShowPhoneContant");
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.add_person_from_contacts_rationale),
                    RC_READ_CONTACTS, perms);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}