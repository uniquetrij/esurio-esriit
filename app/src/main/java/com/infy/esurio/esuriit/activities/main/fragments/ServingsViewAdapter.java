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
import com.infy.esurio.esuriit.app.services.ServingsService;
import com.infy.esurio.middleware.DTO.OutletsDTO;
import com.infy.esurio.middleware.DTO.ServingsDTO;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ServingsViewAdapter extends RecyclerView.Adapter<ServingsViewAdapter.ViewHolder> {

    private static final String TAG = "ServingsViewAdapter";

    private final ObservableList<ServingsDTO> map;
    private final ServingsListFragment.OnServingsItemClickedListener listener;

    public ServingsViewAdapter(ObservableList<ServingsDTO> items, ServingsListFragment.OnServingsItemClickedListener listener) {

        this.map = items;
        this.map.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<ServingsDTO>>() {
            @Override
            public void onChanged(ObservableList<ServingsDTO> sender) {
                Log.d(TAG, "onChanged");
                ServingsViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<ServingsDTO> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<ServingsDTO> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeMoved(ObservableList<ServingsDTO> sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<ServingsDTO> sender, int positionStart, int itemCount) {

            }
        });

        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_servings_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = map.get(position);
        ServingsService.putName(map.get(position), holder.tv_servings_name);
        ServingsService.putImage(map.get(position), holder.iv_servings);
        ServingsService.putInfo(map.get(position), holder.iv_servings_sticker);
        ServingsService.putPrice(map.get(position), holder.tv_servings_price);

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
        public final TextView tv_servings_name;
        private final ImageView iv_servings;
        private final TextView tv_servings_price;
        private final ImageView iv_servings_sticker;
        public ServingsDTO item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_servings_name = view.findViewById(R.id.tv_servings_name);
            iv_servings = view.findViewById(R.id.iv_servings);
            iv_servings_sticker = view.findViewById(R.id.iv_servings_sticker);
            tv_servings_price = view.findViewById(R.id.tv_servings_price);


//            mContentView = view.findViewById(R.id.item_content);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
