package com.example.messenger;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    CircleImageView circleImageView;
    EditText et_username;
    String userNameupdated;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    public static final int  CAMERAPICK = 100; // CAMERA A
    public static final int GALLERYPICK = 200; // GALLERY PIC
    Uri imageUri = null;
    String imageUrl;
    ImageButton updateStuff;
    NavController navController;
    String userid;





    public ProfileFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circleImageView = view.findViewById(R.id.profile_image);
        updateStuff = view.findViewById(R.id.profile_update);
        et_username = view.findViewById(R.id.profile_etname);
        navController = Navigation.findNavController(view);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();



        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        updateStuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userNameupdated = et_username.getText().toString();

                updateUserName(userNameupdated);
            }
        });
    }

    private void updateUserName(String userNameupdated) {



        String theuseridagain = firebaseAuth.getCurrentUser().getUid();

        firestore.collection("Users").document(theuseridagain).update("username", userNameupdated).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (isAdded()) {

                    if (task.isSuccessful()) {

                        navController.navigate(R.id.action_profileFragment_to_userFragment);
                        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });


    }


    private void showImagePickDialog() {

        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Choose an Option");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (i==0) {

                    showDialogCamera();


                }

                if (i==1) {

                    showGalleryDialogue();



                }

            }
        });

        builder.create().show();


    }

    private void showGalleryDialogue() {


        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERYPICK);

    }

    private void showDialogCamera() {


        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        values.put(MediaStore.Images.Media.TITLE, "Temp Desc");
        imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERAPICK);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (requestCode == GALLERYPICK && resultCode == RESULT_OK) {

            Uri uri = data.getData();

            String timestamp = String.valueOf(System.currentTimeMillis());
            String path = "Photos/" + "photos_" + timestamp;

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(path);

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String photoid = uri.toString();
                            imageUri = uri;

                            Glide.with(getContext()).load(imageUri).centerCrop().into(circleImageView);


                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            userid = user.getUid();



                            firestore.collection("Users").document(userid).update("imageUrl", photoid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });



                        }
                    });



                }
            });



        }



        if (requestCode == CAMERAPICK && resultCode == RESULT_OK) {



            Uri uri = imageUri;

            String timestamp = String.valueOf(System.currentTimeMillis());
            String path = "Photos" +"photos_" + timestamp;

            StorageReference ref = FirebaseStorage.getInstance().getReference(path);


            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();

                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String photoid = uri.toString();

                            imageUri = uri;

                            Glide.with(getContext()).load(imageUri).centerCrop().into(circleImageView);


                            firestore.collection("Users").document(userid).update("imageUrl", photoid).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });








                        }
                    });

                }
            });




        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String theuserid = user.getUid();


        firestore.collection("Users").document(theuserid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {

                    DocumentSnapshot ssnahot = task.getResult();

                    String userName = ssnahot.getString("username");
                    imageUrl = ssnahot.getString("imageUrl");

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefsss", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorShared = sharedPreferences.edit();
                    editorShared.putString("username", userName);
                    editorShared.apply();

                    et_username.setText(userName);

                    Glide.with(getContext()).load(imageUrl).centerCrop().into(circleImageView);



                }

            }
        });
    }






}