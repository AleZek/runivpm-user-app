package com.ids.idsuserapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.db.entity.Mappa;
import com.ids.idsuserapp.utils.FileHelper;

import java.util.ArrayList;
import java.util.List;


public class MappaRecyclerAdapter extends RecyclerView.Adapter<MappaRecyclerAdapter.MappaViewHolder> implements Filterable{


    private Context context;
    private final LayoutInflater mInflater;
    private List<Mappa> mMappe; // Cached copy
    private List<Mappa> mMappeCercate; // Cached copy
    private String lastFilter = "";
    private MappaUpdater mappaUpdater;


    public MappaRecyclerAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mMappe = new ArrayList<>();
        mMappeCercate = new ArrayList<>();
        mappaUpdater = (MappaUpdater) context;
    }




    //VIEWHOLDER
    class MappaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        private final TextView mapName;
        private final ImageView mapImage;
        private Integer idMappa;



        private MappaViewHolder(View itemView) {
            super(itemView);
            mapName = itemView.findViewById(R.id.item_name);
            mapImage = itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);

            itemView.setOnCreateContextMenuListener(this);
        }




        @Override
        public void onClick(View v){

        }



        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Seleziona una azione");
            menu.add(0,0,0, "Modifica Mappa");
            menu.add(0,1,0, "Cancella Mappa");
            menu.add(0,2,0, "Gestisci Beacon");
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
    public MappaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.places_recycler_item, parent, false);
        return new MappaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MappaViewHolder holder, int position) {
        Mappa current = mMappeCercate.get(position);
        holder.mapName.setText(current.getName());
        String img_name = current.getImmagine();
        holder.idMappa = current.getId();
        if (!img_name.equals("null")){
            FileHelper fileHelper = new FileHelper(context);
            String directory_mappe = context.getResources().getString(R.string.directory_mappe);
            Bitmap img_bitmap = fileHelper.getBitmapFromFile(directory_mappe, img_name);
            holder.mapImage.setImageBitmap(img_bitmap);
        }
        else{
            holder.mapImage.setImageBitmap(null);
            holder.mapImage.setImageResource(R.drawable.ic_map_blue_48dp);
        }

    }




    // getItemCount() is called many times, and when it is first called,
    // mMappeCercate has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mMappeCercate != null)
            return mMappeCercate.size();
        else return 0;
    }



    // getter e setter
    public List<Mappa> getmMappe() {
        return mMappe;
    }

    public void setmMappe(List<Mappa> mMappe) {
        this.mMappe = mMappe;
        notifyDataSetChanged();
        getFilter().filter(lastFilter);
        notifyDataSetChanged();
    }

    public List<Mappa> getmMappeCercate() {
        return mMappeCercate;
    }

    public void setmMappeCercate(List<Mappa> mMappeCercate) {
        this.mMappeCercate = mMappeCercate;
    }

    // filtro
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mMappeCercate = (List<Mappa>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                lastFilter = constraint.toString();
                mMappeCercate.clear();

                if (lastFilter.length() == 0 || lastFilter.equals("")) {
                    mMappeCercate.addAll(mMappe);
                } else {
                    mMappeCercate = getFilteredResults(lastFilter.toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = mMappeCercate;
                results.count = mMappeCercate.size();
                return results;
            }
        };
    }


    protected List<Mappa> getFilteredResults(String constraint) {
        List<Mappa> results = new ArrayList<>();

        for (Mappa item : mMappe) {
            if (item.getName().toLowerCase().contains(constraint.toLowerCase())) {
                results.add(item);
            }
        }
        return results;
    }




    public interface MappaUpdater{

        public void eliminaMappa(int id);
//        public void aggiornaMappa();
    }

}


