package com.infy.esurio.esuriit.activities.main.fragments;

import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infy.esurio.R;

import com.infy.esurio.esuriit.activities.main.fragments.dummy.DummyContent.DummyItem;
import com.infy.esurio.esuriit.app.This;
import com.infy.esurio.esuriit.app.services.FoodcourtService;
import com.infy.esurio.middleware.DTO.FoodcourtsDTO;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FoodcourtsViewAdapter extends RecyclerView.Adapter<FoodcourtsViewAdapter.ViewHolder> {

    private static final String TAG = "FoodcourtsViewAdapter";

    private final ObservableList<FoodcourtsDTO> map;
    private final FoodcourtListFragment.OnOrdersItemClickedListener listener;

    public FoodcourtsViewAdapter(ObservableList<FoodcourtsDTO> items, FoodcourtListFragment.OnOrdersItemClickedListener listener) {
        Log.d(TAG, "OrdersViewAdapter");
        this.map = items;
        this.map.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<FoodcourtsDTO>>() {
            @Override
            public void onChanged(ObservableList<FoodcourtsDTO> sender) {
                Log.d(TAG, "onChanged");
                FoodcourtsViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<FoodcourtsDTO> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<FoodcourtsDTO> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeMoved(ObservableList<FoodcourtsDTO> sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<FoodcourtsDTO> sender, int positionStart, int itemCount) {

            }
        });

        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_foodcourts_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = map.get(position);
        FoodcourtService.putName(map.get(position), holder.tv_foodcourts_name);
        FoodcourtService.putImage(map.get(position), holder.iv_foodcourts_img);
        FoodcourtService.putDistance(map.get(position), holder.tv_foodcourts_distance);
        FoodcourtService.putServiceTime(map.get(position), holder.tv_foodcourts_servtime);
        FoodcourtService.putOccupancy(map.get(position), holder.tv_foodcourts_occupancy);
        FoodcourtService.putMapIntent(map.get(position), holder.iv_location);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onListFragmentInteraction(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tv_foodcourts_name;
        public final TextView tv_foodcourts_distance;
        private final TextView tv_foodcourts_servtime;
        private final TextView tv_foodcourts_occupancy;
        private final ImageView iv_foodcourts_img;
        private final ImageView iv_location;
        public FoodcourtsDTO item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_foodcourts_name = view.findViewById(R.id.tv_foodcourts_name);
            tv_foodcourts_distance = view.findViewById(R.id.tv_foodcourts_distance);
            tv_foodcourts_servtime = view.findViewById(R.id.tv_foodcourts_servtime);
            tv_foodcourts_occupancy = view.findViewById(R.id.tv_foodcourts_occupancy);
            iv_foodcourts_img = view.findViewById(R.id.iv_foodcourts_img);
            iv_location = view.findViewById(R.id.iv_location);


//            mContentView = view.findViewById(R.id.item_content);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
