package com.habapp.ui.farm.list;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import com.habapp.MainActivity;
import com.habapp.databinding.FragmentMapLocationFarmBinding;
import com.habapp.utils.Location;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;


public class MapLocationFarmFragment extends Fragment {

    FragmentMapLocationFarmBinding binding;
    MapView map = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapLocationFarmBinding.inflate(inflater, container, false);

        MainActivity activity = (MainActivity) this.getActivity();
        LiveData<Location> location = activity.getLocation();
        double latitude = location.getValue().getLatitude();
        double longitude = location.getValue().getLongitude();

        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        map = binding.map;
        map.setTileSource(TileSourceFactory.MAPNIK);

        IMapController mapController = map.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(latitude, longitude);
        mapController.setCenter(startPoint);

        Marker poiMarker = new Marker(map);
        Drawable poiIcon =
                ResourcesCompat.getDrawable(getResources(), org.osmdroid.bonuspack.R.drawable.marker_default, null);
        poiMarker.setIcon(poiIcon);
        poiMarker.setPosition(startPoint);
        map.getOverlays().add(poiMarker);

        MapEventsOverlay overlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                activity.setLocation(new Location(p.getLatitude(), p.getLongitude()));
                poiMarker.setPosition(p);
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        });

        map.getOverlays().add(0, overlay);

        return binding.getRoot();
    }
}