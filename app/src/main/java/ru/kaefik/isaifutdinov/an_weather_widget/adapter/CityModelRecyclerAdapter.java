package ru.kaefik.isaifutdinov.an_weather_widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import ru.kaefik.isaifutdinov.an_weather_widget.ConfigActivity;
import ru.kaefik.isaifutdinov.an_weather_widget.R;

public class CityModelRecyclerAdapter extends RecyclerView.Adapter<CityModelRecyclerAdapter.ViewHolder> {

    private List<String> mDataSet;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.ItemTextView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CityModelRecyclerAdapter(List<String> myDataset) {
        mDataSet = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CityModelRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        Log.i(ConfigActivity.TAG_SERVICE, " onCreateViewHolder -> ");
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_view_recycler, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.i(ConfigActivity.TAG_SERVICE, " onBindViewHolder mDataSet -> " + mDataSet.toString());
        String dataProvider = mDataSet.get(position);
        Log.i(ConfigActivity.TAG_SERVICE, " onBindViewHolder -> " + dataProvider);

        holder.mTextView.setText(dataProvider);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        Log.i(ConfigActivity.TAG_SERVICE, " getItemCount()  -> mDataSet.size() " + Integer.toString(mDataSet.size()));
        return mDataSet.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
