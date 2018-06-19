package com.ids.idsuserapp;

import android.app.SearchManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class BeaconNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_navigation);

        findViewById(R.id.starting_beacon_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(BeaconNavigationActivity.this, "Cerca", "Seleziona beacon", null, initData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        Toast.makeText(BeaconNavigationActivity.this,"Hai selezionato il "+searchable.getTitle(),Toast.LENGTH_SHORT).show();
                        baseSearchDialogCompat.dismiss();

                    }
                }).show();


            }



    });

        findViewById(R.id.ending_beacon_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleSearchDialogCompat(BeaconNavigationActivity.this, "Cerca", "Seleziona beacon", null, initData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        Toast.makeText(BeaconNavigationActivity.this,"Hai selezionato il "+searchable.getTitle(),Toast.LENGTH_SHORT).show();
                        baseSearchDialogCompat.dismiss();

                    }
                }).show();


            }



        });




    }

        private ArrayList<SearchModel> initData(){
            ArrayList<SearchModel> items = new ArrayList<>();
            items.add(new SearchModel("beacon1"));
            items.add(new SearchModel("beacon2"));
            items.add(new SearchModel("beacon3"));
            items.add(new SearchModel("beacon4"));
            items.add(new SearchModel("beacon5"));
            items.add(new SearchModel("beacon6"));
            items.add(new SearchModel("beacon7"));

            return items;
        }
    }
