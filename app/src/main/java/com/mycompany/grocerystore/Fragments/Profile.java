package com.mycompany.grocerystore.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mycompany.grocerystore.Activities.RegisterPhoneNumber;
import com.mycompany.grocerystore.Models.User;
import com.mycompany.grocerystore.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class Profile extends Fragment {
        public static final int PICK_IMAGE = 100;
    FirebaseUser user;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    TextView username, userEmail, userPhone;
    ImageView profileImage;
    ImageButton editName, editEmail;
    Button logout, changeDP;
    ImageView newImage;
    Uri image_uri;
    ProgressDialog progressDialog;
    String oldUsername, oldPhoneNumber, oldUrl, olduserID, oldemail, oldDeviceID;
    String newName, download_url, newEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile, container, false);

        IntialiseVariable(view);

        user = FirebaseAuth.getInstance().getCurrentUser();
        olduserID = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(olduserID);
        storageReference = FirebaseStorage.getInstance().getReference().child("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                oldUsername = user.getUserName();
                oldemail = user.getUserEmail();
                oldPhoneNumber = user.getPhoneNumber();
                oldUrl = user.getProfilePhotoUrl();
                oldDeviceID = user.getDeviceId();

                username.setText(oldUsername);
                userPhone.setText(oldPhoneNumber);

                if (oldemail.equals("")) {
                    userEmail.setText("");
                } else {
                    editEmail.setVisibility(View.INVISIBLE);
                    userEmail.setText(oldemail);

                }

                if (oldUrl.equals("")) {
                    profileImage.setImageResource(R.drawable.ic_user);
                } else {
                    Picasso.get().load(oldUrl).fit().centerCrop().into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNameUpdateDialog();
            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEmailUpdateDialog();
            }
        });
        changeDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageUpdateDialog();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignOut();
            }
        });
        return view;
    }


    private void SignOut() {
        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Do You want to Logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getContext(), RegisterPhoneNumber.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void IntialiseVariable(View view) {
        username = view.findViewById(R.id.frag_username);
        userEmail = view.findViewById(R.id.frag_email);
        userPhone = view.findViewById(R.id.frag_phone);
        profileImage = view.findViewById(R.id.frag_image);
        editEmail = view.findViewById(R.id.editEmail);
        editName = view.findViewById(R.id.editName);
        logout = view.findViewById(R.id.logout);
        changeDP = view.findViewById(R.id.changDP);
        progressDialog = new ProgressDialog(getContext());
    }

    private void showNameUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_details, null);
        builder.setView(view);

        TextView oldTextview = view.findViewById(R.id.oldTextview);
        TextView newTextview = view.findViewById(R.id.newTextview);
        TextView oldPname = view.findViewById(R.id.oldDetail);

        final EditText newPname1 = view.findViewById(R.id.newDetail);
        Button button = view.findViewById(R.id.btn_update);

        builder.setTitle("Update Profile Name");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        oldTextview.setText("Old Name:");
        oldPname.setText(oldUsername);
        newTextview.setText("New Name:");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName = newPname1.getText().toString().trim();
                updateName(newName);
                alertDialog.dismiss();
            }
        });
    }

    private void updateName(String name) {
        User user = new User(name, olduserID, oldPhoneNumber, oldUrl, oldemail, oldDeviceID);
        databaseReference.setValue(user);
        Toast.makeText(getContext(), "Name Updated", Toast.LENGTH_LONG).show();
    }

    private void showEmailUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_details, null);
        builder.setView(view);

        TextView oldTextview = view.findViewById(R.id.oldTextview);
        TextView newTextview = view.findViewById(R.id.newTextview);
        TextView oldPname = view.findViewById(R.id.oldDetail);

        final EditText newPname1 = view.findViewById(R.id.newDetail);
        Button button = view.findViewById(R.id.btn_update);

        builder.setTitle("Update Email");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        oldTextview.setText("Old Email:");
        oldPname.setText(oldemail);
        newTextview.setText("New Email:");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEmail = newPname1.getText().toString().trim();
                updateEmail(newEmail);
                alertDialog.dismiss();
            }
        });
    }

    private void updateEmail(String email) {
        User user = new User(oldUsername, olduserID, oldPhoneNumber, oldUrl, email, oldDeviceID);
        databaseReference.setValue(user);
        Toast.makeText(getContext(), "Email Updated", Toast.LENGTH_LONG).show();
    }

    private void showImageUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.update_image, null);
        builder.setView(view);

        ImageView oldimage = view.findViewById(R.id.oldImage);
        newImage = view.findViewById(R.id.newImage);
        Button chooseImage = view.findViewById(R.id.chooseImage);
        final Button updateImage = view.findViewById(R.id.updateImagebtn);

        builder.setTitle("Update Profile Image");
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        if (oldUrl.equals("")) {
            oldimage.setImageResource(R.drawable.ic_user);
        } else {
            Picasso.get().load(oldUrl).fit().centerCrop().into(oldimage);
        }
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseNewImage();
            }
        });
        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateImage();
                alertDialog.dismiss();
            }
        });
    }

    private void chooseNewImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            image_uri = data.getData();
            newImage.setImageURI(image_uri);

        }
    }


    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void updateImage() {
        if (image_uri != null) {
            progressDialog.setTitle("Please Wait...");
            progressDialog.show();
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "."
                    + GetFileExtension(image_uri));
            final UploadTask uploadTask = reference.putFile(image_uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Objects.requireNonNull(getContext()), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            download_url = reference.getDownloadUrl().toString();
                            return reference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Profile Image Updated Successfully",
                                    Toast.LENGTH_LONG).show();
                            if (task.isSuccessful()) {
                                download_url = task.getResult().toString();
                                User user = new User(oldUsername, olduserID, oldPhoneNumber, download_url, oldemail, oldDeviceID);
                                databaseReference.setValue(user);

                            }
                        }
                    });
                }
            });
        }
    }
}
