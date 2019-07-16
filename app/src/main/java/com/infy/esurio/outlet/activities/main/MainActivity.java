package com.infy.esurio.outlet.activities.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.github.nkzawa.socketio.client.IO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.infy.esurio.R;
import com.infy.esurio.middleware.DTO.OrdersDTO;
import com.infy.esurio.outlet.activities.main.fragments.OrdersListFragment;
import com.infy.esurio.outlet.app.This;
import com.infy.esurio.middleware.DTO.FoodcourtsDTO;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OrdersListFragment.OnOrdersItemClickedListener {
    private static final String TAG = "MainActivity";
    private TextView mTextMessage;
    private List<Fragment> fragmentStack = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new OrdersListFragment());
                    return true;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };
    private TextView tv_actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        This.CONTEXT.wrap(getApplicationContext());
        This.APPLICATION.wrap(getApplication());
        This.MAIN_ACTIVITY.wrap(this);
        This.NOTIFICATION_MANAGER.wrap((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("orders", "orders", NotificationManager.IMPORTANCE_DEFAULT);
            This.NOTIFICATION_MANAGER.self().createNotificationChannel(channel);
        }

        try {
            This.SOCKET.wrap(IO.socket(This.Static.URL));
            This.SOCKET.self().connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        This.FCM_TOKEN.wrap(task.getResult().getToken());
                        String msg = getString(R.string.msg_token_fmt, This.FCM_TOKEN.self());
                        Log.d(TAG, msg);
                    }
                });

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        View view = getSupportActionBar().getCustomView();


        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


//        loadFragment(new ServingsListFragment());
    }


    private boolean loadFragment(Fragment fragment) {
        Log.d(TAG, "loadFragment");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            moveTaskToBack(true);
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onListFragmentInteraction(OrdersDTO item) {

    }
}
