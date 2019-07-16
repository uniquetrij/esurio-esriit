package com.infy.esurio.esuriit.activities.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.github.nkzawa.socketio.client.IO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.infy.esurio.R;
import com.infy.esurio.esuriit.activities.main.fragments.FoodcourtListFragment;
import com.infy.esurio.esuriit.activities.main.fragments.GooglePayFragment;
import com.infy.esurio.esuriit.app.This;
import com.infy.esurio.middleware.DTO.FoodcourtsDTO;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity implements FoodcourtListFragment.OnOrdersItemClickedListener {
    private static final String TAG = "MainActivity";
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    loadFragment(new FoodcourtListFragment());
                    return true;
                case R.id.navigation_dashboard:
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
                    loadFragment(new GooglePayFragment());
                    return true;
            }
            return false;
        }
    };

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



        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new FoodcourtListFragment());
    }

    @Override
    public void onListFragmentInteraction(FoodcourtsDTO item) {
        Toast.makeText(getApplicationContext(), item.getIdentifier(), Toast.LENGTH_LONG).show();
    }

    private boolean loadFragment(Fragment fragment) {
        Log.d(TAG, "loadFragment");
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

}
