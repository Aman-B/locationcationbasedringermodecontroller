package com.bewtechnologies.gtone;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aman  on 7/23/2015.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private String[] mDataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        TextView placename;
        TextView ringermode;
        ImageView logo;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            placename = (TextView)itemView.findViewById(R.id.place_name);
            ringermode = (TextView)itemView.findViewById(R.id.ringermode);
            logo = (ImageView)itemView.findViewById(R.id.logo);
        }
    }



    List<PlaceDetails> places;

    public CardAdapter(List<PlaceDetails> places) {
        this.places=places;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.placename.setText(places.get(i).place_name);
        viewHolder.ringermode.setText(places.get(i).place_ringermode);
        viewHolder.logo.setImageResource(R.drawable.ic_launcher);

    }

    @Override
    public int getItemCount() {
        return places.size();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
