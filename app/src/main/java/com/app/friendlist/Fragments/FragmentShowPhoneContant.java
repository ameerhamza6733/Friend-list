package com.app.friendlist.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.friendlist.MainActivity;
import com.app.friendlist.Model.Friend;
import com.app.friendlist.Model.User;
import com.app.friendlist.R;
import com.app.friendlist.ViewModel.CreateAccountViewModel;
import com.app.friendlist.ViewModel.FriendViewModel;
import com.app.friendlist.ViewModel.LoginViewModel;
import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;

import java.util.ArrayList;
import java.util.List;
public
class FragmentShowPhoneContant extends Fragment {
    private String TAG="FragmentContantTAG";
    private SimpleCursorAdapter cursorAdapter;
    private ProgressBar progressBar;
    private Observer<String> observerMakeFriend;
    private Observer<User> observerUser;
    private FriendViewModel friendViewModel;
    private LoginViewModel loginViewModel;
    private ListView contantListView;
    private User currentUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_phone_contact_list,
                container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contantListView=view.findViewById(R.id.list_contant);
        progressBar=view.findViewById(R.id.progressBar);
        friendViewModel=new ViewModelProvider(this).get(FriendViewModel.class);
        loginViewModel= new ViewModelProvider(this).get(LoginViewModel.class);
        observerUser= new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser=user;
            }
        };
        observerMakeFriend=new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressBar.setVisibility(View.INVISIBLE);
                if (s.equals(CreateAccountViewModel.SUCCESS)){
                    ((MainActivity)getActivity()).replaceFragments(MyFriendsListFragment.class,"MyFriendsListFragment2");

                }else {
                    Toast.makeText(getActivity(),"Error :"+s,Toast.LENGTH_LONG).show();
                }
            }
        };
        loginViewModel.userLiveData().observe(getViewLifecycleOwner(),observerUser);
        final List<Contact> contacts = Contacts.getQuery().find();
        List<String> contactsNames=new ArrayList<>();
        for (Contact contact : contacts){
            contactsNames.add(contact.getDisplayName());
        }
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contactsNames);
        contantListView.setAdapter(itemsAdapter);

        contantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                progressBar.setVisibility(View.VISIBLE);
                Friend friend=new Friend();
                friend.setDisplayName(contacts.get(arg2).getDisplayName());
                friend.setRegisterUser(false);
                friend.setuID(contacts.get(arg2).getPhoneNumbers().get(0).getNumber().replace("+",""));
                Log.d(TAG,"friend phone number"+contacts.get(arg2).getPhoneNumbers().get(0).getNumber().replace("+","").replace(" ",","));
                friendViewModel.makeFriend(currentUser,friend).observe(getViewLifecycleOwner(),observerMakeFriend);
            }

        });
    }
}
