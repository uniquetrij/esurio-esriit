package com.infy.esurio.outlet.activities.main.fragments;

import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.infy.esurio.R;

import com.infy.esurio.outlet.app.services.OrdersService;
import com.infy.esurio.middleware.DTO.OrdersDTO;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class OrdersViewAdapter extends RecyclerView.Adapter<OrdersViewAdapter.ViewHolder> {

    private static final String TAG = "OrdersViewAdapter";

    private final ObservableList<OrdersDTO> map;
    private final OrdersListFragment.OnOrdersItemClickedListener listener;

    public OrdersViewAdapter(ObservableList<OrdersDTO> items, OrdersListFragment.OnOrdersItemClickedListener listener) {
        Log.d(TAG, "OrdersViewAdapter");
        this.map = items;
        this.map.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<OrdersDTO>>() {
            @Override
            public void onChanged(ObservableList<OrdersDTO> sender) {
                Log.d(TAG, "onChanged");
                OrdersViewAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(ObservableList<OrdersDTO> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<OrdersDTO> sender, int positionStart, int itemCount) {

            }

            @Override
            public void onItemRangeMoved(ObservableList<OrdersDTO> sender, int fromPosition, int toPosition, int itemCount) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<OrdersDTO> sender, int positionStart, int itemCount) {

            }
        });

        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_orders_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = map.get(position);
        OrdersService.putNumber(map.get(position), holder.tv_orders_number);

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
        public final TextView tv_orders_number;
        public OrdersDTO item;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_orders_number = view.findViewById(R.id.tv_orders_number);


//            mContentView = view.findViewById(R.id.item_content);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
