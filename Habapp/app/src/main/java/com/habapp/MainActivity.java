package com.habapp;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.habapp.databinding.ActivityMainBinding;
import com.habapp.models.Plot;
import com.habapp.models.relations.PlotWithVegetables;
import com.habapp.utils.Location;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private MutableLiveData<List<Plot>> plots;
    private MutableLiveData<Location> location;
    private MutableLiveData<List<PlotWithVegetables>> pwvs;

    public MutableLiveData<List<PlotWithVegetables>> getPwvs() {
        return this.pwvs;
    }

    public void setPwvs(List<PlotWithVegetables> pwvs) {
        this.pwvs.setValue(pwvs);
    }

    public void addPwv(PlotWithVegetables pwv) {
        List<PlotWithVegetables> pwvs = this.pwvs.getValue();
        pwvs.add(pwv);
        this.pwvs.setValue(pwvs);
    }

    public MutableLiveData<List<Plot>> getPlots() {
        return this.plots;
    }

    public void setPlots(List<Plot> plots) {
        this.plots.setValue(plots);
    }

    public void addPlot(Plot plot) {
        List<Plot> plots = this.plots.getValue();
        plots.add(plot);
        this.plots.setValue(plots);
    }

    public void clearPlots() {
        this.plots.setValue(new ArrayList<>());
    }

    public MutableLiveData<Location> getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location.setValue(location);
    }

    public void resetLocation() {
        this.location.setValue(new Location(-34.9213, -57.9543));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        this.plots = new MutableLiveData<>();
        this.plots.setValue(new ArrayList<>());

        this.pwvs = new MutableLiveData<>();
        this.pwvs.setValue(new ArrayList<>());

        this.location = new MutableLiveData<>();
        this.resetLocation();

        com.habapp.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_farm, R.id.nav_vegetables, R.id.nav_visits)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}