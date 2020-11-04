package com.app.friendlist.ViewModel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.friendlist.Model.Friend;
import com.app.friendlist.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendViewModel extends ViewModel {
    private MutableLiveData<List<Friend>> userMutableLiveData;
    private MutableLiveData<List<User>> userSearchQureryResult;
    private MutableLiveData<String> friendAddedMutableLiveDate;
    private MutableLiveData<String> friendRemoveMutabelLiveDate;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ValueEventListener searchByUserNameEventListener;
    private ValueEventListener valueEventListener;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private List<Friend> userList;
    private String TAG="FriendViewModel";
    private String lastQuery="";

    public LiveData<List<Friend>> getFriendList(){

        if (userMutableLiveData==null){
            Log.d(TAG,"geting friend list from server");
            userMutableLiveData=new MutableLiveData<>();
            userList= new ArrayList<>();
            if (valueEventListener==null){
                valueEventListener= new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot eachUser : snapshot.getChildren()){
                            userList.add(eachUser.getValue(Friend.class));
                        }
                        userMutableLiveData.setValue(userList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        error.toException().printStackTrace();
                        userMutableLiveData.setValue(null);
                    }
                };
            }
            database.getReference("myFriendList").child(firebaseAuth.getCurrentUser().getUid()).getRef().addListenerForSingleValueEvent(valueEventListener);

        }
            return userMutableLiveData;
    }



    public LiveData<List<User> >findFriend(final String user) {
        if (searchByUserNameEventListener == null) {
            userSearchQureryResult = new MutableLiveData<>();
            final List<User> userArrayList = new ArrayList<>();
            searchByUserNameEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot each : snapshot.getChildren() ){
                        userArrayList.add(each.getValue(User.class));
                    }
                    userSearchQureryResult.setValue(userArrayList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
        }
       if (!lastQuery.equals(user)){
           lastQuery=user;
           database.getReference("users")
                   .orderByChild("displayName")
                   .startAt(user)
                   .endAt(user)
                   .addListenerForSingleValueEvent(searchByUserNameEventListener);
       }
    return userSearchQureryResult;
    }

    public LiveData<String> deleteFriend(final User me,final  User friend){
        HashMap<String, Boolean> map = new HashMap<>();
        map.put(friend.getuID(), false);
        if (friendRemoveMutabelLiveDate==null)
            friendRemoveMutabelLiveDate=new MutableLiveData<>();

        database.getReference("myFriends")
                .child(me.getuID())
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            database.getReference("myFriendList")
                                    .child(me.getuID())
                                    .child(friend.getuID())
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                friendRemoveMutabelLiveDate.setValue(CreateAccountViewModel.SUCCESS);
                                            }else {
                                                friendRemoveMutabelLiveDate.setValue(task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            friendRemoveMutabelLiveDate.setValue(task.getException().getMessage());

                        }
                    }
                });
        return friendRemoveMutabelLiveDate;
    }

    public LiveData<String> makeFriend(final User me,final User friend) {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put(friend.getuID(), true);
        if (friendAddedMutableLiveDate==null)
            friendAddedMutableLiveDate=new MutableLiveData<>();

        database.getReference("myFriends")
                .child(me.getuID())
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            database.getReference("myFriendList")
                                    .child(me.getuID())
                                    .child(friend.getuID())
                                    .setValue(friend)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                friendAddedMutableLiveDate.setValue(CreateAccountViewModel.SUCCESS);
                                            }else {
                                                friendAddedMutableLiveDate.setValue(task.getException().getMessage());
                                            }
                                        }
                                    });
                        } else {
                            friendAddedMutableLiveDate.setValue(task.getException().getMessage());

                        }
                    }
                });
    return friendAddedMutableLiveDate;
    }
    }


