package com.habapp.ui.farm;

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

import com.habapp.databinding.FragmentMapFarmBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapFarmFragment extends Fragment {

    FragmentMapFarmBinding binding;
    MapView map = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapFarmBinding.inflate(inflater, container, false);

        float latitude = getArguments().getFloat("latitude");
        float longitude = getArguments().getFloat("longitude");
        String name = getArguments().getString("name");

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
        poiMarker.setTitle(name);
        poiMarker.setIcon(poiIcon);
        poiMarker.setPosition(startPoint);
        map.getOverlays().add(poiMarker);

        return binding.getRoot();
    }
}