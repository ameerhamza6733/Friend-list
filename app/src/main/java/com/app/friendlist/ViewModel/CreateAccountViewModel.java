package com.app.friendlist.ViewModel;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.friendlist.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateAccountViewModel extends ViewModel {
    public static String SUCCESS="success";
    public static String USER_NAME_ALREADY_TAKEN="userNameAlreadyTaken";

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private MutableLiveData<String> liveData=new MutableLiveData<>();
    private MutableLiveData<String> liveDataUpdateUserInDatabase=new MutableLiveData<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private MutableLiveData<String> userNameAvalibal=new MutableLiveData<>();


    public LiveData<String> isUserNameAvalibal(String userName){
        database.getReference("all_user_name")
                .child(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            userNameAvalibal.setValue(USER_NAME_ALREADY_TAKEN);
                        }else {
                            userNameAvalibal.setValue(SUCCESS);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        userNameAvalibal.setValue(error.getMessage());
                    }
                });
        return userNameAvalibal;
    }

    public LiveData<String> createAccount(String emai,String password){
        mAuth.createUserWithEmailAndPassword(emai, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            liveData.setValue(SUCCESS);
                        } else {
                            liveData.setValue(task.getException().getLocalizedMessage());

                        }

                    }
                });
        return liveData;
    }

    public LiveData<String> createUserInFireBaseRealTime(final User user){

        database.getReference("users")
                .child(user.getuID())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    liveDataUpdateUserInDatabase.setValue(SUCCESS);
                }else {
                    liveDataUpdateUserInDatabase.setValue(task.getException().getMessage());
                }
            }
        });
        return liveDataUpdateUserInDatabase;
    }


    public LiveData<String> updateUser(User user){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getDisplayName())
                .build();

       mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()){
                   liveData.setValue(SUCCESS);
               }else {
                   liveData.setValue(task.getException().getMessage());
                   task.getException().printStackTrace();
               }
           }
       });
       return liveData;
    }

}
