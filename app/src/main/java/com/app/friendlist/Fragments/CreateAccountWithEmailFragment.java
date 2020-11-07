package com.app.friendlist.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.friendlist.MainActivity;
import com.app.friendlist.Model.User;
import com.app.friendlist.R;
import com.app.friendlist.ViewModel.CreateAccountViewModel;
import com.app.friendlist.ViewModel.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;


public class CreateAccountWithEmailFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Observer<String> observerCreateAccount;
    private User user = new User();
    private Observer<String> updateUserProfile;
    private Observer<String> createUserInFirebaseObserver;
    private Observer<String> loginObserver;
    private LoginViewModel loginViewModel;
    private Button btCreateAccount;
    private EditText editTextEmail;
    private EditText tvEnterName;
    private ProgressBar progressBar;
    private EditText password;
    private EditText password2;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private CreateAccountViewModel createAccountViewModel;

    public CreateAccountWithEmailFragment() {
        // Required empty public constructor
    }


    public static CreateAccountWithEmailFragment newInstance(String param1, String param2) {
        CreateAccountWithEmailFragment fragment = new CreateAccountWithEmailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        updateUserProfile = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(CreateAccountViewModel.SUCCESS)){
                    createAccountViewModel.createUserInFireBaseRealTime(user).observe(CreateAccountWithEmailFragment.this,createUserInFirebaseObserver);
                }

            }
        };
        createUserInFirebaseObserver= new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(CreateAccountViewModel.SUCCESS)){
                    User loginUser = new User();
                    loginUser.setEmail(editTextEmail.getText().toString().toLowerCase());
                    loginUser.setPassword(password.getText().toString());
                    loginViewModel.login(loginUser).observe(getViewLifecycleOwner(),loginObserver);
                    Toast.makeText(getActivity(),"Your Account is created",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(),"Error "+s,Toast.LENGTH_LONG).show();
                }
            }
        };
        observerCreateAccount=new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s.equals(CreateAccountViewModel.SUCCESS)){

                    user.setDisplayName(tvEnterName.getText().toString().toLowerCase());
                    user.setuID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail().toLowerCase());
                    createAccountViewModel.updateUser(user).observe(CreateAccountWithEmailFragment.this,updateUserProfile);
                }else {
                    Toast.makeText(getActivity(),"Error: "+s,Toast.LENGTH_LONG).show();
                }
            }
        };
        loginObserver=new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals(CreateAccountViewModel.SUCCESS)){
                    ((MainActivity) getActivity()).replaceFragments(MyFriendsListFragment.class,"MyFriendsListFragment");

                }else {
                    Toast.makeText(getActivity(),"Error "+s,Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account_with_email, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loginViewModel= new ViewModelProvider(this).get(LoginViewModel.class);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create Account");
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btCreateAccount=view.findViewById(R.id.btCreateAccount);
        editTextEmail=view.findViewById(R.id.enterEmail);
        tvEnterName=view.findViewById(R.id.tvEnterName);
        password=view.findViewById(R.id.enterPassword);
        password2=view.findViewById(R.id.enterPassword2);
        progressBar=view.findViewById(R.id.progressBar);

        btCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValaid()){
                    createAccountNow();
                }
            }
        });
    }
    private void createAccountNow(){
        progressBar.setVisibility(View.VISIBLE);

          createAccountViewModel =new  ViewModelProvider(this).get(CreateAccountViewModel.class);
        createAccountViewModel.createAccount(editTextEmail.getText().toString().toLowerCase(),password.getText().toString()).observe(this,observerCreateAccount);
    }

    private boolean isValaid(){
        if (tvEnterName.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Please enter name",Toast.LENGTH_LONG).show();
        }
        if (editTextEmail.getText().toString().isEmpty()){
            Toast.makeText(getContext(),"Please enter email",Toast.LENGTH_LONG).show();
            return false;
        } if (password.getText().toString().isEmpty()){
            Toast.makeText(getContext()," enter password",Toast.LENGTH_LONG).show();

            return false;
        }  if (password2.getText().toString().isEmpty()){
            Toast.makeText(getContext()," enter password",Toast.LENGTH_LONG).show();

            return false;
        }
        if (!password.getText().toString().equals(password2.getText().toString())){
            Toast.makeText(getContext(),"  password not match",Toast.LENGTH_LONG).show();

            return false;
        }
        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_search);
        if(item!=null)
            item.setVisible(false);
    }
}