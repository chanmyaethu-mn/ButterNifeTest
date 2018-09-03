package com.test.cmt.butternifetest.activity;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.test.cmt.butternifetest.R;
import com.test.cmt.butternifetest.common.Constants;
import com.test.cmt.butternifetest.common.util.PermissionUtils;
import com.test.cmt.butternifetest.entity.LocationEntity;
import com.test.cmt.butternifetest.viewmodel.LocationViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.test.cmt.butternifetest.common.Constants.LOCATION_PERMISSION_REQUEST_CODE;
import static com.test.cmt.butternifetest.common.Constants.M_RESULT_UPDATE;

public class EditLocationActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.editLocNameEt)
    EditText editLocNameEt;

    @BindView(R.id.editLocCorTv)
    TextView editLocCorTv;

    @BindView(R.id.editLocSaveBtn)
    Button editLocSaveBtn;

    @BindView(R.id.editLocDelBtn)
    Button editLocDelBtn;

    GoogleMap googleMap;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private LocationViewModel mLocationViewModel;

    private long mLocId;

    private boolean mPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        ButterKnife.bind(this);

        final long locId = getIntent().getLongExtra(Constants.EDIT_LOCATION_ID, -1);
        if (-1 == locId) {
            setResult(Constants.M_RESULT_ERROR);
            finish();
        }

        mLocId = locId;

        mLocationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        /*Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }*/


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.editLocMap);
        mapFragment.getMapAsync(this);

    }

    @OnClick(R.id.editLocDelBtn)
    void delBtnClick() {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(Constants.DELETE_LOCATION_ID, mLocId);
        setResult(Constants.M_RESULT_DELETE, replyIntent);
        finish();
    }

    @OnClick(R.id.editLocSaveBtn)
    void updateBtnClick() {

        String name = editLocNameEt.getText().toString();
        String cor = editLocCorTv.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(cor)) {
            Toast.makeText(this, "Fill data correctly", Toast.LENGTH_SHORT).show();
        } else {
            Intent replyIntent = new Intent();
            LocationEntity locationEntity = new LocationEntity(mLocId, name, cor);
            replyIntent.putExtra(Constants.EDIT_LOCATION_EXTRA_REPLY, locationEntity);
            setResult(Constants.M_RESULT_UPDATE, replyIntent);
            finish();
        }
    }

    private class LocationByIdAsyncTask extends AsyncTask<Long, Void, LocationEntity> {

        @Override
        protected LocationEntity doInBackground(Long... longs) {
            long locId = longs[0];
            LocationEntity locationEntity = mLocationViewModel.getLocationById(locId);
            return locationEntity;
        }

        @Override
        protected void onPostExecute(LocationEntity locationEntity) {
            super.onPostExecute(locationEntity);

            // Add a marker in Sydney and move the camera

            //if (null != mLocationEntity) {
            String[] latLngArr = locationEntity.getLatLon().split(",");

            LatLng editLoc = new LatLng(Double.parseDouble(latLngArr[0]), Double.parseDouble(latLngArr[1]));
            //LatLng editLoc = new LatLng(22.135256,95.122832);
            googleMap.setMinZoomPreference(12);
            googleMap.addMarker(new MarkerOptions().position(editLoc).title(locationEntity.getPlaceName()));
            //googleMap.addMarker(new MarkerOptions().position(editLoc).title("MmHash Co., Ltd."));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(editLoc));

            editLocNameEt.setText(locationEntity.getPlaceName());
            editLocCorTv.setText(locationEntity.getLatLon());
        }
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

        // Add a marker in Sydney and move the camera
        /*LatLng defaultLocation = new LatLng(22.135256, 95.122832);
        googleMap.setMinZoomPreference(12);
        googleMap.setIndoorEnabled(true);

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(defaultLocation);
        markerOptions.title("MmHash Co., Ltd.");
        googleMap.addMarker(markerOptions);*/

        /*googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));*/

        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        new LocationByIdAsyncTask().execute(mLocId);

        enableMyLocation();

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (googleMap != null) {
            // Access to the location has been granted to the app.
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        //editLocMv.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //editLocMv.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //editLocMv.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //editLocMv.onStop();
    }

    @Override
    protected void onPause() {
        //editLocMv.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //editLocMv.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //editLocMv.onLowMemory();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        //if (null != mMap) {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            enableMyLocation();
        } else {
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));

            if (null != location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                //Toast.makeText(this, latitude + "," + longitude, Toast.LENGTH_SHORT).show();

                editLocCorTv.setText(latitude+","+longitude);
            }

        }

        //}
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }
}
