package com.infy.esurio.esuriit.activities.main.fragments;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import com.infy.esurio.R;
import com.infy.esurio.esuriit.app.services.OutletService;
import com.infy.esurio.middleware.DTO.OutletsDTO;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class OutletsViewAdapter extends RecyclerView.Adapter<OutletsViewAdapter.ViewHolder> {

    private static final String TAG = "FoodcourtsViewAdapter";

    private final ObservableList<OutletsDTO> map;
    private final OutletListFragment.OnOutletsItemClickedListener listener;

    public OutletsViewAdapter(ObservableList<OutletsDTO> items, OutletListFragment.OnOutletsItemClickedListener listener) {
        Log.d(TAG, "OrdersViewAdapter");
        this.map = items;
        this.map.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<OutletsDTO>>() {
            @Override
            public void onChanged(ObservableList<OutletsDTO> sender) {
                Log.d(TAG, "onChanged");
                OutletsViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<OutletsDTO> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<OutletsDTO> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeMoved(ObservableList<OutletsDTO> sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<OutletsDTO> sender, int positionStart, int itemCount) {

            }
        });

        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_outlets_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = map.get(position);
        OutletService.putName(map.get(position), holder.tv_outlets_name);
        OutletService.putImage(map.get(position), holder.iv_outlets);
        OutletService.putServiceTime(map.get(position), holder.tv_outlets_servtime);
        OutletService.putRating(map.get(position), holder.rb_outlets);

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
        public final TextView tv_outlets_name;
        private final TextView tv_outlets_servtime;
        private final ImageView iv_outlets;
        private final RatingBar rb_outlets;
        public OutletsDTO item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_outlets_name = view.findViewById(R.id.tv_outlets_name);
            tv_outlets_servtime = view.findViewById(R.id.tv_outlets_servtime);
            iv_outlets = view.findViewById(R.id.iv_outlets);
            rb_outlets = view.findViewById(R.id.rb_outlets);


//            mContentView = view.findViewById(R.id.item_content);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
