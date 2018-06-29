package com.ids.idsuserapp.autentication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;


import com.android.volley.toolbox.Volley;
import com.ids.idsuserapp.HomeActivity;
import com.ids.idsuserapp.R;
import com.ids.idsuserapp.entityhandlers.UserRequestHandler;
import com.ids.idsuserapp.percorso.BaseFragment;
import com.ids.idsuserapp.utils.ConnectionChecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.microedition.khronos.egl.EGLDisplay;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {
    public static final String TAG = RegistrationFragment.class.getSimpleName();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RegistrationFragment.ViewHolder holder;

    private UserRequestHandler userRequestHandler;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


            /*userRequestHandler = new UserRequestHandler(getContext());

            if (ConnectionChecker.getInstance().isNetworkAvailable(getContext()))
                creaUserServer();
       */ }
    }



    /*public void creaUserServer(){
        userRequestHandler.creaUserServer(email,password);
    }*/


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_registration, container, false);
        holder = new RegistrationFragment.ViewHolder(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public class ViewHolder extends BaseFragment.ViewHolder {

        public final Button registerButton;
        public final EditText email;
        public final EditText password;
        public final EditText pwd_confirmation;
        public UserRequestHandler userRequestHandler;


        public ViewHolder(View view) {


            registerButton = view.findViewById(R.id.register_btn);
            email = view.findViewById(R.id.email_text);
            password = view.findViewById(R.id.pwd_text);
            pwd_confirmation = view.findViewById(R.id.confirmation_text);

            userRequestHandler = new UserRequestHandler(getContext());






            registerButton.setOnClickListener(new View.OnClickListener(){

                public void onClick(View v) {

                    String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +"\\@" +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +"(" +"\\." +"[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +")+";



                    String mail = email.getText().toString();

                    Matcher matcher= Pattern.compile(validemail).matcher(mail);


                    if (!matcher.matches()){
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("oops!");
                        alertDialog.setMessage("Inserire email valida");
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                    }
                                });
                        alertDialog.show();
                    }
                    else if(password.getText().toString().equals("")){

                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("oops!");
                        alertDialog.setMessage("Hai dimenticato di inserire la password");
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                    }
                                });

                        alertDialog.show();
                    }

                    else if(!pwd_confirmation.getText().toString().equals(password.getText().toString())){
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("oops!");
                        alertDialog.setMessage("Le due password non combaciano");
                        alertDialog.setButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //dismiss the dialog
                                    }
                                });
                        alertDialog.show();
                    }


                    else{

                        String new_mail = email.getText().toString();

                        String pas = password.getText().toString();


                        if (ConnectionChecker.getInstance().isNetworkAvailable(getContext()))
                            userRequestHandler.creaUserServer(new_mail,pas);


                    }

                }
            });







        }
    }
}



