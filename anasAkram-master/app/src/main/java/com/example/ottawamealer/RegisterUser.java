package com.example.ottawamealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView registerUser, registerCook;
    private EditText editTextFirstName,editTextLastName, editTextAddress, editTextEmail, editTextPassword, editTextCreditCard, editTextExpiryDate, editTextCvv;
    private ProgressBar progressBar;
    private ImageView banner;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner=(ImageView) findViewById(R.id.bannerc);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        registerCook = (TextView) findViewById(R.id.reg_cook);
        registerCook.setOnClickListener(this);

        editTextFirstName = (EditText) findViewById(R.id.firstName);
        editTextLastName = (EditText) findViewById(R.id.lastName);
        editTextEmail = (EditText) findViewById(R.id.email_reg);
        editTextPassword =(EditText) findViewById(R.id.pswrd_reg);
        editTextAddress =(EditText) findViewById(R.id.address);
        editTextCreditCard =(EditText) findViewById(R.id.credit_card);
        editTextExpiryDate =(EditText) findViewById(R.id.expiry_date);
        editTextCvv =(EditText) findViewById(R.id.CVV);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_cook:
                startActivity(new Intent(this, RegisterCook.class));
                break;

            case R.id.bannerc:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.registerUser:
                registerUser();
                break;

        }

    }
    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String creditCard = editTextCreditCard.getText().toString().trim();
        String expiryDate = editTextExpiryDate.getText().toString().trim();
        String cvv = editTextCvv.getText().toString().trim();

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
        if(creditCard.isEmpty()){
            editTextCreditCard.setError("Credit card number is required");
            editTextCreditCard.requestFocus();
            return;
        }
        if(creditCard.length()!=16){
            editTextCreditCard.setError("Credit card number should ave 16 digits!");
            editTextCreditCard.requestFocus();
            return;
        }
        if(expiryDate.isEmpty()){
            editTextExpiryDate.setError("Expiry date is required");
            editTextExpiryDate.requestFocus();
            return;
        }
        if(expiryDate.length()!=4){
            editTextExpiryDate.setError("Enter MM followed by YY");
            editTextExpiryDate.requestFocus();
            return;
        }
        if(cvv.isEmpty()){
            editTextCvv.setError("CVV is required");
            editTextCvv.requestFocus();
            return;
        }
        if(cvv.length()!=3){
            editTextCvv.setError("Enter the 3 digits at the back of your card");
            editTextCvv.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(firstName, lastName, email);

                            FirebaseDatabase.getInstance().getReference("Users/Client")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                DatabaseReference nameToID = FirebaseDatabase.getInstance().getReference("ClientNameToID");
                                                nameToID.child(user.getFullName()).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                Toast.makeText(RegisterUser.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);

                                                //redirect to login layout
                                            }
                                            else{
                                                Toast.makeText(RegisterUser.this, "Failed to register Try again!", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(RegisterUser.this, "Failed to register Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });



    }

}