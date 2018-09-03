package com.test.cmt.butternifetest.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.test.cmt.butternifetest.R;
import com.test.cmt.butternifetest.activity.EditLocationActivity;
import com.test.cmt.butternifetest.common.Constants;
import com.test.cmt.butternifetest.entity.LocationEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.LocationViewHolder> {

    private final LayoutInflater mInflater;

    private List<LocationEntity> mLocations;

    private final Context mContext;

    public LocationRecyclerAdapter(Context context) {

        mInflater = LayoutInflater.from(context);

        mContext = context;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mInflater.inflate(R.layout.rec_loc_item_layout, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        if (null != mLocations) {
            LocationEntity locationEntity = mLocations.get(position);
            holder.locationNameTV.setText(locationEntity.getPlaceName());
            holder.locationCoordinateTV.setText(locationEntity.getLatLon());
        }
    }

    @Override
    public int getItemCount() {
        if (null != mLocations) {
            return mLocations.size();
        } else {
            return 0;
        }
    }

    public void setmLocations(List<LocationEntity> mLocations) {
        this.mLocations = mLocations;
        notifyDataSetChanged();
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.locationNameTV)
        TextView locationNameTV;

        @BindView(R.id.locationCoordinateTV )
        TextView locationCoordinateTV;


        public LocationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick
        void itemClick() {
            //Toast.makeText(mContext, getAdapterPosition()+"",Toast.LENGTH_SHORT).show();
            LocationEntity locationEntity = mLocations.get(getAdapterPosition());
            long id = locationEntity.get_id();

            Intent intent = new Intent(mContext, EditLocationActivity.class);
            intent.putExtra(Constants.EDIT_LOCATION_ID, id);

            ((Activity)mContext).startActivityForResult(intent, Constants.EDIT_LOCATION_ACTIVITY_REQUEST_CODE);
        }
    }


}
