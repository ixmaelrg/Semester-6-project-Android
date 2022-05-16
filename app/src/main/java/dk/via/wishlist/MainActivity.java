package dk.via.wishlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import dk.via.wishlist.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_login,
                R.id.nav_logout,
                R.id.nav_home,
                R.id.nav_create_wishlist,
                R.id.nav_my_wishlists)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(PreferenceConfig.PREF_LOGIN_STATE)) {
            boolean isLoggedIn = PreferenceConfig.getLoginState(this);
            if(isLoggedIn == true)
            {
                binding.navView.getMenu().getItem(R.id.nav_login).setVisible(false);
                binding.navView.getMenu().getItem(R.id.nav_logout).setVisible(true);
            }
            else
            {
                binding.navView.getMenu().getItem(R.id.nav_login).setVisible(true);
                binding.navView.getMenu().getItem(R.id.nav_logout).setVisible(false);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceConfig.registerPreference(getApplicationContext(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceConfig.unregisterPreference(getApplicationContext(), this);
    }
}