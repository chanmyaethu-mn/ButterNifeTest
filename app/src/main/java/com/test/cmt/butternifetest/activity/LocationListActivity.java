package com.test.cmt.butternifetest.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.test.cmt.butternifetest.R;
import com.test.cmt.butternifetest.adapter.LocationRecyclerAdapter;
import com.test.cmt.butternifetest.common.Constants;
import com.test.cmt.butternifetest.entity.LocationEntity;
import com.test.cmt.butternifetest.viewmodel.LocationViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationListActivity extends AppCompatActivity {

    @BindView(R.id.locationRV)
    RecyclerView locationRV;

    @BindView(R.id.fab)
    FloatingActionButton locationFab;

    private LocationViewModel mLocationViewModel;

    private static final int NEW_LOCATION_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_list);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LocationRecyclerAdapter adapter = new LocationRecyclerAdapter(this);
        locationRV.setAdapter(adapter);
        locationRV.setLayoutManager(new LinearLayoutManager(this));
        locationRV.setItemAnimator(new DefaultItemAnimator());

        mLocationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        mLocationViewModel.getmAllLocations().observe(this, new Observer<List<LocationEntity>>() {
            @Override
            public void onChanged(@Nullable List<LocationEntity> locationEntities) {
                // Update the cached copy of the locations in the adapter.
                adapter.setmLocations(locationEntities);
            }
        });
    }

    @OnClick(R.id.fab)
    void addNewLocation() {
        Toast.makeText(this, "Add New Location", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LocationListActivity.this, NewLocationActivity.class);
        startActivityForResult(intent, NEW_LOCATION_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_LOCATION_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            LocationEntity locationEntity = (LocationEntity) data.getSerializableExtra(Constants.NEW_LOCATION_EXTRA_REPLY);
            mLocationViewModel.insert(locationEntity);
        } else if (requestCode == Constants.EDIT_LOCATION_ACTIVITY_REQUEST_CODE && resultCode == Constants.M_RESULT_DELETE) {
            long locId = data.getLongExtra(Constants.DELETE_LOCATION_ID, -1);
            if (-1 != locId) {
                mLocationViewModel.deleteLocationById(locId);
                Toast.makeText(this, "Delete Successful!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Constants.EDIT_LOCATION_ACTIVITY_REQUEST_CODE && resultCode == Constants.M_RESULT_UPDATE) {
            LocationEntity locationEntity = (LocationEntity) data.getSerializableExtra(Constants.EDIT_LOCATION_EXTRA_REPLY);
            mLocationViewModel.update(locationEntity);
        }
    }
}
