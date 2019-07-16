package com.infy.esurio.esuriit.activities.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.infy.esurio.R;
import com.infy.esurio.esuriit.app.This;
import com.infy.esurio.esuriit.app.services.OutletService;
import com.infy.esurio.middleware.DTO.OutletsDTO;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OutletListFragment extends Fragment {

    private static final String TAG = "OutletListFragment";
    private OnOutletsItemClickedListener outletItemClickedListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OutletListFragment() {
        OutletService.fetch();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((TextView)This.MAIN_ACTIVITY.self.findViewById(R.id.tv_actionbar)).setText("Outlets");
        View view = inflater.inflate(R.layout.fragment_outlets_list, container, false);
        Log.d(TAG,"onCreate "+(view instanceof RecyclerView) );
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new OutletsViewAdapter(This.OUTLETS, outletItemClickedListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOutletsItemClickedListener) {
            outletItemClickedListener = (OnOutletsItemClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFoodcourtsItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        outletItemClickedListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnOutletsItemClickedListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(OutletsDTO item);
    }


}
