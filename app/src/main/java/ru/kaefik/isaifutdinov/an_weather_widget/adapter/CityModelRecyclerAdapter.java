package ru.kaefik.isaifutdinov.an_weather_widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.kaefik.isaifutdinov.an_weather_widget.AddNewCityActivity;
import ru.kaefik.isaifutdinov.an_weather_widget.R;

public class CityModelRecyclerAdapter extends RecyclerView.Adapter<CityModelRecyclerAdapter.ViewHolder> {

    private List<String> mDataSet;

    private final OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(String item);
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        protected TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.NameCityTextView);
        }

        public void bind(final String item, final OnItemClickListener listener) {

            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public CityModelRecyclerAdapter(List<String> myDataset, OnItemClickListener listener) {
        this.mDataSet = myDataset;
        this.listener = listener;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CityModelRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        Log.i(AddNewCityActivity.TAG_SERVICE, " onCreateViewHolder -> ");
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
        Log.i(AddNewCityActivity.TAG_SERVICE, " onBindViewHolder mDataSet -> " + mDataSet.toString());
        String dataProvider = mDataSet.get(position);
        Log.i(AddNewCityActivity.TAG_SERVICE, " onBindViewHolder -> " + dataProvider);

        holder.mTextView.setText(dataProvider);

        holder.bind(dataProvider, listener);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        Log.i(AddNewCityActivity.TAG_SERVICE, " getItemCount()  -> mDataSet.size() " + Integer.toString(mDataSet.size()));
        return mDataSet.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
