package com.ids.idsuserapp;

import ir.mirrajabi.searchdialog.core.Searchable;

public class SearchModel implements Searchable {

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    private String mTitle;



    public String getTitle() {
        return mTitle;
    }


    public SearchModel(String mTitle) {
        this.mTitle = mTitle;
    }




}
