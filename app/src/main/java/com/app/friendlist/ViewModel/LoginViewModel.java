package com.app.friendlist.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.friendlist.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private MutableLiveData<String> liveData=new MutableLiveData<>();
    private MutableLiveData<User> userMutableLiveData= new MutableLiveData<>();
    public static String SUCCESS="success";

    public LiveData<String> login(User user){
        mAuth.signInWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                   liveData.setValue(SUCCESS);
                }else {
                    liveData.setValue(""+((FirebaseAuthException)task.getException()).getErrorCode());


                }
            }
        });
        return liveData;
    }

    public LiveData<User> userLiveData(){
        FirebaseUser firebaseUser =  mAuth.getCurrentUser();
        User user = new User();
        user.setEmail(firebaseUser.getEmail());
        user.setuID(firebaseUser.getUid());
        user.setDisplayName(firebaseUser.getDisplayName());
        userMutableLiveData.setValue(user);
        return userMutableLiveData;

    }
}
