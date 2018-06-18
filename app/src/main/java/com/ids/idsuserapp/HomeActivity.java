package com.ids.idsuserapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ids.idsuserapp.db.entity.Beacon;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    private void openSelectionPath(View v) {
        SelectionFragment selectionFragment;
        Beacon alreadySelectedNode = null;

        int code = 1;
        switch (v.getId()) {
            case R.id.scegli_da_mappa:
                code = ORIGIN_SELECTION_REQUEST_CODE;
                alreadySelectedNode = destination;
                break;
            case R.id.navigation_input_destination:
                code = DESTINATION_SELECTION_REQUEST_CODE;
                alreadySelectedNode = origin;
                break;
        }

        selectionFragment = SelectionFragment.newInstance(code, alreadySelectedNode, offline);
        selectionFragment.setTargetFragment(this, code);
        ((NavigationActivity) getActivity()).changeFragment(selectionFragment);
    }
}
