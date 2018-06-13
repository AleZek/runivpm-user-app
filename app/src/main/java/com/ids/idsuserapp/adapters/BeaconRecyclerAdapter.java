package com.ids.idsuserapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.dao.BeaconDao;
import com.ids.idsuserapp.db.entity.Beacon;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by zek on 28/03/18.
 */

public class BeaconRecyclerAdapter extends RecyclerView.Adapter<BeaconRecyclerAdapter.BeaconViewHolder> implements Filterable {

    private Context context;
    private final LayoutInflater mInflater;
    private List<Beacon> mBeacon; // Cached copy
    private List<BeaconDao.BeaconWithMap> beaconWithMap; // Cached copy
    private List<Beacon> mBeaconCercati; // Cached copy
    private String lastFilter = "";
    private BeaconUpdater beaconUpdater;
    private SearchView searchView; //questa variabile punta alla searchview da aggiornare


    public BeaconRecyclerAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mBeacon = new ArrayList<>();
        mBeaconCercati = new ArrayList<>();
        //beaconUpdater = (BeaconUpdater) context;
    }


    //VIEWHOLDER
    class BeaconViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{

        private final TextView beaconNameItemView;
        private Integer idBeacon;


        private BeaconViewHolder(View itemView) {
            super(itemView);
            beaconNameItemView = itemView.findViewById(R.id.item_name);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, mBeacon.get(getAdapterPosition()).getNome(), Toast.LENGTH_LONG).show();
            searchView.setQuery(mBeaconCercati.get(getAdapterPosition()).getNome(),true);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            menu.setHeaderTitle("Seleziona una azione");
            menu.add(0,0,0, "Modifica Beacon");
            menu.add(0,1,0, "Cancella Beacon");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
            itemView.setOnClickListener(this);
        }
    }

    //METODI VIEW
    @Override
    public BeaconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.places_recycler_item1, parent, false);
        return new BeaconRecyclerAdapter.BeaconViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(BeaconRecyclerAdapter.BeaconViewHolder holder, int position) {
        Beacon current = mBeaconCercati.get(position);

        holder.beaconNameItemView.setText(current.getNome());
        holder.idBeacon = current.getId();
    }

    // getItemCount() is called many times, and when it is first called,
    // mBeaconCercati has not been updated (means initially, it's null, and we can't return null).

    public int getItemCount() {
        if (mBeaconCercati != null)
            return mBeaconCercati.size();
        else return 0;
    }


    // getter e setter
    public List<Beacon> getmBeacon() {
        return mBeacon;
    }

    public void setmBeacon(List<Beacon> mBeacon) {
        this.mBeacon = mBeacon;
        notifyDataSetChanged();
        getFilter().filter(lastFilter);
        notifyDataSetChanged();
    }

    public void setmBeaconWithMap(List<BeaconDao.BeaconWithMap> mBeacon) {
        this.beaconWithMap = mBeacon;
        notifyDataSetChanged();
        getFilter().filter(lastFilter);
        notifyDataSetChanged();
    }

    public List<Beacon> getmBeaconCercati() {
        return mBeaconCercati;
    }

    public void setmBeaconCercati(List<Beacon> mBeaconCercati) {
        this.mBeaconCercati = mBeaconCercati;
    }

    // filtro
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mBeaconCercati = (List<Beacon>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                lastFilter = constraint.toString();
                mBeaconCercati.clear();

                if (lastFilter.length() == 0 || lastFilter.equals("")) {
                    mBeaconCercati.addAll(mBeacon);
                } else {
                    mBeaconCercati = getFilteredResults(lastFilter.toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = mBeaconCercati;
                results.count = mBeaconCercati.size();
                return results;
            }
        }; 
    }

    protected List<Beacon> getFilteredResults(String constraint) {
        List<Beacon> results = new ArrayList<>();

        for (Beacon item : mBeacon) {
            if (item.getNome().toLowerCase().contains(constraint.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }

    public interface BeaconUpdater{

        public void eliminaBeacon(int id);
//        public void aggiornaBeacon();
    }

    public void setSearchView(SearchView searchView) {
        this.searchView = searchView;
    }
}
