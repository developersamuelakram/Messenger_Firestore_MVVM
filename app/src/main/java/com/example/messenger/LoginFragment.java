package com.example.messenger;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment {


    TextView gotSignupActivity;
    NavController navController;
    String email, password;
    EditText et_email, et_password;
    Button signInbtn;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;



    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gotSignupActivity = view.findViewById(R.id.gotosignup);
        navController = Navigation.findNavController(view);
        firebaseAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(getContext());

        et_email = view.findViewById(R.id.etEmail);
        et_password = view.findViewById(R.id.etPassword);
        signInbtn = view.findViewById(R.id.signInBtn);


        gotSignupActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                password = et_password.getText().toString();


                if (email.isEmpty() || password.isEmpty()) {

                    Toast.makeText(getContext(), "Credentials Required", Toast.LENGTH_SHORT).show();

                }

                if (password.length() < 6) {

                    et_password.setError("Password Length Must Be 6 or more Chars");


                }


                signInTheUser(email, password);
            }
        });



    }

    private void signInTheUser(String email, String password) {
        pd.show();
        pd.setMessage("Signing In");

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    pd.dismiss();

                    Toast.makeText(getContext(), "Signed In", Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_loginFragment_to_userFragment);

                }

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser()!=null) {

            navController.navigate(R.id.action_loginFragment_to_userFragment);


        }
    }
}