package com.app.friendlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.friendlist.Fragments.ChooseLoginOrCreateFragment;
import com.app.friendlist.Fragments.FragmentShowPhoneContant;
import com.app.friendlist.Fragments.MyFriendsListFragment;
import com.app.friendlist.Fragments.SearchFriendFragment;
import com.app.friendlist.ViewModel.FriendViewModel;
import com.github.tamir7.contacts.Contacts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private MaterialSearchView searchView;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    @Override
    protected void onStart() {
        super.onStart();
        Contacts.initialize(this);
        SharedPref.init(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         fragmentManager = getSupportFragmentManager();
        firebaseAuth=FirebaseAuth.getInstance();
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setEllipsize(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (firebaseAuth.getCurrentUser()!=null){
                    SharedPref.write(SharedPref.SEARCH_TERM,query);
                    replaceFragments(SearchFriendFragment.class,"SearchFriendFragment");
                }else {
                    Toast.makeText(MainActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });


        if (firebaseAuth.getCurrentUser()==null){

            replaceFragments(ChooseLoginOrCreateFragment.class,"ChooseLoginOrCreateFragment");
        }else {
           replaceFragments(MyFriendsListFragment.class,"MyFriendsListFragment");
        }


    }



    public void replaceFragments(Class fragmentClass,String tag) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment


        if (fragmentManager.findFragmentByTag ( tag ) == null){
            fragmentManager.beginTransaction().replace(R.id.fragment_contrainer, fragment).addToBackStack(tag)
                    .commit();
        }else {
            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag ( tag ));
          //  fragmentManager.beginTransaction().replace(R.id.fragment_contrainer, fragmentManager.findFragmentByTag ( tag )).commit();
        }

    }
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {

                super.onBackPressed();


        }
    }

    public MaterialSearchView getSearchView() {
        return searchView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_manu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id==R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}