package com.test.cmt.butternifetest.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.test.cmt.butternifetest.R;
import com.test.cmt.butternifetest.common.Constants;
import com.test.cmt.butternifetest.common.util.PermissionUtils;
import com.test.cmt.butternifetest.entity.LocationEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.test.cmt.butternifetest.common.Constants.LOCATION_PERMISSION_REQUEST_CODE;
import static com.test.cmt.butternifetest.common.Constants.MAP_VIEW_BUNDLE_KEY;

public class NewLocationActivity extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.newLocNameEt)
    EditText newLocNameEt;

    @BindView(R.id.newLocCorTv)
    TextView newLocCorTv;

    @BindView(R.id.newLocSaveBtn)
    Button newLocSaveBtn;

    @BindView(R.id.newLocCancelBtn)
    Button newLocCancelBtn;

    GoogleMap mMap;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);

        ButterKnife.bind(this);

       /* Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }*/

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @OnClick(R.id.newLocSaveBtn)
    void saveBtnClick() {
        Intent replyIntent = new Intent();
        String name = newLocNameEt.getText().toString();
        String cor = newLocCorTv.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(cor)) {
            Toast.makeText(this, "Fill data correctly", Toast.LENGTH_SHORT).show();
        } else {
            LocationEntity locationEntity = new LocationEntity(name, cor);
            replyIntent.putExtra(Constants.NEW_LOCATION_EXTRA_REPLY, locationEntity);
            setResult(RESULT_OK, replyIntent);
            finish();
        }
    }

    @OnClick(R.id.newLocCancelBtn)
    void cancelBtnClick() {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        mMap = gMap;

        // Add a marker in Sydney and move the camera
        LatLng defaultLocation = new LatLng(22.135256, 95.122832);
        mMap.setMinZoomPreference(12);
        mMap.setIndoorEnabled(true);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(defaultLocation);
        markerOptions.title("MmHash Co., Ltd.");
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        //newLocMv.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //newLocMv.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //newLocMv.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //newLocMv.onStop();
    }

    @Override
    protected void onPause() {
        //newLocMv.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //newLocMv.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //newLocMv.onLowMemory();
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
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
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

                    newLocCorTv.setText(latitude+","+longitude);
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
