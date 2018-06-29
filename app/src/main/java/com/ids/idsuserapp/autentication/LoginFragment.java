package com.ids.idsuserapp.autentication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ids.idsuserapp.R;
import com.ids.idsuserapp.percorso.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

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

        public ViewHolder(View view) {

        }
    }
}
