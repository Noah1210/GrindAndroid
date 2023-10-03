package com.noah.npardon.grind.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.noah.npardon.grind.R;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    AccountFragment accountFragment = new AccountFragment();
    MovieFragment movieFragment = new MovieFragment();
    ShowFragment showFragment = new ShowFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.noah.npardon.grind.R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Log.d("fragment", "HOME: ");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                        return true;
                    case R.id.account:
                        Log.d("fragment", "ACCOUNT: ");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, accountFragment).commit();
                        return true;
                    case R.id.movie:
                        Log.d("fragment", "MOVIE: ");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, movieFragment).commit();
                        return true;
                    case R.id.show:
                        Log.d("fragment", "SHOW: ");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, showFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}
