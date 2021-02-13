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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterFragment extends Fragment {


    TextView gotoSignIn;
    NavController navController;
    String name, email, password;
    EditText et_email, et_password, et_name;
    Button signUp;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String userid;

    ProgressDialog pd;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        gotoSignIn = view.findViewById(R.id.gottoSignInActivity);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(getContext());

        navController = Navigation.findNavController(view);
        et_email = view.findViewById(R.id.etEmailSignUp);
        et_name  = view.findViewById(R.id.etNameSignUp);
        et_password = view.findViewById(R.id.etPasswordSignUp);
        signUp = view.findViewById(R.id.signUpBtn);



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = et_name.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();


                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {

                    Toast.makeText(getContext(), "Fields are required", Toast.LENGTH_SHORT).show();
                }

                if (name.isEmpty()) {

                    et_name.setError("Enter a name");


                }

                if (password.length() < 6) {

                    et_password.setError("Password Length Must Be 6 or more Chars");
                }

                if (email.isEmpty()) {

                    et_email.setError("Enter email Bitch");



                }


                signUptheDamnUser(name, email, password);






            }
        });


        gotoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });


    }

    private void signUptheDamnUser(String name, String email, String password) {

        pd.show();
        pd.setMessage("Signing Up");

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {


                    pd.dismiss();
                    userid = firebaseAuth.getCurrentUser().getUid();


                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("userid", userid);
                    hashMap.put("imageUrl", "default");
                    hashMap.put("status", "offline");
                    hashMap.put("username", name);

                    firestore.collection("Users").document(userid).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });



                    Toast.makeText(getContext(), "Signed Up", Toast.LENGTH_SHORT).show();

                    navController.navigate(R.id.action_registerFragment_to_loginFragment);



                }

            }
        });




    }


}