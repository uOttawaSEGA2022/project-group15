package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class RegisterCook extends AppCompatActivity implements View.OnClickListener {
    private TextView registerCook, registerUser;
    private ImageView banner;
    private EditText editTextFirstName, editTextLastName, editTextAddress, editTextEmail, editTextPassword, editTextDescription;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cook);

        mAuth = FirebaseAuth.getInstance();

        banner=(ImageView) findViewById(R.id.bannerc);
        banner.setOnClickListener(this);

        registerCook = (Button) findViewById(R.id.registerCook);
        registerCook.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.regUser);
        registerUser.setOnClickListener(this);

        editTextFirstName = (EditText) findViewById(R.id.firstNamec);
        editTextLastName = (EditText) findViewById(R.id.lastNamec);
        editTextEmail = (EditText) findViewById(R.id.email_regc);
        editTextPassword =(EditText) findViewById(R.id.pswrd_regc);
        editTextAddress =(EditText) findViewById(R.id.addressc);
        editTextDescription = (EditText) findViewById(R.id.description);

        progressBar = (ProgressBar) findViewById(R.id.progressBarc);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bannerc:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerCook:
                registerCook();
                break;
            case R.id.regUser:
                startActivity(new Intent(this, RegisterUser.class));

        }
    }

    private void registerCook() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();



        if(firstName.isEmpty()){
            editTextFirstName.setError("First name is required");
            editTextFirstName.requestFocus();
            return;
        }
        if(lastName.isEmpty()){
            editTextLastName.setError("Last name is required");
            editTextLastName.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(address.isEmpty()){
            editTextAddress.setError("Address is required");
            editTextAddress.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextEmail.setError("Password is required");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email");
            editTextEmail.requestFocus();
            return;
        }
        if(password.length()<6){
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return;
        }
        if (description.isEmpty()){
            editTextDescription.setError("A short description of the cook is required!");
            editTextDescription.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            UserCook user = new UserCook(firstName, lastName, email);

                            // We can make it so that a cook is stored inside the admin
                            FirebaseDatabase.getInstance().getReference("Users/Cook")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                DatabaseReference nameToID = FirebaseDatabase.getInstance().getReference("CookNameToID");
                                                nameToID.child(user.getFullName()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                Toast.makeText(RegisterCook.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                //redirect to login layout
                                            }
                                            else{
                                                Toast.makeText(RegisterCook.this, "Failed to register Try again!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(RegisterCook.this, "Failed to register Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
        Intent intent = new Intent(RegisterCook.this,VoidCheckUploadPage.class);
        intent.putExtra("email",email);
        startActivity(intent);

    }

}