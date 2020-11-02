package com.app.friendlist.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.friendlist.MainActivity;
import com.app.friendlist.Model.User;
import com.app.friendlist.R;
import com.app.friendlist.ViewModel.LoginViewModel;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Observer<String> observerLogin;
    private LoginViewModel loginViewModel;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button btLogin;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Login");

        editTextEmail=view.findViewById(R.id.editTextTextEmailAddress);
        editTextPassword=view.findViewById(R.id.editTextTextPassword);
        btLogin=view.findViewById(R.id.btLogin);
        loginViewModel=new ViewModelProvider(this).get(LoginViewModel.class);
        observerLogin=new Observer<String>() {
            @Override
            public void onChanged(String s) {
               if (s.equals(LoginViewModel.SUCCESS)){
                   ((MainActivity)getActivity()).replaceFragments(MyFriendsListFragment.class,"MyFriendsListFragment2");

               }else {
                   Toast.makeText(getActivity(),"Error "+s,Toast.LENGTH_LONG).show();
               }
            }
        };
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user=new User();
                user.setEmail(editTextEmail.getText().toString());
                user.setPassword(editTextPassword.getText().toString());
                loginViewModel.login(user).observe(getViewLifecycleOwner(),observerLogin);
            }
        });
    }


}