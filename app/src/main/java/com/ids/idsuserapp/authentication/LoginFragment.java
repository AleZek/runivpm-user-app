package com.ids.idsuserapp.authentication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import android.app.ProgressDialog;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.entityhandlers.UserRequestHandler;
import com.ids.idsuserapp.percorso.BaseFragment;
import com.ids.idsuserapp.threads.LocatorThread;
import com.ids.idsuserapp.utils.ConnectionChecker;
import com.ids.idsuserapp.utils.PermissionsUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();
    public static final int ACCOUNT_CREATED = 1;
    public static final String INTENT_KEY_EMAIL = "email";
    public static final String INTENT_KEY_PASSWORD = "password";
    private static final String EMAIL = "email";

    private String email;
    //private EmailAutocompleter emailAutocompleter;

    private PermissionsUtil permissionsUtil;


    private LoginFragment.ViewHolder holder;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param email Parameter 1.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String email) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(EMAIL);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        holder = new LoginFragment.ViewHolder(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    public class ViewHolder extends BaseFragment.ViewHolder {

        public final EditText emailTxt;
        public final EditText pwdTxt;
        public final Button loginButton;
        public ProgressDialog progressDialog;
        public UserRequestHandler userRequestHandler;
        private ProgressBar progressbar;
        final RelativeLayout layout;


        public ViewHolder(View view) {
            emailTxt = view.findViewById(R.id.email_txt);
            pwdTxt = view.findViewById(R.id.pwd_txt);
            loginButton = view.findViewById(R.id.login_btn);
            layout = view.findViewById(R.id.relative_layout);


            userRequestHandler = new UserRequestHandler(getContext());


            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String validemail = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";


                    String mail = emailTxt.getText().toString();

                    Matcher matcher = Pattern.compile(validemail).matcher(mail);


                    if (!matcher.matches()) {
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
                    } else if (pwdTxt.getText().toString().equals("")) {

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
                    } else {

                        String new_mail = emailTxt.getText().toString();

                        String pas = pwdTxt.getText().toString();


                        if (ConnectionChecker.getInstance().isNetworkAvailable(getContext())) {
                            userRequestHandler.loginUserServer(new_mail, pas);
                        }
                        else
                            showOfflineAlert();
                    }

                }
            });


        }



        private void showOfflineAlert() {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("oops!");
            alertDialog.setMessage("Nessuna connessione a Internet. Connettersi a una rete e riprovare");
            alertDialog.setButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss the dialog
                        }
                    });

            alertDialog.show();
        }

        }
    }

